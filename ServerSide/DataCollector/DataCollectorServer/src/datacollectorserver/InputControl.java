package datacollectorserver;

import java.net.*;
import java.util.*;
import java.io.*;


public class InputControl implements Runnable{
    Master m;
    
    
    public InputControl(Master m){
        this.m = m;
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting for user input, type 'QUIT' to exit program safely during runtime.");
            Scanner sc = new Scanner(System.in);
            while(true){
                if(sc.nextLine().equalsIgnoreCase("QUIT")){
                    System.out.println("***************************************************");
                    System.out.println("*           SAFELY EXITING PROGRAM...             *");
                    System.out.println("***************************************************");
                    this.m.closeAllSockets(); //close all sockets
                    System.exit(0); //then terminate the whole programs and all threads
                } else {
                    System.out.println("Please enter 'QUIT' to safely exit program.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
