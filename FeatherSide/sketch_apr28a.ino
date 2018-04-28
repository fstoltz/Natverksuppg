#include <ESP8266WiFi.h>

#define ssid  "TeliaGateway9C-97-26-57-05-5B"
#define pwd  "226DFC4358"
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

void loop()       //main loop

{
int analogValue = analogRead(outputpin);
float millivolts = (analogValue/1024.0) * 3300; //3300 is the voltage provided by NodeMCU
float celsius = millivolts/10;
Serial.print("in DegreeC=   ");
Serial.println(celsius);

//---------- Here is the calculation for Fahrenheit ----------//

float fahrenheit = ((celsius * 9)/5 + 32);
Serial.print(" in Farenheit=   ");
Serial.println(fahrenheit);
  digitalWrite(LED_BUILTIN, LOW);
  client.println(namn+":"+celsius);
  digitalWrite(LED_BUILTIN, HIGH);
  delay(2000);
//delay(1000);
}
