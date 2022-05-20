<?php

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/itinerario.php');

$database = new Database();
$itinerario = new Itinerario($database->getConnection());

$itinerario->nome = $_POST["nome"];

$stmt = $itinerario->test();

$num = $stmt->rowCount();

if($num > 0) {

  $arr = array();

  while ($row = $stmt->fetch(PDO::FETCH_ASSOC))
  {
    extract($row);

    $user_data = array(
      "id_itinerario" => $id_itinerario,
  
    );
  }
  echo json_encode($user_data);
  http_response_code(200);
}
else
{
  http_response_code(404);
}

?>
