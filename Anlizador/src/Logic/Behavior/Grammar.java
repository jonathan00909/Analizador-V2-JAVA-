package Logic.Behavior;

///define el comportamiento de la gramatica
public interface Grammar extends Information{
    //es el primer metodo, para iniciar el analisis.
    public boolean Start(String set_to_analyze);
}
