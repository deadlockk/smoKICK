package com.github.pocmo.sensordashboard;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.*;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

/*
 * @author: Sangwon
 */
public class Details extends AppCompatActivity {

    EditText name, price, number;
    EditText age;
    RadioButton male;
    RadioButton female;
    SignInButton submit;
    ProgressBar loginProgress;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // 구글로그인 result 상수
    private static final int RC_SIGN_IN = 900;
    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 파이어베이스 실시간 DB
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = (EditText) findViewById(R.id.editTextName);
        age = (EditText) findViewById(R.id.editTextAge);
        female = (RadioButton) findViewById(R.id.radioButtonFemale);
        male = (RadioButton) findViewById(R.id.radioButtonMale);
        submit = (SignInButton) findViewById(R.id.buttonSubmit);
        price = (EditText) findViewById(R.id.editTextprice);
        number = (EditText) findViewById(R.id.editTextnumber);
        loginProgress = (ProgressBar)findViewById(R.id.loginProgress);



        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        Intent i = new Intent("smoKICK_Info");
        sendBroadcast(i);

        sharedPreferences = getSharedPreferences("smoKICK", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.length() > 0 && age.length() > 0 && (male.isChecked() || female.isChecked()) && price.length() > 0 && number.length() > 0) {
                    showProgress(true);
                    Intent signInIntent = googleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);

                    editor.putString("Name", name.getText().toString().trim());
                    editor.putInt("Age", Integer.parseInt(age.getText().toString().trim()));
                    float price_one = (float)Integer.parseInt(price.getText().toString().trim())/20;
                    int number_daily = Integer.parseInt(number.getText().toString().trim());
                    editor.putInt("dly", number_daily);
                    editor.putFloat("prc", price_one);
                    long now=System.currentTimeMillis();
                    editor.putLong("Time",now);

                    if (male.isChecked()) {
                        editor.putString("Gender", "Male");
                    } else {
                        editor.putString("Gender", "Female");

                    }

                    editor.putInt("Days", 0);
                    editor.putInt("Dayssmoke", 0);
                    editor.apply();
                    editor.commit();


                } else {
                    Toast.makeText(Details.this, "Please Enter all Details", Toast.LENGTH_LONG).show();
                }

            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 구글로그인 버튼 응답
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // 구글 로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }

    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
    // Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase에 인증합니다.
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(Details.this, "구글 아이디 연동에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            Intent i = new Intent(Details.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // 로그인 실패
                            Toast.makeText(Details.this, "구글 아이디 연동에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    /**
     * Progressing view
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            loginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final FirebaseUser f_user = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User(f_user.getUid(), f_user.getEmail(), "false", "null");//User객체에 token, email, isTrue=false로 초기화
        databaseReference.child("user_info").child(user.getUsername()).setValue(user); // push()를 하지 않기 때문에 중복처리 가능
    }
}