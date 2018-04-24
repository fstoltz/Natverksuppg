package datacollectorserver;

import java.net.*;
import java.util.*;
import java.io.*;

/*This class will implement the Runnable interface,
in the run method, it will sit blocked, waiting for the
giver to send data, when it receives data, it should take this data
and insert it into the MySQL Server.

The object that handles the MySQL integration needs to be shared among
all of these threads. This means that the method that does the INSERT
has to be 'synchronized' in order to avoid issues between threads calling
the same method and corrupting something.*/



public class DataGiverHandle {
    
}
