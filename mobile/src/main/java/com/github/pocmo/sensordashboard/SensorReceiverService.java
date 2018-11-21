package com.github.pocmo.sensordashboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.github.pocmo.sensordashboard.database.DataEntry;
import com.github.pocmo.sensordashboard.shared.DataMapKeys;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class SensorReceiverService extends WearableListenerService {
    private static final String TAG = "MYLOG";

    SharedPreferences sp;
    private RemoteSensorManager sensorManager;

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = RemoteSensorManager.getInstance(this);
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        Log.i(TAG, "Connected: " + peer.getDisplayName() + " (" + peer.getId() + ")");
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        super.onPeerDisconnected(peer);

        Log.i(TAG, "Disconnected: " + peer.getDisplayName() + " (" + peer.getId() + ")");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged()");

        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = dataEvent.getDataItem();
                Uri uri = dataItem.getUri();
                String path = uri.getPath();

                if (path.startsWith("/sensors/")) {
                    unpackSensorData(
                            Integer.parseInt(uri.getLastPathSegment()),
                            DataMapItem.fromDataItem(dataItem).getDataMap()
                    );
                }
            }
        }
    }

    private void unpackSensorData(int sensorType, DataMap dataMap) {
        int accuracy = dataMap.getInt(DataMapKeys.ACCURACY);
        long timestamp = dataMap.getLong(DataMapKeys.TIMESTAMP);
        float[] values = dataMap.getFloatArray(DataMapKeys.VALUES);

        Date date = new Date(timestamp);
        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
        String current_time = datef.format(date);

        sp = getSharedPreferences("smokingInformation", Activity.MODE_PRIVATE);

        int cnt = sp.getInt("smokingInformation", -1);
        String str = sp.getString("smokingTime", "");
        str +=current_time+"/";
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("smokingInformation", cnt + 1);
        editor.putString("smokingTime", str);
        editor.commit();


        // Log.d(TAG, "Received sensor data " + sensorType + " = " + Arrays.toString(values));
        Log.d("smoking", "흡연 발생 시간" + current_time);
        Toast.makeText(getApplicationContext(), "흡연 탐지! 일시: " + current_time, Toast.LENGTH_SHORT).show();
        sensorManager.addSensorData(sensorType, accuracy, timestamp, values);
    }
}
