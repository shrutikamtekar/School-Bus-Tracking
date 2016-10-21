<?php
  // PHP variable to store the host address
 $db_host  = "127.0.0.1";
 // PHP variable to store the username
 $db_uid  = "root";
 // PHP variable to store the password
 $db_pass = "";
 // PHP variable to store the Database name  
 $db_name  = "schoolbus"; 
        // PHP variable to store the result of the PHP function 'mysql_connect()' which establishes the PHP & MySQL connection  
 $db_con = mysql_connect($db_host,$db_uid,"") or die('could not connect');
 mysql_select_db($db_name);

$tag=mysql_real_escape_string($_POST['tagno']);

$sql="SELECT mobileno,boarded from student where tagno = '$tag'";

$check=mysql_query("SELECT boarded from student where tagno = '$tag' and boarded='0'");
if(mysql_num_rows($check)>0)
{
	$sql1="UPDATE student SET boarded='1' where tagno = '$tag'";	
}
else
{
	$sql1="UPDATE student SET boarded='0' where tagno = '$tag'";	
}
$result = mysql_query($sql);
$result1 = mysql_query($sql1);
if($result===FALSE||$result1===FALSE)
{
	die(mysql_error());
}
while($row=mysql_fetch_assoc($result))
{
    $output[]=$row;
} 


print(json_encode($output));

 mysql_close();   
?>