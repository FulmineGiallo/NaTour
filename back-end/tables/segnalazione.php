<?php
class Segnalazione
{
	private $conn;
	private $table_name = "segnalazione";

	public $id_segnalazione;
  public $titolo;
	public $descrizione;
	public $fk_utente;
  public $fk_itinerario;

	public function __construct($db)
	{
		$this->conn = $db;
	}

	function insert()
	{
		$stmt = $this->conn->prepare("INSERT INTO segnalazione (titolo, descrizione, fk_utente, fk_itinerario) VALUES (?,?,?,?)");

		$stmt->bindParam(1, $this->titolo, PDO::PARAM_STR);
    $stmt->bindParam(2, $this->descrizione, PDO::PARAM_STR);
		$trimUtente = trim($this->fk_utente, " \n\r\t\v\x00");
		$stmt->bindParam(3, $trimUtente, PDO::PARAM_STR);
		$trimItinerario = trim($this->fk_itinerario, " \n\r\t\v\x00");
		$stmt->bindParam(4, $trimItinerario, PDO::PARAM_STR);

    $stmt->execute();
    return $stmt;
	}

	function get_segnalazione()
	{
		  $trim = trim($this->fk_itinerario, " \n\r\t\v\x00");
			$stmt = $this->conn->prepare("SELECT * FROM segnalazione WHERE fk_itinerario = '$trim'");

			$stmt->execute();
	    return $stmt;

	}
	function getCountSegnalazione()
	{
        $stmt = $this->conn->prepare("SELECT COUNT(*) AS numero
	                                    FROM segnalazione AS A INNER JOIN itinerario AS B ON A.fk_itinerario = B.id_itinerario
	                                    WHERE A.fk_itinerario = ?");
        $trimItinerario = trim($this->fk_itinerario, " \n\r\t\v\x00");
        $stmt->bindParam(1, $trimItinerario, PDO::PARAM_STR);
      	$stmt->execute();
        return $stmt;
  }
	function count()
	{
				$stmt = $this->conn->prepare("SELECT COUNT(*) AS numero
																			FROM segnalazione");
				$stmt->execute();
		    return $stmt;
	}
}
?>
