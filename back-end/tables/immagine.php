<?php
class Immagine
{
	private $conn;
	private $table_name = "immagine";

	public $id_key;
	public $lat_posizione;
	public $long_posizione;
	public $fk_itinerario;

	public function __construct($db)
	{
		$this->conn = $db;
	}

	function insert()
	{
		$stmt = $this->conn->prepare("INSERT INTO immagine (id_key, lat_posizione, long_posizione, fk_itinerario) VALUES (?,?,?,?)");

		$stmt->bindParam(1, $this->id_key, PDO::PARAM_STR);
		$stmt->bindParam(2, $this->lat_posizione, PDO::PARAM_STR);
    $stmt->bindParam(3, $this->long_posizione, PDO::PARAM_STR);
		$stmt->bindParam(4, $this->fk_itinerario, PDO::PARAM_STR);


    $stmt->execute();
    return $stmt;
	}

	function get_image()
	{
		  $trim = trim($this->fk_itinerario, " \n\r\t\v\x00");
			$stmt = $this->conn->prepare("SELECT * FROM immagine WHERE fk_itinerario = '$trim'");

			$stmt->execute();
	    return $stmt;

	}
	function count()
	{
				$stmt = $this->conn->prepare("SELECT COUNT(*) AS numero
																			FROM immagine");
				$stmt->execute();
				return $stmt;
	}
}
?>
