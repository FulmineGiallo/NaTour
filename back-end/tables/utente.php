<?php
class Utente
{
	private $conn;
	private $table_name = "utente";

	public $token;
	public $id_utente;
	public $data_di_nascita;
	public $is_admin;
	public $nome;
	public $cognome;

	public function __construct($db)
	{
		$this->conn = $db;
	}


	function login($token)
	{
		$token = trim($token, " \n\r\t\v\x00");
		$stmt = $this->conn->prepare("SELECT nome, cognome FROM utente WHERE token = '$token'");

		$stmt->execute();
		return $stmt;
	}


	function register()
	{
		$stmt = $this->conn->prepare("INSERT INTO utente (data_di_nascita, token, nome, cognome) VALUES (?,?,?,?)");

		$stmt->bindParam(1, $this->data_di_nascita, PDO::PARAM_STR);
		$stmt->bindParam(2, $this->token, PDO::PARAM_STR);
		$stmt->bindParam(3, $this->nome, PDO::PARAM_STR);
		$stmt->bindParam(4, $this->cognome, PDO::PARAM_STR);
		if ($stmt->execute())
		return true;
		else
		return false;
	}

	function getBirthdate($token)
	{
		$token = trim($token, " \n\r\t\v\x00");
		$stringa = "SELECT data_di_nascita FROM utente WHERE token = '$token'";
		$stmt = $this->conn->prepare("SELECT data_di_nascita FROM utente WHERE token = '$token'");

		$stmt->execute();
		return $stmt;
	}

	function insertUtente()
	{
		$stmt = $this->conn->prepare("INSERT INTO utente (token, nome, cognome) VALUES (?,?,?)");
		$trimtoken = trim($this->token, " \n\r\t\v\x00");
		$stmt->bindParam(1, $trimtoken, PDO::PARAM_STR);
		$stmt->bindParam(2, $this->nome, PDO::PARAM_STR);
		$stmt->bindParam(3, $this->cognome, PDO::PARAM_STR);

		$stmt->execute();
		return $stmt;
	}

	function isAdmin()
	{
		$trimtoken = trim($this->token, " \n\r\t\v\x00");
		$stmt = $this->conn->prepare("SELECT is_admin FROM utente WHERE token = '$trimtoken'");
		$stmt->execute();
		return $stmt;
	}

	function count()
	{
				$stmt = $this->conn->prepare("SELECT COUNT(*) AS numero
																			FROM utente");
				$stmt->execute();
		    return $stmt;
	}


}
?>
