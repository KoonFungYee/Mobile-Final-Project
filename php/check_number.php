<?php
error_reporting(0);
include_once("dbconnect.php");

$restname = $_POST['name'];
$state = $_POST['state'];

$sql = "select * from `food` where `restname` = '$restname' and `state` = '$state'";
$result = $conn->query($sql);
$get=mysqli_num_rows($result);
echo $get;
?>