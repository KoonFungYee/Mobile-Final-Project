<?php
error_reporting(0);
include_once("dbconnect.php");

$state = $_POST['state'];

$sql = "select * from `restaurant` where `state` = '$state'";
$result = $conn->query($sql);
$get=mysqli_num_rows($result);
echo $get;
?>