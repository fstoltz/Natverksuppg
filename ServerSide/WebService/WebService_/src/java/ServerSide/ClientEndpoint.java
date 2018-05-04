package ServerSide;

import javax.json.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
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
import javax.json.Json;

/*
This class will work as the WebService, it will return generated content
to the webbrowser, this data is retrieved from the MySQL Server / DataServer.

OLD--->This service will automatically retrieve latest values from the SQL tables
every 10 seconds(?) and broadcast it to all connected clients. This way all
browsers will view real-time data coming from the sensors. The clients will
receive the updates via JavaScript websockets and DOM manipulation.

NEW--->A thread is created at onOpen and this thread performs interprocess communication via TCP/IP
on the local machine to the DataServer, the thread is always listening for messages and proxies each message received
directly to the JavaScript WebSocket, thereby achieving close to real-time updates at the browser.
*/
@ServerEndpoint("/panel")
public class ClientEndpoint {
    private Session session;
    //static here means that all objects will share the same variable, meaning everyone has access to the List of connected clients
    private static final Set<ClientEndpoint> endpoints = new CopyOnWriteArraySet<>();
    
    @OnOpen
    public void onOpen(Session session) throws IOException{
        /*Takes care of starting a new thread that handles real-time updates for that session*/
        this.session = session;
        endpoints.add(this);
        EndpointUpdater endpUpdater = new EndpointUpdater(session); //Create a endpointupdater, give access to the session object
        Thread endpUpdaterThread = new Thread(endpUpdater); //Creates a new thread
        endpUpdaterThread.start(); //Initiates the 'run' method
    }
    
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException, SQLException{
        /*This method will probably be the place where
        a request for 'Historical Values' are recevied from
        the client, and then we retreive values from the SQL
        database and send these back to the client(using sendToEndpoint)
        where DOM manipulation is made to fill the html table with relevant data*/
        
        //Extract the name of the sensor entered by the browser input form
        String username = "";
        try (javax.json.JsonReader reader = Json.createReader(new StringReader(message))) {
            javax.json.JsonObject jsonMessage = reader.readObject();
            username = jsonMessage.getString("sensorName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sensorlogs?autoReconnect=true&useSSL=false", "iot17", "nackademin123");
        Statement stmt = (Statement) con.createStatement();
        //ResultSet rs = stmt.executeQuery("SELECT * FROM tempdata WHERE name='"+username+"';");
        ResultSet rs = stmt.executeQuery("SELECT MIN(timestamp) as timestamp, MIN(name) as name, MIN(value) as value FROM tempdata WHERE name='"+username+"' GROUP BY DATE(timestamp), HOUR(timestamp);");
        
        ArrayList<String> result = new ArrayList<>(); //this will be sent to the JavaScript websocket
        result.add("HISTORY_INC"); //Add this element in order to inform the WebSocket
                                   //that this message is the result of a history retrieval event

        String name;
        double val;
        Timestamp date;
        while(rs.next()){
            name = rs.getString("name");
            val = rs.getDouble("value");
            date = rs.getTimestamp("timestamp");
            
            //Adds the values in each row to the array
            result.add(name);
            result.add(String.valueOf(val));
            result.add(date.toString());
        }

        con.close(); //Close the connection to the MySQL server
        Gson gson = new Gson();
        String jsonString = gson.toJson(result); //Transform the result arraylist to a JSON string
        this.session.getBasicRemote().sendObject(jsonString); //sending all history for specific sensor to the WebSocket
    }
    
    @OnClose
    public void onClose(Session session) throws IOException{
        /*The code enters here when a client closes their window/application*/
        endpoints.remove(this.session);
        this.session.close(); //Might be overkill considering it went in here because the WebSocket was closed.
    }
    
    @OnError
    public void onError(Throwable error){
        /*NO USAGE AT THE MOMENT*/
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
}
