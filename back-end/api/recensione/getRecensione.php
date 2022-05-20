<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/recensione.php');


$database = new Database();
$recensione = new Recensione($database->getConnection());
$recensione->fk_itinerario = $_POST["fk_itinerario"];

$stmt = $recensione->get_recensione();
$num = $stmt->rowCount();

if($num > 0)
{
  $jsonarray = array();

  while ($row = $stmt->fetch(PDO::FETCH_ASSOC))
  {
    extract($row);
    $user_data = array(
      "id_recensione" => $id_recensione,
      "testo" => $testo,
      "valutazione" => $valutazione,
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
