package com.example.sensortest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements
        SensorEventListener {

    private TextView txtAzimuth, txtPitch, txtRoll;
    private  static SensorManager mSensorManager;
    private Sensor mAccelerometer; //가속도 센스
    private Sensor mMagnetometer; // 자력계 센스
    float[] mGravity = null;
    float[] mGeomagnetic = null;
    final float alpha = (float)0.8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtAzimuth = (TextView) findViewById(R.id.textzaxis);
        txtPitch = (TextView) findViewById(R.id.textxaxis);
        txtRoll = (TextView) findViewById(R.id.textyaxis);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    public void onSensorChanged(SensorEvent event) {

        float azimut, pitch, roll;

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values;
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values;
        }

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I,
                    mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                // orientation contains: azimut, pitch  and roll
                azimut = orientation[0];
                pitch = orientation[1];
                roll = orientation[2];

                txtAzimuth.setText("z 좌표:" + String.valueOf(azimut));
                txtPitch.setText("y 좌표:" + String.valueOf(pitch));
                txtRoll.setText("x 좌표 : " + String.valueOf(roll));
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }
}

