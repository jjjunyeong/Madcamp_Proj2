package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapActivity extends AppCompatActivity {

    TextView textView;
    String coordinates;
    String id;

    FloatingActionButton fbtn_add, fbtn_flag, fbtn_chat, fbtn_share, fbtn_recording;
    Animation fromBottom, toBottom, rotateOpen, rotateClose;
    Boolean add_clicked = false;
    Boolean start_clicked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("aa", "aa");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        textView = findViewById(R.id.textView2);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        coordinates = intent.getExtras().getString("coordinates");
        textView.setText(coordinates);


        fromBottom = AnimationUtils.loadAnimation(MapActivity.this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(MapActivity.this, R.anim.to_bottom_anim);
        rotateOpen = AnimationUtils.loadAnimation(MapActivity.this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(MapActivity.this, R.anim.rotate_close_anim);

        fbtn_add = findViewById(R.id.fbtn_add);
        fbtn_chat = findViewById(R.id.fbtn_chat);
        fbtn_flag = findViewById(R.id.fbtn_flag);
        fbtn_share = findViewById(R.id.fbtn_share);
        fbtn_recording = findViewById(R.id.fbtn_start);

        fbtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(add_clicked, v);
                setAnimation(add_clicked);
                add_clicked = !add_clicked;
            }
        });

        fbtn_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start_clicked){
                    start_clicked = false;
                    fbtn_recording.setImageResource(R.drawable.ic_start);
                } else {
                    start_clicked = true;
                    fbtn_recording.setImageResource(R.drawable.ic_stop);
                }
            }
        });

    }

    private void setVisibility(Boolean clicked, View view){
        if(!clicked){
            fbtn_chat.setVisibility(view.VISIBLE);
            fbtn_flag.setVisibility(view.VISIBLE);
            fbtn_share.setVisibility(view.VISIBLE);
        }
        else{
            fbtn_chat.setVisibility(view.INVISIBLE);
            fbtn_flag.setVisibility(view.INVISIBLE);
            fbtn_share.setVisibility(view.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked) {
        if(!clicked){
            fbtn_add.startAnimation(rotateOpen);
            fbtn_chat.startAnimation(fromBottom);
            fbtn_flag.startAnimation(fromBottom);
            fbtn_share.startAnimation(fromBottom);
        }
        else{
            fbtn_add.startAnimation(rotateClose);
            fbtn_chat.startAnimation(toBottom);
            fbtn_flag.startAnimation(toBottom);
            fbtn_share.startAnimation(toBottom);
        }
    }

}
