<?php
error_reporting(0);
include_once("dbconnect.php");

$email = $_POST['email'];

$sql = "select `name` from `user` where `email` = '$email'";
$result = $conn->query($sql);
$get=mysqli_fetch_array($result);
echo $get['name'];
?>
