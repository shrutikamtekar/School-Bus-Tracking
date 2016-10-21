<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body>
<?php
        if(!isset($page_content))
       {
           $page_content='home.php';
           include_once 'masterpage.php';
           exit;
       }
           ?>
        <table>
            <tr><td align="left"><img src="htc.jpg"/></td></tr>
            <tr><td align="left"><img src="home2.jpg"/></td><tr>
            <tr><td align="left"><img src="home3.jpg"/></td></tr>
        </table>
    </body>
</html>
