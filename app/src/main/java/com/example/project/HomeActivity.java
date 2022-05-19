package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements SensorEventListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public ListView LView;

    SensorManager sensorManager;
    Sensor sensor;
    Boolean permission = false;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        view = this.getWindow().getDecorView();
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        LView = (ListView) findViewById(R.id.listview);
        ArrayList<String> array = new ArrayList<>();
        array.add("Dark Souls");
        array.add("The Witcher");
        array.add("League of Legends");
        array.add("Forza Horizon");
        array.add("Diablo");
        array.add("Warcraft");
        array.add("CS: GO");

        ArrayAdapter arrAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        LView.setAdapter(arrAdapter);

        boolean ifUserLog = getIntent().getBooleanExtra("Log_user", false);
        if(ifUserLog) {
            drawerLayout = findViewById(R.id.my_drawer_layout);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        else {
        }

        LView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), TheGame.class);
                if(ifUserLog) {
                    intent.putExtra("Log_user", true);
                    intent.putExtra("Name_of_user", getIntent().getStringExtra("Name_of_user"));
                }
                else {
                    intent.putExtra("Log_user", false);
                }
                intent.putExtra("Game", array.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            if (sensorEvent.values[0] <= 150) {
                if(permission) {
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 50);
                }
                view.setBackgroundResource((R.color.black));
            } else if (sensorEvent.values[0] > 250 && sensorEvent.values[0] <= 450) {
                if(permission) {
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 150);
                }
                view.setBackgroundResource((androidx.cardview.R.color.cardview_dark_background));
            } else if (sensorEvent.values[0] >= 550) {
                if(permission) {
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 250);
                }
                //view.setBackgroundResource(R.color.white);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}