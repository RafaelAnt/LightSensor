package com.example.rafael.lightsensor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.View;
import android.widget.TextView;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.content.Intent;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mLight, mAccelerometer;
    private TextView tv_lumens, tv_lightAcc, tv_accelerometerAcc,tv_accelerometerX, tv_accelerometerY, tv_accelerometerZ;


    public final static String EXTRA_MESSAGE = "com.example.rafael.lightsensor.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tv_lumens = (TextView) findViewById(R.id.t_lumens);
        this.tv_lightAcc = (TextView) findViewById(R.id.t_lightAcc);
        this.tv_accelerometerAcc = (TextView) findViewById(R.id.t_accelerometerAcc);
        this.tv_accelerometerX = (TextView) findViewById(R.id.t_acelerometerX);
        this.tv_accelerometerY = (TextView) findViewById(R.id.t_acelerometerY);
        this.tv_accelerometerZ = (TextView) findViewById(R.id.t_acelerometerZ);

        //Keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        TextView tv2 = (TextView) findViewById(R.id.t_helloC);
        tv2.setText("Hello From Java!!");

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        switch(sensor.getType()){
            case(Sensor.TYPE_LIGHT):
                tv_lightAcc.setText("Accuracy Changed to " + accuracy);
                break;
            case(Sensor.TYPE_ACCELEROMETER):
                tv_accelerometerAcc.setText("Accuracy Changed to " + accuracy);
                break;
            default:
        }

    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case(Sensor.TYPE_LIGHT):
                float lumens = event.values[0];
                tv_lumens.setText(Float.toString(lumens));
                break;
            case(Sensor.TYPE_ACCELEROMETER):
                tv_accelerometerX.setText(Float.toString(event.values[0]));
                tv_accelerometerY.setText(Float.toString(event.values[1]));
                tv_accelerometerZ.setText(Float.toString(event.values[2]));
                break;
            default:
        }

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
