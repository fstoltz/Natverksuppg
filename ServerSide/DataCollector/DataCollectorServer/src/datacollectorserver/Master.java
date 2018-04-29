package datacollectorserver;

import java.net.*;
import java.util.*;
import java.io.*;

public class Master {
    //list of outstreams to the instreams at EndpointUpdater(websockets)
    List<ObjectOutputStream> listOfOutStreams = new ArrayList<>();
    List<Socket> listOfSockets = new ArrayList<>();
    /*debug attempt1:
    removing synchronized from both methods*/
    
    
    //added synchronized because several object might calll this method at the same time
    public void addOutputStream(ObjectOutputStream newStream){
        this.listOfOutStreams.add(newStream);
    }
    
    
    //added synchronized because several object might calll this method at the same time
    public void sendToEveryone(String clientString) throws IOException{
        ObjectOutputStream tmp = null;
        try {
            int count = 0;
            
            for (ObjectOutputStream out : this.listOfOutStreams) {
                //System.out.println("clientString: " + clientString);
                tmp = out; //keep track of the outstream incase a exception is thrown
                out.writeObject(clientString);
                out.flush();
                count++;
            }
        System.out.println("> Sent a message to " + count + " streams.\n");
        } catch (SocketException se) { //Incase a Broken Pipe Socket Exception is generated, we assume it's because the browser client has closed the connection and this outstream is not 'active' anymore
            se.printStackTrace();
            this.listOfOutStreams.remove(tmp); //remove this outstream from the list because it's no longer 'active'
            tmp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





/*

java.net.SocketException: Broken pipe (Write failed)
        at java.net.SocketOutputStream.socketWrite0(Native Method)
        at java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:111)
        at java.net.SocketOutputStream.write(SocketOutputStream.java:155)
        at java.io.ObjectOutputStream$BlockDataOutputStream.drain(ObjectOutputStream.java:1877)
        at java.io.ObjectOutputStream$BlockDataOutputStream.flush(ObjectOutputStream.java:1822)
        at java.io.ObjectOutputStream.flush(ObjectOutputStream.java:719)
        at datacollectorserver.Master.sendToEveryone(Master.java:27)
        at datacollectorserver.DataGiverHandle.run(DataGiverHandle.java:60)
        at java.lang.Thread.run(Thread.java:748)
*/