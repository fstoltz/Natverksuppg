package datacollectorserver;


import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class SimulatedDataGiver {
    Socket socketToServer;
    PrintWriter out;
    Random rand;
    
    public SimulatedDataGiver() throws UnknownHostException, IOException{
        this.socketToServer = new Socket(InetAddress.getByName("huerty.com"), 44321);
        System.out.println("> Connected to the server!");
        this.out = new PrintWriter(socketToServer.getOutputStream());
        this.rand = new Random();
    }
    
    public void startGeneratingData() throws InterruptedException{
        int count = 0;
        while(true){
            float dbl = rand.nextFloat();
            out.println("SIMULATEDDEVICEfred:" + String.valueOf(dbl));
            out.flush();
            count++;
            System.out.println("> Sent an update! Total number of updates sent: " + count);
            Thread.sleep(5000);
        }
    }
    
    
    public static void main(String[] args) throws IOException, InterruptedException{
        SimulatedDataGiver sim = new SimulatedDataGiver();
        sim.startGeneratingData();
    }
}
