package Logic.Analyzer.Sintactico;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public interface Exp {
   public String toString();

   public String semanticAnalize();

   @Data
   class Num implements Exp {
      public String type;
      public String value;

      public Num(String t, String v) {
         value = v;
         type = t;
      }

      public String toString() {
         return " <" + type + "," + value + "> ";
      }

      public String semanticAnalize() {
         return type;
      }
   }

   class Variable {
      public @Getter @Setter String type;
      public @Getter @Setter String name;

      public Variable(String t, String n) {
         name = n;
         type = t;
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((name == null) ? 0 : name.hashCode());
         return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null)
            return false;
         if (getClass() != obj.getClass())
            return false;
         Variable other = (Variable) obj;
         if (name == null) {
            if (other.name != null)
               return false;
         } else if (!name.equals(other.name))
            return false;
         return true;
      }
   }

   class ValueOf implements Exp{
      Variable variable;
      public ValueOf(Variable variable) {
         this.variable = variable;
      }
      public String toString() {
         return " <" + variable.getType() + "," + variable.getName() + "> ";
      }

      public String semanticAnalize() {
         return variable.getType();
      }
   }

   class Binary implements Exp {
      public String operator;
      public Exp left;
      public Exp right;

      public Binary(String o, Exp l, Exp r) {
         operator = o;
         left = l;
         right = r;
      }

      public String toString() {
         return " ( " + operator + " " + left.toString() + "," + right.toString() + ") ";
      }

      @Override
      public String semanticAnalize() {
         String typeLeft = left.semanticAnalize(), typeRight = right.semanticAnalize();
         if (!typeLeft.equals(typeRight)) {
            throw new CancellationException("error en la operacion: " + operator + " no coinciden los tipos: " + typeLeft + " con " + typeRight);
         }
         return typeLeft;
      }
   }

   class Unary implements Exp {
      public String operator;
      public Exp operand;

      public Unary(String o, Exp e) {
         operator = o;
         operand = e;
      }

      public String toString() {
         return operator + " " + operand.toString();
      }

      @Override
      public String semanticAnalize() {
         return operand.semanticAnalize();
      }
   }
   class VarDecl {
        private List<Exp.Variable>varDeclarations = new LinkedList<Exp.Variable>();
        
        public void clear(){
            this.varDeclarations.clear();
        }

        public void setValue(String type, String name) throws CancellationException{
            Exp.Variable newDeclaration = new Exp.Variable(type, name);
            if(this.varDeclarations.contains(newDeclaration)){
                throw new CancellationException("Variable duplicada:  " + "'" + name + "'");
            }
            this.varDeclarations.add(newDeclaration);
        }
        public Exp.ValueOf getVariable(String name){
            int index = this.varDeclarations.indexOf(new Exp.Variable(" ", name));
            if(index == -1){
               throw new CancellationException("Variable aun no declarada:  " + "'" + name + "'");
            }
            return new Exp.ValueOf(this.varDeclarations.get(index));
        }
    }
  /*
   class Call implements Exp {
      public String name;
      public List<Exp> arguments;

      public Call(String nm, List<Exp> s) {
         name = nm;
         arguments = s;
      }

      public String toString() {
         String value = "0";
         for (Exp s : arguments) {
            value = s.toString() + '\n';
         }
         return name + " " + value;
      }

      @Override
      public String semanticAnalize() {
         return " ";
      }

   }

   class Projection implements Exp {
      public Exp record;
      public String attribute;

      public Projection(Exp v, String a) {
         record = v;
         attribute = a;
      }

      public String toString() {
         return record.toString() + " " + attribute;
      }

      @Override
      public String semanticAnalize() {
         return record.semanticAnalize();
      }
   }

   class Record implements Exp {
      public List<RecordElement> elements;

      public Record(List<RecordElement> el) {
         elements = el;
      }

      public String toString() {
         String value = "0";
         for (RecordElement s : elements) {
            value = s.toString() + '\n';
         }
         return value;
      }

      @Override
      public String semanticAnalize() {
         return " ";
      }

   }

   class RecordElement {
      public String attribute;
      public Exp value;

      public RecordElement(String a, Exp v) {
         attribute = a;
         value = v;
      }

      public String toString() {
         return attribute + " " + value.toString();
      }
   }
   */
}
