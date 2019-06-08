<?php
error_reporting(0);
include_once("dbconnect.php");

$foodname = $_POST['foodname'];
$restname = $_POST['restname'];
$state = $_POST['state'];
$status = $_POST['status'];

$sql = "update `food` set `status` = '$status' WHERE `foodname` = '$foodname' and `restname` = '$restname' AND state = '$state'";
$result = $conn->query($sql);

if ($conn->query($sql) === TRUE) {
    echo "success";
}else {
    echo "failed";
}
?>
