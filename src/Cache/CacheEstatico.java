/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cache;

import java.util.LinkedHashMap;
import java.util.HashMap;

/**
 *
 * @author Xiao
 */
public class CacheEstatico {
    
    int size;
    LinkedHashMap <String, String> cacheE;
    
    public CacheEstatico(int size) { //Constructor
        this.size = size;
        this.cacheE = new LinkedHashMap<>();
    }
    
    // Método para encontrar una entrada en el cache
    public String getEntryFromCache(String query) {
        String result = cacheE.get(query);
        if(result != null) {
            cacheE.remove(query);
            cacheE.put(query, result);
        }
        return result;
    }
    // Método para saber si el cache está lleno.
    public int lleno()
    {
        if(cacheE.size() == this.size){// esta lleno
            return 1;
        }
        else{
            return 0;
        }
    }
    // Método para ingresar una entrada al cache.
    public void addEntryToCache(String query, String answer) {
        if (cacheE.containsKey(query)) { // HIT
            
            System.out.println("Hit Estático");
        }else{ // MISS
            
            if(cacheE.size() == this.size){// cache lleno
                // ir a cache dinamico
                System.out.println("cache lleno");
            }
            else{
                cacheE.put(query, answer);
            }
        }
    }
    // Método para imprimir por consola las entradas en el cache
    public void print() {
        System.out.println("===== My StaticCache =====");
        System.out.println(cacheE.entrySet());
        System.out.println("========================");
    }
}