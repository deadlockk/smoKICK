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
import android.widget.Toast;

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
    static EditText betContents;
    private Button btn;
    private DatabaseReference mDatabase;
    ArrayList<User> userArrayList = new ArrayList<>();

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

        userArrayList = (ArrayList<User>)getIntent().getSerializableExtra("userList");
        Iterator<User> userIterator = userArrayList.iterator();
        while (userIterator.hasNext()) {
            User tempUser = userIterator.next();
            Log.e("제발1", tempUser.getIsVS() + " / " + myTokenID);
            if(tempUser.getIsVS().equals(myTokenID)) {
                Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                intent.putExtra("userList", userArrayList);
                intent.putExtra("betContents", "null");
                startActivity(intent);
                finish();
                break;
            }
            Log.e("제발4", tempUser.getEmail() + " / " + tempUser.getIsVS());
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<User> checkEmail = userArrayList.iterator();
                while (checkEmail.hasNext()) {
                    User tempUser = checkEmail.next();
                    if ((searchEmail.getText().toString()).equals(tempUser.getEmail())) {
                        Log.e("제발", searchEmail.getText() + "이메일이 있습니다");
                        mDatabase.child("user_info").child(myTokenID).child("isVS").setValue(tempUser.getUsername());
                        mDatabase.child("user_info").child(myTokenID).child("betting").setValue(betContents.getText().toString());
                        mDatabase.child("user_info").child(tempUser.getUsername()).child("isVS").setValue(myTokenID);
                        mDatabase.child("user_info").child(tempUser.getUsername()).child("betting").setValue(betContents.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                        intent.putExtra("betContents", betContents.getText().toString());
                        intent.putExtra("userList", userArrayList);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
                Toast.makeText(getApplicationContext(), "Please, enter the available e-mail",Toast.LENGTH_LONG).show();// 유효한 이메일 검색이 아닐 때
            }
        });
    }
}