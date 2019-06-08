<?php
error_reporting(0);
include_once("dbconnect.php");

$name = $_POST['name'];
$state = $_POST['state'];

$sql = "SELECT * FROM restaurant WHERE name = '$name' and `state` = '$state'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
	$row = $result ->fetch_assoc();
	echo "yes";
}else{
    echo "";
}

?>