package com.github.pocmo.sensordashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VSmode extends AppCompatActivity {
    String tokenID;
    String email;
    EditText editBet;
    TextView textBet;
    TextView myEmail;
    EditText opponentEmail;
    TextView opponentText;
    String betting;
    EditText editText;
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
    }
}
