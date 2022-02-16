package com.example.natour.view.InserimentoItinerarioActivity;

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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.natour.R;
import com.example.natour.view.Dialog.ErrorDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.slider.Slider;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Route;
import io.ticofab.androidgpxparser.parser.domain.WayPoint;

import static android.app.Activity.RESULT_OK;


public class InserimentoItinerarioFragment extends Fragment
{
    private Slider difficolta;

    private FrameLayout containerMappa;
    private MaterialCardView boxMappa;
    private ImageView buttonNascosto;
    private InserimentoPercorsoFragment percorsoFragment;
    private MappaFragment mappaFragment;
    private ImageButton backToTabActivity;
    private EditText edtNome;
    private EditText edtDurata;
    private EditText edtDescrizione;
    private MaterialButton btn_gpx;
    private static final int PICKFILE_REQUEST_CODE = 100;
    private Intent intent;


    public InserimentoItinerarioFragment()
    {

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
        difficolta = getView().findViewById(R.id.slider_difficoltà);
        difficolta.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

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
            }
        });


        boxMappa = getView().findViewById(R.id.cardMap);
        containerMappa = getView().findViewById(R.id.containerMappa);
        buttonNascosto = getView().findViewById(R.id.btn_nascoto);

        backToTabActivity = getView().findViewById(R.id.btn_indietroInsItineraio);
        edtNome = getView().findViewById(R.id.nomeItinerario);
        edtDurata = getView().findViewById(R.id.edt_durata);
        edtDescrizione = getView().findViewById(R.id.descrizioneEditText);
        btn_gpx = getView().findViewById(R.id.btn_gpx);

        btn_gpx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        backToTabActivity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().onBackPressed();
            }
        });

        edtNome.setFocusable(false);
        edtNome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initEditText(edtNome);
            }
        });
        edtDurata.setFocusable(false);
        edtDurata.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initEditText(edtDurata);
            }
        });
        edtDescrizione.setFocusable(false);
        edtDescrizione.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initEditText(edtDescrizione);
            }
        });


        if(getView().findViewById(R.id.containerMappa) != null)
        {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mappaFragment = new MappaFragment();

            ft.add(R.id.containerMappa, mappaFragment);
            ft.commit();
        }


        buttonNascosto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(percorsoFragment == null)
                    percorsoFragment = new InserimentoPercorsoFragment(mappaFragment);

                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentMappa, percorsoFragment)
                        .addToBackStack(InserimentoItinerarioFragment.class.getSimpleName())
                        .commit();

            }
        });
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
        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edtNome, 0);
    }

}