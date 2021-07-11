package com.example.myapplication3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Intent intent = getIntent();

        gridView = findViewById(R.id.gridView);

        //show items on gridview
        show_items();
    }

    private void show_items(){
        HashMap<String, String> map = new HashMap<>();
        map.put("shared", "true");

        Call<List<GetTravelResult>> call = LoginActivity.retrofitInterface.getSharedTravel(map);

        call.enqueue(new Callback<List<GetTravelResult>>() {
            @Override
            public void onResponse(Call<List<GetTravelResult>> call, Response<List<GetTravelResult>> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "download shared items", Toast.LENGTH_SHORT).show();
                    List<GetTravelResult> result = response.body();
                    JourneyGridviewAdapter adapter = new JourneyGridviewAdapter(result, getApplicationContext());
                    gridView.setAdapter(adapter);

                    //on item click, go to MapActivity
                    //show item map, focus on coordinates, ...
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long o) {
                            String coordinates = adapter.getItemCoordinates(i);
                            String id = adapter.getItemUserId(i);
                            Intent intent = new Intent(ShareActivity.this, MapActivity.class);
                            intent.putExtra("shared", "all");
                            intent.putExtra("coordinates", coordinates);
                            startActivity(intent);
                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<List<GetTravelResult>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



}
