package Logic.Analyzer.Lexico.Lexema;

import lombok.AllArgsConstructor;
import lombok.Data;

public @AllArgsConstructor @Data class Lexema{
    public Lexema (Lexema d){
        this(d.start,d.end,d.subString,d.regEx);
    }
    
    public Lexema(){
        this.start = -1;
        this.end = 0;
        this.subString = " ";
        this.regEx = " ";
    }
    
    private int start;
    private int end;
    private String subString;
    private String regEx;
}
