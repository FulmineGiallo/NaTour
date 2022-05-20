<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once ('../../connection/user_connection.php');
include_once ('../../tables/compilation.php');


$database = new Database();
$compilation = new Compilation($database->getConnection());

$compilation->id_utente = $_POST["id_utente"];

$stmt = $compilation->get_compilation_from_utente();
$num = $stmt->rowCount();

if($num > 0)
{
  $jsonarray = array();

  while ($row = $stmt->fetch(PDO::FETCH_ASSOC))
  {
    extract($row);
    $user_data = array(
      "id_compilation" => $id_compilation,
      "nome" => $nome,
      "descrizione" => $descrizione,
      "id_utente" => $id_utente,
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
