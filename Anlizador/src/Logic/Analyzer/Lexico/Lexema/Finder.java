/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 * 
 * clases para buscarlo
 */
package Logic.Analyzer.Lexico.Lexema;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author josue
 */
public class Finder {
    private @Getter @Setter Lexema lexema; //creo los Getter y setters de lexema
    private Pattern pattern;
    private Matcher matcher;

    private boolean isValidEntry(String to_analize, String regex){
        //revisa si es valida la cadena
        if(!isValidString(regex)){  //de regex
            return false;
        }
        else if(!isValidString(to_analize)){ //y to_analize
            return false;
        }
        return true; //devuelvo verdadero
    }
    
    private boolean isValidString(String string){
        //reviso si el string no es direccion nula
        if(string == null){
            return false;
        }
        //reviso si esta vacio, y devuelvo su resultado
        return !string.equals(" ");
    }
    
    public Matcher findNext_to(String to_analize, String regex, int start_in){
        //recorto la cadena, hasta donde debe de iniciar el analisis.
        to_analize = to_analize.substring(start_in);
        //llamo la otra funcion y regreso su resultado
        return this.findNext_to(to_analize,regex);
    }
    
    public Matcher findNext_to(String to_analize, String regex){
        if(!this.isValidEntry(to_analize,regex)){ //reviso si son entradas validas
            return null;
        }
        pattern = Pattern.compile(regex); //cargo la regex a usar
        matcher = pattern.matcher(to_analize); //cargo la cadena a analizar
        
        //revisa si esta el regex en donde se leyo en la cadena.
        final boolean match = matcher.lookingAt(); 
        if(!match){ //si no esta
            return null; //retorna null
        }
        return matcher;
    }
    
    public Matcher find(String to_analize, String regex, int start_in){
        if(!this.isValidEntry(to_analize,regex)){ //reviso si son entradas validas
            return null;
        }
        pattern = Pattern.compile(regex); //cargo la regex a usar
        matcher = pattern.matcher(to_analize); //cargo la cadena a analizar
        
        final boolean match = matcher.find(start_in); //busca el regex en la cadena
        if(!match){ // si no lo encuentra
            return null; //retorna null
        }
        return matcher;
    }
    
    public Matcher find(String to_analize, String regex){
        //nomas define que empezara desde el inicio
        return this.find(to_analize,regex,0); 
    }
    
    public Lexema retrieveLexema(Matcher find){
        if(find == null){ //ve si es null
            return new Lexema();
        }
        //devuelve el lexema recuperado, en donde empezo, y donde acabo, con el lexema
        return new Lexema(find.start(),find.end(),find.group(0),find.pattern().pattern());
    }
}
