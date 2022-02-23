package com.example.natour.view.InserimentoItinerarioActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.R;
import com.example.natour.controller.ControllerItinerario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;


public class InserimentoItinerarioFragment extends Fragment
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
        FrameLayout mapContainer = view.findViewById(R.id.containerMappa);
        ViewGroupCompat.setTransitionGroup(mapContainer,true);
        ViewCompat.setTransitionName(mapContainer, "tiny_map");

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
        confermaInserimentoItinerario = requireView().findViewById(R.id.btn_confermaItinerario);
        selectDisabili = requireView().findViewById(R.id.switch_disabili);
        addImage = requireView().findViewById(R.id.btn_addimg);
        ImageButton backToTabActivity = requireView().findViewById(R.id.btn_indietroInsItineraio);
        edtNome = requireView().findViewById(R.id.nomeItinerario);
        edtDurata = requireView().findViewById(R.id.edt_durata);
        edtDescrizione = requireView().findViewById(R.id.descrizioneEditText);
        MaterialButton btn_gpx = requireView().findViewById(R.id.btn_gpx);
        mRecyclerView = requireView().findViewById(R.id.rec_view_images);

        btn_gpx.setOnClickListener(view1 ->
        {
            controllerItinerario.getGPXFromDevice();
        });

        backToTabActivity.setOnClickListener(view12 -> controllerItinerario.goBack());

        edtNome.setFocusable(false);
        edtNome.setOnClickListener(view13 -> initEditText(edtNome));
        edtDurata.setFocusable(false);
        edtDurata.setOnClickListener(view14 -> initEditText(edtDurata));
        edtDescrizione.setFocusable(false);
        edtDescrizione.setOnClickListener(view15 -> initEditText(edtDescrizione));
        controllerItinerario.setMapView(this, R.id.containerMappa);
        controllerItinerario.updateScrollViewImage(mRecyclerView);

        addImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                controllerItinerario.uploadFile();

            }
        });

        buttonNascosto.setOnClickListener(view16 -> controllerItinerario.gotoPercorsoFragment(mapContainer));


        selectDisabili.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (isChecked)
                stateDisabili = true;
        }
        });

        confermaInserimentoItinerario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!edtNome.getText().toString().isEmpty() && !edtDurata.getText().toString().isEmpty() && !edtDescrizione.getText().toString().isEmpty())
                    controllerItinerario.inserisciItinerario(edtNome.getTransitionName(), edtDurata.getText().toString(), stateDisabili, edtDescrizione.getText().toString());
            }
        });


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