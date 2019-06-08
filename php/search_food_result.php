<?php
error_reporting(0);
include_once("dbconnect.php");

$name = $_POST['name'];
$state = $_POST['state'];

$sql = "SELECT * FROM food where `restname` = '$name' and `state` = '$state'";

$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["result"] = array();
    while ($row = $result ->fetch_assoc()){
        $resultlist = array();
        $resultlist[foodname] = $row["foodname"];
        $resultlist[price] = $row["price"];
        $resultlist[period] = $row["period"];
        $resultlist[remarks] = $row["remarks"];
        $resultlist[rating] = $row["rating"];
        array_push($response["result"], $resultlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>