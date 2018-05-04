package ServerSide;

import com.google.gson.Gson;
import java.net.*;
import java.util.*;
import java.io.*;
import java.sql.Connection;
import javax.websocket.Session;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class EndpointUpdater implements Runnable{
    private Session session;
    public boolean running = true;

    //***NOT-IN-USE***
    //This was the old way of retreiving "realtime" updates, the run method would
    //send the result of this method every three seconds.
    synchronized public String getCurrentSQLValues() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sensorlogs?autoReconnect=true&useSSL=false", "iot17", "nackademin123");
        Statement stmt = (Statement) con.createStatement();
        //Get all the latest values from the table,
        ResultSet rs = stmt.executeQuery("SELECT * FROM livetempdata;");
        
        /*ENCODING PART*/
        ArrayList<String> result = new ArrayList<>(); //this will be sent to the JavaScript websocket
        String name;
        double val;
        while(rs.next()){
            name = rs.getString("name");
            val = rs.getDouble("value");
            //String together = name + String.valueOf(val);
            result.add(name);
            result.add(String.valueOf(val));
        }
        con.close();
        Gson gson = new Gson();
        String jsonString = gson.toJson(result);
        
        return jsonString;
        /*ENCODING PART*/
    }
    
    
    public EndpointUpdater(Session session){
        this.session = session;
    }
    
    @Override
    public void run() {
        try {
            //Create a socket to the DataCollectorServer
            Socket s = new Socket(InetAddress.getByName("huerty.com"), 44321); //Connects to itself

            PrintWriter out = new PrintWriter(s.getOutputStream()); //Uses this outstream once, to inform the DataServer.
            
            ObjectInputStream in = new ObjectInputStream(s.getInputStream()); //This is the instream we get updates from
            
            out.println("WEBSOCKET"); //This is to tell the DataServer that this Socket was created by a JS WebSocket
            out.flush();
            while(this.session.isOpen()){
                String read = (String) in.readObject(); //This is where the magic happens. Realtime updates. 
                
                Gson gson = new Gson();
                ArrayList<String> data = new ArrayList<>();
                data.add(read);
                String jsonData = gson.toJson(data);
                
                this.session.getBasicRemote().sendObject(jsonData); //This is where the magic happens. Realtime updates. 
                //As soon as a sensor sends something to the DataServer, this 'WebService' sends it back as a message to the JS WebSocket.
            }
            //gets here if the session.isOpen() evaluates to false
            s.close(); //if the session was closed by 'onClose' in ClientEndpoint then close the socket to the dataserver!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}