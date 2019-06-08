<?php
error_reporting(0);
include_once("dbconnect.php");

$category = $_POST['category'];
$state = $_POST['state'];

if (strcasecmp($state, "All") == 0){
    $sql = "SELECT * FROM RESTAURANT where `category` = '$category'"; 
}else{
    $sql = "SELECT * FROM restaurant where `category` = '$category' and `state` = '$state'";
}

$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["result"] = array();
    while ($row = $result ->fetch_assoc()){
        $resultlist = array();
        $resultlist[name] = $row["name"];
        $resultlist[building] = $row["building"];
		$resultlist[state] = $row["state"];
        $resultlist[starttime] = $row["starttime"];
		$resultlist[endtime] = $row["endtime"];
        $resultlist[rating] = $row["rating"];
        $resultlist[longtitude] = $row["longtitude"];
        $resultlist[latitude] = $row["latitude"];
		$resultlist[telnumber] = $row["telnumber"];
		$resultlist[fbpage] = $row["fbpage"];
        array_push($response["result"], $resultlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>