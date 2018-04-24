package datacollectorserver;


import java.net.*;
import java.util.*;
import java.io.*;



/*This class will create a connection to the MySQL server(lati).
Will function as a Singleton, i.e only one instance.
The DataCollectorServer should have access to the instance of this
class, so it can pass it to each of the DataGiverHandle so that they
can write data to the MySQL Server.
*/
public class SQLHandle {
    
    public SQLHandle(){
        //Setup the connection to the MySQL Server
    }
    
    
}
