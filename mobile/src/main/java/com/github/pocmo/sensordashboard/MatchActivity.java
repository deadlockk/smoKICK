package com.github.pocmo.sensordashboard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class MatchActivity extends AppCompatActivity {
    ArrayList<User> userArrayList = new ArrayList<>();
    private String betContents;
    private String yourEmail;
    private String myEmail;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView myProfile = (ImageView) findViewById(R.id.myProfile);
        Glide.with(this).load(user.getPhotoUrl().toString()).into(myProfile); //프로필 사진 보여주기


        userArrayList = (ArrayList<User>) getIntent().getSerializableExtra("userList");
        Intent intent = getIntent();
        betContents = intent.getExtras().getString("betContents");

        String yourUid = "";
        Iterator<User> userIterator = userArrayList.iterator();
        while (userIterator.hasNext()) {
            final User tempUser = userIterator.next();
            if (tempUser.getUsername().equals(user.getUid())) {
                if (betContents.equals("null")) {
                    betContents = tempUser.getBetting();

                }
                ValueEventListener userListener = new ValueEventListener() { // User디비 Callback메소드
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                            Log.e("제발000", tempUser.getEmail() + snapshot.child(tempUser.getUsername()).getValue() + tempUser.getBetting() + tempUser.getUsername());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                databaseReference.child("user_info").addValueEventListener(userListener);

                yourUid = tempUser.getIsVS();


                myEmail = tempUser.getEmail();
                break;
            }
        }

        userArrayList = (ArrayList<User>) getIntent().getSerializableExtra("userList");
        Iterator<User> userIteratorr = userArrayList.iterator(); // while 문 안들어가길래 걍 하나 더 만듦
        while (userIteratorr.hasNext()) {
            User tempUserr = userIteratorr.next();

            if (tempUserr.getUsername().equals(yourUid)) {
                yourEmail = tempUserr.getEmail();
                break;
            }
        }

        TextView yourID = (TextView) findViewById(R.id.yourID);
        if(yourEmail != null)//matching이후 두번째 진입 시 부터
            yourID.setText(yourEmail.split("@")[0]);
        else {//처음 matching 시
            yourID.setText("smoKICK");
        } 
        TextView myID = (TextView) findViewById(R.id.myID);
        myID.setText(myEmail.split("@")[0]);
        TextView contents = (TextView) findViewById(R.id.contents);
        contents.setText(betContents);

    }

}
