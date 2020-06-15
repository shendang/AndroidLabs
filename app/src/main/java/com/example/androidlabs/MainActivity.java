package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("account", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("emailAddress","");
        String password = prefs.getString("passWord","");
        EditText emailEditText = findViewById(R.id.emailAddress);
        emailEditText.setText(emailAddress);
        EditText passWordET = findViewById(R.id.passWord);
        passWordET.setText(password);


        Button loginBtn = findViewById(R.id.loginBtn);
        Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
        loginBtn.setOnClickListener(click->{
            goToProfile.putExtra("email",emailEditText.getText().toString());
            startActivity(goToProfile);

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("emailAddress",((EditText)findViewById(R.id.emailAddress)).getText().toString());
        edit.putString("passWord",((EditText)findViewById(R.id.passWord)).getText().toString());
        edit.commit();

    }
}