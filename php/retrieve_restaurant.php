<?php
error_reporting(0);
include_once("dbconnect.php");

$name = $_POST['name'];
$state = $_POST['state'];


$sql = "SELECT * FROM restaurant where `name` = '$name' and `state` = '$state'";

$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["result"] = array();
    while ($row = $result ->fetch_assoc()){
        $resultlist = array();
        $resultlist[building] = $row["building"];
        $resultlist[street] = $row["street"];
        $resultlist[starttime] = $row["starttime"];
        $resultlist[endtime] = $row["endtime"];
        $resultlist[telNumber] = $row["telnumber"];
		$resultlist[fbpage] = $row["fbpage"];
		$resultlist[category] = $row["category"];
        array_push($response["result"], $resultlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>