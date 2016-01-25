/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cache;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
//PROCESAMIENTO
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.json.simple.JSONObject;

/**
 *
 * @author Xiao
 */
public class Servidor {

    public static LRUCache lru_cache1; //Declaramos atributos
    public static LRUCache lru_cache2;
    public static LRUCache lru_cache3;
    public static CacheEstatico cestatico;

    public Servidor(int tamCache) //Constructor
    {
    }

    // ===== Método hash =====
    // Retorna un número consistente (siempre el mismo) para un string.
    public static long hash(String hashKey) {
        int b = 378551;
        int a = 63689;
        long hash = 0;
        for (int i = 0; i < hashKey.length(); i++) {
            hash = hash * a + hashKey.charAt(i);
            a = a * b;
        }
        return Math.abs(hash);
    }//=======================

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Se calcula el tamaño de las particiones del cache
        // de manera que la relación sea
        // 25% Estático y 75% Dinámico,
        // siendo la porción dinámica particionada en 3 partes.

        Lector l = new Lector(); // Obtener tamaño total del cache.
        int tamCache = l.leerTamCache("config.txt");
        //===================================
        int tamCaches = 0;
        if (tamCache % 4 == 0) { // Asegura que el nro sea divisible por 4.
            tamCaches = tamCache / 4;
        } else {              // Si no, suma para que lo sea.
            tamCaches = (tamCache - (tamCache) % 4 + 4) / 4;
        } // y divide por 4.

        System.out.println("Tamaño total Cache: " + (int) tamCache); // imprimir tamaño cache.
        System.out.println("Tamaño particiones y parte estática: " + tamCaches); // imprimir tamaño particiones.
        //===================================

        lru_cache1 = new LRUCache(tamCaches); //Instanciar atributos.

        lru_cache2 = new LRUCache(tamCaches);
        lru_cache3 = new LRUCache(tamCaches);
        cestatico = new CacheEstatico(tamCaches);
        cestatico.addEntryToCache("query3", "respuesta cacheEstatico a query 3");
        cestatico.addEntryToCache("query7", "respuesta cacheEstatico a query 7");

        try {
            ServerSocket servidor = new ServerSocket(4500); // Crear un servidor en pausa hasta que un cliente llegue.
            while (true) {
                Socket clienteNuevo = servidor.accept();// Si llega se acepta.
                // Queda en pausa otra vez hasta que un objeto llegue.
                ObjectInputStream entrada = new ObjectInputStream(clienteNuevo.getInputStream());

                System.out.println("Objeto llego");
                //===================================
                Cache1 hilox1 = new Cache1(); // Instanciar hebras.
                Cache2 hilox2 = new Cache2();
                Cache3 hilox3 = new Cache3();

                // Leer el objeto, es un String.
                JSONObject request = (JSONObject) entrada.readObject();
                String b = (String) request.get("busqueda");

                //*************************Actualizar CACHE**************************************
                int actualizar = (int) request.get("actualizacion");
                // Si vienen el objeto que llego viene del Index es que va a actualizar el cache
                if (actualizar == 1) {
                    int lleno = cestatico.lleno();
                    if (lleno == 0) {
                        cestatico.addEntryToCache((String) request.get("busqueda"), (String) request.get("respuesta"));
                    } else {
                            // si el cache estatico esta lleno
                        //agrego l cache dinamico

                        if (hash(b) % 3 == 0) {
                            lru_cache1.addEntryToCache((String) request.get("busqueda"), (String) request.get("respuesta"));
                        } else {
                            if (hash(b) % 3 == 1) {
                                lru_cache2.addEntryToCache((String) request.get("busqueda"), (String) request.get("respuesta"));

                            } else {
                                lru_cache3.addEntryToCache((String) request.get("busqueda"), (String) request.get("respuesta"));

                            }
                        }

                    }
                } //***************************************************************
                else {

                    // Para cada request del arreglo se distribuye
                    // en Cache 1 2 o 3 según su hash.
                    JSONObject respuesta = new JSONObject();
                    if (hash(b) % 3 == 0) {
                        respuesta = hilox1.fn(request);  //Y corre la función de una hebra.
                    } else {
                        if (hash(b) % 3 == 1) {
                            respuesta = hilox2.fn(request);
                        } else {
                            respuesta = hilox3.fn(request);
                        }
                    }

                    //RESPONDER DESDE EL SERVIDOR
                    ObjectOutputStream resp = new ObjectOutputStream(clienteNuevo.getOutputStream());// obtengo el output del cliente para mandarle un msj
                    resp.writeObject(respuesta);
                    System.out.println("msj enviado desde el servidor");

                    //clienteNuevo.close();
                    //servidor.close();
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
