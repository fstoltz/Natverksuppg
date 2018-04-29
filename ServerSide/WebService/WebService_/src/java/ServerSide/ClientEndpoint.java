package ServerSide;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.net.*;
import java.util.*;
import java.io.*;

/*
This class will work as the WebService, it will return generated content
to the webbrowser, this data is retrieved from the MySQL Server.

This service will automatically retrieve latest values from the SQL tables
every 10 seconds(?) and broadcast it to all connected clients. This way all
browsers will view real-time data coming from the sensors. The clients will
receive the updates via JavaScript websockets and DOM manipulation.
*/
@ServerEndpoint("/panel")
public class ClientEndpoint {
    private Session session;
    //static here means that all objects will share the same variable, meaning everyone has access to the List of connected clients
    private static final Set<ClientEndpoint> endpoints = new CopyOnWriteArraySet<>();
    public static List<EndpointUpdater> endpList = new ArrayList<>();
    
    @OnOpen
    public void onOpen(Session session) throws IOException{
        /*Maybe here we could start a create a new object that takes care of
        the continues updates to the client. Another class that implements Runnable
        */
        this.session = session;
        endpoints.add(this);
        EndpointUpdater endpUpdater = new EndpointUpdater(session);
        endpList.add(endpUpdater);
        Thread endpUpdaterThread = new Thread(endpUpdater);
        endpUpdaterThread.start();
    }
    
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException, SQLException{
        /*This method will probably be the place where
        a request for 'Historical Values' are recevied from
        the client, and then we retreive values from the SQL
        database and send these back to the client(using sendToEndpoint)
        where DOM manipulation is made to fill the html table with relevant data*/
        Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sensorlogs?autoReconnect=true&useSSL=false", "iot17", "nackademin123");
        Statement stmt = (Statement) con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tempdata;");
        
        ArrayList<String> result = new ArrayList<>(); //this will be sent to the JavaScript websocket
        
        result.add("HISTORY_INC");

        String name;
        double val;
        Timestamp date;
        while(rs.next()){
            name = rs.getString("name");
            val = rs.getDouble("value");
            date = rs.getTimestamp("timestamp");
            //String together = name + String.valueOf(val);
            result.add(name);
            result.add(String.valueOf(val));
            result.add(date.toString());
        }
        
        con.close();
        Gson gson = new Gson();
        String jsonString = gson.toJson(result);
        
        //this.session.getBasicRemote().sendObject("HISTORY_INC"); //in order for client to know what type of data is coming
        this.session.getBasicRemote().sendObject(jsonString); //sending all history
    }
    
    @OnClose
    public void onClose(Session session) throws IOException{
        /*The code enters here when a client closes their window/application(i think)*/
        endpoints.remove(this.session);
        this.session.close();
        //set running to false for the specific endpointupdater
    }
    
    @OnError
    public void onError(Throwable error){
        /*?Don't know what to use this one for at the moment*/
    }
    
    public void sendToEndpoint(){
        /*This is where we can send the historical data
        to the specific client that requested this.
        This will not broadcast to all connected clients,
        only the one who requested historical data.
        This is where we'll send data to the JavaScript WebSocket
        and then the WebSocket takes these values and uses them
        for created & filling in an HTML table(or something like that)*/
    }
    
    /*
    I'm thinking we will need a broadcast method here,
    this method will continue in an infinite loop, 
    sending latest data to the WebSocket, waiting 10 seconds,
    then sending another broadcast, and so on to all connected clients.
    
    Not sure how this will be implemented, with threads etc.
    Because the code will always be in this part, meaning it has to run
    separate from the rest of the 'Endpoint' code I guess..?
    */
    
}
