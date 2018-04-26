var ws;


function connect(){
    ws = new WebSocket("ws://2.248.81.16:8080/WebService_/panel");
    console.log("Connected to WebSocket!");
    ws.onmessage = handleMessage;
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
        
    }
}

function getHistory(){
    ws.send("GET_HISTORY");
}






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