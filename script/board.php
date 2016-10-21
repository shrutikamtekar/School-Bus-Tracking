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

$tag=mysql_real_escape_string($_POST['tagno']);
$bus=$_POST['busno'];

$sql="SELECT name,mobileno,boarded,busno from student where tagno = '$tag'";

$check=mysql_query("SELECT boarded from student where tagno = '$tag' and boarded='0' and busno='$bus'");
if(mysql_num_rows($check)>0)
{
	$sql1="UPDATE student SET boarded='1' where tagno = '$tag'";	
}


$result = mysql_query($sql);
$result1 = mysql_query($sql1);

if($result===FALSE)
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