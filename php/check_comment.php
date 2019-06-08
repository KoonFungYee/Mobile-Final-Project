<?php
error_reporting(0);
include_once("dbconnect.php");

$foodname = $_POST['foodname'];
$restname = $_POST['restname'];
$state = $_POST['state'];

$sql = "select * from `comment` where `foodname` = '$foodname' and `restname` = '$restname' and `state` = '$state'";
$result = $conn->query($sql);
$get=mysqli_num_rows($result);
echo $get;
?>