/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cache;

import static Cache.Servidor.cestatico;
import static Cache.Servidor.lru_cache1;
import org.json.simple.JSONObject;

/**
 * @author Xiao
 */
public class Cache1 extends Thread {

    int numHilo;
    String consulta;

    public Cache1() {
    }

    public Cache1(String q) {

        //numHilo = num;
        consulta = q;
    }

    public JSONObject fn(JSONObject request) {

        //String http_method = null;
        String string_busqueda = (String)request.get("busqueda");
        JSONObject respuesta = new JSONObject();
        
        
        
        
       

                    //OPERACIONES CON CACHE
        //=======================================================================================================
        // La hebra busca en la partición estática del cache.
        // En caso de no encontrar la entrada, busca en la partición LRU correspondiente.
        // Si queda espacio en el cache estático, la entrada se agrega al mismo.
        // Si no se encuentra en el LRU, se pide al IndexService.
        
        String estaEnCacheEstatico = cestatico.getEntryFromCache(string_busqueda); // Buscar en el cache estático
        
        
        
        if (estaEnCacheEstatico == null) { // Si no esta en el cache estatico
            int lleno = cestatico.lleno();
            
             //************* MISS EN CACHE ESTATICO DEVUELVOO MISS*********************
            if (lleno == 0)// y aún no esta lleno, agregar al mismo.
            {
                System.out.println("Miss en cache estático");
                respuesta.put("id",string_busqueda);
                respuesta.put("estado", "miss");
                respuesta.put("respuesta","");
             //***********************************************************
            } else //si no esta en el cache estatico y este esta lleno entonces pasamos al dinamico 
            {

                estaEnCacheEstatico = lru_cache1.getEntryFromCache(string_busqueda);// busco en el cache el id del request
                //*******************MISS EN EL CACHE DINAMICO RETORNO MISS************************
                if (estaEnCacheEstatico == null) { // MISS
                    System.out.println("MISS :(");

                    respuesta.put("id",string_busqueda);
                    respuesta.put("estado", "miss");
                    respuesta.put("respuesta","");
                    
                    //****************************************************************

                    // **********SI ESTA EN EL CACHE dinamico******************************
                } else {
                    respuesta.put("estado", "hit");
                    respuesta.put("id", string_busqueda);
                    respuesta.put("respuesta", estaEnCacheEstatico);
                    System.out.println("HIT!");

                }
                //****************************************************************
                lru_cache1.print(1);
                System.out.println("");

            }
        } else// si esta en el cache estatico
        {
            System.out.println("Hit en el cache estatico");
            respuesta.put("estado", "hit");
            respuesta.put("id", string_busqueda);
            respuesta.put("respuesta", estaEnCacheEstatico);
            
            cestatico.print();
        }
        
        return respuesta;

    }
    
                    //=======================================================================================================
}

