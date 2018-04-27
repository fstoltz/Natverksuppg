package datacollectorserver;

import java.net.*;
import java.util.*;
import java.io.*;

/*This class will implement the Runnable interface,
in the run method, it will sit blocked, waiting for the
giver to send data, when it receives data, it should take this data
and insert it into the MySQL Server.

The object that handles the MySQL integration needs to be shared among
all of these threads. This means that the method that does the INSERT
has to be 'synchronized' in order to avoid issues between threads calling
the same method and corrupting something.*/



public class DataGiverHandle implements Runnable{
    SQLHandle sqlHandle; //Everyone shares this object(is set in the constructor)
    Socket dataGiverSocket;
    
    BufferedReader in; //inputstream from the giver socket
    Master m;
    
    public DataGiverHandle(Socket dataGiverSocket, SQLHandle sqlHandle, Master m) throws IOException{
        this.dataGiverSocket = dataGiverSocket;
        this.sqlHandle = sqlHandle;
        this.in = new BufferedReader(new InputStreamReader(this.dataGiverSocket.getInputStream()));
        this.m = m;
        /*Create a socket outstream here with port 45000, when something gets in, send
        it to localhost at 45000 and also write it to the sql history table*/
    }
    
    
    
    @Override
    public void run(){
        //sit in a loop waiting for input from the socket, take this input
        //and write it into the MySQL Server
        try {
            String giverString;
            while((giverString = in.readLine()) != null){
                
                /*Om stränge är "WEBSOCKET" lägg till denna klient
                till listan av uppkopplade websockets*/
                if(giverString.equalsIgnoreCase("WEBSOCKET")){
                    //m.addOutputStream(this.dataGiverSocket.getOutputSteam());
                    ObjectOutputStream objOut = new ObjectOutputStream(this.dataGiverSocket.getOutputStream());
                    this.m.addOutputStream(objOut);
                } else { //this means the message received is normal data from a sensor
                    this.sqlHandle.parseInput(giverString);
                    this.m.sendToEveryone(giverString);
                }
                
                
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
