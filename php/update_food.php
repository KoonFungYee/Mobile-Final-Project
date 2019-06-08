<?php
error_reporting(0);
include_once("dbconnect.php");

$foodname = $_POST['foodname'];
$price = $_POST['price'];
$period=$_POST['period'];
$remarks=$_POST['remarks'];
$id=$_POST['id'];

$sqlinsert = "update `food` set `foodname` = '$foodname', `price` = '$price', `period` = '$period', `remarks` = '$remarks' where `id` = '$id'";
if ($conn->query($sqlinsert) === TRUE){
    echo "success";
}else {
    echo "failed";
}

?>