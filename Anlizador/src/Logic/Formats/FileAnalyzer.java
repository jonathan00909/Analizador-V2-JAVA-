package Logic.Formats;

import Logic.Behavior.Analyzer;
import Logic.Behavior.Grammar;

// Analiza un archivo, y lo guarda en String
public class FileAnalyzer implements Analyzer{
    private FileManager fileManager; //el que abre los archivos
    private String infoAnalyze; //guarda el estado del analisis

    public FileAnalyzer() { //constructor
        this.fileManager = new FileManager();
        this.infoAnalyze = "haga una analisis antes";
    }

    public String information(){ //devuelve el estado de la lectura del archivo.
        return this.infoAnalyze;
    }  

    //defino, el archivo a analizar
    //y la gramatica que voy a analizar
    @Override
    public boolean analyze(String file_to_analyze, Grammar grammar_to_analyze) { 
        String stringAnalyze;
        stringAnalyze = fileManager.fileToString(file_to_analyze);
        Boolean fileExists = stringAnalyze != null;
        if(!fileExists){
            infoAnalyze = "No se encontro el archivo";
            return false;
        }
        boolean belongsHere = grammar_to_analyze.Start(stringAnalyze);
        infoAnalyze = grammar_to_analyze.information();
        return belongsHere;
    }
    
}
