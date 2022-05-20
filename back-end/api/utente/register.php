<?php

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/utente.php');

$database = new Database();

$user = new FireBaseUser($database->getConnection());

$user->id_key = $_POST["id_key"];

if ($user->register()) {
  http_response_code(200);
}

else {
  http_response_code(404);
}

?>
