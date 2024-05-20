/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic.Analyzer.Lexico.Token;

import Logic.Analyzer.Lexico.Lexema.Lexema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author josue
 */
public  @AllArgsConstructor @Data class Token {
    public Token(Token d){
        this(new TokenId(d.id),new Lexema(d.lexema));
    }
    public Token(){
        this.id = new TokenId();
        this.lexema = new Lexema();
    }
    
    private TokenId id;
    private Lexema lexema;
    
}
