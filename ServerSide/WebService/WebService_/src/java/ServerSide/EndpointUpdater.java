package ServerSide;

import java.net.*;
import java.util.*;
import java.io.*;
import javax.websocket.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class EndpointUpdater implements Runnable{
    private Session session;
    //private static final Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sensorlogs", "root", "nacka17");
    
    synchronized public String getCurrentSQLValues() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sensorlogs", "root", "nacka17");
        Statement stmt = (Statement) con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM livetempdata;");
        
        //Get all the latest values from the table,
        //insert "Name:21" as an element into the string array that will then 
        //be sent to the client. Then we maybe can decode it somehow client-side
        //or just show it as plain text
        
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
        
        return result.toString();
    }
    
    
    public EndpointUpdater(Session session){
        this.session = session;
    }

    @Override
    public void run() {
        try {
            while(true){
                this.session.getBasicRemote().sendObject(this.getCurrentSQLValues());
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
