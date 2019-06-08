<?php
error_reporting(0);
include_once("dbconnect.php");

$name = $_POST['name'];

$sql = "SELECT * FROM food where `foodname` = '$name'";

$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["result"] = array();
    while ($row = $result ->fetch_assoc()){
        $resultlist = array();
        $resultlist[foodname] = $name;
        $resultlist[price] = $row["price"];
        $resultlist[period] = $row["period"];
        $resultlist[remarks] = $row["remarks"];
        $resultlist[rating] = $row["rating"];
		$resultlist[restname] = $row["restname"];
		$resultlist[building] = $row["building"];
		$resultlist[state] = $row["state"];
        array_push($response["result"], $resultlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>