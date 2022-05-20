<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");


include_once ('../../connection/user_connection.php');
include_once ('../../tables/statistiche.php');

$database = new Database();
$statistiche = new Statistiche($database->getConnection());

$stmt = $statistiche->updateLogin();
$num = $stmt->rowCount();

if($num > 0)
{
    http_response_code(200);
}
else
{
  http_response_code(404);
}

?>
