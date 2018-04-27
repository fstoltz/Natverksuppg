package datacollectorserver;

import java.net.*;
import java.util.*;
import java.io.*;

public class Master {
    //list of outstreams to the instreams at EndpointUpdater(websockets)
    List<ObjectOutputStream> listOfOutStreams = new ArrayList<>();
    
    
    
    public void addOutputStream(ObjectOutputStream newStream){
        listOfOutStreams.add(newStream);
    }
    
    
        
    public void sendToEveryone(String clientString) throws IOException{
        int count = 0;
        for (ObjectOutputStream out : listOfOutStreams) {
            out.writeObject(clientString);
            out.flush();
            count++;
        }
        System.out.println("Sent a message to " + count + "streams.");
    }
}
