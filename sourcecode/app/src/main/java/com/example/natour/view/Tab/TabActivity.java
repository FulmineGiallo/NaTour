package com.example.natour.view.Tab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Explode;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.natour.R;
import com.example.natour.model.Itinerario;
import com.example.natour.model.Utente;
import com.example.natour.util.AnalyticsUseCase;
import com.example.natour.view.MessaggioActivity.MessaggioFragment;
import com.example.natour.view.Profile.ProfileFragment;
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

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
            Drawable unwrappedDrawable = bottomNavigationView.getBackground();
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable).mutate();
            switch (item.getItemId())
            {
                case R.id.home:
                    AnalyticsUseCase.event("sezione_homepage", "click", "homepage", getApplicationContext());
                    homepage.itinerarioAdded((Itinerario)getIntent().getSerializableExtra("itinerario"));
                    DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.colore_barrahomepage));
                    getSupportFragmentManager().beginTransaction().addToBackStack(homepage.toString()).replace(R.id.container, homepage).commit();
                    break;
                case R.id.profile:
                    AnalyticsUseCase.event("sezione_profile", "click", "profilo", getApplicationContext());

                    DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.colore_barraprofilo));
                    getSupportFragmentManager().beginTransaction().addToBackStack(profile.toString()).replace(R.id.container, profile).commit();
                    break;
                case R.id.cerca:
                    AnalyticsUseCase.event("sezione_cerca", "click", "cerca", getApplicationContext());
                    DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.colore_barraricerca));
                    getSupportFragmentManager().beginTransaction().addToBackStack(cerca.toString()).replace(R.id.container, cerca).commit();
                    break;
                case R.id.messaggio:
                    AnalyticsUseCase.event("sezione_messaggio", "click", "messaggio", getApplicationContext());

                    DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.colore_barrachat));
                    getSupportFragmentManager().beginTransaction().addToBackStack(messaggio.toString()).replace(R.id.container, messaggio).commit();
                    break;
                case R.id.notifica:
                    AnalyticsUseCase.event("notifica", "click", "notifica", getApplicationContext());

                    DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.colore_barranotifiche));
                    getSupportFragmentManager().beginTransaction().addToBackStack(notifica.toString()).replace(R.id.container, notifica).commit();
                    break;

            }



            return true;
        }
    };
}