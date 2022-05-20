<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/compilation.php');


$database = new Database();
$compilation = new Compilation($database->getConnection());

$compilation->id_compilation = $_POST["id_compilation"];
$id_itinerario = $_POST["id_itinerario"];




if ($compilation->add_itinerario($id_itinerario))
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
