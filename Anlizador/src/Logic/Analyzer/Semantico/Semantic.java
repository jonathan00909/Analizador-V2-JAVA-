package Logic.Analyzer.Semantico;

import java.util.concurrent.CancellationException;
import Logic.Analyzer.Sintactico.Exp;
import lombok.Getter;

public class Semantic {
   private @Getter String message;

   public boolean semanticAnalize(Exp Ast_toAnalize) {
       try {
           if(Ast_toAnalize == null) {
              this.message = "esta vacio el arbol";
              return false; 
           }
           System.out.println(Ast_toAnalize.semanticAnalize());
           this.message = "Correcto";
           return true;
       } catch (CancellationException e) {
           this.message = e.getMessage();
           System.out.println(this.message);
           return false;
       }
   }
}
