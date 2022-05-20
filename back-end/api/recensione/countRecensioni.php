<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");


include_once ('../../connection/user_connection.php');
include_once ('../../tables/recensione.php');

$database = new Database();
$recensione = new Recensione($database->getConnection());

$stmt = $recensione->count();
$num = $stmt->rowCount();

if($num > 0)
{

  while ($row = $stmt->fetch(PDO::FETCH_ASSOC))
  {
    extract($row);
    $user_data = array(
      "numero" => $numero
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
