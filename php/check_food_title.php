<?php
error_reporting(0);
include_once("dbconnect.php");

$foodname = $_POST['foodname'];
$restname = $_POST['restname'];
$state = $_POST['state'];

$sql = "SELECT status FROM food WHERE `foodname` = '$foodname' and `restname` = '$restname' AND state = '$state'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
	$row = $result ->fetch_assoc();
	echo $row["status"];
}else{
    echo "Error";
}

?>