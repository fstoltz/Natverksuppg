package ServerSide;

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
    
    synchronized public ArrayList<String> getCurrentSQLValues() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sensorlogs?autoReconnect=true&useSSL=false", "root", "nacka17");
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
            String together = name + String.valueOf(val);
            result.add(together);
        }
        
        con.close();
        
        return result;
        /*ENCODING PART*/
    }
    
    
    public EndpointUpdater(Session session){
        this.session = session;
    }

    @Override
    public void run() {
        try {
            while(true){
                /*What we could potentially do here is that instead of querying the databse for latest values,
                we have a serverSocket listening for input from the sensors DIRECTLY, raw, realtime.. interesting
                maybe all the endpointupdaters can share this same socket thingy, and that 'master' reads in updates
                from all feathers, and as soon as one feather sends something, this master return that name and data
                not sure. instead of having .sleep for 3 seconds, we could maybe integrate a .readLine from an inputstream
                and this inputstream is like a master inputstream, that a master object holds, and as soon as the master
                gets a .readLine from one of the feathers, it delivers this to all of these endpointupdaters and
                then it gets pushed out to the individual client. This setup might make it more real-time, since all
                connected browsers ought to get the same update at the same time, more or less.
                And so when a feather sends something to the dataserver, all that happens is that those values enter the
                historical table, and do not bother with a livetempdata table, since the live values are pushed
                directly to connected clients.*/
                /*More and more I'm thinking we might not even need two separate "Server" projects. Maybe we can integrate
                the datacollectorServer as a thread into this program instead??? and that will be what I above called the 'Master'
                that reads input from all sensors and forwards that to all the endpointupdaters that are listening.*/
                //gets the latest values from the livetempdata table and sends it to this client
                this.session.getBasicRemote().sendObject(this.getCurrentSQLValues());
                //sleeps for 3 seconds before sending another update to this client
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
