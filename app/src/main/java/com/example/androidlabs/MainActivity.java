package com.example.androidlabs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener( v->
                Toast.makeText(MainActivity.this,R.string.toast_msg,Toast.LENGTH_LONG ).show());

        CheckBox checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener( (buttonView, isChecked)->{
            Snackbar.make(btn,R.string.snack_msg,Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.snack_action_msg,v->checkBox.setChecked(!isChecked))
                    .show();
        });
    }
}