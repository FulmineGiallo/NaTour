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
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerario;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class HomePageFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private ExtendedFloatingActionButton fab;
    private Intent intentInserimentoItinerario;
    private String token;
    private ControllerHomePage controllerHomePage;
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

        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getUtente().observe(getViewLifecycleOwner(),
                utente ->
                {
                    token = utente.getToken();
                }
        );
        fab = requireView().findViewById(R.id.btn_inserimentoItinerario);
        intentInserimentoItinerario = new Intent(HomePageFragment.this.getActivity(), InserimentoItinerario.class);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rec_view);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        controllerHomePage.setAdapter(mRecyclerView);


        fab.setOnClickListener(view1 ->
        {
            intentInserimentoItinerario.putExtra("token", token);
            startActivity(intentInserimentoItinerario);

        });
    }



}