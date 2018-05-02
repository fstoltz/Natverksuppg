#include <ESP8266WiFi.h>

#define ssid  "anton"
#define pwd  "nackademin"
WiFiClient client;
int listeningPort = 44321;
IPAddress connect_2(2,248,81,16);
String namn = "Fredrik";  //ÄNDRA TILL DITT EGET NAMN//
// initializes or defines the output pin of the LM35 temperature sensor
int outputpin= A0;

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
  
  if(client.connect(connect_2,listeningPort)){
    Serial.print("Du ar nu ansluten till: ");
    Serial.print(connect_2);
  }
  
  
}


void connectToServer(){
  boolean status = false;
  while(status == false){
    Serial.println("Trying to connect...");
    if((client.connect(connect_2,listeningPort)) == true){
      status = true;
      Serial.println("Connected again!");
    }
    delay(5000);
  }
  /*while(!client.connect(connect_2,listeningPort)){
    delay(8000);
    Serial.println("Loopar runt...");
  }
  Serial.println("Kopplade upp mig!");*/
}


void loop() {
if((client.connected()) == false){
  connectToServer();
} else {
  int analogValue = analogRead(outputpin);
  float millivolts = (analogValue/1024.0) * 3300; //3300 is the voltage provided by NodeMCU
  float celsius = millivolts/10;
  Serial.print("in DegreeC=   ");
  Serial.println(celsius);
  
  //---------- Here is the calculation for Fahrenheit ----------//
  
  float fahrenheit = ((celsius * 9)/5 + 32);
  //Serial.print(" in Farenheit=   ");
  //Serial.println(fahrenheit);
  digitalWrite(LED_BUILTIN, LOW);
  client.println(namn+":"+celsius);
  digitalWrite(LED_BUILTIN, HIGH);
  boolean val = client.connected();
  Serial.println(val);
  delay(2000);
  }
}
