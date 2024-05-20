/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic.Analyzer.Lexico.Token;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Logic.Analyzer.Lexico.Lexema.Lexema;
import Logic.Analyzer.Lexico.Lexema.Lexycal;
import lombok.Getter;

/**
 *
 * @author josue
 */
public class LexycalComponents {
    // Regex (String), Token (TokenID, Lexema)
    private Map<String,TokenId> tokenIdList;
    private List<Token> tokenList;
    private @Getter String errorMessage;
    private @Getter int lastPosition;
    private @Getter boolean errorAnalize;
    
    public LexycalComponents(Map<String,TokenId> tokenIdList) {
        this.tokenIdList = tokenIdList;
        tokenList = new LinkedList<>();
    }
    
    public void vaciar(){
        tokenList = new LinkedList<>();
    }

    public List<Token> getTokenList(){
        return tokenList;
    }
    
    public void setLexycalComponent(Token token){
        tokenList.add(token);
    }
    
    public Token getToken(Lexema lexema){
        TokenId id = this.tokenIdList.getOrDefault(lexema.getRegEx(), new TokenId());
        if(id.equals(new TokenId())){
            return new Token();
        }
        return new Token(id,lexema);
    }
    
    public Token findToken(String to_evaluate) {
        Lexema lexema;
        Lexycal lexycalAnalizer = new Lexycal();
        
        for(String regEx : tokenIdList.keySet()) {
            lexema = lexycalAnalizer.getLexema(to_evaluate,regEx);
            this.lastPosition = lexycalAnalizer.getLastPosition();
            boolean find = !lexema.equals(new Lexema());
            if (find) {
                return this.getToken(lexema);
            }
        }

        this.errorMessage = lexycalAnalizer.errorMessage();
        this.errorAnalize = lexycalAnalizer.getErrorCode() <2;
        if(lexycalAnalizer.getErrorCode() == 3){
           this.errorMessage = "se encontro un token no reconocible por la gramatica";
        }

        return new Token();
    }

    
}
