package datacollectorserver;


import java.net.*;
import java.util.*;
import java.io.*;

public class SimulatedDataGiver {
    Socket socketToServer;
    PrintWriter out;
    
    public SimulatedDataGiver() throws UnknownHostException, IOException{
        this.socketToServer = new Socket(InetAddress.getByName("huerty.com"), 44321);
        this.out = new PrintWriter(socketToServer.getOutputStream());
    }
    
    public void startGeneratingData() throws InterruptedException{
        out.println("Simulated:37.37");
        Thread.sleep(5000);
    }
    
    
    public static void main(String[] args) throws IOException, InterruptedException{
        SimulatedDataGiver sim = new SimulatedDataGiver();
        sim.startGeneratingData();
    }
}
