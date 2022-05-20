<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/itinerario.php');


$database = new Database();
$itinerario = new Itinerario($database->getConnection());

$itinerario->id_itinerario = $_POST["id_itinerario"];
$itinerario->nome = $_POST["nome"];
$itinerario->durata = $_POST["durata"];
$itinerario->disabile = $_POST["disabile"];
$itinerario->difficolta = $_POST["difficolta"];
$itinerario->descrizione = $_POST["descrizione"];
$itinerario->fk_utente = $_POST["fk_utente"];
$itinerario->nomefile = $_POST["nomefile"];



if ($itinerario->insert())
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
