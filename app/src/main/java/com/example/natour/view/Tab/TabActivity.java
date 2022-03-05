package com.example.natour.view.Tab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowAnimationFrameStats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.natour.R;
import com.example.natour.model.Itinerario;
import com.example.natour.model.Utente;
import com.example.natour.view.MessaggioActivity.MessaggioFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TabActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Utente utente;
    private String TAG = "tab activity";
    private HomePageFragment homepage = null;
    private ProfileFragment profile = null;
    private CercaFragment cerca = null;
    private MessaggioFragment messaggio = null;
    private NotificaFragment notifica = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().setEnterTransition(new Explode());
        super.onCreate(savedInstanceState);
        SharedViewModel model = new ViewModelProvider(this).get(SharedViewModel.class);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        model.setUtente((Utente) getIntent().getSerializableExtra("utente"));
        //TODO: eliminare la classe singleton e ogni suo utilizzo
        setContentView(R.layout.activity_tab);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomePageFragment()).commit();
        homepage = new HomePageFragment();
        profile = new ProfileFragment();
        cerca = new CercaFragment();
        messaggio = new MessaggioFragment();
        notifica = new NotificaFragment();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {

            switch (item.getItemId())
            {
                case R.id.home:
                    homepage.itinerarioAdded((Itinerario)getIntent().getSerializableExtra("itinerario"));
                    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(TabActivity.this, R.color.colore_barrahomepage));
                    getSupportFragmentManager().beginTransaction().addToBackStack(homepage.toString()).replace(R.id.container, homepage).commit();
                    break;
                case R.id.profile:
                    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(TabActivity.this, R.color.colore_barraprofilo));
                    getSupportFragmentManager().beginTransaction().addToBackStack(profile.toString()).replace(R.id.container, profile).commit();
                    break;
                case R.id.cerca:
                    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(TabActivity.this, R.color.colore_barraricerca));
                    getSupportFragmentManager().beginTransaction().addToBackStack(cerca.toString()).replace(R.id.container, cerca).commit();
                    break;
                case R.id.messaggio:
                    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(TabActivity.this, R.color.colore_barrachat));
                    getSupportFragmentManager().beginTransaction().addToBackStack(messaggio.toString()).replace(R.id.container, messaggio).commit();
                    break;
                case R.id.notifica:
                    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(TabActivity.this, R.color.colore_barranotifiche));
                    getSupportFragmentManager().beginTransaction().addToBackStack(notifica.toString()).replace(R.id.container, notifica).commit();
                    break;

            }



            return true;
        }
    };
}