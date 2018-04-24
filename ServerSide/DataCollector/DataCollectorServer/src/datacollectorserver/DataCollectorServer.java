package datacollectorserver;


import java.net.*;
import java.util.*;
import java.io.*;


/*This is the file where a serverSocket will be created,
when a socket is returned, this program will immediately
instansiate a DataGiverHandle and pass the socket as
a parameter to the constructor, and go back to listening again.

Potentielly I'll also pass the object that holds the MySQL connection
to the constructor for the DataGiverHandle aswell.
*/

public class DataCollectorServer {
    static int listeningPort = 37500;
    SQLHandle sqlHandle;
    
    public DataCollectorServer(){
        
    }
    
    
    public void startServer(){
        
    }
    
    
    public static void main(String[] args) {
        //create a SQLHandle and setup the connection to the MySQL server
        
        //Create a DataCollectorServer, call the function with
        //the listener, that it will stay in until program exits.
    }
}
