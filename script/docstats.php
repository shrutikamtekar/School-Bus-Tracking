<?php


  // PHP variable to store the host address
 $db_host  = "127.0.0.1";
 // PHP variable to store the username
 $db_uid  = "root";
 // PHP variable to store the password
 $db_pass = "";
 // PHP variable to store the Database name  
 $db_name  = "schooladmin"; 
        // PHP variable to store the result of the PHP function 'mysql_connect()' which establishes the PHP & MySQL connection  
 $db_con = mysql_connect($db_host,$db_uid,"") or die('could not connect');
 mysql_select_db($db_name);
 $i = 2000;
 
 

$email=$_POST['email'];
									
$sql = "SELECT teacher_name,teacher_department,teacher_designation FROM loginteacher where teacher_email='$email'";
		



 $result = mysql_query($sql);
 while($row=mysql_fetch_assoc($result))
  $output[]=$row;
 print(json_encode($output));
 mysql_close();   
?>
