package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageButton imageButton;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(ACTIVITY_NAME, "In function: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageButton = findViewById(R.id.takePictureBtn);
        imageButton.setOnClickListener(click -> this.dispatchTakePictureIntent());

        Intent fromMain = getIntent();
        EditText email = findViewById(R.id.email);
        String emailAddress = fromMain.getStringExtra("email");
        email.setText(emailAddress);

        Intent gotoChatIntent = new Intent(ProfileActivity.this, ChatRoomActivity.class);
        Button goToChatBtn = findViewById(R.id.go_to_chat_btn);
        goToChatBtn.setOnClickListener(click -> {
            startActivity(gotoChatIntent);
        });

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(ACTIVITY_NAME, "In function: onActivityResult()");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function: onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function: onStop()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function: onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function: onDestroy()");
    }
}