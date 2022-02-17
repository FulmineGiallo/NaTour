package com.example.natour.view.RegisterActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.natour.R;
import com.example.natour.view.LoginActivity.Login;

public class TransazioneRegister extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transazione_register);
        ImageView logo = findViewById(R.id.img_logoReg);
        ImageView ok = findViewById(R.id.img_regconfermata);
        ImageView gufo = findViewById(R.id.img_gufo);
        TextView conferma = findViewById(R.id.txt_reg);
        /* anim anima l'alpha la vibilità/invibilità colore (parametri 1: alpha partenza, parametro 2 aplha finale)
        *  alpha = trasparenza
        * */

        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);

        /*setRepeatMode per scegliere la modalità in che modo deve essere fatta */
        anim.setRepeatMode(Animation.REVERSE);

        /* AnimationSet, più animazioni nello stesso tempo oppure animazioni con opportuni ritardi con animazioni continue nella stessa view*/
        AnimationSet animationSet = new AnimationSet(true);

        /* piovt = riferimento */
        RotateAnimation rotateAnimation = new RotateAnimation(360f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());


        /*Translate per muovere le animazioni, per muovere una view nel suo container*/
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                1f, Animation.RELATIVE_TO_PARENT, ok.getX(), Animation.RELATIVE_TO_PARENT,
                ok.getY(), Animation.RELATIVE_TO_PARENT, ok.getY());
        translateAnimation.setDuration(1000);
        translateAnimation.setInterpolator(new DecelerateInterpolator());

        /* Set Di animazioni che voglio fare */
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);

        /* view.startAnimation(), inizia le animazioni */
        logo.startAnimation(anim);
        ok.startAnimation(animationSet);
        gufo.startAnimation(anim);
        conferma.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {

                startActivity(new Intent(TransazioneRegister.this, Login.class));
            }
        },3000);




    }
}