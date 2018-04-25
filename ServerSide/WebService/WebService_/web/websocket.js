var ws;

function connect(){
    ws = new WebSocket("ws://localhost:8080/WebService_/panel");
    
    ws.onmessage = function(event) {
        console.log(event.data);
    }
    
}