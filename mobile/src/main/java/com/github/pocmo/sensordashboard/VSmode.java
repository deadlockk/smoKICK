package com.github.pocmo.sensordashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    public static EditText searchEmail;
    static EditText betContents;
    private Button btn;
    private DatabaseReference mDatabase;


    ProgressBar loginProgress;

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
        loginProgress = (ProgressBar) findViewById(R.id.loginProgress);

        btn = findViewById(R.id.complete);
        Log.e("가나다", "" + myTokenID + ", " + myEmail);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        userArrayList = (ArrayList<User>) getIntent().getSerializableExtra("userList");
        Iterator<User> userIterator = userArrayList.iterator();
        while (userIterator.hasNext()) {
            User tempUser = userIterator.next();
            Log.e("제발1", tempUser.getIsVS() + " / " + myTokenID);
            if (tempUser.getIsVS() != null) {// null 때문에 안돌아가서 null 이 아닐 때만
                if (tempUser.getIsVS().equals(myTokenID)) {
                    Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                    intent.putExtra("userList", userArrayList);
                    intent.putExtra("betContents", "null");
                    startActivity(intent);
                    finish();
                    break;
                }
            }
            Log.e("제발4", tempUser.getEmail() + " / " + tempUser.getIsVS());
        }

        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Iterator<User> checkEmail = userArrayList.iterator();
                int a =0;
                while (checkEmail.hasNext()) {
                    User tempUser = checkEmail.next();
                    if ((searchEmail.getText().toString()).equals(tempUser.getEmail())) {
                        a=1;
                        showProgress(true);
                        mDatabase.child("user_info").child(myTokenID).child("isVS").setValue(tempUser.getUsername());
                        mDatabase.child("user_info").child(myTokenID).child("betting").setValue(betContents.getText().toString());
                        mDatabase.child("user_info").child(tempUser.getUsername()).child("isVS").setValue(myTokenID);
                        mDatabase.child("user_info").child(tempUser.getUsername()).child("betting").setValue(betContents.getText().toString());

                        Log.e("제발 디비","확인확인");
                        Handler timer = new Handler(); //Handler 생성
                        timer.postDelayed(new Runnable() { //2초후 쓰레드를 생성하는 postDelayed 메소드
                            public void run() {

                                final Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                                intent.putExtra("betContents", betContents.getText().toString());
                                intent.putExtra("userList", userArrayList);
                                startActivity(intent); //다음 액티비티 이동
                                finish(); // 이 액티비티를 종료
                                showProgress(false);
                            }
                        }, 2000); //2000은 2초를 의미한다.
                    }
                }
                if(a==0)
                Toast.makeText(getApplicationContext(), "Please, enter the available e-mail", Toast.LENGTH_LONG).show();// 유효한 이메일 검색이 아닐 때
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
}