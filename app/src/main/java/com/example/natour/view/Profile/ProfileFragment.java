package com.example.natour.view.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.natour.R;
import com.example.natour.controller.ControllerProfile;
import com.example.natour.model.Itinerario;
import com.example.natour.view.PannelloAdmin.AdminPanel;
import com.example.natour.view.Tab.SharedViewModel;
import com.example.natour.view.dialog.ConfermaDialog;
import com.example.natour.view.dialog.ConfermaDialogInterfaccia;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment
{
    private ControllerProfile controllerProfile;
    private ImageButton options;
    private TextView txtUtente;
    private NavigationView menuView;
    private MaterialCardView cardMenu;
    private LottieAnimationView animationView;
    private TextView noItinerario;
    private List<Itinerario> itinerari = new ArrayList<>();
    private String tokenUtente;
    private RecyclerView recView;

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
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        txtUtente = requireView().findViewById(R.id.utente);
        menuView = requireView().findViewById(R.id.menuview);
        cardMenu = requireView().findViewById(R.id.cardMenu);
        animationView = view.findViewById(R.id.animationViewProfile);
        noItinerario = view.findViewById(R.id.txt_noItinerario);

        controllerProfile = new ControllerProfile(this, getParentFragmentManager(), getActivity(), tokenUtente, itinerari);
        setInterface();

        model.getUtente().observe(getViewLifecycleOwner(),
                utente ->
                {
                    txtUtente.setText(utente.getNome() +" "+ utente.getCognome());
                    tokenUtente = utente.getToken();
                    controllerProfile.setToken(tokenUtente);
                    controllerProfile.getItinerariProfile();
                    controllerProfile.isAdmin(tokenUtente, utente);
                }
        );
        options = requireView().findViewById(R.id.btn_optionsProfile);
        options.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                menuView.setVisibility(View.VISIBLE);
                cardMenu.setVisibility(View.VISIBLE);
                menuView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.option_1:
                                Toast.makeText(getContext(),"Modifica Profilo", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.option_2:
                                Toast.makeText(getContext(),"Le mie compilation", Toast.LENGTH_SHORT).show();
                                ProfileCompilation profileCompilation = new ProfileCompilation(controllerProfile);
                                getParentFragmentManager()
                                        .beginTransaction()
                                        .addToBackStack(profileCompilation.toString())
                                        .replace(R.id.container, profileCompilation)
                                        .commit();
                                return true;
                            case R.id.option_3:
                                Toast.makeText(getContext(),"Logout", Toast.LENGTH_SHORT).show();
                                new ConfermaDialog("Sei sicuro di voler uscire?", new ConfermaDialogInterfaccia()
                                {
                                    @Override
                                    public void actionConferma()
                                    {
                                        controllerProfile.signOut();
                                    }

                                    @Override
                                    public void actionAnnulla()
                                    {

                                    }
                                }).show(getParentFragmentManager(), null);
                                return true;
                            case R.id.option_admin:
                                Toast.makeText(getContext(),"Pannello admin", Toast.LENGTH_SHORT).show();
                                requireActivity().startActivity(new Intent(requireActivity(), AdminPanel.class));
                                return true;
                            case R.id.option_4:
                                Toast.makeText(getContext(),"Chiudo MENU", Toast.LENGTH_SHORT).show();
                                menuView.setVisibility(View.INVISIBLE);
                                cardMenu.setVisibility(View.INVISIBLE);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
    }

    public void setInterface(){
        GridLayoutManager GridLayoutManager = new GridLayoutManager(getContext(), 3);
        recView = (RecyclerView) getView().findViewById(R.id.rec_view);
        recView.setLayoutManager(GridLayoutManager);
        controllerProfile.setAdapter(recView);
    }


    public void showItems() {
        noItinerario.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.VISIBLE);
    }

    public void showAdminOption()
    {
        Menu menu = menuView.getMenu();
        MenuItem item = menu.getItem(3);
        SpannableString s = new SpannableString(getResources().getString(R.string.pannello_admin));
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        item.setTitle(s);
        item.setVisible(true);
    }
}