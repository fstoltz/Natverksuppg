package datacollectorserver;

import java.net.*;
import java.util.*;
import java.io.*;

/*This class will implement the Runnable interface,
in the run method, it will sit blocked, waiting for the
giver to send data, when it receives data, it should take this data
and insert it into the MySQL Server and send it to all of Masters outstreams.

The object that handles the MySQL integration needs to be shared among
all of these threads. This means that the method that does the INSERT
has to be 'synchronized' in order to avoid issues between threads calling
the same method and corrupting something.*/

public class DataGiverHandle implements Runnable{
    SQLHandle sqlHandle; //Everyone shares this object(is set in the constructor)
    Socket dataGiverSocket;
    BufferedReader in; //inputstream from the giver socket
    Master m;
    ObjectOutputStream objOut;

    
    public DataGiverHandle(Socket dataGiverSocket, SQLHandle sqlHandle, Master m) throws IOException{
        this.dataGiverSocket = dataGiverSocket;
        this.sqlHandle = sqlHandle; //Everyone shares the same SQL object
        this.objOut = new ObjectOutputStream(this.dataGiverSocket.getOutputStream()); //Creates an outputstream
        this.in = new BufferedReader(new InputStreamReader(this.dataGiverSocket.getInputStream())); //Creates an inputstream
        this.m = m; //Everyone shares the same Master object, which provides functionality for sending data to all outstreams
        this.m.addSocket(dataGiverSocket); //add all sockets to this list, both webjssocket ones and featherdata ones
    }
    
    @Override
    public void run(){
        //sit in a loop waiting for input from the socket, take this input
        //and write it into the MySQL Server and send to it all Endpoint threads(which sends to WebSocket)
        try {
            String giverString;
            while((giverString = in.readLine()) != null){
                //giverString="Anton:23.75" ---> mastern ska skicka ut till alla
                //giverString="WEBSCOKET"   ---> läggas till i listan av webbklienter
                if(giverString.equalsIgnoreCase("WEBSOCKET")){ //This is what EndpointUpdater thread does at start (only happens once per client)
                    this.m.addOutputStream(this.objOut); //Add this outstream to Masters list
                    System.out.println("> Added a 'Endpoint/WebSocket' client!\n");
                } else { //this means we can assume the message received is normal data from a sensor
                    this.sqlHandle.parseInput(giverString); //Send this data to the MySQL History Table
                    this.m.sendToEveryone(giverString); //Send this update to all clients
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}