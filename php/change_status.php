<?php
error_reporting(0);
include_once("dbconnect.php");

$name = $_POST['name'];
$state = $_POST['state'];
$status = $_POST['status'];

$sql = "update restaurant set status = '$status' WHERE `name` = '$name' AND state = '$state'";
$result = $conn->query($sql);

if ($conn->query($sql) === TRUE) {
    echo "success";
}else {
    echo "failed";
}
