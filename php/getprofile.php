<?php
error_reporting(0);
include_once("dbconnect.php");

$email = $_POST['email'];

$sql = "SELECT * FROM user WHERE email = '$email'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["profile"] = array();
    while ($row = $result ->fetch_assoc()){
        $datalist= array();
        $datalist[name] = $row["name"];
        $datalist[phone] = $row["phone"];
        $datalist[gender] = $row["gender"];
        $datalist[password] = $row["password"];
		$datalist[location] = $row["location"];
        array_push($response["profile"], $datalist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>