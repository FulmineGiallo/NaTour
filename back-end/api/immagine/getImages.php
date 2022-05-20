<?php
  header("Access-Control-Allow-Origin: *");
  header("Access-Control-Allow-Methods: POST");
  header("Access-Control-Max-Age: 3600");
  header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

  include_once ('../../connection/user_connection.php');
  include_once ('../../tables/immagine.php');


  $database = new Database();
  $immagine = new Immagine($database->getConnection());

  $immagine->fk_itinerario = $_POST["fk_itinerario"];

  $stmt = $immagine->get_image();
  $num = $stmt->rowCount();




  if($num > 0)
  {
    $jsonarray = array();
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC))
    {
      extract($row);
      $user_data = array(
        "id_key" => $id_key,
        "lat_posizione" => $lat_posizione,
        "long_posizione" => $long_posizione,
      );
      array_push($jsonarray, $user_data);

    }
    echo json_encode($jsonarray);
    http_response_code(200);
  }
  else
  {
    $array2 = array();
    echo json_encode($array2);
    http_response_code(404);
  }

?>
