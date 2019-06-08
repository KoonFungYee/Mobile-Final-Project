<?php
error_reporting(0);
include_once("dbconnect.php");

$category = $_POST['category'];
$state = $_POST['state'];

$sql = "SELECT * FROM restaurant WHERE state = '$state' and category = '$category'";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	echo mysqli_num_rows($result);
}else{
    echo "0";
}
?>