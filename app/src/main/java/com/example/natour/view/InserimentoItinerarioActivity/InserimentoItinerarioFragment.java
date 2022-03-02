package com.example.natour.view.InserimentoItinerarioActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.R;
import com.example.natour.controller.ControllerItinerario;
import com.example.natour.databinding.FragmentInserimentoItinerarioBinding;
import com.example.natour.model.Immagine;
import com.example.natour.view.dialog.ErrorDialog;
import com.google.android.material.slider.Slider;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;


public class InserimentoItinerarioFragment extends Fragment implements LocationListener
{
    private ControllerItinerario controllerItinerario;

    private EditText edtNome;
    private EditText edtDurata;
    private EditText edtDescrizione;
    private SwitchCompat selectDisabili;
    private boolean stateDisabili = false;
    private static final int PICKFILE_REQUEST_CODE = 100;
    private Button addImage;
    private RecyclerView mRecyclerView;
    private Button confermaInserimentoItinerario;
    private MapView mapView;
    private OverlayViewModel model;
    private final LinkedList<Immagine> imgList = new LinkedList<>();
    private Road road;
    private Polyline roadOverlay;
    private ProgressBar progMapLoading;
    private ImageView buttonNascosto;
    private ArrayList<GeoPoint> waypoints;

    private FragmentInserimentoItinerarioBinding mBinding;

    public InserimentoItinerarioFragment(ControllerItinerario controllerItinerario)
    {
        this.controllerItinerario = controllerItinerario;
    }

    public InserimentoItinerarioFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inserimento_itinerario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.containerMappa);
        ViewGroupCompat.setTransitionGroup(mapView,true);
        ViewCompat.setTransitionName(mapView, "tiny_map");
        IMapController mapController = mapView.getController();
        mapController.setZoom(13.5);
        mapController.setCenter(controllerItinerario.currentPositionPhone(this));
        mapView.invalidate();


        progMapLoading = requireView().findViewById(R.id.prog_map_loading);
        buttonNascosto = requireView().findViewById(R.id.btn_nascoto);
        confermaInserimentoItinerario = requireView().findViewById(R.id.btn_confermaItinerario);
        selectDisabili = requireView().findViewById(R.id.switch_disabili);
        addImage = requireView().findViewById(R.id.btn_addimg);
        ImageButton backToTabActivity = requireView().findViewById(R.id.btn_indietroInsItineraio);
        edtNome = requireView().findViewById(R.id.nomeItinerario);
        edtDurata = requireView().findViewById(R.id.edt_durata);
        edtDescrizione = requireView().findViewById(R.id.descrizioneEditText);
        mRecyclerView = requireView().findViewById(R.id.rec_view_images);


        OSRMRoadManager roadManager = new OSRMRoadManager(requireContext(), "MyOwnUserAgent/1.0");
        roadManager.setMean(OSRMRoadManager.MEAN_BY_FOOT);

        model = new ViewModelProvider(requireActivity()).get(OverlayViewModel.class);

        model.getInizio().observe(getViewLifecycleOwner(),
                p ->{
                    if(p != null){
                        Marker marker = new Marker(mapView);
                        marker.setPosition(p);
                        marker.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_location_on_24));
                        mapView.getOverlays().add(marker);
                        mapController.setCenter(p);
                        mapView.invalidate();
                    }
                });
        model.getFine().observe(getViewLifecycleOwner(),
                p->{
                    if(p != null){
                        Marker marker = new Marker(mapView);
                        marker.setPosition(p);
                        marker.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_finepercorso));

                        mapView.getOverlays().add(marker);
                        mapView.invalidate();
                    }
                });
        model.getWaypoints().observe(getViewLifecycleOwner(),
                newWaypoints ->{
                    if(newWaypoints.size()>=1){
                        new Thread(()->{

                            mapView.getOverlays().remove(roadOverlay);
                            road = roadManager.getRoad(newWaypoints);
                            roadOverlay = RoadManager.buildRoadOverlay(road);
                            mapView.getOverlays().add(roadOverlay);
                            //per aggiornare l'UI della mappa è necessario farlo nel main thread
                            requireActivity().runOnUiThread(() -> mapView.invalidate());

                            /* Calcolo media tra due punti */
                            double latInizio, lonInizio, latFine, lonFine;
                            latInizio = newWaypoints.get(0).getLatitude();
                            lonInizio = newWaypoints.get(0).getLongitude();
                            latFine   = newWaypoints.get(newWaypoints.size()-1).getLatitude();
                            lonFine   = newWaypoints.get(newWaypoints.size()-1).getLongitude();

                            GeoPoint puntoMedio = new GeoPoint((latInizio + latFine) / 2, (lonInizio + lonFine) / 2);
                            BoundingBox boundingBox = BoundingBox.fromGeoPointsSafe(newWaypoints);

                            //viene messo come centro della mappa il punto medio tra i marker
                            requireActivity().runOnUiThread(() ->
                            {
                                mapController.setCenter(puntoMedio);
                                zoomToBounds(boundingBox);

                            });
                        }).start();
                    }
                    waypoints = newWaypoints;
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

        Slider difficolta = requireView().findViewById(R.id.slider_difficoltà);
        difficolta.addOnChangeListener((slider, value, fromUser) ->
        {
            switch ((int) value)
            {
                case 1:
                    slider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.facile)));
                    slider.setHaloTintList(ColorStateList.valueOf(getResources().getColor(R.color.facile)));
                    break;
                case 2:
                    slider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.dilettante)));
                    slider.setHaloTintList(ColorStateList.valueOf(getResources().getColor(R.color.dilettante)));
                    break;
                case 3:
                    slider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.intermedio)));
                    slider.setHaloTintList(ColorStateList.valueOf(getResources().getColor(R.color.intermedio)));
                    break;
                case 4:
                    slider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.difficile)));
                    slider.setHaloTintList(ColorStateList.valueOf(getResources().getColor(R.color.difficile)));
                    break;
                case 5:
                    slider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.esperto)));
                    slider.setHaloTintList(ColorStateList.valueOf(getResources().getColor(R.color.esperto)));
                    break;
            }
        });

        mapView.setOnTouchListener((v, event) -> true);




        backToTabActivity.setOnClickListener(view12 -> controllerItinerario.goBack());

        edtNome.setFocusable(false);
        edtNome.setOnClickListener(view13 -> initEditText(edtNome));
        edtDurata.setFocusable(false);
        edtDurata.setOnClickListener(view14 -> initEditText(edtDurata));
        edtDescrizione.setFocusable(false);
        edtDescrizione.setOnClickListener(view15 -> initEditText(edtDescrizione));
        controllerItinerario.updateScrollViewImage(mRecyclerView);

        addImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                controllerItinerario.uploadFile();
            }
        });

        buttonNascosto.setOnClickListener(view16 -> controllerItinerario.gotoPercorsoFragment(mapView));


        selectDisabili.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            stateDisabili = isChecked;
        }
        });

        confermaInserimentoItinerario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!edtNome.getText().toString().isEmpty() && !edtDurata.getText().toString().isEmpty() && !waypoints.isEmpty())
                    controllerItinerario.inserisciItinerario(difficolta.getValue(),edtNome.getText().toString(), edtDurata.getText().toString(), stateDisabili, edtDescrizione.getText().toString(), waypoints, imgList, getContext());
                else
                    new ErrorDialog("Itinerario non inserito correttamente").show(getParentFragmentManager(),null);
            }
        });


    }

    public void zoomToBounds(final BoundingBox box) {
        if (mapView.getHeight() > 0) {
            mapView.zoomToBoundingBox(box, true);

        } else {
            ViewTreeObserver vto = mapView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    mapView.zoomToBoundingBox(box, true);
                    ViewTreeObserver vto2 = mapView.getViewTreeObserver();
                    vto2.removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    public void addPhotoMarker(Immagine uriImage, float[] latLong)
    {
        Marker photoMarker = new Marker(mapView);
        photoMarker.setPosition(new GeoPoint(latLong[0], latLong[1]));
        uriImage.setMarker(photoMarker);
        imgList.add(uriImage);
        model.setImgList(imgList);

    }
    public void removeImage(Immagine img)
    {
        mapView.getOverlays().remove(img.getMarker());
        imgList.remove(img);
        model.setImgList(imgList);
        mapView.invalidate();
    }
    public void initEditText(EditText editText)
    {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
        editText.requestFocus();
        ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edtNome, 0);
    }

    public void startMapLoading()
    {
        buttonNascosto.setClickable(false);
        buttonNascosto.setAlpha(0.25f);
        progMapLoading.setVisibility(View.VISIBLE);
    }

    public void stopMapLoading()
    {
        buttonNascosto.setClickable(true);
        buttonNascosto.setAlpha(0f);
        progMapLoading.setVisibility(View.GONE);
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

    public void resetListImage()
    {
        imgList.clear();
        model.setImgList(imgList);
    }
}