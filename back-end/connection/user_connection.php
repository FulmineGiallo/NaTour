<?php

class Database
{
	private $host = "localhost";
	private $db_name = "natourapp";
	private $username = "postgres";
	private $password = "Carmine123";
	public $conn;

	public function getConnection()
	{
		$this->conn = null;
		try
		{
			$this->conn = new PDO("pgsql:host=" . $this->host . ";dbname=" . $this->db_name . ';options=\'--client_encoding=UTF-8\'', $this->username, $this->password);
		}
		catch(PDOException $exception)
		{
			echo "Connection error: " . $exception->getMessage();
		}
		return $this->conn;
	}

}
?>
