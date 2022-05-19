package com.example.natour.view.InserimentoItinerarioActivity;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionInflater;

import com.example.natour.R;
import com.example.natour.controller.ControllerItinerario;
import com.example.natour.model.Immagine;
import com.google.android.material.textfield.TextInputLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class InserimentoPercorsoFragment extends Fragment implements MapEventsReceiver, LocationListener
{
    private MapView mapView;
    private OverlayViewModel model;
    private ImageButton indietro;
    private ControllerItinerario controllerItinerario;

    private FrameLayout backContainer;

    private Marker mrk_inizioPercorso;
    private Marker mrk_finePercorso;
    private ArrayList<GeoPoint> waypoints = new ArrayList<>();

    private Road road;
    private Polyline roadOverlay;

    private EditText edt_inizioPercorso;
    private EditText edt_finePercorso;
    private ImageButton deleteMarkerFine;
    private ImageButton deleteMarkerInizio;
    private ImageButton deleteRoad;
    private ImageButton insertGPX;


    private boolean inizio = false;
    private boolean fine = false;


    public InserimentoPercorsoFragment()
    {
        // Required empty public constructor
    }

    //passiamo il fragment della mappa e quello precedente per matenere la persistenza dei dati
    public InserimentoPercorsoFragment(ControllerItinerario controllerItinerario)
    {
        this.controllerItinerario = controllerItinerario;
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(requireContext()).inflateTransition(R.transition.shared_image));
        setEnterTransition(TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_inserimento_percorso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);



        OSRMRoadManager roadManager = new OSRMRoadManager(requireContext(), "MyOwnUserAgent/1.0");
        roadManager.setMean(OSRMRoadManager.MEAN_BY_FOOT);


        mapView = view.findViewById(R.id.map);
        ViewGroupCompat.setTransitionGroup(mapView,true);
        ViewCompat.setTransitionName(mapView, "big_map");

        String MY_USER_AGENT = "MyOwnUserAgent/1.0";
        Configuration.getInstance().setUserAgentValue(MY_USER_AGENT);
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        mapView.getOverlays().add(0,mapEventsOverlay);
        mapView.setMultiTouchControls(true);
        //si setta lo stato UI iniziale per la mappa
        IMapController mapController = mapView.getController();
        mapController.setZoom(13.5);
        mapController.setCenter(controllerItinerario.currentPositionPhone(this));

        //comando usato per aggiornare la mappa
        mapView.invalidate();

        mrk_inizioPercorso = new Marker(mapView);
        mrk_finePercorso = new Marker(mapView);



        mapView = requireView().findViewById(R.id.map);
        model = new ViewModelProvider(requireActivity()).get(OverlayViewModel.class);

        model.getInizio().observe(getViewLifecycleOwner(),
                p ->{
                    if(p != null){
                        inizio = true;
                        Marker marker = new Marker(mapView);
                        marker.setPosition(p);
                        marker.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_location_on_24));
                        marker.setTitle("inizio");
                        mrk_inizioPercorso = marker;

                        new Thread(()->{
                            String address = getAddress(p);
                            requireActivity().runOnUiThread(()->edt_inizioPercorso.setText(address));
                        }).start();

                        mapView.getOverlays().add(mrk_inizioPercorso);
                        mapController.setCenter(p);
                        mapView.invalidate();

                        deleteMarkerInizio.setEnabled(true);
                        deleteMarkerInizio.setImageAlpha(255);
                    }
                });
        model.getFine().observe(getViewLifecycleOwner(),
                p->{
                    if(p != null){
                        fine = true;
                        Marker marker = new Marker(mapView);
                        marker.setPosition(p);
                        marker.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_finepercorso));
                        marker.setTitle("fine");
                        mrk_finePercorso = marker;

                        new Thread(()->{
                            String address = getAddress(p);
                            requireActivity().runOnUiThread(()->edt_finePercorso.setText(address));
                        }).start();



                        mapView.getOverlays().add(mrk_finePercorso);
                        mapView.invalidate();

                        deleteMarkerFine.setEnabled(true);
                        deleteMarkerFine.setImageAlpha(255);
                    }


                });
        model.getWaypoints().observe(getViewLifecycleOwner(),
                newWaypoints ->{

                    if(newWaypoints.size()>=1){
                        new Thread(()->{
                            mapView.getOverlays().remove(roadOverlay);
                            road = roadManager.getRoad(waypoints);
                            roadOverlay = RoadManager.buildRoadOverlay(road);
                            mapView.getOverlays().add(roadOverlay);
                            //per aggiornare l'UI della mappa è necessario farlo nel main thread
                            requireActivity().runOnUiThread(() -> mapView.invalidate());
                        }).start();
                        deleteRoad.setEnabled(true);
                        deleteRoad.setImageAlpha(255);
                    }
                });
        model.getImgList().observe(getViewLifecycleOwner(),
                newImgList ->
                {
                    for (Immagine img: newImgList)
                    {
                        Marker imgMarker = new Marker(mapView);
                        imgMarker.setPosition(img.getMarker().getPosition());
                        imgMarker.setIcon(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_photo_location));
                        try
                        {
                            InputStream is = requireActivity().getContentResolver().openInputStream(img.getUri());
                            imgMarker.setImage(Drawable.createFromStream(is, img.getUri().toString()));
                        } catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                        img.setMarker(imgMarker);
                        mapView.getOverlays().add(imgMarker);
                        mapView.invalidate();
                    }
                });

        backContainer = requireView().findViewById(R.id.frameIndietro);
        edt_inizioPercorso = requireView().findViewById(R.id.edt_inizioPercorso);
        edt_finePercorso = requireView().findViewById(R.id.edt_finePercorso);
        indietro = requireView().findViewById(R.id.btn_indietro);
        deleteMarkerInizio = requireView().findViewById(R.id.btn_deletemarkerInizio);
        deleteMarkerFine = requireView().findViewById(R.id.btn_deletemarkerFine);
        deleteRoad = requireView().findViewById(R.id.removeroad);
        insertGPX = requireView().findViewById(R.id.btn_filegpx);

        if(!inizio){
            deleteMarkerInizio.setImageAlpha(66);
            deleteMarkerInizio.setEnabled(false);
        }
        if(!fine){
            deleteMarkerFine.setImageAlpha(66);
            deleteMarkerFine.setEnabled(false);
        }
        if(!inizio || !fine){
            deleteRoad.setImageAlpha(66);
            deleteRoad.setEnabled(false);
        }

        deleteMarkerInizio.setOnClickListener(deleteMarkerStart ->
        {
            mapView.getOverlays().remove(mrk_inizioPercorso);
            waypoints.remove(0);
            model.setWaypoints(waypoints);
            model.setInizio(null);
            mapView.invalidate();
            deleteMarkerInizio.setImageAlpha(66);
            deleteMarkerInizio.setEnabled(false);
            edt_inizioPercorso.setText("");
            inizio = false;
        });
        deleteMarkerFine.setOnClickListener(deleteMarkerEnd ->
        {
            mapView.getOverlays().remove(mrk_finePercorso);
            waypoints.remove(mrk_finePercorso.getPosition());
            model.setWaypoints(waypoints);
            model.setFine(null);
            mapView.invalidate();
            deleteMarkerFine.setImageAlpha(66);
            deleteMarkerFine.setEnabled(false);
            edt_finePercorso.setText("");
            fine = false;
        });
        deleteRoad.setOnClickListener(deleteStrada ->
        {
            mapView.getOverlays().remove(mrk_inizioPercorso);
            mapView.getOverlays().remove(mrk_finePercorso);
            mapView.getOverlays().remove(roadOverlay);
            waypoints.clear();
            model.setInizio(null);
            model.setFine(null);
            model.setWaypoints(waypoints);
            mapView.invalidate();
            deleteRoad.setImageAlpha(66);
            deleteRoad.setEnabled(false);
            deleteMarkerFine.setImageAlpha(66);
            deleteMarkerFine.setEnabled(false);
            deleteMarkerInizio.setImageAlpha(66);
            deleteMarkerInizio.setEnabled(false);
            edt_inizioPercorso.setText("");
            edt_finePercorso.setText("");
            inizio = false;
            fine = false;
        });
        insertGPX.setOnClickListener( btn ->
        {
            controllerItinerario.getGPXFromDevice();
        });


        //Inizialmente i bottoni per aggiungere marker dal testo devono essere nascosti
        TextInputLayout til_inizio = (TextInputLayout) edt_inizioPercorso.getParent().getParent();
        til_inizio.setEndIconVisible(false);
        TextInputLayout til_fine = (TextInputLayout) edt_finePercorso.getParent().getParent();
        til_fine.setEndIconVisible(false);

        //listener per poter aggiungere un marker partendo da un indirizzo inserito manualmente
        til_inizio = (TextInputLayout) edt_inizioPercorso.getParent().getParent();
        til_inizio.setEndIconOnClickListener(til ->
        {
            if(!edt_inizioPercorso.getText().toString().isEmpty()){

                GeoPoint p = controllerItinerario.getLocationFromAddress(edt_inizioPercorso.getText().toString());
                if (p != null){
                    mapView.getOverlays().remove(mrk_inizioPercorso);
                    waypoints.add(0,p);
                    model.setWaypoints(waypoints);
                    model.setInizio(p);
                }
            }

        });
        til_fine = (TextInputLayout) edt_finePercorso.getParent().getParent();
        til_fine.setEndIconOnClickListener(til ->
        {
            if(!edt_finePercorso.getText().toString().isEmpty()){

                GeoPoint p = controllerItinerario.getLocationFromAddress(edt_finePercorso.getText().toString());
                if (p != null){
                    mapView.getOverlays().remove(mrk_finePercorso);
                    waypoints.add(p);
                    model.setWaypoints(waypoints);
                    model.setFine(p);
                }
            }

        });

        TextInputLayout finalTil_inizio = til_inizio;
        edt_inizioPercorso.setOnFocusChangeListener((til, b) -> finalTil_inizio.setEndIconVisible(b));
        TextInputLayout finalTil_fine = til_fine;
        edt_finePercorso.setOnFocusChangeListener((til, b) -> finalTil_fine.setEndIconVisible(b));

        //questo listener contiene l'azione e l'animazione per tornare indietro
        backContainer.setOnClickListener(view1 ->
        {
            buttonAnimation();//l'animazione dura in tutto 700ms

            /* permette all'animazione di compiersi prima di tornare all'attività precedente*/
            new Handler().postDelayed(() ->
            {
                indietro.setVisibility(View.INVISIBLE);
                backContainer.setVisibility(View.INVISIBLE);
                controllerItinerario.goBack();
            },700);
        });

        /*codice necessario per impedire che sia attivato il focus per il campo di testo
         * al lancio del fragment ma al click diventa di nuovo focusabile
         * */
        edt_inizioPercorso.setFocusable(false);
        edt_inizioPercorso.setOnClickListener(view12 ->
        {
            clickEditText(edt_inizioPercorso);
        });

        /*codice necessario per impedire che sia attivato il focus per il campo di testo
         * al lancio del fragment ma al click diventa di nuovo focusabile
         * */
        edt_finePercorso.setFocusable(false);
        edt_finePercorso.setOnClickListener(view13 ->
        {
            clickEditText(edt_finePercorso);
        });




    }

    private void buttonAnimation()
    {
        //animazione per la pulsazione del tasto back
        AnimationSet animationSet = new AnimationSet(true);
        Animation bigger = new ScaleAnimation(1f, 1.25f, // Start and end values for the X axis scaling
                1f, 1.25f, // Start and end values for the Y axis scaling
                Animation.ABSOLUTE, backContainer.getPivotX(), // Pivot point of X scaling
                Animation.ABSOLUTE, backContainer.getPivotY());
        bigger.setDuration(500);
        Animation smaller = new ScaleAnimation(1f, 0, // Start and end values for the X axis scaling
                1f, 0, // Start and end values for the Y axis scaling
                Animation.ABSOLUTE, backContainer.getPivotX(), // Pivot point of X scaling
                Animation.ABSOLUTE, backContainer.getPivotY());
        smaller.setDuration(200);
        smaller.setStartOffset(200);
        animationSet.addAnimation(bigger);
        animationSet.addAnimation(smaller);
        animationSet.setFillAfter(true);


        //fade out dell'icona back
        Animation fadeOut = new AlphaAnimation(indietro.getAlpha(), 0f);
        fadeOut.setFillAfter(true);
        fadeOut.setDuration(500);

        indietro.startAnimation(fadeOut);
        backContainer.startAnimation(animationSet);
    }

    public EditText getEdt_inizioPercorso()
    {
        return edt_inizioPercorso;
    }

    public EditText getEdt_finePercorso()
    {
        return edt_finePercorso;
    }

    public ImageButton getDeleteMarkerFine()
    {
        return deleteMarkerFine;
    }

    public ImageButton getDeleteMarkerInizio()
    {
        return deleteMarkerInizio;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p)
    {
        
        if(!inizio){
            waypoints.add(0,p);
            model.setWaypoints(waypoints);
            model.setInizio(p);
        }
        else if(!fine){
            waypoints.add(p);
            model.setWaypoints(waypoints);
            model.setFine(p);
        }
        else{
            mapView.getOverlays().remove(mrk_finePercorso);
            waypoints.add(p);
            model.setWaypoints(waypoints);
            model.setFine(p);
        }
        return false;
    }
    private void clickEditText(EditText edt)
    {
        edt.setFocusable(true);
        edt.setFocusableInTouchMode(true);
        edt.setEnabled(true);
        edt.requestFocus();
        //((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edt, 0);
    }
    @Override
    public boolean longPressHelper(GeoPoint p)
    {
        return false;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        mapView.onDetach();
    }


    public void setPointsFromGPX(ArrayList<GeoPoint> waypoints)
    {
        mapView.getOverlays().remove(mrk_inizioPercorso);
        mapView.getOverlays().remove(mrk_finePercorso);
        model.setInizio(waypoints.get(0));
        model.setFine(waypoints.get(waypoints.size()-1));
        this.waypoints = waypoints;
        model.setWaypoints(this.waypoints);
    }


    @Override
    public void onLocationChanged(@NonNull Location location)
    {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public String getAddress(GeoPoint point)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        String address = null;
        try
        {
            addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        return address;
    }
}