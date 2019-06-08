<?php
error_reporting(0);
include_once("dbconnect.php");

$state = $_POST['state'];

$sql = "SELECT * FROM restaurant where `state` = '$state'";

$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["num"] = array();
    while ($row = $result ->fetch_assoc()){
        $resultlist = array();
        $resultlist[number] = $result->num_rows;
        array_push($response["num"], $resultlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>