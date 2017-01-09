package com.example.karol.wildboartraining.ConnectionPackage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;


public class ClientConnection {
    public static boolean isLogged=false;
    private static SSLConnector sslConnector;
    public static boolean isLogged(){
        return isLogged;
    }
    public static boolean logIn(String Login,String Password) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("message_type", "LoginRequest");
        data.put("login", Login);
        data.put("password", Password);
        JSONObject message = new JSONObject(data);
        String messageString = message.toString();
        try {
            PrintWriter pw = null;
            pw = new PrintWriter(sslConnector.sslsocket.getOutputStream());
            pw.write(messageString);
            pw.write("\n");
            pw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(sslConnector.sslsocket.getInputStream()));
            String serverAnswer = br.readLine();

            JSONObject JSONanswer = new JSONObject(serverAnswer);
            String wynik = JSONanswer.getString("message_type");
            if(!wynik.equals("LoginRequest")){return false;}
            boolean islogged = JSONanswer.getBoolean("islogged");
            isLogged=islogged;
            return  islogged;
        } catch (IOException|JSONException e) {
            System.out.println(e);
            return false;
        }
    }

    public static void main(String[] args) {

        String currentDir = System.getProperty("user.dir")+"/testkeysore.p12";
        System.setProperty("javax.net.ssl.keyStore",currentDir);
        System.setProperty("javax.net.ssl.keyStorePassword","dzikidzik");
        System.setProperty("javax.net.ssl.keyStoreType","PKCS12");
        System.setProperty("javax.net.ssl.trustStore",currentDir);
        System.setProperty("javax.net.ssl.trustStorePassword","dzikidzik");
        System.setProperty("javax.net.ssl.trustStoreType","PKCS12");

        sslConnector = SSLConnector.getInstance();
        try {
            sslConnector.sslsocket.startHandshake();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(logIn("admin", "admin"));
    }
}
