#include <SoftwareSerial.h>
SoftwareSerial rfid(10,9);

void setup()
{
   Serial.begin(38400);
   rfid.begin(9600);
   pinMode(13,OUTPUT);
   digitalWrite(13,HIGH);
   pinMode(12,OUTPUT);
   digitalWrite(12,HIGH);
}
void loop()
{
 
    char a[13];
if (rfid.available())
{
  rfid.readBytes(a,13);
 for(int i=0;i<13;i++)//to print the entire tag no
  Serial.print(a[i]);
  Serial.println();
}
  Serial.flush();
}
