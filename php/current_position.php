<?php
error_reporting(0);
include_once("dbconnect.php");

$email = $_POST['email'];

$sql = "SELECT * FROM user where `email` = '$email'";

$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["position"] = array();
    while ($row = $result ->fetch_assoc()){
        $resultlist = array();
        $resultlist[latitude] = $row["latitude"];
        $resultlist[longtitude] = $row["longtitude"];
        array_push($response["position"], $resultlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>