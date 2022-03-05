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

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {

            switch (item.getItemId())
            {
                case R.id.home:
                    if(homepage == null)
                        homepage = new HomePageFragment();
                    homepage.itinerarioAdded((Itinerario)getIntent().getSerializableExtra("itinerario"));
                    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(TabActivity.this, R.color.colore_barrahomepage));
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homepage).commit();
                    break;
                case R.id.profile:
                    if(profile == null)
                        profile = new ProfileFragment();
                    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(TabActivity.this, R.color.colore_barraprofilo));
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, profile).commit();
                    break;
                case R.id.cerca:
                    if(cerca == null)
                        cerca = new CercaFragment();
                    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(TabActivity.this, R.color.colore_barraricerca));
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, cerca).commit();
                    break;
                case R.id.messaggio:
                    if(messaggio == null)
                        messaggio = new MessaggioFragment();
                    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(TabActivity.this, R.color.colore_barrachat));
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, messaggio).commit();
                    break;
                case R.id.notifica:
                    if(notifica == null)
                        notifica = new NotificaFragment();
                    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(TabActivity.this, R.color.colore_barranotifiche));
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, notifica).commit();
                    break;

            }



            return true;
        }
    };
}