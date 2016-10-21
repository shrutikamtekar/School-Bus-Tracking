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
 
 
 
$stsem=$_POST['sem'];
$stdept=$_POST['dept'];
									
$sql = "SELECT doc_name,doc_time,doc_subject,doc_teacher,doc_department,doc_designation,doc_semester,doc_description,doc_link FROM documents WHERE doc_department='$stdept' AND doc_semester=$stsem";		



 $result = mysql_query($sql);
 while($row=mysql_fetch_assoc($result))
  $output[]=$row;
 print(json_encode($output));
 mysql_close();   
?>
