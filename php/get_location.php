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
        $datalist[longtitude] = $row["longtitude"];
        $datalist[latitude] = $row["latitude"];
        array_push($response["profile"], $datalist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>