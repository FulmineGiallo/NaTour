<?php
echo "0";
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

$image_url = $_POST["image_url"];
echo "0.5";
$curl = curl_init();

echo "1";

curl_setopt_array($curl, [
  CURLOPT_URL => "https://nsfw-images-detection-and-classification.p.rapidapi.com/adult-content",
  CURLOPT_RETURNTRANSFER => true,
  CURLOPT_FOLLOWLOCATION => true,
  CURLOPT_ENCODING => "",
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "POST",
  CURLOPT_POSTFIELDS => "{\r\n    \"url\": \"$image_url\"\r\n}",
  CURLOPT_HTTPHEADER => [
    "content-type: application/json",
    "x-rapidapi-host: nsfw-images-detection-and-classification.p.rapidapi.com",
    "x-rapidapi-key: 5c07866848msh3b1095760b64876p1af9a6jsn00d8006d23e7"
  ],
]);

echo "2";

$response = curl_exec($curl);
$err = curl_error($curl);

echo "3";
$isSave;

curl_close($curl);


echo "4";


if ($err) {
  echo "cURL Error #:" . $err;
} else {
  if (str_contains($response, 'true')) {
    $isSave = false;
  } else {
    $isSave = true;
  }
  $vector = array("is_save" => $isSave);
  echo json_encode($vector);
}

echo "5";

?>
