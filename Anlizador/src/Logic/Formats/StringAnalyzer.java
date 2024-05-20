package Logic.Formats;

import Logic.Behavior.Analyzer;
import Logic.Behavior.Grammar;

//  guarda en String, la cadena a analizar
public class StringAnalyzer implements Analyzer{
    private String infoAnalyze; //es el estado del analisis de la cadena

    public StringAnalyzer() { //inicializa la variable
        this.infoAnalyze = "haga una analisis antes";
    }

 

    //obtiene el resultado del analisis.
    @Override
    public String information() {
        return this.infoAnalyze;
    }

    @Override
    public boolean analyze(String string_to_analyze, Grammar grammar_to_analyze) {
        Boolean stringExists = string_to_analyze != null;
        if (!stringExists) {
            infoAnalyze = "No hay cadena para analizar";
            return false;
        }
        boolean belongsHere = grammar_to_analyze.Start(string_to_analyze);
        infoAnalyze = grammar_to_analyze.information();
        return belongsHere;
    }
    
}
