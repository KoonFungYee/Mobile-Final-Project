<?php
error_reporting(0);
include_once("dbconnect.php");

$name = $_POST['name'];
$rating = $_POST['rating'];
$comment = $_POST['comment'];
$date = $_POST['date'];
$foodname = $_POST['foodname'];
$restname=$_POST['restname'];
$state=$_POST['state'];

$conn->query("INSERT INTO `comment`(`name`, `rating`, `comment`, `date`, `foodname`, `restname`, `state`)
VALUES ('$name','$rating','$comment','$date','$foodname','$restname','$state')");

$countComment=$conn->query("select * from `comment` where `restname`='$restname' and `state`='$state'
and `foodname`='$foodname'");
$commentNumber=mysqli_num_rows($countComment);

$totalCommentRating=0;
while ($eachComment=mysqli_fetch_array($countComment)){
    $totalCommentRating+=$eachComment['rating'];
}

$newFoodRating=$totalCommentRating/$commentNumber;
$newFoodRating=sprintf('%0.2f', $newFoodRating);
$conn->query("update food set `rating`='$newFoodRating' where `restname`='$restname' and `state`='$state'
and `foodname`='$foodname'");

$countFood=$conn->query("select `rating` from food where `restname`='$restname' and `state`='$state'");
$foodNumber=mysqli_num_rows($countFood);
$totalFoodRating=0;
while ($eachFood=mysqli_fetch_array($countFood)){
    $totalFoodRating+=$eachFood['rating'];
}

$newRestRating=$totalFoodRating/$foodNumber;
$newRestRating=sprintf('%0.2f', $newRestRating);
$conn->query("update restaurant set `rating`='$newRestRating' where `name`='$restname' and `state`='$state'");

?>