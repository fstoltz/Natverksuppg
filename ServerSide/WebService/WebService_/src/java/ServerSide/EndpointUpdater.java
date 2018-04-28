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
    //private static final Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sensorlogs", "root", "nacka17");
    
    synchronized public String getCurrentSQLValues() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sensorlogs?autoReconnect=true&useSSL=false", "iot17", "nackademin123");
        Statement stmt = (Statement) con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM livetempdata;");
        
        //Get all the latest values from the table,
        //insert "Name:21" as an element into the string array that will then 
        //be sent to the client. Then we maybe can decode it somehow client-side
        //or just show it as plain text
        
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
            Socket s = new Socket(InetAddress.getByName("huerty.com"), 44321);
            //We only want to get input from this socket
            PrintWriter out = new PrintWriter(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            out.println("WEBSOCKET");
            out.flush();
            
            while(true){
                this.session.getBasicRemote().sendObject(in.readLine());
                //Thread.sleep(3000);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
