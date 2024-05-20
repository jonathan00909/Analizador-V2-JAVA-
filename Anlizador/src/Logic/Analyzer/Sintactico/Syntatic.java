package Logic.Analyzer.Sintactico;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;

import Logic.Analyzer.Lexico.Token.Token;
import lombok.Getter;

public class Syntatic {
    private List<Token> tokens;
    private int index;
    private @Getter String stateMessage;
    private Exp.VarDecl Declaraciones = new Exp.VarDecl();
    private String type;
    private @Getter List<String> errorSemantic = new LinkedList<String>();

    private void coincidir(int token) {
        System.out.println("coincidir" + token);
        if((tokens.get(index).getId().getId() != token)){
            System.out.println("error");
            throw new CancellationException("El numero que se esperaba de token '" + token + "' no el token '" + tokens.get(index).getId().getDescription() + "'");  
        }
        System.out.println("si coincidio: " + tokens.get(index).getLexema().getSubString() + " -- " + index + " de: " + (tokens.size()-1));
        if ((index == tokens.size() - 1 )) {
            return;
        }
        if ((index < tokens.size() - 1 )) {
            index++;
            System.out.println("index aumento " + index);
        }
    }

    private void programa() {
        System.out.println("programa");
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case 1:
                coincidir(1);
                coincidir(3);
                coincidir('('); // numero de "("
                coincidir(')'); // numero de ")"
                coincidir('{');
                declaraciones();
                ordenes();
                coincidir(11);
                coincidir('e');
                coincidir(';');
                coincidir('}');
                break;
            default:
                break;
        }
    }

    private void declaraciones() {
        System.out.println("declaraciones");
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case 1:
                coincidir(1);
                //se define el tipo
                this.type = token.getLexema().getSubString(); //int
                lista_variables();
                coincidir(';');
                declaraciones();
                break;
            case 2:
                coincidir(2);
                //se define el tipo
                this.type = token.getLexema().getSubString(); //float
                lista_variables();
                coincidir(';');
                declaraciones();
                break;
            default:
                break;
                
        }
        System.out.println("declaraciones fin");
    }
    private void lista_variables(){
        System.out.println("lista_variables");
        //Aqui se declara la variable
        this.Declaraciones.setValue(type, this.tokens.get(index).getLexema().getSubString());
        coincidir('i');
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case ',':
                coincidir(',');
                lista_variables();
            default:
                break;
        } 
        System.out.println("lista_variables fin");
    }

    private void ordenes() {
     System.out.println("ordenes");
        orden();
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case 9:case 10:case 'i':
                ordenes();
                break;
            default:
                break;
        }
      System.out.println("ordenes fin");
    }
    private void orden(){
        System.out.println("orden");
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case 9:
               condicion(); 
                break;
            case 10:
                bucle_while();
                break;
            case 'i':
                asignaciones();
                break;
            default:
                break;
        }
        System.out.println("orden fin");
    }
 // asignaciones
    private void asignaciones() {
        Exp variableExp,expresion_aritExp,resultExp;
        System.out.println("asignaciones");
        //Revisa si fue declarada anteriormente, sino, lanza excepcion
        Token token = this.tokens.get(index);
        try {
            variableExp = this.Declaraciones.getVariable(token.getLexema().getSubString());
        } catch (CancellationException expected) {
            errorSemantic.add("Error en token: " + this.index + " " + expected.getMessage());
            variableExp = new Exp.ValueOf(new Exp.Variable("<No_Existe>", token.getLexema().getSubString()));    
        }
        coincidir('i');
        coincidir('=');
        expresion_aritExp = expresion_arit();
        coincidir(';');  
        resultExp = new Exp.Binary("=", variableExp, expresion_aritExp);
         try {
            resultExp.semanticAnalize();
        } catch (CancellationException expected) {
            errorSemantic.add("Error en token: " + this.index + " " + expected.getMessage());   
        }
        System.out.println(resultExp.toString());
        System.out.println("asignaciones fin");       
    }
    
    private Exp expresion_arit() {
        Exp resultExp, expresion_aritExp, varExp;
        System.out.println("expresion_arit");
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case '(':
                coincidir('(');
                expresion_aritExp = expresion_arit();
                coincidir(')');
                resultExp = exp_arit(expresion_aritExp);
            break;
            case 'e': // numero de "entero"
                varExp = new Exp.Num("int", token.getLexema().getSubString());
                coincidir('e'); // numero de "entero"
                resultExp = exp_arit(varExp);
            break;
            case 'f': // numero de "real"
                varExp = new Exp.Num("float", token.getLexema().getSubString());
                coincidir('f'); // numero de "real"
                resultExp = exp_arit(varExp);
            break;
            case 'i':
                try {
                    varExp = this.Declaraciones.getVariable(token.getLexema().getSubString());
                } catch (CancellationException expected) {
                    errorSemantic.add("Error en token: " + this.index + " " + expected.getMessage());  
                    varExp = new Exp.ValueOf(new Exp.Variable("<No_Existe>", token.getLexema().getSubString())); 
                }
                coincidir('i'); // numero de "identificador"
                resultExp = exp_arit(varExp);
            break;
            default:
                throw new CancellationException("no es un operador valido");
        }
        System.out.println(resultExp.toString());
        System.out.println("expresion_arit fin");
        return resultExp;
    }

    private Exp exp_arit(Exp exp_aritHer){
        String operador_aritExp;
        Exp expresion_aritExp, resultExp;
        System.out.println("exp_arit");
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case '+':case '-':case '*':case '/': 
                operador_aritExp = operador_arit();
                expresion_aritExp = expresion_arit();
                resultExp = exp_arit(new Exp.Binary(operador_aritExp, exp_aritHer, expresion_aritExp));
            break;
            default:
                resultExp = exp_aritHer;
            break;
        }
        System.out.println(resultExp.toString());
        System.out.println("exp_arit fin");
        return resultExp;
    }

    private String operador_arit() {
        String operador_aritExp;
        System.out.println("operador_arit");
        Token token = this.tokens.get(index);
        operador_aritExp = token.getLexema().getSubString();
        switch (token.getId().getId()) {
            case '+': // +
                coincidir('+'); // +
            break;
            case '*': // *
                coincidir('*'); // *
            break;
            case '-': // -
                coincidir('-'); // -
            break;
            case '/': // /
                coincidir('/'); // /
            break;
            default:
                throw new CancellationException("no es un operador valido");
        }
        System.out.println(operador_aritExp);
        System.out.println("operador_arit fin");
        return operador_aritExp;
    }
//----------------------------------------------------------------------------------

// condicion --------------------------------------------------------------------
    private void condicion (){
        coincidir(9);
        coincidir('(');
        comparacion();
        coincidir(')');
        coincidir('{');
        ordenes();
        sig_condicion();
    } 
    private void sig_condicion(){
        System.out.println("sig_condicion");
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case '}': //cierra llave
                coincidir('}'); //cierra llave
                if(this.tokens.get(index).getId().getId() == 12){// else
                    coincidir(12); // else
                    coincidir('{');
                    ordenes();
                    coincidir('}');
                } 
            break;
            default:
                throw new CancellationException("no cerro correctamente la condicion");
        }
        System.out.println("sig_condicion fin");
    }
//----------------------------------------------------------------------------------------
// comparacion ---------------------------------------------------------------------------------- 
    private void comparacion(){
        Exp operador1Exp, operador2Exp, resulExp;
        String condicion_opExp;
        System.out.println("comparacion");
        operador1Exp = operador(); // guarda el operador 1
        condicion_opExp = condicion_op(); // la operacion
        operador2Exp = operador(); // operador 2
        resulExp = new Exp.Binary(condicion_opExp, operador1Exp, operador2Exp); // los guarda como operacion
        try {
            resulExp.semanticAnalize(); // y analiza si son del mismo tipo, de no serlo, saldra una excepcion
        } catch (CancellationException expected) {
            errorSemantic.add("Error en token: " + this.index + " " + expected.getMessage());   
        }
        System.out.println("comparacion fin");
    }

    private Exp operador(){
        System.out.println("operador");
        Exp resultExp;
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case 'e': // numero de "entero"
                resultExp = new Exp.Num("int", token.getLexema().getSubString());
                coincidir('e'); // numero de "entero"
                break;
            case 'f': // numero de "real"
                resultExp = new Exp.Num("float", token.getLexema().getSubString());
                coincidir('f'); // numero de "real"
                break;
            case 'i':
                try {
                    resultExp = this.Declaraciones.getVariable(token.getLexema().getSubString());
                } catch (CancellationException expected) {
                    errorSemantic.add("Error en token: " + this.index + " " + expected.getMessage());  
                    resultExp = new Exp.ValueOf(new Exp.Variable("<No_Existe>", token.getLexema().getSubString())); 
                }
                System.out.println(resultExp.toString());
                coincidir('i'); // numero de "identificador"
                break;
            default:
                throw new CancellationException("no es un operador valido");
        }
        System.out.println("operador fin");
        return resultExp;
    }

    private String condicion_op() {
        /// usare if, y mi funcion isThere aqui
        System.out.println("condicion_op");
        Token token = this.tokens.get(index);
        System.out.println(String.valueOf((char)token.getId().getId()));
        String condicion_opExp = String.valueOf((char)token.getId().getId());
        switch (token.getId().getId()) {
            case 17: // =
                coincidir(17); // =
                break;
            case 18: // <=
                coincidir(18); // <=
                break;
            case 19: // >=
                coincidir(19); // >=
                break;
            case '<': // <
                coincidir('<'); // <
                break;
            case '>': // >
                coincidir('>'); // >
                break;
            default:
                throw new CancellationException("no es un operador relacional valido");
        }
        System.out.println("condicion_op fin");
        return condicion_opExp;
    }
//------------------------------------------------------------------------------------
// while ----------------------------------------------------------------------------------
    private void bucle_while (){
        coincidir(10);
        coincidir('(');
        comparacion();
        coincidir(')');
        coincidir('{');
        ordenes();
        coincidir('}');
    } 
//----------------------------------------------------------------------------------------
/* private void tipoDato() {
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case 4: // Palabra reservada "int"
                coincidir(4);
                tipoDato();
                break;
            case 3: // Palabra reservada "float"
                coincidir(3);
                break;
            case 2: // Palabra reservada "void"
                coincidir(2);
                break;
            default:
                throw new CancellationException("Se esperaba palabra reservada 'int', 'float' o 'void' en el token " + index);
        }
    }*/



    public boolean isValid(List<Token> listaTokens) {
        listaTokens.add(new Token());
        this.tokens = listaTokens;
        this.index = 0;
        this.Declaraciones.clear();
        this.errorSemantic.clear();
        this.type = "";
    
        try {
            if (this.tokens.isEmpty()) {
                throw new CancellationException("La lista de tokens no se ha inicializado");
            }
            // Agregar una verificaciÃ³n para imprimir el tamaÃ±o de la lista
            System.out.println("TamaÃ±o de tokens en isValid: " + this.tokens.size());
            programa();
            if ( index < this.tokens.size() - 1){
                throw new CancellationException("El token '" + tokens.get(index).getLexema().getSubString() + "' no es valido");
            }
        } catch (CancellationException expected) {
            this.stateMessage = expected.getMessage();
            return false;
        }
        this.stateMessage = "Todo bien";
        return true;
    }
}       