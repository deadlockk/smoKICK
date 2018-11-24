package com.github.pocmo.sensordashboard;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class SensorService extends Service implements SensorEventListener {
    private static final String TAG = "SensorService";

    private final static int SENS_ACCELEROMETER = Sensor.TYPE_ACCELEROMETER;
    private final static int SENS_MAGNETIC_FIELD = Sensor.TYPE_MAGNETIC_FIELD;
    // 3 = @Deprecated Orientation
    private final static int SENS_GYROSCOPE = Sensor.TYPE_GYROSCOPE;
    private final static int SENS_LIGHT = Sensor.TYPE_LIGHT;
    private final static int SENS_PRESSURE = Sensor.TYPE_PRESSURE;
    // 7 = @Deprecated Temperature
    private final static int SENS_PROXIMITY = Sensor.TYPE_PROXIMITY;
    private final static int SENS_GRAVITY = Sensor.TYPE_GRAVITY;
    private final static int SENS_LINEAR_ACCELERATION = Sensor.TYPE_LINEAR_ACCELERATION;
    private final static int SENS_ROTATION_VECTOR = Sensor.TYPE_ROTATION_VECTOR;
    private final static int SENS_HUMIDITY = Sensor.TYPE_RELATIVE_HUMIDITY;
    // TODO: there's no Android Wear devices yet with a body temperature monitor
    private final static int SENS_AMBIENT_TEMPERATURE = Sensor.TYPE_AMBIENT_TEMPERATURE;
    private final static int SENS_MAGNETIC_FIELD_UNCALIBRATED = Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED;
    private final static int SENS_GAME_ROTATION_VECTOR = Sensor.TYPE_GAME_ROTATION_VECTOR;
    private final static int SENS_GYROSCOPE_UNCALIBRATED = Sensor.TYPE_GYROSCOPE_UNCALIBRATED;
    private final static int SENS_SIGNIFICANT_MOTION = Sensor.TYPE_SIGNIFICANT_MOTION;
    private final static int SENS_STEP_DETECTOR = Sensor.TYPE_STEP_DETECTOR;
    private final static int SENS_STEP_COUNTER = Sensor.TYPE_STEP_COUNTER;
    private final static int SENS_GEOMAGNETIC = Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR;
    private final static int SENS_HEARTRATE = Sensor.TYPE_HEART_RATE;

    SensorManager mSensorManager;

    private Sensor mHeartrateSensor;

    private DeviceClient client;
    private ScheduledExecutorService mScheduler;

    private long currentTime;
    private double sum=0, sum2=0;
    private int count=0,count2=0, totcount=0;
    private static final int windowSize=1000;
    //private static final int DOWN=0;
    private int status;
    private boolean isStart=false;
    private long startTime, smokeTime=0;
    private double[] sample=new double[1000];

    @Override
    public void onCreate() {
        super.onCreate();
        currentTime=System.currentTimeMillis();
        client = DeviceClient.getInstance(this);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("smoKICK");
        builder.setContentText("Detecting Smoking....");
        builder.setSmallIcon(R.drawable.ic_launcher);

        startForeground(1, builder.build());

        startMeasurement();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopMeasurement();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void startMeasurement() {
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));

        if (BuildConfig.DEBUG) {
            logAvailableSensors();
        }

        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(SENS_ACCELEROMETER);
//        Sensor ambientTemperatureSensor = mSensorManager.getDefaultSensor(SENS_AMBIENT_TEMPERATURE);
//        Sensor gameRotationVectorSensor = mSensorManager.getDefaultSensor(SENS_GAME_ROTATION_VECTOR);
//        Sensor geomagneticSensor = mSensorManager.getDefaultSensor(SENS_GEOMAGNETIC);
        Sensor gravitySensor = mSensorManager.getDefaultSensor(SENS_GRAVITY);
        Sensor gyroscopeSensor = mSensorManager.getDefaultSensor(SENS_GYROSCOPE);
//        Sensor gyroscopeUncalibratedSensor = mSensorManager.getDefaultSensor(SENS_GYROSCOPE_UNCALIBRATED);
        mHeartrateSensor = mSensorManager.getDefaultSensor(SENS_HEARTRATE);
        Sensor heartrateSamsungSensor = mSensorManager.getDefaultSensor(65562);
//        Sensor lightSensor = mSensorManager.getDefaultSensor(SENS_LIGHT);
//        Sensor linearAccelerationSensor = mSensorManager.getDefaultSensor(SENS_LINEAR_ACCELERATION);
//        Sensor magneticFieldSensor = mSensorManager.getDefaultSensor(SENS_MAGNETIC_FIELD);
//        Sensor magneticFieldUncalibratedSensor = mSensorManager.getDefaultSensor(SENS_MAGNETIC_FIELD_UNCALIBRATED);
//        Sensor pressureSensor = mSensorManager.getDefaultSensor(SENS_PRESSURE);
//        Sensor proximitySensor = mSensorManager.getDefaultSensor(SENS_PROXIMITY);
//        Sensor humiditySensor = mSensorManager.getDefaultSensor(SENS_HUMIDITY);
//        Sensor rotationVectorSensor = mSensorManager.getDefaultSensor(SENS_ROTATION_VECTOR);
//        Sensor significantMotionSensor = mSensorManager.getDefaultSensor(SENS_SIGNIFICANT_MOTION);
//        Sensor stepCounterSensor = mSensorManager.getDefaultSensor(SENS_STEP_COUNTER);
//        Sensor stepDetectorSensor = mSensorManager.getDefaultSensor(SENS_STEP_DETECTOR);


        // Register the listener
        if (mSensorManager != null) {
            if (accelerometerSensor != null) {
                mSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.w(TAG, "No Accelerometer found");
            }

//            if (ambientTemperatureSensor != null) {
//                mSensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.w(TAG, "Ambient Temperature Sensor not found");
//            }
//
//            if (gameRotationVectorSensor != null) {
//                mSensorManager.registerListener(this, gameRotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.w(TAG, "Gaming Rotation Vector Sensor not found");
//            }

//            if (geomagneticSensor != null) {
//                mSensorManager.registerListener(this, geomagneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.w(TAG, "No Geomagnetic Sensor found");
//            }
//
            if (gravitySensor != null) {
                mSensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.w(TAG, "No Gravity Sensor");
            }

            if (gyroscopeSensor != null) {
                mSensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.w(TAG, "No Gyroscope Sensor found");
            }

//            if (gyroscopeUncalibratedSensor != null) {
//                mSensorManager.registerListener(this, gyroscopeUncalibratedSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.w(TAG, "No Uncalibrated Gyroscope Sensor found");
//            }
//
            if (mHeartrateSensor != null) {
                final int measurementDuration   = 30;   // Seconds
                final int measurementBreak      = 15;    // Seconds

                mScheduler = Executors.newScheduledThreadPool(1);
                mScheduler.scheduleAtFixedRate(
                        new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "register Heartrate Sensor");
                                mSensorManager.registerListener(SensorService.this, mHeartrateSensor, SensorManager.SENSOR_DELAY_FASTEST);

                                try {
                                    Thread.sleep(measurementDuration * 1000);
                                } catch (InterruptedException e) {
                                    Log.e(TAG, "Interrupted while waitting to unregister Heartrate Sensor");
                                }

                                Log.d(TAG, "unregister Heartrate Sensor");
                                mSensorManager.unregisterListener(SensorService.this, mHeartrateSensor);
                            }
                        }, 3, measurementDuration + measurementBreak, TimeUnit.SECONDS);

            } else {
                Log.d(TAG, "No Heartrate Sensor found");
            }

            if (heartrateSamsungSensor != null) {
                mSensorManager.registerListener(this, heartrateSamsungSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "Samsungs Heartrate Sensor not found");
            }

//            if (lightSensor != null) {
//                mSensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No Light Sensor found");
//            }

//            if (linearAccelerationSensor != null) {
//                mSensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No Linear Acceleration Sensor found");
//            }

//            if (magneticFieldSensor != null) {
//                mSensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No Magnetic Field Sensor found");
//            }
//
//            if (magneticFieldUncalibratedSensor != null) {
//                mSensorManager.registerListener(this, magneticFieldUncalibratedSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No uncalibrated Magnetic Field Sensor found");
//            }

//            if (pressureSensor != null) {
//                mSensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No Pressure Sensor found");
//            }
//
//            if (proximitySensor != null) {
//                mSensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No Proximity Sensor found");
//            }
//
//            if (humiditySensor != null) {
//                mSensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No Humidity Sensor found");
//            }
//
//            if (rotationVectorSensor != null) {
//                mSensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No Rotation Vector Sensor found");
//            }
//
//            if (significantMotionSensor != null) {
//                mSensorManager.registerListener(this, significantMotionSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No Significant Motion Sensor found");
//            }
//
//            if (stepCounterSensor != null) {
//                mSensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No Step Counter Sensor found");
//            }
//
//            if (stepDetectorSensor != null) {
//                mSensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Log.d(TAG, "No Step Detector Sensor found");
//            }
        }
    }

    private void stopMeasurement() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        if (mScheduler != null && !mScheduler.isTerminated()) {
            mScheduler.shutdown();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //데이터 전송
        //   client.sendSensorData(event.sensor.getType(), event.accuracy, event.timestamp, event.values);
        if(event.sensor.getType()==1) {
            //   sample[count] = event.values[0];
            if(event.values[0]>=-10||event.values[0]<=10) //noise filter
                sum+=event.values[0];
            count++;
            Log.d("smoking", "" + event.values[0]);
        }
        else if(event.sensor.getType()==Sensor.TYPE_HEART_RATE)
        {
            sum2+=event.values[0];
            count2++;
            Log.d("heart"," "+event.values[0]);
        }

        long now=System.currentTimeMillis();

        if(now-currentTime>=windowSize) //windowSize 마다
        {
            currentTime=now;
            Log.d("smoking"," 초당 데이터 개수"+count);
            Log.d("smoking","초당 평균 값"+sum/count);
            if(sum/count>=5)  //손이 올라간 상태
            {
                if(!isStart) {
                    Log.d("smoking","손이 올라감 / 측정 시작!");
                    startTime = currentTime;
                    isStart = true;
                }
                else
                {
                    if(currentTime-startTime>=4000) //손이 4초이상 아래로 내려가지 않을 때 초기화
                    {
                        Log.d("smoking","손이 올라갔는데 안내려감 / 측정 종료!");
                        isStart = false;
                    }
                }
            }

            else if(sum/count<=-7) //손이 내려간 상태
            {
                if (isStart) //올라가 있었다면
                {
                    Long temp=now-startTime;

                    int min=temp.compareTo(1000L);
                    int max=temp.compareTo(3000L);
                    //             Log.d("smoking","비교"+min+" "+max);
                    if (min>=0 && max<=0&&sum2/count2>10) //1~3초동안 지속되었다면
                    {
                        sum2=0;
                        count2=0;
                        temp=now-smokeTime;
                        min=temp.compareTo(3000L);
                        max=temp.compareTo(30000L);
                        if (min<=0 || max>=0) //3~30초 사이 안에 이루어지지 않았다면
                            totcount = 1;
                        else {
                            totcount++;
                            Log.d("smoking","흡입동작 관측!");

                            if (totcount == 5) //5회 이상 smoking action 감지시
                            {
                                Log.d("smoking","흡연 탐지!");
                                client.sendSensorData(event.sensor.getType(), event.accuracy, now, event.values); //데이터 전송
                                totcount=0;
                            }
                        }
                        smokeTime = now;
                    }
                    else
                        Log.d("smoking","흡연동작은 아님! / 측정 종료!");
                    isStart = false;

                }
            }
            count=0;
            sum=0.0;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Log all available sensors to logcat
     */
    private void logAvailableSensors() {
        final List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.d(TAG, "=== LIST AVAILABLE SENSORS ===");
        Log.d(TAG, String.format(Locale.getDefault(), "|%-35s|%-38s|%-6s|", "SensorName", "StringType", "Type"));
        for (Sensor sensor : sensors) {
            Log.v(TAG, String.format(Locale.getDefault(), "|%-35s|%-38s|%-6s|", sensor.getName(), sensor.getStringType(), sensor.getType()));
        }

        Log.d(TAG, "=== LIST AVAILABLE SENSORS ===");
    }

}