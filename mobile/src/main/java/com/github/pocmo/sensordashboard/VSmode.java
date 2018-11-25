package com.github.pocmo.sensordashboard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

/*
 *  @author: AHJ
 */

public class VSmode extends AppCompatActivity {
    private String tokenID;
    private String email;
    private EditText editBet;
    private TextView textBet;
    private TextView myEmail;
    private EditText opponentEmail;
    private TextView opponentText;
    private String betting;
    private EditText editText;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vsmode);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tokenID = user.getUid();
        email = user.getEmail();
        editBet = findViewById(R.id.editBet);
        //textBet = findViewById(R.id.textBet);
        myEmail = findViewById(R.id.i);
        myEmail.setText(email);
        opponentEmail = findViewById(R.id.editOpponent);
        Log.e("가나다", "" + tokenID + ", " + email);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query userQuery = getQuery(mDatabase);
        ArrayList<User> al = new ArrayList<>();
        String newUser;
    }

    public Query getQuery(DatabaseReference databaseReference) {
        Query userQuery = databaseReference.child("user");
        return userQuery;
    }
}
