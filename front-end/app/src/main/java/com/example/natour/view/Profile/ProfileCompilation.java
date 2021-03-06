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
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.natour.R;
import com.example.natour.controller.ControllerProfile;
import com.example.natour.view.PannelloAdmin.AdminPanel;
import com.example.natour.view.Tab.SharedViewModel;
import com.example.natour.view.dialog.ConfermaDialog;
import com.example.natour.view.dialog.ConfermaDialogInterfaccia;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

public class ProfileCompilation extends Fragment
{
    private ControllerProfile controllerProfile;
    private TextView txtUtente;
    private ImageButton options;
    private NavigationView menuView;
    private MaterialCardView cardMenu;
    private RecyclerView recyclerView;
    private LottieAnimationView animation;
    private TextView compilationVuote;

    public ProfileCompilation()
    {
        // Required empty public constructor
    }

    public  ProfileCompilation(ControllerProfile controllerProfile){
        this.controllerProfile = controllerProfile;
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
        return inflater.inflate(R.layout.fragment_profile_compilation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        txtUtente = requireView().findViewById(R.id.utente);
        menuView = requireView().findViewById(R.id.menuview);
        cardMenu = requireView().findViewById(R.id.cardMenu);
        recyclerView = requireView().findViewById(R.id.rec_item_compilation);
        animation = requireActivity().findViewById(R.id.animationViewCompilation);
        compilationVuote = requireActivity().findViewById(R.id.txt_noCompilation);
        controllerProfile.setCompilationAdapter(recyclerView);
        controllerProfile.setFragmentController(this);
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getUtente().observe(getViewLifecycleOwner(),
                utente -> {
                        txtUtente.setText(utente.getNome() + " " + utente.getCognome());
                        controllerProfile.setCompilations(utente);
                        controllerProfile.isAdminCompilation(utente.getToken(), utente);
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
                                Toast.makeText(getContext(),"I miei itinerari", Toast.LENGTH_SHORT).show();
                                ProfileFragment profile = new ProfileFragment();
                                getParentFragmentManager().beginTransaction().addToBackStack(profile.toString()).replace(R.id.container, profile).commit();
                                return true;
                            case R.id.option_3:
                                Toast.makeText(getContext(),"Logout", Toast.LENGTH_SHORT).show();
                                new ConfermaDialog("Sei sicuro di voler uscire?", new ConfermaDialogInterfaccia()
                                {
                                    @Override
                                    public void actionConferma()
                                    {
                                        //controllerProfile.signOut();
                                    }

                                    @Override
                                    public void actionAnnulla()
                                    {

                                    }
                                }).show(getParentFragmentManager(), null);


                                return true;
                            case R.id.option_admin_compilation:
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
    public void showAdminOption()
    {
        Menu menu = menuView.getMenu();
        MenuItem item = menu.getItem(3);
        SpannableString s = new SpannableString(getResources().getString(R.string.pannello_admin));
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        item.setTitle(s);
        item.setVisible(true);
    }

    public void showItemsCompilation()
    {
        animation.setVisibility(View.VISIBLE);
        compilationVuote.setVisibility(View.VISIBLE);
    }
}