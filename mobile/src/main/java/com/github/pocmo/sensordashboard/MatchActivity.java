package com.github.pocmo.sensordashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class MatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        ImageView myProfile = (ImageView)findViewById(R.id.myProfile);
        Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()).into(myProfile); //프로필 사진 보여주기

        VSmode vsmode = new VSmode();
        String yourEmail = vsmode.searchEmail.getText().toString();
        TextView yourID = (TextView)findViewById(R.id.yourID);
        yourID.setText(yourEmail.split("@")[0]);

        TextView myID = (TextView)findViewById(R.id.myID);
        myID.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0]);

        String betContents = vsmode.betContents.getText().toString();
        TextView contents = (TextView)findViewById(R.id.contents);
        contents.setText(betContents);

    }
}
