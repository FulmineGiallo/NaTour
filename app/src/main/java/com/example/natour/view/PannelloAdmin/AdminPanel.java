package com.example.natour.view.PannelloAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.natour.R;
import com.example.natour.view.admin.VisualizzaStatistiche;
import com.example.natour.controller.ControllerProfile;
import com.example.natour.view.Profile.ProfileCompilation;
import com.example.natour.view.Signout;
import com.example.natour.view.Tab.HomePageFragment;
import com.example.natour.view.Tab.TabActivity;
import com.example.natour.view.dialog.ConfermaDialog;
import com.example.natour.view.dialog.ConfermaDialogInterfaccia;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

public class AdminPanel extends AppCompatActivity {

    private NavigationView menuView;
    private MaterialCardView cardMenu;
    private CardView cardMenuPicture;
    private ImageButton options;
    private FrameLayout fragment;
    private ImageButton buttonRefresh;
    private TextView textStatistiche;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);


        menuView = findViewById(R.id.menuviewAdmin);
        cardMenu = findViewById(R.id.cardMenuAdmin);
        options = findViewById(R.id.btn_optionsAdmin);
        cardMenuPicture = findViewById(R.id.cardView3);
        textStatistiche = findViewById(R.id.txt_statistiche);
        fragment = findViewById(R.id.fragment_statistiche);
        buttonRefresh = findViewById(R.id.btn_refresh_admin);
        options.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                menuView.setVisibility(View.VISIBLE);
                cardMenu.setVisibility(View.VISIBLE);
                cardMenuPicture.setVisibility(View.INVISIBLE);
                menuView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_1:
                                menuView.setVisibility(View.INVISIBLE);
                                cardMenu.setVisibility(View.INVISIBLE);
                                cardMenuPicture.setVisibility(View.VISIBLE);
                                break;
                            case R.id.option_2:
                                textStatistiche.setVisibility(View.VISIBLE);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_statistiche, new VisualizzaStatistiche()).commit();
                                buttonRefresh.setVisibility(View.VISIBLE);
                                //TODO: CALL API STATISTICHE
                                break;
                            default:
                                return false;
                        }
                        return false;
                    }
                });
            }
        });
    }

    public void refresh(View view) {
        //TODO: CALL API STATISTICHE
    }

    public void backToHomepage(View view) {
        Toast.makeText(this,"Homepage", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TabActivity.class));
    }

    public void logOutAdmin(View view) {
        Toast.makeText(this,"Logout", Toast.LENGTH_SHORT).show();
        new ConfermaDialog("Sei sicuro di voler uscire?", new ConfermaDialogInterfaccia()
        {
            @Override
            public void actionConferma()
            {
                startActivity(new Intent(AdminPanel.this, Signout.class));
            }

            @Override
            public void actionAnnulla()
            {

            }
        }).show(getSupportFragmentManager(), null);
    }
}