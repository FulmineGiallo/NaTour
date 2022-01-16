package com.example.natour.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.natour.R;
import com.example.natour.controller.ControllerLogin;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.tasks.Task;

import java.sql.Connection;

public class Login extends AppCompatActivity
{


    Intent intentRegister;
    Button btn_register;
    Button btn_login;
    ControllerLogin controllerLogin;
    private String email;
    private String password;
    EditText edtEmail;
    EditText edtPassword;
    boolean login = false;
    FragmentManager fm = getSupportFragmentManager();
    ImageButton googleLogin;
    private static final int RC_SIGN_IN = 007;
    Intent intentLoginHappened;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        controllerLogin = new ControllerLogin(fm, this);
        btn_login = findViewById(R.id.btn_login);
        googleLogin = findViewById(R.id.image_google);

        intentRegister = new Intent(this, Register.class);

        btn_register = findViewById(R.id.btn_signin);
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(intentRegister);
            }
        });

        /* ---------------------- ACCESSO UTENTE --------------------------------------------- */
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);


        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                login = controllerLogin.checkLogin(email, password);
            }
        });


        /* ----------------------------------------------------------------*/

        /* ------------------ ACCESSO GOOGLE ------------------------------*/
        intentLoginHappened = new Intent(this, TabActivity.class);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        googleLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        /* ----------------------------------------------------------------*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

   private void handleSignInResult(GoogleSignInResult result)
   {
       String TAG = "GOOGLE";
       Log.d("GOOGLE", "handleSignInResult:" + result.isSuccess());
       if (result.isSuccess())
       {
           // Signed in successfully, show authenticated UI.
           GoogleSignInAccount acct = result.getSignInAccount();

           Log.e(TAG, "display name: " + acct.getDisplayName());

           String personName = acct.getDisplayName();
           String personPhotoUrl = acct.getPhotoUrl().toString();
           String email = acct.getEmail();

           Log.e(TAG, "Name: " + personName + ", email: " + email
                   + ", Image: " + personPhotoUrl);


           /*txtName.setText(personName);
           txtEmail.setText(email);
           Glide.with(getApplicationContext()).load(personPhotoUrl)
                   .thumbnail(0.5f)
                   .into(imgProfilePic);
                   TODO: Implmentare logica per recuperare l'utente su cognito

                   */

           updateUI(true);
       }
       else
           {
           // Signed out, show unauthenticated UI.
            updateUI(false);
           }
   }


    private void updateUI(boolean isSignedIn)
    {
        if(isSignedIn)
        {
            startActivity(intentLoginHappened);
        }
        else
        {
            //TODO: Errore login google, fare schermata;
        }

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        /*OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("GOOGLE CACHED", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }
}