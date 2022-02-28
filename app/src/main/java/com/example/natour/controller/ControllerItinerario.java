package com.example.natour.controller;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.rx.RxStorageBinding;
import com.amplifyframework.storage.result.StorageUploadFileResult;
import com.amplifyframework.storage.result.StorageUploadInputStreamResult;
import com.example.natour.R;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.connection.RequestAPI;
import com.example.natour.model.dao.ImmagineDAO;
import com.example.natour.model.dao.ItinerarioDAO;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerario;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerarioFragment;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoPercorsoFragment;
import com.example.natour.view.adapter.ImageAdapter;
import com.example.natour.view.dialog.ErrorDialog;

import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.rxjava3.subjects.PublishSubject;
import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;

public class ControllerItinerario
{
    private String nomeItinerario;
    private String durataItinerario;
    private boolean disabiliItinerario;
    private int difficoltàItinerario; //Ha 5 possibili valori, da 1 a 5
    private File gpx;
    private String descrizioneItnerario;
    private static final int PICKFILE_REQUEST_CODE = 100;
    private ImageAdapter imageAdapter;
    private String token;

    //informazioni della mappa
    private final ArrayList<GeoPoint> waypoints = new ArrayList<>();

    /* Coppia valore Key = URI */
    private List<Immagine> mapKeyURI;


    private final FragmentManager fragmentManager;
    private final InserimentoItinerario inserimentoItinerarioActivity;
    private InserimentoPercorsoFragment percorsoFragment;
    private InserimentoItinerarioFragment inserimentoItinerarioFragment;


    public void inserisciItinerario(float value, String nome, String durata, boolean disabili, String descrizione, ArrayList<GeoPoint> waypoints, LinkedList<Immagine> imgList, Context context)
    {
        Itinerario itinerarioInserito = new Itinerario();
        String chiave = UUID.randomUUID().toString();;
        /* INSERIMENTO DELL'ID ALL'INTERNO DEL DATABASE E DELLE SUE INFORMAZIONI DI BASE */
        /* Chiamato all'ItinerarioDAO */
        try
        {
            File gpx = createFileGPX(waypoints, chiave);

            /* UPLOAD FILE SU S3 */
            uploadFileGPXOnS3(gpx, chiave);
            ItinerarioDAO itinerarioDAO = new ItinerarioDAO();
            PublishSubject<JSONObject> risultato = itinerarioDAO.insertItinerario(chiave, nome, durata, disabili,(int) value, descrizione, context, token, chiave);
            risultato.subscribe(
                    data ->
                    {
                        if(data.getBoolean("risultato"))
                        {
                            /*  Se INSERT dell'itinerario è andato a buon fine, allora gli associo le immagini */
                            ImmagineDAO insertImmagineItinerario = new ImmagineDAO();

                        }
                        else
                        {
                            /* INSERT FALLITO */
                            new ErrorDialog("Caricamento dell'itinerario fallito, controlla la tua connessione e riprova :(").show(fragmentManager, null);
                            //TODO: eliminare il file da S3
                        }
                    },
                    error ->
                    {


                    }
            );

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



    }

    private void uploadFileGPXOnS3(File gpx, String chiave)
    {

        RxStorageBinding.RxProgressAwareSingleOperation<StorageUploadFileResult> rxUploadOperation =
                RxAmplify.Storage.uploadFile(chiave, gpx);

        rxUploadOperation
                .observeResult()
                .subscribe(
                        result -> Log.i("File Posizione", "Successfully uploaded: " + result.getKey()),
                        error -> Log.e("MyAmplifyApp", "Upload failed", error)
                );
    }

    private File createFileGPX(ArrayList<GeoPoint> waypoints, String chiave) throws IOException
    {
        // open file handle
        StringBuffer buffer = new StringBuffer();
        File gpxfile = null;
        for(GeoPoint p : waypoints)
        {
            buffer.append(p.getLatitude() + "," + p.getLongitude() + ":");
        }
        buffer.append(";");
        Environment.getExternalStorageState();
        try
        {
            File root = new File(inserimentoItinerarioActivity.getCacheDir(),"filetmp");
            if (!root.exists())
            {
                root.mkdirs();
            }
            gpxfile = new File(root, "waypoints"+ chiave +".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(buffer.toString());
            writer.flush();
            writer.close();

            Toast.makeText(inserimentoItinerarioActivity, inserimentoItinerarioActivity.getCacheDir().toString(), Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return gpxfile;
    }

    public ControllerItinerario(FragmentManager fragmentManager, InserimentoItinerario inserimentoItinerarioActivity, String token)
    {
        this.fragmentManager = fragmentManager;
        this.inserimentoItinerarioActivity = inserimentoItinerarioActivity;
        this.mapKeyURI = new ArrayList<>();
        this.imageAdapter = new ImageAdapter(mapKeyURI, fragmentManager, this);
        this.token = token;
    }

    public void inizializzaInterfaccia()
    {
        if (inserimentoItinerarioActivity.findViewById(R.id.fragmentMappa) != null)
        {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            inserimentoItinerarioFragment = new InserimentoItinerarioFragment(this);
            ft.add(R.id.fragmentMappa, inserimentoItinerarioFragment);
            ft.commit();

        }
    }

    public void gotoPercorsoFragment(MapView imageView)
    {
        if (percorsoFragment == null)
            percorsoFragment = new InserimentoPercorsoFragment(this);

        fragmentManager
                .beginTransaction()
                .addSharedElement(imageView, "big_map")
                .replace(R.id.fragmentMappa, percorsoFragment)
                .addToBackStack(InserimentoItinerarioFragment.class.getSimpleName())
                .commit();
    }


    public void goBack()
    {
        imageAdapter.notifyDataSetChanged();
        inserimentoItinerarioActivity.onBackPressed();
    }


    public GeoPoint currentPositionPhone( LocationListener locationListener)
    {
        LocationManager locManager = (LocationManager) inserimentoItinerarioActivity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(inserimentoItinerarioActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(inserimentoItinerarioActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, locationListener);
            Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude;
            double latitude;
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            return new GeoPoint(latitude, longitude);
        } else
            return new GeoPoint(40.839326626673405, 14.185227261143826);
    }

    public GeoPoint getLocationFromAddress(String strAddress)
    {
        Geocoder coder = new Geocoder(inserimentoItinerarioActivity);
        List<Address> address;
        GeoPoint p1;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint(location.getLatitude(), location.getLongitude());

            return p1;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void uploadFile()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(inserimentoItinerarioActivity.getPackageManager()) != null)
            inserimentoItinerarioActivity.startActivityForResult(intent, PICKFILE_REQUEST_CODE);

    }

    public void fileInserito(int requestCode, int resultCode, Intent intent)
    {

        Uri singolaFoto;
        String singolaKey = UUID.randomUUID().toString();


        if (resultCode == RESULT_OK)
        {
            /* Inserimento singolo di un immagine */
            if(intent.getData() != null)
            {
                singolaFoto = intent.getData();
                Immagine immagine = new Immagine(singolaFoto, singolaKey);



                if(!controlloUriDoppio(mapKeyURI, immagine))
                {
                    mapKeyURI.add(immagine);
                    imageAdapter.notifyItemInserted(mapKeyURI.indexOf(immagine));
                    uploadOnS3Bucket(immagine, mapKeyURI.indexOf(immagine));
                }
            }
        }
    }
    public boolean controlloUriDoppio(List<Immagine> immagini, Immagine findUri)
    {
        return immagini.contains(findUri);
    }

    public void uploadOnS3Bucket(Immagine immagine, int positionImage)
    {
        try
        {
            InputStream exampleInputStream = inserimentoItinerarioActivity.getContentResolver().openInputStream(immagine.getUri());
            RxStorageBinding.RxProgressAwareSingleOperation<StorageUploadInputStreamResult> rxUploadOperation =
                    RxAmplify.Storage.uploadInputStream(immagine.getKey(), exampleInputStream);

            inserimentoItinerarioFragment.startMapLoading();
            rxUploadOperation
                    .observeResult()
                    .subscribe(
                            result ->
                            {
                                Log.i("MyAmplifyApp", "Successfully uploaded: ");
                                RxAmplify.Storage.getUrl(immagine.getKey()).subscribe(
                                        risultato -> {
                                            Log.i("MyAmplifyApp", "Successfully generated: " + risultato.getUrl());
                                            String URL = "https://besimsoft.com/.natour21/uq6PMpSfiZ/api/itinerary/nfws";
                                            String URLImage = risultato.getUrl().toString();
                                            //Controllo effettuata sulla singola aggiunta
                                            Map<String, String> paramsUrl = new HashMap<>();
                                            paramsUrl.put("image_url", URLImage);
                                            RequestAPI requestImage = new RequestAPI("", inserimentoItinerarioActivity, paramsUrl);
                                            requestImage.setEndpoint(URL);
                                            PublishSubject<JSONObject> response = requestImage.sendRequest();
                                            response.subscribe(
                                                    imageResult ->
                                                    {
                                                        if(imageResult.getBoolean("is_save"))
                                                        {
                                                            Log.i("CONFERMA IMG", "Rimane nel Bucket, immagine valida");
                                                            /* recupero metadati */
                                                            immagine.setURL(URLImage);
                                                            getMetadatiImage(exampleInputStream, immagine);
                                                            inserimentoItinerarioFragment.stopMapLoading();
                                                        }
                                                        else
                                                        {
                                                            inserimentoItinerarioFragment.stopMapLoading();
                                                            removeImageFromS3Bucket(immagine, positionImage);
                                                            new ErrorDialog("L'immagine che hai inserito è esplicità, è stata rimossa!").show(fragmentManager, null);
                                                        }
                                                    },
                                                    imageError ->
                                                    {
                                                        inserimentoItinerarioFragment.stopMapLoading();
                                                        Log.e("ERRORIMG", imageError.getLocalizedMessage());
                                                        new ErrorDialog("Errore nel caricamento dell'ìmmagine, riprova con un'altra immagine!").show(fragmentManager, null);
                                                    }
                                            );
                                        },
                                        error ->
                                        {
                                            Log.e("MyAmplifyApp", "URL generation failure", error);
                                            inserimentoItinerarioFragment.stopMapLoading();
                                        }
                                );

                            },
                            error ->
                            {
                                Log.e("MyAmplifyApp", "Upload failed", error);
                                inserimentoItinerarioFragment.stopMapLoading();
                            }
                    );
        }
        catch (FileNotFoundException error)
        {
            Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
        }
    }

    public void removeImageFromS3Bucket(Immagine img, int positionImage)
    {
        /* Rimozione dalla Lista e poi dal Bucket S3 */
        mapKeyURI.remove(positionImage);
        imageAdapter.notifyItemRemoved(positionImage);
        inserimentoItinerarioFragment.removeImage(img);
        /* Rimozione da S3 */
        RxAmplify.Storage.remove(img.getKey())
                .subscribe(
                        onRemove ->
                        {
                            Log.i("MyAmplifyApp", "Successfully removed: " + onRemove.getKey());

                        },
                        error ->
                        {
                            Log.e("MyAmplifyApp", "Remove failure", error);
                        }
                );
    }

    public void removeImageFromS3Bucket(Immagine img)
    {


        /* Rimozione da S3 */
        inserimentoItinerarioFragment.removeImage(img);
        RxAmplify.Storage.remove(img.getKey())
                .subscribe(
                        onRemove ->
                        {
                            Log.i("MyAmplifyApp", "Successfully removed: " + onRemove.getKey());

                        },
                        error ->
                        {
                            Log.e("MyAmplifyApp", "Remove failure", error);
                        }
                );
    }

    public void removeOnBackPressedImage()
    {
        for(Immagine img : mapKeyURI)
        {
            removeImageFromS3Bucket(img);
        }
        mapKeyURI.clear();
    }

    public void updateScrollViewImage(RecyclerView mRecyclerView)
    {
        LinearLayoutManager linearLayout = new LinearLayoutManager(inserimentoItinerarioFragment.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setAdapter(imageAdapter);


    }

    public void getMetadatiImage(InputStream immagine, Immagine uriImage)
    {
        /* Se la posizione dell'immagine è presente */
        try
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            {
                immagine = inserimentoItinerarioActivity.getContentResolver().openInputStream(uriImage.getUri());
                ExifInterface metadati = new ExifInterface(immagine);
                float[] latLong = new float[2];
                if(metadati.getLatLong(latLong))
                {
                    Log.i("Metadati", "POS" + latLong[0] + " " + latLong[1]);
                    inserimentoItinerarioFragment.addPhotoMarker(uriImage, latLong);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public void getGPXFromDevice()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/xml");
        Log.i("LANCIO", "GPX");
        if (intent.resolveActivity(inserimentoItinerarioActivity.getPackageManager()) != null)
            inserimentoItinerarioActivity.startActivityForResult(intent, 20);

    }
    public void insertGPX(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == 20)
            if (resultCode == RESULT_OK)
            {
                String fileName = inserimentoItinerarioActivity.getContentResolver().getType(intent.getData());
                Log.e("FILENAME", fileName);
                // Inserito il file dal File Explorer viene effettuato un controllo per garantire che il file sia del tipo .gpx
                if (fileName.contains("xml"))
                {
                    // Essendo il file di tipo .gpx vengono presi dall'intent le informazioni per gestire l'itinerario
                    GPXParser parser = new GPXParser();
                    Gpx parsedGpx = null;
                    try
                    {
                        InputStream in = inserimentoItinerarioActivity.getContentResolver().openInputStream(intent.getData());
                        parsedGpx = parser.parse(in);
                    }
                    catch (IOException  | XmlPullParserException e)
                    {
                        new ErrorDialog("Errore nel caricamento del file .gpx").show(fragmentManager, null);
                        e.printStackTrace();
                    }
                    if (parsedGpx == null)
                    {
                        new ErrorDialog("Errore nel caricamento del file .gpx").show(fragmentManager, null);
                    }
                    else
                    {
                        /*
                            Nei casi precedenti viene controllato che il file gpx sia stato correttamente  analizzato dal parser
                            Ed entra in questo else solamente se vengono passati tutti i controlli procedendo poi a recuperare eventuali routes e waypoint
                        */
                        if(parsedGpx.getTracks() != null)
                        {
                            waypoints.clear();
                            for(int i = 0; i < parsedGpx.getTracks().size(); i++){
                                for(int j = 0; j < parsedGpx.getTracks().get(i).getTrackSegments().get(i).getTrackPoints().size(); j++){
                                    waypoints.add(new GeoPoint(parsedGpx.getTracks().get(i).getTrackSegments().get(i).getTrackPoints().get(j).getLatitude(), parsedGpx.getTracks().get(i).getTrackSegments().get(i).getTrackPoints().get(j).getLongitude()));
                                    Log.i("WAYPOINTS", waypoints.get(j).getLatitude() + "    " + waypoints.get(j).getLongitude());
                                }
                            }
                            percorsoFragment.setPointsFromGPX(waypoints);
                            /* Creare Route nel mappaFragment */
                            Log.e("WAYPOINTS SIZE", String.valueOf(waypoints.size()));
                        }

                    }
                }
                else
                {
                    new ErrorDialog("Il file inserito non è di tipo .gpx").show(fragmentManager, null);
                }
            }
    }



}
