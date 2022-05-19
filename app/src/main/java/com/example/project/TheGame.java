package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

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
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class TheGame extends AppCompatActivity implements SensorEventListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    SensorManager sensorManager;
    Sensor sensor;
    Boolean permission = false;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);


        view = this.getWindow().getDecorView();
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


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

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();

        String game = getIntent().getStringExtra("Game");
        switch (game) {
            case "Dark Souls":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, DarkSouls.class, null).setReorderingAllowed(true).addToBackStack("name").commit();
                break;
            case "The Witcher":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, TheWitcher.class, null).setReorderingAllowed(true).addToBackStack("name").commit();
                break;
            case "League of Legends":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, LeagueOfLegends.class, null).setReorderingAllowed(true).addToBackStack("name").commit();
                break;
            case "Forza Horizon":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, ForzaHorizon.class, null).setReorderingAllowed(true).addToBackStack("name").commit();
                break;
            case "Diablo":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, Diablo.class, null).setReorderingAllowed(true).addToBackStack("name").commit();
                break;
            case "Warcraft":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, Warcraft.class, null).setReorderingAllowed(true).addToBackStack("name").commit();
                break;
            case "CS: GO":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, CSGO.class, null).setReorderingAllowed(true).addToBackStack("name").commit();
                break;
        }

        Toast.makeText(this, "Clicked: "+ getIntent().getStringExtra("Game"), Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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