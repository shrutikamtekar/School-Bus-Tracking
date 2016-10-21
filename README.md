# School-Bus-Tracking
Tracking school buses and students has become a mainstream issue beacuse decision of whether it would be quicker to wait for the arrival of school bus or to hire a cab as the bus is late/missed to reach school. Therefore this project intend to aid in the bus arrival intimation by bringing in an application that will help in successfully tracking the school bus and the child onboard. Application can also be used to inform the parents whether their child has boarded the school bus as well as arrival at school. GPS based Android Phone will be used by parents to enter the bus number in which their child travels. Parents can also enter the source and the user will get detailed information about the location of the bus

## Application Description
This application uses RFID and GPS based Android Phone where the tracker is used for locating the current geographic position of the bus and RFID(Radio Frequency Identification Device) is used to uniquely identify every child.This system uses a database on backend to store the relevant information about the bus and the child which is used to update the application.
![alt tag](https://github.com/shrutikamtekar/School-Bus-Tracking/blob/master/images/Architectural%20Diagram.png)
The diagram is explained as follows:  
•	The school bus consists of android phone which has the driver side application. The driver enters the bus number he is driving and this will start the GPS of the phone and the location will be sent to the server. After this driver will have to select the board option select the Bluetooth device to read the tags and send SMS to the parent.  
•	The school bus is fitted with a RFID module whose connections are shown in **setting up the hardware section** and will be explained below.  
•	The Parent side android application has two features. First it provides an option where the parents can register for the RFID tag which will be then be provided by the school administrator. Second option is the locate bus, where the parents need to enter the bus number they want to search for and the stop name for which they want to see the expected arrival time.  
•	The Server receives request and data from both application i.e. driver side and parent side application which is then stored in the database. The server sends the mobile number of the parent for a specific tag number to the driver side application to send the SMS.  


##Setting up the hardware for reading RFID tags
<p align="center">
<img src="https://github.com/shrutikamtekar/School-Bus-Tracking/blob/master/images/Hardware%20connection.png">
</p>
The above figure show the connection of the hardware device which forms the RFID module. The connection is explained below:  
**Step 1:** Connecting HC-05 to Arduino  
1.	VCC  pin of HC-05 is used to power the module.  It needs to be connected to the Arduino pin no 12.  
2.	GND pin of HC-05 is the ground pin. It needs to be connected to the Arduino Ground pin.  
3.	RXD pin of HC-05 is used to receive data. RXD pin no 0, It needs to be connected to voltage divider output.  
**Step 2:** Connecting the RFID EM18 to Arduino  
1.	Simply VCC pin of the EM-18 module is connected to the 5v of the Arduino board because it will take the 5v supply.  
2.	GND of the EM-18 module is connected to the Arduino module ground.  
3.	 TX pin of the EM-18 module used to write the read tag number, so is it connected to the Arduino pin 10.  
4.	SEL pin of the EM-18 module is connected to the Arduino pin 13.  
**Step 3:** Connecting the 9V battery to the Arduino power supply pin.  
**Step 4:** Burning the Arduino code  
The Arduino code is burnt to the board using the Arduino IDE software by connecting it to the laptop using a USB cable and removing the RXD pin connection of the Arduino board. This code needs to be burned only once. The code is attached in the Appendix.

