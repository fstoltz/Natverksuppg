var ws;

function connect(){
    //2.248.81.16
    ws = new WebSocket("ws://2.248.81.16:8080/WebService_/panel");
    
    ws.onmessage = function(event) {
        console.log(event.data);
        
        var myArr = JSON.parse(event.data);
        
        console.log(myArr[0]);
        
        //Funktion fölrr att trycka ut datat på skärmen.
    }
    //Funkltion för att lista historiusk data.
}