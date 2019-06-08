<?php
error_reporting(0);
include_once("dbconnect.php");

$email = $_POST['email'];
$longtitude = $_POST['longtitude'];
$latitude = $_POST['latitude'];

$sqlinsert = "update user set `longtitude`='$longtitude', `latitude`='$latitude' where `email`='$email'";
if ($conn->query($sqlinsert) === TRUE){
    echo "success";
}else {
    echo "failed";
}

?>