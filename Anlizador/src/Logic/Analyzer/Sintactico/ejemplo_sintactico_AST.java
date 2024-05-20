package Logic.Analyzer.Sintactico;
import java.util.List;
import java.util.concurrent.CancellationException;

import Logic.Analyzer.Lexico.Token.Token;
import lombok.Getter;

public class ejemplo_sintactico_AST {
    private List<Token> tokens;
    private int index;
    private @Getter String stateMessage;
    private @Getter Exp arbol_Sintactico;

    private void coincidir(int token) {
        System.out.println("coincidir");
        if((tokens.get(index).getId().getId() != token)){
            System.out.println("error");
            this.stateMessage = "El numero que se esperaba de token '" + token + "' en el token " + index;
            throw new CancellationException();  
        }
        System.out.println("si coincidio: " + index + " de: " + (tokens.size()-1));
        if ((index == tokens.size() - 1 )) {
            return;
        }
        if ((index < tokens.size() - 1 )) {
            index++;
            System.out.println("index aumento " + index);
        }
    }


    private Exp factor() {
        Exp factorExp;
        System.out.println("factor");
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case '(': // "("
                coincidir('('); // numero de "("
                factorExp = expr();
                coincidir(')'); // numero de ")"
            break;
            default:
               factorExp = digito();
        }
        System.out.println("factor fin");
        return factorExp;
    }

    private Exp digito() {
        Exp digitoExp;
        System.out.println("digito");
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case 'e': // numero de "entero"
                coincidir('e'); // numero de "entero"
                digitoExp = new Exp.Num("e", token.getLexema().getSubString());
            break;
            case 'f': //numero de "real"
                coincidir('f'); // numero de "real"
                digitoExp = new Exp.Num("f", token.getLexema().getSubString());
            break;
            default:
                coincidir('i'); // numero de "identificador"
                digitoExp = new Exp.Num("e", token.getLexema().getSubString());
            }
        System.out.println("digito fin");
        return digitoExp;
    }
    
    private Exp divMul(Exp divMulHer) {
        Exp factorExp, divMulExp, resultExp;
        System.out.println("divMul");
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case '*': /// cambiar numero "*"
                this.coincidir('*');
                factorExp = factor();
                divMulExp = divMul(new Exp.Binary(token.getLexema().getSubString(), divMulHer,factorExp));
                resultExp = divMulExp;
            break;
            case '/': /// cambiar numero "/"
                this.coincidir('/'); 
                factorExp = factor();
                divMulExp = divMul(new Exp.Binary(token.getLexema().getSubString(), divMulHer,factorExp));
                resultExp = divMulExp;
            break;
            default:
                resultExp = divMulHer;
        }
        System.out.println("divMul fin");
        return resultExp;
    }

    private Exp subAdd(Exp subAddHer){
        Exp termExp, subAddExp, resultExp;
        System.out.println("subAdd");
        Token token = this.tokens.get(index);
        switch (token.getId().getId()) {
            case '+': 
                this.coincidir('+'); /// poner numero "+"
                termExp = term();
                subAddExp = subAdd(new Exp.Binary(token.getLexema().getSubString(), subAddHer, termExp));
                resultExp = subAddExp;
            break;
            case '-':
                this.coincidir('-'); /// poner numero "-"
                termExp = term();
                subAddExp = subAdd(new Exp.Binary(token.getLexema().getSubString(), subAddHer, termExp));
                resultExp = subAddExp;
            break;
            default:
                resultExp = subAddHer;
        }
        System.out.println("subAdd fin");
        return resultExp;
    }

    private Exp term(){
        Exp factorExp, divMulExp;
        System.out.println("term");
        factorExp = factor();
        divMulExp = divMul(factorExp);
        System.out.println("term fin");
        return divMulExp;
    }

    private Exp expr(){
        Exp termExp, subAddExp;
        System.out.println("expr");
        termExp = term();
        subAddExp = subAdd(termExp);
        System.out.println("expr fin");
        return subAddExp;
    }

    
   /* private void tipoDato() {
        switch (this.tokens.get(index).getId().getId()) {
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
    
        try {
            if (this.tokens.isEmpty()) {
                this.stateMessage = "La lista de tokens no se ha inicializado";
                throw new CancellationException();
            }
            // Agregar una verificaciÃ³n para imprimir el tamaÃ±o de la lista
            System.out.println("TamaÃ±o de tokens en isValid: " + this.tokens.size());
            this.arbol_Sintactico = expr();
            System.out.println(this.arbol_Sintactico.toString());
        } catch (CancellationException expected) {
            return false;
        }
        this.stateMessage = "Todo bien";
        return true;
    }
    
}