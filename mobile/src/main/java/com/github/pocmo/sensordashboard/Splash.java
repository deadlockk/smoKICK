package com.github.pocmo.sensordashboard;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/*
 * @author: Sangwon
 */

public class Splash extends AppCompatActivity {

    ImageView title;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();
        sharedPreferences=getSharedPreferences("smoKICK",MODE_PRIVATE);
        title=(ImageView)findViewById(R.id.titlesplash);
        h.sendEmptyMessageDelayed(102,0);

        FirebaseUser cur_user = FirebaseAuth.getInstance().getCurrentUser();

        if(sharedPreferences.contains("Name") && cur_user != null){
            h.sendEmptyMessageDelayed(103,2000);
        }
        else{
            h.sendEmptyMessageDelayed(101,2000);
        }

    }
    Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==103){
                Intent i=new Intent(Splash.this,MainActivity.class);
                startActivity(i);
                finish();
            }
            if(msg.what==102){
                Animation animation= AnimationUtils.loadAnimation(Splash.this,R.anim.splash);
                title.setVisibility(View.VISIBLE);
                title.startAnimation(animation);
            }
            if(msg.what==101){
                Intent i=new Intent(Splash.this,Details.class);
                startActivity(i);
                finish();
            }
        }
    };
}
