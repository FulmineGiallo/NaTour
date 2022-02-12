package com.example.natour.view.InserimentoItinerarioActivity;

import android.content.Context;
import android.os.Bundle;
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


public class InserimentoPercorsoFragment extends Fragment
{
    private EditText inizioPercorso;
    private EditText finePercorso;
    private ImageButton indietro;


    private FrameLayout backContainer;
    private InserimentoItinerarioFragment inserimentoItinerarioFragment;
    private MappaFragment mappaFragment;


    public InserimentoPercorsoFragment()
    {
        // Required empty public constructor
    }
    public static InserimentoPercorsoFragment newInstance(String param1, String param2)
    {
        InserimentoPercorsoFragment fragment = new InserimentoPercorsoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public InserimentoPercorsoFragment(MappaFragment mappa, InserimentoItinerarioFragment inserimentoItinerarioFragment)
    {
        this.mappaFragment = mappa;
        this.inserimentoItinerarioFragment = inserimentoItinerarioFragment;
    }

    public Fragment recreateFragment(Fragment fragment)
    {
        Fragment newIstance = null;
        try
        {
            Fragment.SavedState savedState = getParentFragmentManager().saveFragmentInstanceState(fragment);
            newIstance = fragment.getClass().newInstance();
            newIstance.setInitialSavedState(savedState);

        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (java.lang.InstantiationException e)
        {
            e.printStackTrace();
        }

        return newIstance;
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
        backContainer = getView().findViewById(R.id.frameIndietro);
        inizioPercorso = getView().findViewById(R.id.edt_inizioPercorso);
        finePercorso = getView().findViewById(R.id.edt_finePercorso);
        indietro = getView().findViewById(R.id.btn_indietro);
        backContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
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

                //fade out del tasto back

                Animation fadeOut = new AlphaAnimation(indietro.getAlpha(), 0f);
                fadeOut.setFillAfter(true);
                fadeOut.setDuration(500);

                backContainer.startAnimation(animationSet);

                /* Torna indietro al*/
                getActivity().onBackPressed();
                /*getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentMappa, inserimentoItinerarioFragment)
                        .commit();*/


            }
        });

        inizioPercorso.setFocusable(false);
        inizioPercorso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                inizioPercorso.setFocusable(true);
                inizioPercorso.setFocusableInTouchMode(true);
                inizioPercorso.setEnabled(true);
                inizioPercorso.requestFocus();
                ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(inizioPercorso, 0);
            }
        });
        finePercorso.setFocusable(false);
        finePercorso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                finePercorso.setFocusable(true);
                finePercorso.setFocusableInTouchMode(true);
                finePercorso.setEnabled(true);
                finePercorso.requestFocus();
                ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(finePercorso, 0);
            }
        });



        if(getView().findViewById(R.id.map) != null)
        {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(mappaFragment);

            Fragment newIstance = recreateFragment(mappaFragment);

            ft.add(R.id.map, newIstance);
            ft.commit();
        }
    }

}