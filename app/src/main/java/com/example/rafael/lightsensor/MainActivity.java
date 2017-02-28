package com.example.rafael.lightsensor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mLight, mAccelerometer;
    private TextView tv_lumens, tv_lightAcc;
    private TextView tv_accelerometerAcc, tv_accelerometerX, tv_accelerometerY, tv_accelerometerZ;
    private TextView tv_latitudeGPS, tv_longitudeGPS, tv_alturaGPS;
    private TextView tv_latitudeNet, tv_longitudeNet, tv_alturaNet;
    private TextView tv_permission;
    private TextView tv_cidade;
    private TextView tv_weather;
    private LocationManager locationManager;
    private LocationListener locationListenerGPS, locationListenerNet;
    private final int MY_PERMISSIONS_REQUEST_MULTIPLE_LOCATION = 600;

    private Location locationGPS;
    public final static String EXTRA_MESSAGE = "com.example.rafael.lightsensor.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find Text Views

        this.tv_lumens = (TextView) findViewById(R.id.t_lumens);
        this.tv_lightAcc = (TextView) findViewById(R.id.t_lightAcc);
        this.tv_accelerometerAcc = (TextView) findViewById(R.id.t_accelerometerAcc);
        this.tv_accelerometerX = (TextView) findViewById(R.id.t_acelerometerX);
        this.tv_accelerometerY = (TextView) findViewById(R.id.t_acelerometerY);
        this.tv_accelerometerZ = (TextView) findViewById(R.id.t_acelerometerZ);
        this.tv_latitudeGPS = (TextView) findViewById(R.id.t_latitudeGPS);
        this.tv_longitudeGPS = (TextView) findViewById(R.id.t_longitudeGPS);
        this.tv_alturaGPS = (TextView) findViewById(R.id.t_alturaGPS);
        this.tv_latitudeNet = (TextView) findViewById(R.id.t_latitudeNet);
        this.tv_longitudeNet = (TextView) findViewById(R.id.t_longitudeNet);
        this.tv_alturaNet = (TextView) findViewById(R.id.t_alturaNet);
        this.tv_permission = (TextView) findViewById(R.id.t_permission);
        this.tv_cidade = (TextView) findViewById(R.id.tv_cidade_edit);
        this.tv_weather = (TextView) findViewById(R.id.tv_weather_edit);

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

        // Location
        locationGPS = null;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListenerGPS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(locationGPS == null) locationGPS = location;
                tv_latitudeGPS.setText(Double.toString(location.getLatitude()));
                tv_longitudeGPS.setText(Double.toString(location.getLongitude()));
                tv_alturaGPS.setText(Double.toString(location.getAltitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationListenerNet = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(locationGPS == null) locationGPS = location;
                tv_latitudeNet.setText(Double.toString(location.getLatitude()));
                tv_longitudeNet.setText(Double.toString(location.getLongitude()));
                tv_alturaNet.setText(Double.toString(location.getAltitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_MULTIPLE_LOCATION);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            tv_permission.setText("Permission Denied!");
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        }else{
            tv_permission.setText("Permission Granted!");
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNet);
        }*/

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        switch (sensor.getType()) {
            case (Sensor.TYPE_LIGHT):
                tv_lightAcc.setText("Accuracy Changed to " + accuracy);
                break;
            case (Sensor.TYPE_ACCELEROMETER):
                tv_accelerometerAcc.setText("Accuracy Changed to " + accuracy);
                break;
            default:
        }

    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case (Sensor.TYPE_LIGHT):
                float lux = event.values[0];
                tv_lumens.setText(Float.toString(lux));
                break;
            case (Sensor.TYPE_ACCELEROMETER):
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case (MY_PERMISSIONS_REQUEST_MULTIPLE_LOCATION):
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted
                    tv_permission.setText("Permission Granted!");
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNet);
                    } catch (SecurityException e) {
                        Toast.makeText(getApplicationContext(), "Location Access Denied", Toast.LENGTH_SHORT).show();
                        tv_permission.setText("Permission Denied!");
                    }

                } else {// permission denied
                    tv_permission.setText("Permission Denied!");
                }
            default:
        }
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


    public void touchScreen (View view){
        Intent intent = new Intent(this, TouchScreenActivity.class);
        startActivity(intent);

    }

    public void getCity (View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        AlertDialog alertDialog;

        String city;
        String countryCode;

        if(locationGPS == null){

            // set dialog message
            alertDialogBuilder.setMessage("Location not available...");

            // create alert dialog
            alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();

            return;
        }

        Geocoder gcd = new Geocoder(MainActivity.this, Locale.ENGLISH);
        List<Address> addresses = null;
        try {
            Log.d("location",locationGPS.toString());
            addresses = gcd.getFromLocation(locationGPS.getLatitude(), locationGPS.getLongitude(), 1);
        } catch (IOException e) {
            Log.e("Main Activity","error noGetFromLocation");
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0){
            //System.out.println();
            // set dialog message
            city = addresses.get(0).getSubAdminArea();
            countryCode=addresses.get(0).getCountryCode();
            alertDialogBuilder.setMessage("Location: " + city + ", " + countryCode);
            Log.d("Address","The adress is: " + addresses.get(0));

            // create alert dialog
            alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();

            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{city+","+countryCode});


        }else{
            // set dialog message
            alertDialogBuilder.setMessage("Location not found...");
            // create alert dialog
            alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
    }




    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                //imgView.setImageBitmap(img);
            }

            //Log.d("Result",""+weather.currentCondition.getCondition());
            tv_cidade.setText(weather.location.getCity() + "," + weather.location.getCountry());
            tv_weather.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            /*temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "�C");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg() + "�");*/

        }







    }

}
