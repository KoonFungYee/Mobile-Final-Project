<?php
error_reporting(0);
include_once("dbconnect.php");
$email = $_POST['email'];
$sql = "SELECT * FROM user WHERE email = '$email'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    while ($row = $result ->fetch_assoc()){
        $ran = $row["password"];
    }
    $from = "findfood@socstudent.net";
    $to = $email;
    $subject = "From Find Food. Reset your password";
    $message = "Use the following link to reset your password :"."\n http://www.socstudents.net/findfood/php/reset_password.php?email=".$email."&key=".$ran;
    $headers = "From:" . $from;
    mail($email,$subject,$message,$headers);
    echo "success";
}else{
    echo "No this account. Kindly click register.";
}

?>