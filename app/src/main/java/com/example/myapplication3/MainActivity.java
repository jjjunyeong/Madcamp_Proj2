package com.example.myapplication3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String id;

    FloatingActionButton fbtn_add, fbtn_mine, fbtn_share;
    Animation fromBottom, toBottom, rotateOpen, rotateClose;
    Boolean add_clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        fromBottom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.to_bottom_anim);
        rotateOpen = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_close_anim);

        fbtn_add = findViewById(R.id.fbtn_addm);
        fbtn_mine = findViewById(R.id.fbtn_mine);
        fbtn_share = findViewById(R.id.fbtn_sharem);


        fbtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(add_clicked, v);
                setAnimation(add_clicked);
                add_clicked = !add_clicked;
            }
        });

        fbtn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_menu = new Intent(MainActivity.this, ShareActivity.class);
                startActivity(intent_menu);
            }
        });

        fbtn_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_menu = new Intent(MainActivity.this, MenuActivity.class);
                intent_menu.putExtra("id", id);
                startActivity(intent_menu);
            }
        });

    }

    private void setVisibility(Boolean clicked, View view){
        if(!clicked){
            fbtn_mine.setVisibility(view.VISIBLE);
            fbtn_share.setVisibility(view.VISIBLE);
        }
        else{
            fbtn_mine.setVisibility(view.INVISIBLE);
            fbtn_share.setVisibility(view.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked) {
        if(!clicked){
            fbtn_add.startAnimation(rotateOpen);
            fbtn_mine.startAnimation(fromBottom);
            fbtn_share.startAnimation(fromBottom);
        }
        else{
            fbtn_add.startAnimation(rotateClose);
            fbtn_mine.startAnimation(toBottom);
            fbtn_share.startAnimation(toBottom);
        }
    }

}