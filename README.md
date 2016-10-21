# School-Bus-Tracking
Tracking school buses and students has become a mainstream issue beacuse decision of whether it would be quicker to wait for the arrival of school bus or to hire a cab as the bus is late/missed to reach school. Therefore this project intend to aid in the bus arrival intimation by bringing in an application that will help in successfully tracking the school bus and the child onboard. Application can also be used to inform the parents whether their child has boarded the school bus as well as arrival at school. GPS based Android Phone will be used by parents to enter the bus number in which their child travels. Parents can also enter the source and the user will get detailed information about the location of the bus

## Application Description
This application uses RFID and GPS based Android Phone where the tracker is used for locating the current geographic position of the bus and RFID(Radio Frequency Identification Device) is used to uniquely identify every child.This system uses a database on backend to store the relevant information about the bus and the child which is used to update the application.
![alt tag](https://github.com/shrutikamtekar/School-Bus-Tracking/blob/master/images/Architectural%20Diagram.png)
The diagram is explained as follows:
•	The school bus consists of android phone which have the driver side application first requests the driver enters to enter the bus number he is driving and this will start the GPS of the phone and the location will be sent to the server. After this driver will have to select the board option select the Bluetooth device to read the tags and send SMS to the parent. 
•	The school bus is fitted with a RFID module whose connections are shown in figure 6.2 and will be explained below.
•	The Parent side android application has two features. First it provides an option where the parents can register for the RFID tag which will be then be provided by the school administrator. Second option is the locate bus option where the parent needs to enter the bus number they want to search for and the stop name for which they want to see the expected arrival time.
•	The Server receives request and data from both application i.e. driver side and parent side application which is then stored in the database. The server sends the mobile number of the parent for a specific tag number to the driver side application to send the SMS.


##Setting up the hardware for reading RFID tags

