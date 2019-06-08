<?php
error_reporting(0);
include_once("dbconnect.php");

$email = $_POST['email'];
$password = $_POST['password'];
$name = $_POST['name'];
$phone = $_POST['phone'];
$gender=$_POST['gender'];

$sqlinsert = "INSERT INTO `user`(`email`, `password`, `name`, `phone`, `gender`) VALUES ('$email','$password','$name','$phone','$gender')";
if ($conn->query($sqlinsert) === TRUE){
    echo "success";
}else {
    echo "failed";
}

?>