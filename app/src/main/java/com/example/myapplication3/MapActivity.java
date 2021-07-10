package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    TextView textView;
    String coordinates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("aa", "aa");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        textView = findViewById(R.id.textView2);

        Intent intent = getIntent();
        coordinates = intent.getExtras().getString("coordinates");
        textView.setText(coordinates);

    }
}
