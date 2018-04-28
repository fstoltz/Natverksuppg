package datacollectorserver;

import java.net.*;
import java.util.*;
import java.io.*;

public class Master {
    //list of outstreams to the instreams at EndpointUpdater(websockets)
    List<ObjectOutputStream> listOfOutStreams = new ArrayList<>();
    
    
    //added synchronized because several object might calll this method at the same time
    synchronized public void addOutputStream(ObjectOutputStream newStream){
        this.listOfOutStreams.add(newStream);
    }
    
    
    //added synchronized because several object might calll this method at the same time
    synchronized public void sendToEveryone(String clientString) throws IOException{
        int count = 0;
        for (ObjectOutputStream out : this.listOfOutStreams) {
            out.writeObject(clientString);
            out.flush();
            count++;
        }
        System.out.println("Sent a message to " + count + "streams.");
    }
}
