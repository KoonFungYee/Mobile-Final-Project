<?php
error_reporting(0);
include_once("dbconnect.php");

$state = $_POST['state'];

$sql = "SELECT * FROM restaurant where `state` = '$state' order by `name` asc ";

$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["result"] = array();
    while ($row = $result ->fetch_assoc()){
        $resultlist = array();
        $resultlist[name] = $row["name"];
        $resultlist[building] = $row["building"];
        $resultlist[state] = $row["state"];
        $resultlist[rating] = $row["rating"];
        $resultlist[longtitude] = $row["longtitude"];
        $resultlist[latitude] = $row["latitude"];
		$resultlist[starttime] = $row["starttime"];
		$resultlist[endtime] = $row["endtime"];
		$resultlist[telnumber] = $row["telnumber"];
		$resultlist[fbpage] = $row["fbpage"];
        array_push($response["result"], $resultlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>