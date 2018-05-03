#include <ESP8266WiFi.h>
#include <DHT.h>
#define ssid  "Lagerbergs"
#define pwd  "CyKloP50"
DHT dht(2,DHT11);
WiFiClient client;
#define dht_port  2
int listeningPort = 44321;
IPAddress connect_2(2,248,81,16);
String namn = "Hampus";  //ÄNDRA TILL DITT EGET NAMN//


void setup(){
  Serial.begin(115200);
  Serial.println();
  WiFi.begin(ssid,pwd);
  while(WiFi.status() != WL_CONNECTED){
    delay(300);
    Serial.print(".");
  }
  Serial.println("Du ar nu ansluten till natverket!");
  Serial.println(client.connected());
  Serial.println("Ansluter till servern...");
  while(!try2Connect()){
    delay(2000);
    Serial.println("trying igen....");  
  }
  Serial.println(client.connected());

}

void loop(){
  
  Serial.println(client.status());
  if(client.status()&& WiFi.status() == WL_CONNECTED){
    float tempTmp = dht.readTemperature();
    tempTmp = tempTmp+0.5*random(0,2);
    Serial.println(tempTmp);
    
    client.println(namn+":"+tempTmp);
    //client.status();
  }
  else{
     if(WiFi.status() != WL_CONNECTED){
         WiFi.begin(ssid,pwd);
         Serial.println(".");
        while(WiFi.status() != WL_CONNECTED){
            Serial.print(".");
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
    
  delay(1000);
  
}

bool try2Connect(){
  if(client.connect(connect_2,listeningPort)){
    Serial.print("Du ar nu ansluten till: ");
    Serial.print(connect_2);
    return true;
  }
  return false;
  
}

  
  
  


