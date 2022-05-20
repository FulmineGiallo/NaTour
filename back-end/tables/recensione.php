<?php
class Recensione
{
	private $conn;
	private $table_name = "recensione";

	public $id_recensione;
	public $testo;
	public $valutazione;
	public $fk_utente;
  public $fk_itinerario;

	public function __construct($db)
	{
		$this->conn = $db;
	}

	function insert()
	{
		$stmt = $this->conn->prepare("INSERT INTO recensione (testo, valutazione, fk_utente, fk_itinerario) VALUES (?,?,?,?)");

		$stmt->bindParam(1, $this->testo, PDO::PARAM_STR);
    $stmt->bindParam(2, $this->valutazione, PDO::PARAM_STR);
		$trimUtente = trim($this->fk_utente, " \n\r\t\v\x00");
		$stmt->bindParam(3, $trimUtente, PDO::PARAM_STR);
		$trimItinerario = trim($this->fk_itinerario, " \n\r\t\v\x00");
		$stmt->bindParam(4, $trimItinerario, PDO::PARAM_STR);

    $stmt->execute();
    return $stmt;
	}

	function get_recensione()
	{
		  $trim = trim($this->fk_itinerario, " \n\r\t\v\x00");
			$stmt = $this->conn->prepare("SELECT * FROM recensione WHERE fk_itinerario = '$trim'");

			$stmt->execute();
	    return $stmt;

	}
	function count()
	{
				$stmt = $this->conn->prepare("SELECT COUNT(*) AS numero
																			FROM recensione");
				$stmt->execute();
		    return $stmt;
	}
}
?>
