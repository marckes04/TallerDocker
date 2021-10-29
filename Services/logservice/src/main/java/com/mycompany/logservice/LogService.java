
package com.mycompany.logservice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import static spark.Spark.*;




public class LogService {
    
    public static Map<String,String> imdb = new HashMap();
    
    public static void main(String[] args) {
        port(getPort());
        get("/logmsg", (req, res) -> storeLogMessage(req,res));
    }
    
    public static String storeLogMessage(Request req, Response res)
    {
        String msg = req.queryParams("msg");
        String key = LocalDate.now().toString();
        imdb.put(key,msg);
        return "Message Stored: " + msg;
    }
    
    public static Integer getPort()
    {
    if(System.getenv("PORT") != null)
    {
        return Integer.parseInt(System.getenv("PORT"));
    
    }
    
    return 4567;
    
    }
   
}