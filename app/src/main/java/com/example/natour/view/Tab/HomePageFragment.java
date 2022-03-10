package com.example.natour.view.Tab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.natour.R;
import com.example.natour.controller.ControllerHomePage;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerario;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private ExtendedFloatingActionButton fab;
    private Intent intentInserimentoItinerario;
    private String token;
    private ControllerHomePage controllerHomePage;
    private SharedViewModel model;
    public HomePageFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        if(controllerHomePage == null)
            controllerHomePage = new ControllerHomePage(getParentFragmentManager(), this);

        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getUtente().observe(getViewLifecycleOwner(),
                utente ->
                {
                    token = utente.getToken();
                    controllerHomePage.setToken(token);
                }
        );

        fab = requireView().findViewById(R.id.btn_inserimentoItinerario);
        intentInserimentoItinerario = new Intent(HomePageFragment.this.getActivity(), InserimentoItinerario.class);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rec_view);
        controllerHomePage.setAdapter(mRecyclerView);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);




        fab.setOnClickListener(view1 ->
        {
            intentInserimentoItinerario.putExtra("token", token);
            startActivity(intentInserimentoItinerario);

        });
    }


    public void itinerarioAdded(Itinerario itinerario)
    {
        //controllerHomePage.addItinerario(itinerario);
    }

    public void setModelItinerari(ArrayList<Itinerario> itinerari)
    {
        model.setItinerari(itinerari);
    }

    public void setModelImmagini(List<Immagine> immagineList)
    {
        model.setImmagini(immagineList);
    }
}