<?php
error_reporting(0);
include_once("dbconnect.php");

$name = $_POST['name'];
$phone = $_POST['phone'];
$gender=$_POST['gender'];
$email=$_POST['email'];
$password=$_POST['password'];
$location=$_POST['location'];

$sqlinsert = "update `user` set `name` = '$name', `phone` = '$phone', `gender` = '$gender', `password` = '$password', 
`location` = '$location' where `email`='$email'";
if ($conn->query($sqlinsert) === TRUE){
    echo "success";
}else {
    echo "failed";
}

?>