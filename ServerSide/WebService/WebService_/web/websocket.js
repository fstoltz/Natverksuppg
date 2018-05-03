var ws;

var value;
var name;
var pastColon = 0;
var connected = 0;
var dataSenders = [];

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
    console.log(document.getElementById("searchTxt").value);
    
    
    var myObj = { "sensorName":name };
    var myJSON = JSON.stringify(myObj);
    console.log(myJSON);
    ws.send(myJSON);
    
}



function handleMessage(event){
    json = JSON.parse(event.data);
    console.log(event);
    console.log(event.data);
    
    if(json[0] == "HISTORY_INC"){
        //handle building up a table etc, filling with data
        var index = json.indexOf("HISTORY_INC");  //Removes the "HISTORY_INC" element from the array
        if (index > -1) {
            json.splice(index, 1);
        }

        console.log(json);
        var numberOfRows = (json.length/3);
        var i;
        for(i = 0; i < json.length;){
            addRows(json[i], json[i+1], json[i+2]);
            i = i + 3;
        }
        
    } else { //it's a normal update
        console.log("NORMAL UPDATE");
    
    
    //får in ett värde varje gång,
    //Tiitar på strängen fram till :
    
    //Titta i dataSenderlistan
    //finns namnet ?
    //ja lägg in strängen på samma ställe
    //nej addera strängen till dataSenrers listan
    
    ///Skriver ut varje element i listan.
    //Klä
    /*
    for name in dataSenders
        om event.Namnet finns i dataSenders
            uppdatera dens värde
        annars
            lägg till den i dataSenders
            samt skriv nytt värde..
           
    
    
    dataSenders.add(dataSändarensNamn)
    
    */
    var pastColon = false;
    var name = "";
    var value = "";
    var i;
    var c;
    for(i = 0; i < event.data.length; i++){
        console.log(event.data.charAt(i));
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
        //name += c;
        
    }
    console.log("NAME: " + String(name));
    console.log("VALUE: " + String(value));
    
    if((document.getElementById(name)) == null){
        var p = document.createElement("p"); // Create a <p> element
        p.id = name;
        p.className = "realTime";
        var t = document.createTextNode(name + " ");       // Create a text node
        var t2 = document.createTextNode(value);       // Create a text node
        p.appendChild(t);                                // Append the text to <button>
        p.appendChild(t2);
        var element = document.getElementById("realTimeValues");
        element.appendChild(p);                    // Append <p> to <body>
    } else {
        var element = document.getElementById(name);
        element.innerHTML = name + " " + value;
    }
    
    /*var entry = document.createElement("h3");
    entry.innerHTML = event.data;
    var realTimeDiv = document.getElementById("realTimeValues");
    
    realTimeDiv.insertBefore(entry, realTimeDiv.childNodes[0]);*/
    }
}




function connect(){
    if(connected == 0){
        ws = new WebSocket("ws://2.248.81.16:8080/WebService_new/panel");
        console.log("Connected to WebSocket!");
        ws.onmessage = handleMessage;
        document.getElementById("connectButton").innerHTML = "Connected!";
        document.getElementById("connectButton").style = "color:cyan";
        connected = 1;
    }
}




/*
 } else { //it's a normal update
        console.log(json.length);
        var j;
        var list = document.getElementById("realTime");
        var sq = document.getElementById("everything")
        if(pressedConnect == 1){
            
            for(j = 0; j < json.length;){
                var realTimeEntry = document.createElement("p");
                var data = document.createTextNode(json[j] + "  " + json[j+1]);
                realTimeEntry.id = numberOfEntries;
                realTimeEntry.className = "realTimeValues";
                realTimeEntry.appendChild(data);
                var linebreak = document.createElement("br");
                realTimeEntry.appendChild(linebreak);
                //document.body.appendChild(btn);
                list.insertBefore(realTimeEntry, list.childNodes[0]);
                j = j + 2; //because we want to jump two steps each iteration. --Name Value-- --Name Value---
                numberOfEntries++;
            }
            var realTimeHeader = document.createElement("h2");


*/


















/*var ws;
var pressedConnect = 0;
var isConnected = 0;
var numberOfEntries = 0;

function connect(){
    if(isConnected == 0){
        ws = new WebSocket("ws://2.248.81.16:8080/WebService_/panel");
        console.log("Connected to WebSocket!");
        pressedConnect = 1;
        isConnected = 1;
        document.getElementById("connectButton").innerHTML = "Connected!";
        document.getElementById("connectButton").style.color = "#7FFF00";
        ws.onmessage = handleMessage;
    }
}



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

function handleMessage(event){
    json = JSON.parse(event.data);

    
    
    if(json[0] == "HISTORY_INC"){
        //handle building up a table etc, filling with data
        var index = json.indexOf("HISTORY_INC");  //Removes the "HISTORY_INC" element from the array
        if (index > -1) {
            json.splice(index, 1);
        }

        console.log(json);
        var numberOfRows = (json.length/3);
        var i;
        for(i = 0; i < json.length;){
            addRows(json[i], json[i+1], json[i+2]);
            i = i + 3;
        }
        
    } else { //it's a normal update
        console.log(json.length);
        var j;
        var list = document.getElementById("realTime");
        var sq = document.getElementById("everything")
        if(pressedConnect == 1){
            
            for(j = 0; j < json.length;){
                var realTimeEntry = document.createElement("p");
                var data = document.createTextNode(json[j] + "  " + json[j+1]);
                realTimeEntry.id = numberOfEntries;
                realTimeEntry.className = "realTimeValues";
                realTimeEntry.appendChild(data);
                var linebreak = document.createElement("br");
                realTimeEntry.appendChild(linebreak);
                //document.body.appendChild(btn);
                list.insertBefore(realTimeEntry, list.childNodes[0]);
                j = j + 2; //because we want to jump two steps each iteration. --Name Value-- --Name Value---
                numberOfEntries++;
            }
            var realTimeHeader = document.createElement("h2");
            realTimeHeader.innerHTML = "REALTIME VALUES";
            realTimeHeader.id = "realTimeTitle";
            sq.insertBefore(realTimeHeader, sq.childNodes[0]);
            pressedConnect = 0;
        } else {
            console.log(numberOfEntries);
            var c;
            var a = 0;
            for(c = 0; c < numberOfEntries; c++){
                document.getElementById(c).innerHTML = json[a] + " " + json[a+1] + "<br>";
                a = a + 2;
            }
        }
        */
        /*var realTimeDiv = document.createElement("div"); 
        var c;
        for(c = 0; c < json.length; i++){
            var btn = document.createElement("BUTTON");
            var t = document.createTextNode("CLICK ME");
            btn.appendChild(t);
            document.body.appendChild(btn);
        }*/
        /*
        document.getElementById("realTime1Name").innerHTML=json[0]
        document.getElementById("realTime1Val").innerHTML=json[1]+" celsius";
        document.getElementById("realTime2Name").innerHTML=json[2]
        document.getElementById("realTime2Val").innerHTML=json[3]+" celsius";
        document.getElementById("realTime3Name").innerHTML=json[4]
        document.getElementById("realTime3Val").innerHTML=json[5]+" celsius";
       
        //document.getElementById("realTime2").innerHTML=json[2]+" "+json[3]+" celsius";
        //document.getElementById("realTime3").innerHTML=json[4]+" "+json[5]+" celsius";
    }
}

function getHistory(){
    ws.send("GET_HISTORY");
}

 */




/*


var ws;
var newMessage;
function connect(){
    ws = new WebSocket("ws://2.248.81.16:8080/WebService_/panel");

    ws.onmessage = function(event) {
        console.log(event.data);


        newMessage = JSON.parse(event.data);

        if (newMessage[0] != "HISTORY_INC"){

        document.getElementById("textbox1").innerHTML=newMessage[0]+" "+newMessage[1]+"Grader";
        document.getElementById("textbox2").innerHTML=newMessage[2]+" "+newMessage[3]+"Grader";
        document.getElementById("textbox3").innerHTML=newMessage[4]+" "+newMessage[5]+"Grader";
        }
        else{

        var table = document.getElementById("myTable");
        var i;
        
        for (i = 0; i < newMessage.length; i++) { 
            var row = table.insertRow(0);
            var cell1 = row.insertCell(i);
            var cell2 = row.insertCell(i+1);
            var cell3 = row.insertCell(i+2);
            console.log(newMessage[i]);
            cell1.innerHTML = newMessage[i];
            cell2.innerHTML = newMessage[i+1];
            cell3.inserHTML = newMessage[i+2]+"<br>";
            var row = table.insertRow(0);

            }
        }



    }
    }

    function historicalData(){

      document.getElementById("historiskTextbox1").innerHTML="Satani";

    }
   function  getHistry(){
        ws.send("hej");
        //document.getElementById("hinstoryTextbox").innerHTML=newMessage;
   }


*/  
