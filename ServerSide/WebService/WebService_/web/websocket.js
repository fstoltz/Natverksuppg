var ws;
var value;
var name;
var pastColon = 0;
var connected = 0;

function addRows(name, val, timestamp) {
    var table = document.getElementById("historyTable");
    var row = table.insertRow(0);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    cell1.innerHTML = name;
    cell2.innerHTML = val;
    cell2.id = "tempVal";
    cell3.innerHTML = timestamp;
}

function sendName(){
    var name;
    name = document.getElementById("searchTxt").value;
    
    var myObj = { "sensorName":name };
    var myJSON = JSON.stringify(myObj);
    console.log(myJSON);
    ws.send(myJSON);
}



function handleMessage(event){
    json = JSON.parse(event.data);
    console.log(event.data);
    
    if(json[0] == "HISTORY_INC"){
        //handle building up a table etc, filling with data
        var index = json.indexOf("HISTORY_INC");  //Removes the "HISTORY_INC" element from the array
        if (index > -1) {
            json.splice(index, 1); //Remove the HISTORY_INC element and reindex & reorder the array
        }

        console.log(json);
        var i;
        for(i = 0; i < json.length;){
            addRows(json[i], json[i+1], json[i+2]); //Extracts the name, the value, the timestamp
            i = i + 3; //Take three steps forward, to the next SQL row
        }
    } else { //it's a normal update(real-time from a sensor)
        console.log("Incoming real-time update.");
        //Below is the parsing of the string format "Anton:23.76"
        var pastColon = false;
        var name = "";
        var value = "";
        var i;
        var c;
        for(i = 0; i < event.data.length; i++){
            //console.log(event.data.charAt(i));
            c = event.data.charAt(i);
            if(c == '[' || c == ']' || c == '"'){
                continue;
            }
            if(pastColon == true){
                value += c;
                continue;
            }
            if(c == ':'){
                pastColon = true;
            } else {
                name += c;
            }
        }
        console.log("NAME: " + String(name));
        console.log("VALUE: " + String(value));

        if((document.getElementById(name)) == null){
            var p = document.createElement("p"); // Create a <p> element
            p.id = name;
            p.className = "realTime";
            var t = document.createTextNode(name + " ");       // Create a text node
            var t2 = document.createTextNode(value);       // Create a text node
            p.appendChild(t);                                
            p.appendChild(t2);
            var element = document.getElementById("realTimeValues");
            element.appendChild(p);                    // Append <p> to <body>
        } else {
            var element = document.getElementById(name);
            element.innerHTML = name + " " + value;
        }
    }
}


function connect(){
    if(connected == 0){
        ws = new WebSocket("ws://2.248.81.16:8080/WebService_new/panel"); //Connect to the ServerEndpoint class that's hosted by Glassfish
        console.log("Connected to WebSocket!");
        ws.onmessage = handleMessage; //Set the onMessage function to point to handleMessage
        document.getElementById("connectButton").innerHTML = "Connected!";
        document.getElementById("connectButton").style = "color:#00ff00";
        connected = 1; //Prevent user from creating multiple WebSockets from one tab
    }
}