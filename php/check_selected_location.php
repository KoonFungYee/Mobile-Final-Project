<?php
error_reporting(0);
include_once("dbconnect.php");

$location = $_POST['location'];

$sql = "SELECT * FROM restaurant where `state` = '$location'";

$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["gps"] = array();
    while ($row = $result ->fetch_assoc()){
        $resultlist = array();
        $resultlist[name] = $row["name"];
        $resultlist[longtitude] = $row["longtitude"];
        $resultlist[latitude] = $row["latitude"];
        $resultlist[building] = $row["building"];
        $resultlist[state] = $row["state"];
        $resultlist[starttime] = $row["starttime"];
        $resultlist[endtime] = $row["endtime"];
        $resultlist[telnumber] = $row["telnumber"];
        $resultlist[fbpage] = $row["fbpage"];
        $resultlist[rating] = $row["rating"];
        array_push($response["gps"], $resultlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>