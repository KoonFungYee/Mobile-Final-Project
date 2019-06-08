<?php
error_reporting(0);
include_once("dbconnect.php");

$location = $_POST['location'];
$email=$_POST['email'];

$sqlinsert = "update `user` set `location`='$location' where `email`='$email'";
if ($conn->query($sqlinsert) === TRUE){
    echo "$location";
}else {
    echo "Something Wrong";
}

?>