package com.example.natour.view.Tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.natour.R;
import com.example.natour.controller.ControllerProfile;
import com.example.natour.model.Utente;


public class ProfileFragment extends Fragment
{
    private ControllerProfile controllerProfile;
    private ImageButton options;
    private TextView txtUtente;

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        controllerProfile = new ControllerProfile(getParentFragmentManager(), getActivity());
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        txtUtente = getView().findViewById(R.id.utente);


        model.getUtente().observe(getViewLifecycleOwner(),
                utente ->
                {
                    txtUtente.setText(utente.getNome() +" "+ utente.getCognome());

                }
        );

        options = getView().findViewById(R.id.btn_optionsProfile);
        options.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                controllerProfile.signOut();
            }
        });

    }
}