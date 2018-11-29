package com.github.pocmo.sensordashboard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

/*
 *  @author: AHJ
 */

public class VSmode extends AppCompatActivity {
    private String myTokenID;
    private String myEmail;
    static EditText searchEmail;
    private EditText betContents;
    private Button btn;
    private DatabaseReference mDatabase;
    ArrayList<User> userArrayList = new ArrayList<>();
    ArrayList<Match> matchArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vsmode);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myTokenID = user.getUid();
        myEmail = user.getEmail();
        searchEmail = findViewById(R.id.searchEmail);
        betContents = findViewById(R.id.betContents);
        btn = findViewById(R.id.complete);
        Log.e("가나다", "" + myTokenID + ", " + myEmail);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        ValueEventListener userListener = new ValueEventListener() { // User디비 Callback메소드
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    userArrayList.add(snapshot.getValue(User.class)); // 전체 User정보를 ArrayList에 저장
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.child("user_info").addValueEventListener(userListener);

        ValueEventListener matchListener = new ValueEventListener() { // match 정보 콜백
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                matchArrayList.add(snapshot.getValue(Match.class)); // 매칭 정보를 ArrayList에 저장
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.child("match").addValueEventListener(matchListener);


        Iterator<Match> checkMatch = matchArrayList.iterator();
        while (checkMatch.hasNext()) {
            Match m = checkMatch.next();
            StringTokenizer st = new StringTokenizer(m.getId(), ",");
            if (st.nextToken().equals(myTokenID) || st.nextToken().equals(myTokenID)) {
                // VS모드 진행중 액티비티 startActivity();
                Log.e("제발3", "진입입");
                Intent intent = new Intent(this, MatchActivity.class);
                startActivity(intent);
                finish();
            }
            else { // if "false", then

            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<User> checkEmail = userArrayList.iterator();
                while (checkEmail.hasNext()) {
                    User tempUser = checkEmail.next();
                    if ((searchEmail.getText().toString()).equals(tempUser.getEmail())) {
                        Log.e("제발", searchEmail.getText() + "이메일이 있습니다");
                        Match m = new Match(betContents.getText().toString(), myTokenID + "," + tempUser.getUsername());
                        mDatabase.child("match").child(myTokenID + "," + tempUser.getUsername()).setValue(m);
                        Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    else {
                    }
                }
                // 유효한 이메일 검색이 아닐 때
            }
        });
    }
}