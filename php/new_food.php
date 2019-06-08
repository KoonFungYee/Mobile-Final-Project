<?php
error_reporting(0);
include_once("dbconnect.php");

$foodname = $_POST['foodname'];
$price = $_POST['price'];
$period = $_POST['period'];
$remarks = $_POST['remarks'];
$status="available";
$rating="0.00";
$restname = $_POST['restname'];
$state=$_POST['state'];

$sqlinsert = "INSERT INTO `food`(`foodname`,`price`,`period`,`remarks`,`rating`,`status`,`restname`,`state`)
VALUES ('$foodname','$price','$period','$remarks','$status','$restname','$state')";
if ($conn->query($sqlinsert) === TRUE){
    echo "success";
}else {
    echo "failed";
}

?>