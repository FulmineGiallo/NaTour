<?php
  header("Access-Control-Allow-Origin: *");
  header("Access-Control-Allow-Methods: POST");
  header("Access-Control-Max-Age: 3600");
  header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

  include_once ('../../connection/user_connection.php');
  include_once ('../../tables/immagine.php');


  $database = new Database();
  $immagine = new Immagine($database->getConnection());

  $immagine->id_key = $_POST["id_key"];
  $immagine->lat_posizione = $_POST["lat_posizione"];
  $immagine->long_posizione = $_POST["long_posizione"];
  $immagine->fk_itinerario = $_POST["fk_itinerario"];


  if ($immagine->insert())
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
