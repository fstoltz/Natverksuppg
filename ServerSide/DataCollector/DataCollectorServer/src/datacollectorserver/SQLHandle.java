package datacollectorserver;


import com.mysql.jdbc.Connection;
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
        this.con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/climatelogs", "root", "nacka17");
    }
    
    //Synchronized so there are no collisions between Giver threads
    synchronized public void parseInput(String giverString){
        
    }
    
    
    
}
