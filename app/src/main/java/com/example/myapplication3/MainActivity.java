package com.example.myapplication3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String id = intent.getStringExtra("id");
            String password = intent.getStringExtra("password");

            TextView textView1 = findViewById(R.id.textView1);
            TextView textView2 = findViewById(R.id.textView2);

            textView1.setText(id);
            textView2.setText(password);
        }
    }

}