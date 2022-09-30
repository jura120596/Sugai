#include<SPI.h> 
#include<MFRC522.h> 
#include <WiFi.h> 
#include <HTTPClient.h> 
#define RST_PIN 4 
#define SS_PIN 5 
#define CONNECTION_TIMEOUT 10
MFRC522 mfrc522(SS_PIN, RST_PIN); 
String cardID; 
String token = "null"; 
char floatbutVar[32]; 
 
const int LED = 2; 
 
const String Server = "http://sugai.ru/"; 
 
const char* ssid = "Pixel_7787";      
const char* password = "83300000"; 
 
 
void connect() {
   WiFi.begin(ssid, password); 
   int timeout_counter = 0;

   while(WiFi.status() != WL_CONNECTED){
      Serial.print(".");
      if (timeout_counter % 5 == 0) {
          digitalWrite(LED,HIGH); 
      }
          delay(200);
      if (timeout_counter % 5 == 0) {
          digitalWrite(LED,LOW); 
      }
      timeout_counter++;
      if(timeout_counter >= CONNECTION_TIMEOUT*5){
         ESP.restart();
      }
   }
} 
 
void setup() { 
  digitalWrite(LED,LOW); 
 
  Serial.begin(115200); 
  SPI.begin(); 
 
  pinMode(LED, OUTPUT); 
 
   
  mfrc522.PCD_Init(); 
  WiFi.mode(WIFI_STA); 
  connect();
   
  Serial.print("Сonnected to "); 
  Serial.println(ssid); 
   
  Serial.print("IP address:\t"); 
  Serial.println(WiFi.localIP()); 
 
   
} 

void loop() { 
  rfid_uid(); 
  digitalWrite(LED,LOW); 
  delay(1000); 
  digitalWrite(LED,HIGH); 
  delay(100); 
  digitalWrite(LED,LOW);  
  delay(50);  
  digitalWrite(LED,HIGH); 
  delay(100); 
  digitalWrite(LED,LOW);
} 
 
void rfid_uid(){ 
  if(!mfrc522.PICC_IsNewCardPresent()|| !mfrc522.PICC_ReadCardSerial() ){ 
    return; 
    } 
  String cardID = "" + String(mfrc522.uid.uidByte[0], HEX)+"%20"+String(mfrc522.uid.uidByte[1], HEX)+"%20"+String(mfrc522.uid.uidByte[2], HEX)+"%20"+String(mfrc522.uid.uidByte[3], HEX);
  Serial.println(cardID); 
  if ((WiFi.status() == WL_CONNECTED)){ 
    HTTPClient http; 
    if (token.equals("null")) {
      http.begin(Server + "api/token/client/?client_id=4&client_secret=ercYE7CDSMdKV2EtZF8kqSA9zPU7fbuKzaBfMtra"); 
      int httpCode = http.GET(); 
      if (httpCode != 200) { 
        Serial.println("Error 1: "+httpCode); 
        token = "null";
        http.end();
        return;
      }
      Serial.print("ResponseCode:  "); 
      Serial.println(httpCode); 
      token = http.getString();
      Serial.println("token:" + token); 
      http.end();
    }
    http.end(); 
    HTTPClient http2; 
    http2.begin(Server + "api/client/event?card_id="+cardID); 
    http2.addHeader("Authorization","Bearer " + token); 
    int httpCode2 = http2.GET (); 
    Serial.println(httpCode2); 
    Serial.println(http2.getString().substring(0, 100)); 
    if ((httpCode2 == 200) || (httpCode2 == 401)) { 
      if(httpCode2 == 200){ 
        digitalWrite(LED, HIGH); 
        Serial.println("Success"); 
        delay(3000); 
        digitalWrite(LED, LOW); 
        http2.end(); 
        return;
      }else{ 
        Serial.println("bad token"); 
        token = "null";
      } 
    } 
    else{ 
      Serial.println("Error 2: "+httpCode2);
    } 
    digitalWrite(LED, LOW); 
    delay(3000);  
    http2.end();  
    // освобождаем ресурсы микроконтроллера  
  } else{ 
    Serial.print("НЕТ ПОДКЛЮЧЕНИЯ К WI-FI"); 
    WiFi.disconnect();
    connect();
  } 
}