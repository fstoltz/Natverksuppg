package datacollectorserver;

import java.net.*;
import java.util.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//This code needs to be compilable on the home-server, i.e all libraries etc needs to be sorted out.
/*This class will create a connection to the MySQL server(lati).
Will function as a Singleton, i.e only one instance.
The DataCollectorServer should have access to the instance of this
class, so it can pass it to each of the DataGiverHandle so that they
can write data to the MySQL Server.
*/
public class SQLHandle {
    private Connection con;
    
    
    public SQLHandle() throws SQLException{
        //Setup the connection to the MySQL Server
        this.con = (Connection) DriverManager.getConnection("jdbc:mysql://huerty.com:3306/sensorlogs?autoReconnect=true&useSSL=false", "iot17", "nackademin123");
        /*
        Write code here that creates a database if it doesn't exist.
        Then creates all the necessary tables.
        */
    }
    
    //Synchronized so there are no collisions between Giver threads
    synchronized public void parseInput(String giverString) throws SQLException{
        boolean pastColon = false;
        String nameOfSender = "";
        String temperatureVal = "";
        //Parse the giverString to pass appropriate values it into the MySQL database
        for(int i = 0; i < giverString.length(); i++){
            char c = giverString.charAt(i);
            if(c == ':'){
                pastColon = true;
                continue;
            } else if(pastColon == true){
                temperatureVal += c;
            } else {
                nameOfSender += c;
            }
        }
        
        System.out.println(">>> Raw    : " + giverString);
        System.out.println(">>> Sender : " + nameOfSender);
        System.out.println(">>> Value  : " + temperatureVal);
        
        Statement stmt = (Statement) con.createStatement();
        //Inserts into the history-table
        stmt.executeUpdate("INSERT INTO `sensorlogs`.`tempdata` (`name`, `value`) VALUES ('"+nameOfSender+"', '"+temperatureVal+"');");
        
        //Old way of achieving 'real-time'
        //stmt.executeUpdate("UPDATE `sensorlogs`.`livetempdata` SET `value`='"+temperatureVal+"' WHERE `name`='"+nameOfSender+"';");
    }

    
    public static void main(String[] args) throws SQLException{
        SQLHandle sql = new SQLHandle();
        sql.parseInput("Anton:11.36");
        sql.parseInput("Kevin:33.31");
    }
}