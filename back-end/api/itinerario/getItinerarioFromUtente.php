<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/itinerario.php');


$database = new Database();
$itinerario = new Itinerario($database->getConnection());

$itinerario->fk_utente = $_POST["fk_utente"];

$stmt = $itinerario->getItinerarioFromUtente();
$num = $stmt->rowCount();

if($num > 0)
{
  $jsonarray = array();

  while ($row = $stmt->fetch(PDO::FETCH_ASSOC))
  {
    extract($row);
    $user_data = array(
      "id_itinerario" => $id_itinerario,
      "nome" => $nome,
      "durata" => $durata,
      "disabile" => $disabile,
      "difficolta" => $difficolta,
      "nomefile" => $nomefile,
      "descrizione" =>$descrizione,
    );
    array_push($jsonarray, $user_data);

  }
    echo json_encode($jsonarray);
    http_response_code(200);
}
else
{
  $array = array();
  echo json_encode($array);
  http_response_code(200);
}

 ?>
