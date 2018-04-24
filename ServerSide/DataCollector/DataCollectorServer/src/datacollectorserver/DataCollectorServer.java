package datacollectorserver;


import java.net.*;
import java.util.*;
import java.io.*;
import java.sql.SQLException;


/*This is the file where a serverSocket will be created,
when a socket is returned, this program will immediately
instansiate a DataGiverHandle and pass the socket as
a parameter to the constructor, and go back to listening again.

Potentielly I'll also pass the object that holds the MySQL connection
to the constructor for the DataGiverHandle aswell.
*/

public class DataCollectorServer {
    int listeningPort = 37500;
    SQLHandle sqlHandle;
    ServerSocket serverSocket;
    
    public DataCollectorServer() throws SQLException{
        this.sqlHandle = new SQLHandle();
    }
    
    
    public void startServer() throws IOException{
        this.serverSocket = new ServerSocket(this.listeningPort);
        
        while(true){
            System.out.println("Listening...");
            Socket dataGiverSocket = this.serverSocket.accept();
            System.out.println("I got a socket!");
            DataGiverHandle dataGiverHandle = new DataGiverHandle(dataGiverSocket, this.sqlHandle);
            Thread newGiverThread = new Thread(dataGiverHandle);
            newGiverThread.start();
        }
    }
    
    
    public static void main(String[] args) throws IOException, SQLException {
        //Create a DataCollectorServer, call the function with
        //the listener, that it will stay in until program exits.
        DataCollectorServer dataCollectorServer = new DataCollectorServer();
        dataCollectorServer.startServer();
    }
}
