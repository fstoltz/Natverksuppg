package datacollectorserver;

import java.net.*;
import java.util.*;
import java.io.*;

public class Master {
    //list of outstreams to the instreams at EndpointUpdater(websockets)
    List<ObjectOutputStream> listOfOutStreams = new ArrayList<>();
    List<Socket> listOfSockets = new ArrayList<>();

    
    public void closeAllSockets() throws IOException{
        System.out.println(">>> ATTEMPTING TO CLOSE OUTPUTSTREAMS...");
        int i = 0;
        for(Socket s : this.listOfSockets){
            s.close();
            System.out.println("Socket: " + i + " closed.");
            i++;
        }
        System.out.println(">>> CLOSED ALL STREAMS.");
        this.listOfOutStreams.clear();
        this.listOfSockets.clear();
    }

    
    public void addSocket(Socket s){
        this.listOfSockets.add(s);
    }

    
    public void addOutputStream(ObjectOutputStream newStream){
        this.listOfOutStreams.add(newStream);
    }
    
    
    //synchronized...??
    public void sendToEveryone(String clientString) throws IOException{
        try {
            int count = 0;
            for (ObjectOutputStream out : this.listOfOutStreams) {
                try {
                    out.writeObject(clientString);
                    out.flush();
                    count++;
                } catch (SocketException se) {
                    System.out.println("This is here1.");
                    this.listOfOutStreams.remove(out);
                    out.close();
                    se.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Eaaww.");
                    this.listOfOutStreams.remove(out);
                    out.close();
                    e.printStackTrace();
                }
            }
        System.out.println("> Sent a message to " + count + " streams.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}