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

$stop=$_POST['busstop'];

$sql="SELECT latitude,longitude from stops where stopname='$stop'";		
$result = mysql_query($sql);
if(mysql_num_rows($result)>0)
{
	while($row=mysql_fetch_assoc($result))
	{
		$output[]=$row;
	}	 
	print(json_encode($output));
}
else
{
	print(json_encode("invalid bus no"));
}
 if($result===FALSE)
{
	die(mysql_error());
}

	

 mysql_close();   
?>
