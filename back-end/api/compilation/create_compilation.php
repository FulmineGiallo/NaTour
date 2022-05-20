<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/compilation.php');


$database = new Database();
$compilation = new Compilation($database->getConnection());

$compilation->nome = $_POST["nome"];
$compilation->descrizione = $_POST["descrizione"];
$compilation->id_utente = $_POST["id_utente"];



if ($compilation->create_compilation())
{
  $data = [ 'risultato' => 'true'];
  echo json_encode($data);
  http_response_code(200);
}
else
{
  $data = [ 'risultato' => 'false'];
  echo json_encode($data);
  http_response_code(405);
}

 ?>
