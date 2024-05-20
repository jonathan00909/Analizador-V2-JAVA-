package Logic.Formats;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// Lee el contenido del archivo
public class FileManager {
    //Convierte el contenido del archivo en un string
    public String fileToString(String pathfile){
        File file = new File(pathfile);
        if(!file.exists()){  // si no existe, regresa null
           return null;
        }
        String content;
        try { //obtiene el contenido del archivo.
            content = new String(Files.readAllBytes(Paths.get(pathfile))); 
        } catch (IOException e) { // si hay un error, devuelve nulo
            e.printStackTrace();
            return null;
        }
        return content;
    }
}
