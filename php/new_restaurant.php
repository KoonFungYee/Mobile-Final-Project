<?php
error_reporting(0);
include_once("dbconnect.php");

$name = $_POST['name'];
$building = $_POST['building'];
$state = $_POST['state'];
$starttime=$_POST['starttime'];
$endtime=$_POST['endtime'];
$telnumber=$_POST['telnumber'];
$fbpage=$_POST['fbpage'];
$category=$_POST['category'];
$longtitude=$_POST['longtitude'];
$latitude=$_POST['latitude'];
$rating='0.00';
$status='available';

$sqlinsert = "INSERT INTO `restaurant`(`name`, `building`, `state`, `starttime`, `endtime`, `telnumber`, `fbpage`, `category`, `longtitude`, `latitude`,`rating`,`status`) VALUES ('$name','$building','$state','$starttime','$endtime','$telnumber','$fbpage', '$category', '$longtitude','$latitude','$rating','$status')";
if ($conn->query($sqlinsert) === TRUE){
    echo "success";
}else {
    echo "failed";
}

?>