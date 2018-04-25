package datacollectorserver;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.net.*;
import java.util.*;
import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;



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
        this.con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sensorlogs", "root", "nacka17");
    }
    
    //Synchronized so there are no collisions between Giver threads
    synchronized public void parseInput(String giverString) throws SQLException{
        
        
        boolean pastColon = false;
        String nameOfSender = "";
        String temperatureVal = "";
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
        
        System.out.println("Name of Sender: " + nameOfSender);
        System.out.println("Value: " + temperatureVal);
        
        Statement stmt = (Statement) con.createStatement();
        stmt.executeUpdate("INSERT INTO `sensorlogs`.`tempdata` (`name`, `value`) VALUES ('"+nameOfSender+"', '"+temperatureVal+"');");
        stmt.executeUpdate("UPDATE `sensorlogs`.`livetempdata` SET `value`='"+temperatureVal+"' WHERE `name`='"+nameOfSender+"';");
    }
    
    public static void main(String[] args) throws SQLException{
        SQLHandle sql = new SQLHandle();
        sql.parseInput("Anton:41.36");
        sql.parseInput("Kevin:83.31");
    }
    
}
