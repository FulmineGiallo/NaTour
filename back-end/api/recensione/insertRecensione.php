<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/recensione.php');


$database = new Database();
$recensione = new Recensione($database->getConnection());

$recensione->testo = $_POST["testo"];
$recensione->valutazione = $_POST["valutazione"];
$recensione->fk_utente = $_POST["fk_utente"];
$recensione->fk_itinerario = $_POST["fk_itinerario"];


if ($recensione->insert())
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
