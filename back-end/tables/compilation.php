<?php
class Compilation
{
	private $conn;
	private $table_name = "compilation";

	public $id_compilation;
	public $nome;
	public $descrizione;
	public $id_utente;

	public function __construct($db)
	{
		$this->conn = $db;
	}

	function create_compilation()
	{
		$stmt = $this->conn->prepare("INSERT INTO compilation (nome, descrizione, id_utente) VALUES (?,?,?)");

		$stmt->bindParam(1, $this->nome, PDO::PARAM_STR);
    $stmt->bindParam(2, $this->descrizione, PDO::PARAM_STR);
		$this->id_utente = trim($this->id_utente, " \n\r\t\v\x00");
		$stmt->bindParam(3, $this->id_utente, PDO::PARAM_STR);

    $stmt->execute();
    return $stmt;
	}

  function add_itinerario($id_itinerario)
  {
    $stmt = $this->conn->prepare("INSERT INTO itinerario_compilation (id_itinerario, id_compilation) VALUES (?,?)");
		$id_itinerario = trim($id_itinerario, " \n\r\t\v\x00");
		$stmt->bindParam(1, $id_itinerario, PDO::PARAM_STR);
    $stmt->bindParam(2, $this->id_compilation, PDO::PARAM_INT);
    $stmt->execute();
    return $stmt;
  }


	function get_compilation_from_utente()
	{
		$trim = trim($this->id_utente, " \n\r\t\v\x00");
		$stmt = $this->conn->prepare("SELECT * FROM compilation WHERE id_utente = '$trim'");

		$stmt->execute();
		return $stmt;

	}

  function delete_compilation()
  {

  }

	function get_itinerari_from_compilation()
	{
		$stmt = $this->conn->prepare("SELECT A.id_itinerario, A.nome, A.descrizione, A.nomefile, A.durata, A.difficolta, A.disabile, A.fk_utente
																	FROM itinerario A
																	INNER JOIN itinerario_compilation B on A.id_itinerario = B.id_itinerario
																	WHERE id_compilation = ?");
		$stmt->bindParam(1, $this->id_compilation);
		$stmt->execute();
		return $stmt;
	}
}
?>
