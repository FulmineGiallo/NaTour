package com.example.natour.view.InserimentoItinerarioActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.natour.R;
import com.google.android.material.textfield.TextInputLayout;


public class InserimentoPercorsoFragment extends Fragment
{
    private EditText inizioPercorso;
    private EditText finePercorso;
    private ImageButton indietro;


    private FrameLayout backContainer;
    private MappaFragment mappaFragment;


    public InserimentoPercorsoFragment()
    {
        // Required empty public constructor
    }

    //passiamo il fragment della mappa e quello precedente per matenere la persistenza dei dati
    public InserimentoPercorsoFragment(MappaFragment mappa)
    {
        this.mappaFragment = mappa;
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

        return inflater.inflate(R.layout.fragment_inserimento_percorso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        backContainer = requireView().findViewById(R.id.frameIndietro);
        inizioPercorso = requireView().findViewById(R.id.edt_inizioPercorso);
        finePercorso = requireView().findViewById(R.id.edt_finePercorso);
        indietro = requireView().findViewById(R.id.btn_indietro);
        ImageButton deleteMarkerInizio = requireView().findViewById(R.id.btn_deletemarkerInizio);
        ImageButton deleteMarkerFine = requireView().findViewById(R.id.btn_deletemarkerFine);

        //Inizialmente i bottoni per aggiungere marker dal testo devono essere nascosti
        TextInputLayout til_inizio = (TextInputLayout) inizioPercorso.getParent().getParent();
        til_inizio.setEndIconVisible(false);
        TextInputLayout til_fine = (TextInputLayout) finePercorso.getParent().getParent();
        til_fine.setEndIconVisible(false);

        //questo listener contiene l'azione e l'animazione per tornare indietro
        backContainer.setOnClickListener(view1 ->
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

            /* permette all'animazione di compiersi prima di tornare all'attività precedente*/
            new Handler().postDelayed(() -> requireActivity().onBackPressed(),700);
        });

        /*codice necessario per impedire che sia attivato il focus per il campo di testo
         * al lancio del fragment ma al click diventa di nuovo focusabile
         * */
        inizioPercorso.setFocusable(false);
        inizioPercorso.setOnClickListener(view12 ->
        {

            inizioPercorso.setFocusable(true);
            inizioPercorso.setFocusableInTouchMode(true);
            inizioPercorso.setEnabled(true);
            inizioPercorso.requestFocus();
            ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(inizioPercorso, 0);
        });

        /*codice necessario per impedire che sia attivato il focus per il campo di testo
         * al lancio del fragment ma al click diventa di nuovo focusabile
         * */
        finePercorso.setFocusable(false);
        finePercorso.setOnClickListener(view13 ->
        {

            finePercorso.setFocusable(true);
            finePercorso.setFocusableInTouchMode(true);
            finePercorso.setEnabled(true);
            finePercorso.requestFocus();
            ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(finePercorso, 0);
        });


        /*
        * Qui viene cambiata l'istanza di MappaFragment
        * */
        if(requireView().findViewById(R.id.map) != null)
        {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(mappaFragment);

            MappaFragment newIstance = new MappaFragment();

            newIstance.setEditTextMappa(inizioPercorso, finePercorso, deleteMarkerInizio, deleteMarkerFine);
            ft.add(R.id.map, newIstance);
            ft.commit();
        }
    }

}