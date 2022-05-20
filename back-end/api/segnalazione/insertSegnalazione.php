<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/segnalazione.php');


$database = new Database();
$segnalazione = new Segnalazione($database->getConnection());

$segnalazione->titolo = $_POST["titolo"];
$segnalazione->descrizione = $_POST["descrizione"];
$segnalazione->fk_utente = $_POST["fk_utente"];
$segnalazione->fk_itinerario = $_POST["fk_itinerario"];


if ($segnalazione->insert())
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
