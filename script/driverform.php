<?php
  // PHP variable to store the host address
 $db_host  = "";
 // PHP variable to store the username
 $db_uid  = "";
 // PHP variable to store the password
 $db_pass = "";
 // PHP variable to store the Database name  
 $db_name  = ""; 
        // PHP variable to store the result of the PHP function 'mysql_connect()' which establishes the PHP & MySQL connection  
 $db_con = mysql_connect($db_host,$db_uid,$db_pass) or die('could not connect');
 mysql_select_db($db_name);
 // array for JSON response

date_default_timezone_set( 'Asia/Kolkata' );

$date = date('y/m/d h:i:s'); 
$bus=$_POST['busno'];
$lat=$_POST['lat'];
$long=$_POST['long'];

$check="SELECT busno FROM stops where busno='$bus'";
$ans = mysql_query($check);
if(mysql_num_rows($ans)>0){
  $sql="SELECT * FROM bus$bus";
  $result = mysql_query($sql);
	if(mysql_num_rows($result)>0){
		$sql="UPDATE bus SET Bus_no='$bus',latitude='$lat',longitude='$long',time='$date' where Bus_no=$bus";	
		$result = mysql_query($sql);
		$sql1="INSERT INTO bus$bus VALUES ('$lat','$long','$date')";	
		$result1 = mysql_query($sql1);
		if($result===FALSE||$result1===FALSE)
		{
			die(mysql_error());
		}
	}
	else
	{
		$sql="CREATE TABLE bus$bus(latitude varchar(100),longitude varchar(100),time datetime,UNIQUE (time))";
		$result = mysql_query($sql);
		$sql1="INSERT INTO bus$bus VALUES ('$lat','$long','$date')";	
		$result1 = mysql_query($sql1);
		$sql2="INSERT INTO bus VALUES ('$bus','$lat','$long','$date')";	
		$result2 = mysql_query($sql2);
		if($result===FALSE||$result1===FALSE||$result2===FALSE)
		{
			die(mysql_error());
		}
		
	}
}


 mysql_close();   
?>				
