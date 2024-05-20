/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic.Analyzer.Lexico.Lexema;

import java.util.regex.Matcher;

import javax.swing.JOptionPane;

import lombok.Getter;

/**
 *
 * @author josue
 */
public class Lexycal {
    private Finder finder = new Finder();
    private @Getter int  errorCode = 0;
    private @Getter int lastPosition = 0;
    
    private int getPosition(Matcher matcher){
        if(matcher == null){
            return -1;
        }
        Lexema lexema = finder.retrieveLexema(matcher);
        return lexema.getEnd();
    }

    public int commentaryRecognize(String to_analize){
      int position;
      position = this.getPosition(finder.find(to_analize, "[/][*]"));
       if(position != -1){ // si se encontro, entra al condicional
           this.lastPosition = position;
           position = this.getPosition(finder.find(to_analize, "[*][/]")); // revisa el final del comentario
           if(position == -1){ //si no lo encuentra, mandara un -1.
             this.errorCode = 2; // no se encontro el final del comentario
           } 
           return position;
       }
       //NO ENCONTRE MANERA DE IMPLEMENTAR ESTO, solo el sintactico lo resolvera.
       /*
        *  un comentario largo sin inicio.
        */
       
       //si llega aqui, significa que no lo encontro
       position = 0; //se manda 0, porque no hubo cambios 
       return position;
    }

    public Lexema getLexema(String to_analize, String regex){
       int index = 0; //registra lo que se ha leido
       //se buscan un lugar sin espacios blancos, el menos uno
       //es para que agarre desde el inicio
       int position = this.getPosition(finder.find(to_analize, "[^\\s]"));
       index += position - 1; //guarda la posicion
       if(position == -1){ // si no se encontro
         this.lastPosition = to_analize.length(); 
         this.errorCode = 1; // ya no se encontro nada mas por leer.
         return new Lexema();  //regresa un lexema vacio.
       }
       to_analize = to_analize.substring(index); //ignora espacios en blanco

       position = this.commentaryRecognize(to_analize);
       if(position != 0){ // si encontro el comentario
            if(position == -1){ //si no se encontro el final del mismo
                return this.finder.retrieveLexema(null);   
            }
            index += position; /// *** podria moverlo abajo ***
            position = this.getPosition(finder.find(to_analize, "[^\\s]",position));
            if(position > -1){ //quita espacios en blanco, despues del comentario
                to_analize = to_analize.substring(position);
            }else{ // lo deja igual
                to_analize = to_analize.substring(index);
            }
       }
       //System.out.println(to_analize);

       //devuelve el lexema encontrado
       Lexema lexema = this.finder.retrieveLexema(this.finder.findNext_to(to_analize, regex));
       if(!lexema.equals(new Lexema())){ //si se encontro el lexema buscado
        //cambia a la posicion real que se leyo  
            this.lastPosition = lexema.getEnd() + index; 
            this.errorCode = 0; // todo fue exitoso     
       }else{ //Si no lo encontro el lexema, entonces hay dos razones
            if(to_analize.length() != 0){ //porque no se encontro
                this.lastPosition = index; 
                this.errorCode = 3; // no se encontro el lexema señalado 
            }
            //la cadena esta vacia XD.
       }
       return lexema;
    }

    public String errorMessage(){
        switch(this.errorCode){
            case 0:
                return "todo fue exitoso";
            case 1:
                return "ya no se encontro nada mas por leer";
            case 2:
                return "no se encontro el final del comentario";
            case 3:
                return "no se encontro el lexema señalado";
            default:
               return "no se reconoce el codigo";
        }
    }

    public void showErrorDialog() {
        String errorMessage = errorMessage();
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
