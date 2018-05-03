#include <ESP8266WiFi.h>

#define ssid  "TeliaGateway9C-97-26-57-05-5B"
#define pwd  "226DFC4358"
WiFiClient client;
int listeningPort = 44321;
IPAddress connect_2(2,248,81,16);
String namn = "Fredrik_2";  //ÄNDRA TILL DITT EGET NAMN//
// initializes or defines the output pin of the LM35 temperature sensor
int outputpin= A0;


bool try2Connect(){
  if(client.connect("huerty.com",listeningPort)){
    Serial.print("Du ar nu ansluten till: ");
    Serial.print(connect_2);
    return true;
  }
  return false;
  
}


//this sets the ground pin to LOW and the input voltage pin to high
void setup(){
  pinMode(LED_BUILTIN, OUTPUT);
  Serial.begin(115200);
  Serial.println();
  WiFi.begin(ssid,pwd);
  while(WiFi.status() != WL_CONNECTED){
    delay(300);
    Serial.print(".");
  }
  Serial.println("Du ar nu ansluten till natverket!");
  
 while(!try2Connect()){
    delay(2000);
    Serial.println("trying igen....");  
  }
  Serial.println(client.connected());
}
  
  


void loop() {
if(client.status()&& WiFi.status() == WL_CONNECTED){
  int analogValue = analogRead(outputpin);
  float millivolts = (analogValue/1024.0) * 3300; //3300 is the voltage provided by NodeMCU
  float celsius = millivolts/10;
  digitalWrite(LED_BUILTIN, LOW);
  client.println(namn+":"+celsius);
  digitalWrite(LED_BUILTIN, HIGH);
  //Serial.println(val);
  delay(2000);
  }
else{
     if(WiFi.status() != WL_CONNECTED){
         WiFi.begin(ssid,pwd);
         Serial.println(".");
        while(WiFi.status() != WL_CONNECTED){
            Serial.print(".");
            delay(500);
          }
        Serial.println("WiFi uppe igen!");
      }
    if(!client.status()){
      Serial.println("ANSLUTER IGEN");
      while(!try2Connect()){
        delay(2000);
        Serial.println("trying igen....");  
      } 
  }
}

}



  


