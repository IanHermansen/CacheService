/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cache;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xiao
 */
public class IndexService {

    static String[] queries = {"query1", "query2", "query3", "query4", "query5", "query6", "query7", "query8", "query9", "query10", "query11", "query12", "query13", "query14", "query15", "query16", "query17", "query18", "query19", "query20"};
    static String answers[] = {"query1", "answer2", "answer3", "answer4", "answer5", "answer6", "answer7", "answer8", "answer9", "answer10", "answer11", "answer12", "answer13", "answer14", "answer15", "answer16", "answer17", "answer18", "answer19", "answer20"};

   
    public static void post(String query, String newanswer)
    {
        for(int i = 0;i<queries.length;i++)
        {
            if (queries[i].equals(query)) {
                answers[i] = newanswer;
            }
        }
    }

    public static String getEntry(String query) {
        for (int i = 0; i < queries.length; i++) {
            if (queries[i].equals(query)) {
                return answers[i];
            }
        }
        return null;
    }
}