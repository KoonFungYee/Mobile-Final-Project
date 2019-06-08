<?php
error_reporting(0);
include_once("dbconnect.php");

$name = $_POST['name'];
$state=$_POST['state'];
$building = $_POST['building'];
$starttime = $_POST['starttime'];
$endtime = $_POST['endtime'];
$telnumber = $_POST['telnumber'];
$fbpage = $_POST['fbpage'];
$category = $_POST['category'];
$longtitude = $_POST['longtitude'];
$latitude = $_POST['latitude'];

$sqlinsert = "update `restaurant` set `building` = '$building', `starttime` = '$starttime', `endtime` = '$endtime', `telnumber`='$telnumber', `fbpage`='$fbpage', `category`='$category', `longtitude`='$longtitude', `latitude`='$latitude' where `name`='$name' and `state`='$state'";
if ($conn->query($sqlinsert) === TRUE){
    echo "success";
}else {
    echo "failed";
}

?>