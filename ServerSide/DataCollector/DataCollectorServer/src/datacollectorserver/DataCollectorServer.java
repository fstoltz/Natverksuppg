package datacollectorserver;


import java.net.*;
import java.util.*;
import java.io.*;
import java.sql.SQLException;


/*This is the file where a serverSocket will be created,
when a socket is returned, this program will immediately
instansiate a DataGiverHandle and pass the socket as
a parameter to the constructor and the MySQL object, and go back to listening again.
*/

public class DataCollectorServer {
    int listeningPort = 44321;
    SQLHandle sqlHandle;
    ServerSocket serverSocket;
    Master m;
    InputControl ic;
    
    public DataCollectorServer() throws SQLException{
        this.sqlHandle = new SQLHandle();
        this.m = new Master();
        this.ic = new InputControl(this.m);
    }
    
    
    public void startServer() throws IOException{
        this.serverSocket = new ServerSocket(this.listeningPort);
        Thread icThread = new Thread(this.ic);
        icThread.start(); //This provides functionality for typing 'QUIT' into stdin
                          //Which will safely close all sockets before closing the program(not necessary)
        System.out.println("Started InputControl thread..");
        
        while(true){
            System.out.println("> Listening on "+this.listeningPort+"...");
            Socket dataGiverSocket = this.serverSocket.accept(); //datagiversocket or a websocketupdatersocket
            System.out.println("> Received a socket! Creating new thread...");
            DataGiverHandle dataGiverHandle = new DataGiverHandle(dataGiverSocket, this.sqlHandle, this.m);
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