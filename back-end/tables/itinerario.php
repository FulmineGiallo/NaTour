<?php
class Itinerario
{
	private $conn;
	private $table_name = "itinerario";

	public $id_itinerario;
	public $nome;
	public $durata;
	public $disabile;
  public $difficolta;
  public $descrizione;
  public $fk_utente;
  public $nomefile;

	public function __construct($db)
	{
		$this->conn = $db;
	}
  function test()
  {
    $stmt = $this->conn->prepare("SELECT id_itinerario FROM itinerario WHERE nome = ?");
    $stmt->bindParam(1, $this->nome, PDO::PARAM_STR);
    $stmt->execute();
    return $stmt;
  }

	function insert()
	{
		$stmt = $this->conn->prepare("INSERT INTO itinerario (id_itinerario, nome, durata, disabile, difficolta, descrizione, fk_utente, nomefile) VALUES (?,?,?,?,?,?,?,?)");

		$stmt->bindParam(1, $this->id_itinerario, PDO::PARAM_STR);
		$stmt->bindParam(2, $this->nome, PDO::PARAM_STR);
    $stmt->bindParam(3, $this->durata, PDO::PARAM_STR);
		$stmt->bindParam(4, $this->disabile, PDO::PARAM_STR);
    $stmt->bindParam(5, $this->difficolta, PDO::PARAM_STR);
		$stmt->bindParam(6, $this->descrizione, PDO::PARAM_STR);
    $stmt->bindParam(7, $this->fk_utente, PDO::PARAM_STR);
		$stmt->bindParam(8, $this->nomefile, PDO::PARAM_STR);

    $stmt->execute();
    return $stmt;
	}

	function getHompage()
	{
		$stmt = $this->conn->prepare("SELECT * FROM itinerario");
		$stmt->execute();

		return $stmt;
	}

	function getItinerarioFromUtente()
	{
		$trim = trim($this->fk_utente, " \n\r\t\v\x00");
		$stmt = $this->conn->prepare("SELECT * FROM itinerario WHERE fk_utente = '$trim'");

		$stmt->execute();
		return $stmt;

	}
	function count()
	{
				$stmt = $this->conn->prepare("SELECT COUNT(*) AS numero
																			FROM itinerario");
				$stmt->execute();
		    return $stmt;
	}
}
?>
