# TallerDocker

En el presente desarrollo de practica se realizo un proceso de virtualizacion entre cliente y servidores en Java Maven, primero se extrae las depedencias que 
se van a necesitar en este proyecto por medio del archivo POM como depedencias y plugins pertenecientes a la configuracion sparkWeb. Y de esta manera sea posible
de realizar la siguiente arquitectura.

![image](https://user-images.githubusercontent.com/71477601/139508677-d27db406-8e9f-4456-a20b-8ab61aaa3def.png)

Como Primer paso se establecio por medio del siguiente codigo la implementacion del LB Robin  que es un servidor WEB que cuya funcion cumple en realizar el balance de 
cargas para los diferentes puertos de salida. 


package com.mycompany.logfacaderoundrobin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import static spark.Spark.get;
import static spark.Spark.port;

/**
 *
 * @author pc
 */
public class LogFacadeService {

    public static void main(String[] args) {
        port(4000);
        get("/logfacade", (req, res) -> roundRobinFacadeDelegation(req, res));
    }

    public static String roundRobinFacadeDelegation(Request req, Response res) {
        
        try {
            String serviceUrlStr = "http://localhost:4567/logmsg?msg=";
            serviceUrlStr = serviceUrlStr + req.queryParams("msg");
            URL serviceUrl = new URL(serviceUrlStr);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(serviceUrl.openStream()));

            String inputLine;
           String  resp = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                resp = resp + inputLine;
            }
            in.close();
            return resp;
        } catch (MalformedURLException ex) {
            Logger.getLogger(LogFacadeService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LogFacadeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Something Bad Happened";
    }
}


Y en el siguiente codigo de archivo se realizo el diseño de log service que es la configuracion de cada uno de los puertos donde se recibe la informacion de manera secuencial 
para cada una de las direcciones de los puertos. 


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


Al realizar la ejecucion de estos archivos y emitiendo un mensaje vemos el siguiente resultado de prueba emitiendo lo mismo para cada uno de los puertos.



![image](https://user-images.githubusercontent.com/71477601/139509376-94ece4b0-7c60-4c93-9414-96333119b5e1.png)

![image](https://user-images.githubusercontent.com/71477601/139509641-a970f92b-d4ca-47e7-b25f-d556714cceab.png)



Obtiendo un desarrollo exitoso en la respectiva ejecucion en los codigos anteriores.



Para realizar la comunicacion del cliente se programara un front-end representado en un formulario hecho en lenguaje HTML que se observa acontinuacion, donde 
el cliente debera ingresar el respectivo mensaje que sera procesado en el backend transmitido en diferentes instancias y posteriormente sera almacenado en una base de datos.


![image](https://user-images.githubusercontent.com/71477601/139520242-ae3649a9-b339-4fc2-8896-c093a0787407.png)













