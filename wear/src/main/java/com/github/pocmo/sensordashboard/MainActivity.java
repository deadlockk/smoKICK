//package com.github.pocmo.sensordashboard;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//
//public class MainActivity extends AppCompatActivity {
//
//    private DeviceClient deviceClient;
//    boolean isPlaying = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//
//
//    //    buttonStart = (Button)findViewById(R.id.buttonStart);
////        buttonStart.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if(!isPlaying){
////                    //메시지 전송
////                    buttonStart.setBackgroundColor(Color.GREEN);
////                    sendBroadcast(new Intent("start"));
////                    startService(new Intent(MainActivity.this, SensorService.class));
////                    isPlaying = true;
////                }
////                else{
////                    buttonStart.setBackgroundColor(Color.RED);
////                    sendBroadcast(new Intent("stop"));
////                    isPlaying = false;
////                    stopService(new Intent(MainActivity.this, SensorService.class));
////                }
//
////            }
////        });
//    }
//}
