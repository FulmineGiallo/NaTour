package com.example.natour.view.InserimentoItinerarioActivity;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.natour.R;
import com.example.natour.controller.ControllerItinerario;
import com.example.natour.view.ErrorDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Route;
import io.ticofab.androidgpxparser.parser.domain.WayPoint;


public class InserimentoItinerarioFragment extends Fragment
{
    private ControllerItinerario controllerItinerario;

    private EditText edtNome;
    private EditText edtDurata;
    private EditText edtDescrizione;
    private static final int PICKFILE_REQUEST_CODE = 100;


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


        ImageView buttonNascosto = requireView().findViewById(R.id.btn_nascoto);

        ImageButton backToTabActivity = requireView().findViewById(R.id.btn_indietroInsItineraio);
        edtNome = requireView().findViewById(R.id.nomeItinerario);
        edtDurata = requireView().findViewById(R.id.edt_durata);
        edtDescrizione = requireView().findViewById(R.id.descrizioneEditText);
        MaterialButton btn_gpx = requireView().findViewById(R.id.btn_gpx);

        btn_gpx.setOnClickListener(view1 ->
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivityForResult(intent, PICKFILE_REQUEST_CODE); //TODO: c'era un warning per startActivityForResult deprecato
        });

        backToTabActivity.setOnClickListener(view12 -> controllerItinerario.goBack());

        edtNome.setFocusable(false);
        edtNome.setOnClickListener(view13 -> initEditText(edtNome));
        edtDurata.setFocusable(false);
        edtDurata.setOnClickListener(view14 -> initEditText(edtDurata));
        edtDescrizione.setFocusable(false);
        edtDescrizione.setOnClickListener(view15 -> initEditText(edtDescrizione));


        controllerItinerario.setMapView(this, R.id.containerMappa);


        buttonNascosto.setOnClickListener(view16 -> controllerItinerario.gotoPercorsoFragment());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
    if(resultCode==RESULT_OK) {
        String FilePath = intent.getData().getPath();
        String FileName = intent.getData().getLastPathSegment();
        int lastPos = FilePath.length() - FileName.length();
        String Folder = FilePath.substring(0, lastPos);

        //String textFile = "Full Path: \n" + FilePath + "\n";
        //String textFolder = "Folder: \n" + Folder + "\n";
        String textFileName = "File Name: \n" + FileName + "\n";
        Log.i("bla",textFileName);
        if(textFileName.contains(".gpx")){
            GPXParser parser = new GPXParser();
            Gpx parsedGpx = null;

            try {
                InputStream in = requireContext().getAssets().open(textFileName);
                parsedGpx = parser.parse(in);
            } catch (IOException | XmlPullParserException e) {
                new ErrorDialog("Errore nel caricamento file .gpx").show(getParentFragmentManager(),null);
                e.printStackTrace();
            }
            if (parsedGpx == null) {
                new ErrorDialog("Errore nel caricamento file .gpx").show(getParentFragmentManager(),null);
            }
            else
                {
                // Gestire gpx
                List<WayPoint> waypoints = parsedGpx.getWayPoints();
                List<Route> routes = parsedGpx.getRoutes();
            }
        }
        else
            {
            new ErrorDialog("Il file inserito non è di tipo .gpx").show(getParentFragmentManager(),null);
        }
    }
}

    public void initEditText(EditText editText)
    {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
        editText.requestFocus();
        ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edtNome, 0);
    }

}