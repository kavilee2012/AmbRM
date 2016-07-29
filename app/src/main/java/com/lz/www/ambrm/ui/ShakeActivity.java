package com.lz.www.ambrm.ui;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

/**
 * Created by Administrator on 2016-07-08.
 */
public class ShakeActivity extends AppCompatActivity  {

    private SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    SensorEventListener listener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float xValue=Math.abs(sensorEvent.values[0]);
            float yValue=Math.abs(sensorEvent.values[1]);
            float zValue=Math.abs(sensorEvent.values[2]);
            if(xValue>15||yValue>15||zValue>15){
                Toast.makeText(ShakeActivity.this,"摇了一下",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sensorManager!=null){
            sensorManager.unregisterListener(listener);
        }
    }
}
