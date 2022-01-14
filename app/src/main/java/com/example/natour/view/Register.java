package com.example.natour.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.natour.R;
import com.example.natour.controller.ControllerRegister;
import com.example.natour.databinding.ActivityRegisterBinding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class Register extends AppCompatActivity implements ConfermaRegistrazioneDialog.BottomSheetListener
{
    private EditText nome;
    private EditText cognome;
    private EditText email;
    private EditText password;
    private EditText dataDiNascita;
    private FrameLayout btn_register;
    private ActivityRegisterBinding mBinding;
    private ControllerRegister controllerRegister;
    private ImageButton btn_backToLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        controllerRegister = new ControllerRegister(getSupportFragmentManager(), this, this);
        nome = findViewById(R.id.edt_nome);
        cognome = findViewById(R.id.edt_cognome);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        btn_backToLogin = findViewById(R.id.btn_register_back);
        btn_backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBackToLogin = new Intent(Register.this, Login.class);
                startActivity(intentBackToLogin);
            }
        });
        dataDiNascita = findViewById(R.id.edt_date);
        dataDiNascita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        if(dayOfMonth < 10){
                            dataDiNascita.setText("0" + dayOfMonth+"/"+(month +1)+"/"+year);
                        }
                        if(month < 10){
                            dataDiNascita.setText(dayOfMonth+"/0"+(month +1)+"/"+year);
                        }
                    }
                }, 2000 ,Calendar.getInstance().get(Calendar.MONTH), 1);

                datePickerDialog.show();
            }
        });
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                controllerRegister.registerUser(String.valueOf(nome.getText()),String.valueOf(cognome.getText()),
                        String.valueOf(email.getText()),String.valueOf(password.getText()),String.valueOf(dataDiNascita.getText()));
            }
        });
    }
    public void load()
    {
        animateButtonWidth();
        fadeOutTextAndSetProgressDialog();
        nextAction();
    }

    private void animateButtonWidth(){
        ValueAnimator anim = ValueAnimator.ofInt(mBinding.btnRegister.getMeasuredWidth(),getFinalWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mBinding.btnRegister.getLayoutParams();
                layoutParams.width = value;
                mBinding.btnRegister.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private void fadeOutTextAndSetProgressDialog(){
        mBinding.txtRegistraAnimated.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showProgressDialog();
            }
        }).start();
    }

    private void showProgressDialog(){
        mBinding.progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        mBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void nextAction(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealButton();
                fadeOutProgressDialog();
                delayedStartNextActivity();
            }
        }, 2000);
    }

    private void revealButton(){
        mBinding.btnRegister.setElevation(0f);
        mBinding.revealView.setVisibility(View.VISIBLE);
        int x = mBinding.revealView.getWidth();
        int y = mBinding.revealView.getWidth();

        int startX = (int) (getFinalWidth()/2 + mBinding.btnRegister.getX());
        int startY = (int) (getFinalWidth()/2 + mBinding.btnRegister.getY());

        float radius = Math.max(x,y) * 1.2f;

        Animator reveal = ViewAnimationUtils.createCircularReveal(mBinding.revealView,startX,startY,getFinalWidth(),radius);
        reveal.setDuration(350);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                finish();
            }
        });

        reveal.start();
    }

    private void fadeOutProgressDialog(){
        mBinding.progressBar.animate().alpha(0f).setDuration(200).start();
    }

    private void delayedStartNextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                controllerRegister.passaAlLogin();
            }
        },5000);
    }

    private int getFinalWidth(){
        return (int) getResources().getDimension(R.dimen.get_width);
    }

    @Override
    public void onButtonClicked(String codice, TextView errore, ConfermaRegistrazioneDialog dialog)
    {

        controllerRegister.verficaCodiceCognito(codice, String.valueOf(email.getText()), errore, dialog);


        Log.i("NATOUR","codice = "+ codice);
    }
}