<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/segnalazione.php');


$database = new Database();
$segnalazione = new Segnalazione($database->getConnection());
$segnalazione->fk_itinerario = $_POST["fk_itinerario"];

$stmt = $segnalazione->get_segnalazione();
$num = $stmt->rowCount();

if($num > 0)
{
  $jsonarray = array();

  while ($row = $stmt->fetch(PDO::FETCH_ASSOC))
  {
    extract($row);
    $user_data = array(
      "titolo" => $titolo,
      "descrizione" => $descrizione,
      "fk_utente" => $fk_utente,
    );
    array_push($jsonarray, $user_data);

  }
    echo json_encode($jsonarray);
    http_response_code(200);
}
else
{
  http_response_code(404);
}

 ?>
