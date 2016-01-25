/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cache;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Xiao
 */
public class Lector {
    
    public Lector() //Constructor
    {
    }
    
    public int leerTamCache(String nomArchivo) throws FileNotFoundException, IOException
    {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        int tam = 0;
 
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (nomArchivo);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
            // Lectura del fichero
            String linea;
            linea=br.readLine();
            tam = Integer.parseInt(linea);
            //System.out.println(tam);
            //return tam;
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try{                    
                if( null != fr ){   
                fr.close();     
                }                  
            }catch (Exception e2){ 
                e2.printStackTrace();
            }
        }
        return tam;
    }
}