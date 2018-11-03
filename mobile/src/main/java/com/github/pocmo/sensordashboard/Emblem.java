package com.github.pocmo.sensordashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
/*
 * @author: Sangwon
 */
public class Emblem extends Activity {
    ImageButton profile;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emblem);

        final ImageView img1 = (ImageView)findViewById(R.id.img1);
        ImageView img2 = (ImageView)findViewById(R.id.img2);
        ImageView img3 = (ImageView)findViewById(R.id.img3);



//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        profile = (ImageButton) findViewById(R.id.drawer_imageView);


        img1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "1");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "2");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "3");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
