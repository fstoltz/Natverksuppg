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
        var row = table.insertRow(0);
        for (i = 0; i < newMessage.length; i++) { 

            var cell1 = row.insertCell(i);
            var cell2 = row.insertCell(i+1);
            var cell3 = row.insertCell(i+2);
            console.log(newMessage[i]);
            cell1.innerHTML = newMessage[i];
            cell2.innerHTML = newMessage[i+1];
            cell3.inserHTML = newMessage[i+2]+"<br>";

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


    
