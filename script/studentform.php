<?php

 
  // PHP variable to store the host address
 $db_host  = "mysql6.000webhost.com";
 // PHP variable to store the username
 $db_uid  = "a6054419_sb";
 // PHP variable to store the password
 $db_pass = "school1234";
 // PHP variable to store the Database name  
 $db_name  = "a6054419_sb"; 
        // PHP variable to store the result of the PHP function 'mysql_connect()' which establishes the PHP & MySQL connection  
 $db_con = mysql_connect($db_host,$db_uid,$db_pass) or die('could not connect');
 mysql_select_db($db_name);

$stid=$_POST['rollno'];
$name=$_POST['fname'];
$busno=$_POST['busno'];
$no=$_POST['mobileno'];
$std=$_POST['std'];
$div=$_POST['div'];




$sql="INSERT INTO student (std,division,rollno,name,busno,mobileno) VALUES('$std','$div','$stid','$name','$busno','$no')";
$result = mysql_query($sql);
if($result===FALSE)
{
	die(mysql_error());
}
echo json_encode('registered');


 mysql_close();   
?>