package ServerSide;

import javax.websocket.server.*;
import javax.websocket.*;


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
    
}
