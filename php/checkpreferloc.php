<?php
error_reporting(0);
include_once("dbconnect.php");

$email = $_POST['email'];

$sql = "SELECT location FROM user WHERE email = '$email'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
	$row = $result ->fetch_assoc();
	echo $row["location"];
}else{
    echo "";
}

?>