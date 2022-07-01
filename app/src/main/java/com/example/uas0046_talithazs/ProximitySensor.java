package com.example.uas0046_talithazs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ProximitySensor extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SQLiteDatabase sqLiteDatabase;
    String SQLiteQuery;
    SensorManager sensorManager;
    Sensor sensor;
    TextView textX;
    float x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_sensor);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.bt_sensor);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.bt_db:
                        startActivity(new Intent(getApplicationContext(),ListViewActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.bt_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.bt_sensor:
                        return true;
                }
                return false;
            }
        });

        textX = findViewById(R.id.textX);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensor == null){
            // Use the proximity.
            if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
                BuatDatabase();
                int MINUTES = 2; // The delay in minutes
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        addData(); // If the function you wanted was static
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(ProximitySensor.this, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }
                }, 0, 1000 * 60 * MINUTES);
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            } else{
                textX.setText("Maaf, SmartPhone anda tidak mendukung");
            }
        }

    }
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(proxiListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(proxiListener);
    }

    SensorEventListener proxiListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }
        @SuppressLint("SetTextI18n")
        public void onSensorChanged(SensorEvent event) {
            x = event.values[0];

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 10 seconds
                    textX.setText(+ (float)x+" cm");
                    handler.postDelayed(this, 2000);
                }
            }, 2000);  //the time is in miliseconds
        }
    };
    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }

    private void BuatDatabase() {
        sqLiteDatabase = openOrCreateDatabase("Nama_Database_Baru", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Nama_Tabel (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title VARCHAR, x VARCHAR);");
    }

    private void addData() {
        SQLiteQuery = "INSERT INTO Nama_Tabel (title,x) VALUES ('"+ getCurrentDate() +"', '"+ x +"');";
        sqLiteDatabase.execSQL(SQLiteQuery);
    }

}