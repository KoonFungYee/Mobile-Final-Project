<?php
error_reporting(0);
include_once("dbconnect.php");

$state = $_POST['state'];

$sql = "SELECT * FROM restaurant where `state` = '$state' order by `rating` desc ";

$result = $conn->query($sql);
$n=0;
if ($result->num_rows > 0) {
    $response["result"] = array();
    while ($row = $result ->fetch_assoc()){
        if ($n<10){
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
            $n+=1;
        }
    }
    echo json_encode($response);

}else{
    echo "nodata";
}
?>