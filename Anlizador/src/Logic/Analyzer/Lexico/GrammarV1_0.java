package Logic.Analyzer.Lexico;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import Logic.Analyzer.Lexico.Token.LexycalComponents;
import Logic.Analyzer.Lexico.Token.Token;
import Logic.Analyzer.Lexico.Token.TokenId;
import Logic.Behavior.GetLexycal;
import Logic.Behavior.Grammar;

public class GrammarV1_0 implements Grammar, GetLexycal{
    private String inString;
    private LexycalComponents tokenAnalizer;
    private String stateGrammar;
    private boolean isSucessfully;
    private int last;
    public GrammarV1_0(){
        Map<String,TokenId> tokenIdList = new LinkedHashMap<String,TokenId>(){{
            put("[|]{2}",new TokenId(15, "operador logico OR")); 
            put("[&]{2}",new TokenId(16, "operador logico AND"));
            put("==",new TokenId(17, "operador logico igualdad"));
            put("<=",new TokenId(18, "operador logico igual mayor"));
            put(">=",new TokenId(19,"asignacion"));
            put("[<]",new TokenId('<',"operador relacional menor que"));
            put("[>]",new TokenId('>',"operador relacional mayor que"));
            put("[;]",new TokenId(';',"punto y coma"));
            put("[,]",new TokenId(',',"coma"));
            put("[(]",new TokenId('(',"parentesis abierto"));
            put("[)]",new TokenId(')',"parentesis cerrado"));
            put("[{]",new TokenId('{',"llave abierta"));
            put("[}]",new TokenId('}',"llave cerrada"));
            put("[=]",new TokenId('=',"asignacion"));
            put("[*]",new TokenId('*',"operador aritmetico multiplicacion"));
            put("[/]",new TokenId('/',"operador aritmetico division"));
            put("[+]",new TokenId('+',"operador aritmetico suma"));
            put("[-]",new TokenId('-',"operador aritmetico resta"));
            put("[0-9]+[.][0-9]+",new TokenId('f',"numero decimal"));
            put("[0-9]+",new TokenId('e',"numero entero"));
            put("if",new TokenId(9,"palabra reservada if"));
            put("while",new TokenId(10,"palabra reservada while"));
            put("return",new TokenId(11,"palabra reservada return"));
            put("else",new TokenId(12,"palabra reservada else"));
            put("int",new TokenId(1,"palabra reservada int"));
            put("float",new TokenId(2,"palabra reservada float"));
            put("void",new TokenId(0,"palabra reservada void"));
            put("main",new TokenId(3, "palabra reservada main"));
            put("[a-zA-Z][a-zA-Z0-9]*",new TokenId('i',"identificador"));
        }}; 
        tokenAnalizer = new LexycalComponents(tokenIdList);
    }

    private String cutString (LexycalComponents analyzer, String string_to_cut){
        int toCut = analyzer.getLastPosition();
        if(string_to_cut == null){
            return string_to_cut;
        }
        if(toCut > inString.length()){
            return string_to_cut;
        }
        return string_to_cut.substring(toCut);
    }

    private void activateState(LexycalComponents analizer){
        this.isSucessfully = analizer.isErrorAnalize();
    }

    private Token setPosToken(Token token , int lastPosition){
        this.last += lastPosition;
        int start = this.last - token.getLexema().getEnd();
        token.getLexema().setEnd(this.last);
        token.getLexema().setStart(start);
        return token;
    }

    private boolean found(){
        Token tokenFound = this.tokenAnalizer.findToken(inString);
        //System.out.println(tokenFound.getLexema() + lexical.information());
        //sino encontró token -> actualiza el listado de tokens y lo quita del inString
        if(tokenFound.equals(new Token())){
            this.stateGrammar = this.tokenAnalizer.getErrorMessage() + ", ultimo lugar leido: " + this.last;
            activateState(this.tokenAnalizer);
            //System.out.println(this.stateGrammar);
            showErrorDialog();
            return true;
        }
        inString = cutString(this.tokenAnalizer, inString);
        tokenFound = setPosToken(tokenFound, this.tokenAnalizer.getLastPosition());
        //System.out.println(tokenFound);
        this.tokenAnalizer.setLexycalComponent(tokenFound);
        return false;
        
   }

    @Override
    public boolean Start(String set_to_analyze) { /// es la primera funcion que se usa ///, llama a todas
        this.inString = set_to_analyze;
        tokenAnalizer.vaciar();
        boolean finished = false;
        while(!finished){ /// si aun no acaba el analisis
            finished = this.found(); //encuentra uno por uno
        }
        return this.isSucessfully; 
    }

    @Override
    public List<Token> getLexycal() { //obtiene todos los componentes. obtenidos en el analisis
        return this.tokenAnalizer.getTokenList();
    }

    @Override
    public String information() { //obtiene el estado final del analisis.
        return this.stateGrammar; // devuelve si fue exitoso, o no.
    }
    
    public void showErrorDialog() {
        String errorMessage = this.stateGrammar;
        if(!this.isSucessfully){
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
