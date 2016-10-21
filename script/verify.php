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


$bus=$_POST['busno'];

$sql="SELECT busno FROM stops where busno='$bus'";
$result = mysql_query($sql);
	if(mysql_num_rows($result)>0){
             print(json_encode("yes"));
		
	}
	
                 mysql_close(); 

?>				
