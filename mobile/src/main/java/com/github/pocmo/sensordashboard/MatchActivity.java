package com.github.pocmo.sensordashboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;

public class MatchActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    ArrayList<User> userArrayList = new ArrayList<>();
    private String betContents;
    private String yourEmail;
    private String myEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView myProfile = (ImageView)findViewById(R.id.myProfile);
        Glide.with(this).load(user.getPhotoUrl().toString()).into(myProfile); //프로필 사진 보여주기

        mDatabase = FirebaseDatabase.getInstance().getReference();

        userArrayList = (ArrayList<User>)getIntent().getSerializableExtra("userList");
        Intent intent = getIntent();
        betContents = intent.getExtras().getString("betContents");

        String yourUid = "";
        Iterator<User> userIterator = userArrayList.iterator();
        while (userIterator.hasNext()) {
            User tempUser = userIterator.next();
            if(tempUser.getUsername().equals(user.getUid())) {
                if (betContents.equals("null")) {
                    betContents = tempUser.getBetting();
                }
                yourUid = tempUser.getIsVS();
                myEmail = tempUser.getEmail();
                break;
            }
        }
        while (userIterator.hasNext()) {
            User tempUser = userIterator.next();
            if (tempUser.getUsername().equals(yourUid)) {
                yourEmail = tempUser.getEmail();
                break;
            }
        }

        TextView yourID = (TextView)findViewById(R.id.yourID);
        yourID.setText(yourEmail.split("@")[0]);

        TextView myID = (TextView)findViewById(R.id.myID);
        myID.setText(myEmail.split("@")[0]);
        TextView contents = (TextView)findViewById(R.id.contents);
        contents.setText(betContents);

    }
}
