<?php
error_reporting(0);
include_once("dbconnect.php");

$foodname = $_POST['foodname'];
$restname = $_POST['restname'];
$state = $_POST['state'];

$sql = "SELECT * FROM comment where `foodname` = '$foodname' and `restname` = '$restname' and `state`='$state'";

$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["result"] = array();
    while ($row = $result ->fetch_assoc()){
        $resultlist = array();
        $resultlist[name] = $row["name"];
        $resultlist[rating] = $row["rating"];
        $resultlist[comment] = $row["comment"];
        $resultlist[date] = $row["date"];
        array_push($response["result"], $resultlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>