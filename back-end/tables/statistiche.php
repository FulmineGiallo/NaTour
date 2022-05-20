<?php
class Statistiche
{
	private $conn;
	private $table_name = "statastiche";

	public $num_login;
	public $num_ricerche;


	public function __construct($db)
	{
		$this->conn = $db;
	}

	function updateLogin()
	{
		$stmt = $this->conn->prepare("UPDATE statistiche
																	SET num_login = num_login + 1");
		$stmt->execute();
		return $stmt;
	}
	function updateRicerche()
	{
		$stmt = $this->conn->prepare("UPDATE statistiche
																	SET num_ricerche = num_ricerche + 1");
		$stmt->execute();
		return $stmt;
	}
	function getNumLogin()
	{
		$stmt = $this->conn->prepare("SELECT num_login AS numero
																	FROM statistiche");
		$stmt->execute();
		return $stmt;
	}
	function getNumRicerche()
	{
		$stmt = $this->conn->prepare("SELECT num_ricerche AS numero
																	FROM statistiche");
		$stmt->execute();
		return $stmt;
	}

}
?>
