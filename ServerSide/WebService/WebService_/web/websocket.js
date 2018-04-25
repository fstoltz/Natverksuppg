var ws;

function connect(){
    ws = new WebSocket("ws://2.248.81.16:8080/WebService_/panel");
    
    ws.onmessage = function(event) {
        console.log(event.data);
    }
    
}