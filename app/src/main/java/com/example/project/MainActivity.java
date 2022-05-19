package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    EditText username, password;
    Button signup, signin, signinGuest;
    DataBase DB;

    SensorManager sensorManager;
    Sensor sensor;
    Boolean permission = false;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermission(this);

        view = this.getWindow().getDecorView();
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        signinGuest = (Button) findViewById(R.id.btnguest);
        DB = new DataBase(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuser = DB.checkUsername(user);
                    if(!checkuser){
                        Boolean insert = DB.insertData(user, pass);
                        if(insert){
                            Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "User already exists! Please sign in", Toast.LENGTH_SHORT).show();
                    }
                } }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuser = DB.checkUsername(user);
                    if(checkuser){
                        Boolean check = DB.checkUsernamePassword(user, pass);
                        if(check){
                            Toast.makeText(MainActivity.this, "Log in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            intent.putExtra("Log_user", true);
                            intent.putExtra("Name_of_user", user);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "User doesnt exists! Please register", Toast.LENGTH_SHORT).show();
                    }
                } }
        });


        signinGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Log in as guest successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("Log_user", false);
                startActivity(intent);
            }
        });
    }

    public void askPermission(Context c) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.System.canWrite(c)) {
                //permission access
                permission = true;
            }
            else {
                Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                c.startActivity(i);
                Toast.makeText(MainActivity.this, "Please give My application modify permission", Toast.LENGTH_SHORT).show();
            }
        }
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
                username.setBackgroundColor(R.color.black);
                password.setBackgroundColor(R.color.black);
            } else if (sensorEvent.values[0] > 250 && sensorEvent.values[0] <= 450) {
                if(permission) {
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 150);
                }
                view.setBackgroundResource((androidx.cardview.R.color.cardview_dark_background));
                username.setBackgroundColor(androidx.cardview.R.color.cardview_dark_background);
                password.setBackgroundColor(androidx.cardview.R.color.cardview_dark_background);
            } else if (sensorEvent.values[0] >= 550) {
                if(permission) {
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 250);
                }
                view.setBackgroundResource(R.color.white);
                username.setBackgroundColor(androidx.cardview.R.color.cardview_dark_background);
                password.setBackgroundColor(androidx.cardview.R.color.cardview_dark_background);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}