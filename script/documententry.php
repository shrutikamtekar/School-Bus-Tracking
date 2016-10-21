<?php


  // PHP variable to store the host address
 $db_host  = "";
 // PHP variable to store the username
 $db_uid  = "root";
 // PHP variable to store the password
 $db_pass = "";
 // PHP variable to store the Database name  
 $db_name  = ""; 
        // PHP variable to store the result of the PHP function 'mysql_connect()' which establishes the PHP & MySQL connection  
 $db_con = mysql_connect($db_host,$db_uid,"") or die('could not connect');
 mysql_select_db($db_name);
 //$i = 2000;
 
 
$dname=$_POST['dname'];
$dtime=$_POST['dtime'];

$dsubject=$_POST['dsubject'];
$dteacher=$_POST['dteacher'];
$dept=$_POST['dept'];
$desgn=$_POST['desgn'];

$dsem=$_POST['dsem'];
$description=$_POST['description'];
$dlink=$_POST['dlink'];


									
$sql = "INSERT INTO documents (doc_name,doc_time,doc_subject,doc_teacher,doc_department,doc_designation,doc_semester,doc_description,doc_link) 
		VALUES ('$_POST[dname]','$_POST[dtime]','$_POST[dsubject]','$_POST[dteacher]','$_POST[dept]','$_POST[desgn]',$_POST[dsem],'$_POST[description]','$_POST[dlink]')";
		



$result = mysql_query($sql) or die ("could not save record");

mysql_close();
?>
