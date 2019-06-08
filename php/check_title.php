<?php
error_reporting(0);
include_once("dbconnect.php");

$name = $_POST['name'];
$state = $_POST['state'];

$sql = "SELECT status FROM restaurant WHERE `name` = '$name' AND state = '$state'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
	$row = $result ->fetch_assoc();
	echo $row["status"];
}else{
    echo "Error";
}

?>