/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.logfacaderoundrobin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

/**
 *
 * @author pc
 */
public class LogFacadeService {

    static int currentService = 0;
    static List<String> serviceList = new ArrayList(); 
    static final int numberOfServices = 3;
    public static void main(String[] args) {

        serviceList.add("http://localhost:4567/logmsg?msg=");
        serviceList.add("http://localhost:4568/logmsg?msg=");
        serviceList.add("http://localhost:4569/logmsg?msg=");
        
        staticFiles.location("public");
        port(4000);
        get("/logfacade", (req, res) -> roundRobinFacadeDelegation(req, res));
    }

    public static String roundRobinFacadeDelegation(Request req, Response res) {
       
        System.out.println("Calling Service on:" + serviceList.get(currentService));
        String resp = CallDelegatedService(serviceList.get(currentService), req, res);
        currentService++;

        if (currentService > (numberOfServices - 1)) {

            currentService = 0;

        }

        return resp;
    }

    public static String CallDelegatedService(String urlStr, Request req, Response res) {
        try {
            String serviceUrlStr = urlStr;
            serviceUrlStr = serviceUrlStr + req.queryParams("msg");
            URL serviceUrl = new URL(serviceUrlStr);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(serviceUrl.openStream()));

            String inputLine;
            String resp = "";
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
