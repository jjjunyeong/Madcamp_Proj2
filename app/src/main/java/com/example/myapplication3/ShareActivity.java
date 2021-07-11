package com.example.myapplication3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
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
                    List<GetTravelResult> result = response.body();
                    JourneyGridviewAdapter adapter = new JourneyGridviewAdapter(result, getApplicationContext());
                    gridView.setAdapter(adapter);

                    //on item click, go to MapActivity
                    //show item map, focus on coordinates, ...
                    //do not show add&recording buttons
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long o) {
                            String coordinates = adapter.getItemCoordinates(i);
                            Intent intent = new Intent(ShareActivity.this, MapActivity.class);
                            intent.putExtra("shared", "all");
                            intent.putExtra("coordinates", coordinates);
                            startActivity(intent);
                        }
                    });

                    //on item long-click, "like" the journey
                    gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShareActivity.this);
                            builder.setTitle("Like Journey");
                            builder.setMessage("do you want to like this journey?");
                            builder.setPositiveButton("like",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String a = adapter.getItemJourneyName(i);
                                            String b = adapter.getItemDate(i);
                                            String c = adapter.getItemCompanionIds(i);
                                            String d = adapter.getItemShared(i);
                                            String e = adapter.getItemLike(i);
                                            String f = adapter.getItemCoordinates(i);
                                            String g = adapter.getItemLat(i);
                                            String h = adapter.getItemLng(i);
                                            String j = adapter.getItemLikeIds(i);
                                            String k = adapter.getItemUserId(i);
                                            search_liked_item(a, b, c, d, e, f, g, h, j, k);
                                            dialog.cancel();
                                        }
                                    });
                            builder.setNegativeButton("cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.show();
                            return true;
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

    private void search_liked_item(String journey_name, String date, String cpns, String shared, String like, String coordinates, String lat, String lng, String likeids, String id) {
        HashMap<String, String> map = new HashMap<>();

        map.put("journey_name", journey_name);
        map.put("id", id);
        map.put("likeids", id);

        Call<Void> call = LoginActivity.retrofitInterface.searchSharedTravel(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Already liked this journey", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    like_item(journey_name, date, cpns, shared, like, coordinates, lat, lng, likeids, id);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void like_item(String journey_name, String date, String cpns, String shared, String like, String coordinates, String lat, String lng, String likeids, String id) {
        HashMap<String, List<String>> map = new HashMap<>();

        List<String> find_arraylist1 = new ArrayList<>();
        find_arraylist1.add(journey_name);
        map.put("find_journey_name", find_arraylist1);

        String[] subtokens = cpns.split(", ");
        List<String> find_arraylist2 = new ArrayList<>();
        for(int i=0; i<subtokens.length; i++){
            find_arraylist2.add(subtokens[i]);
        }
        map.put("find_id", find_arraylist2);

        map.put("journey_name", find_arraylist1);
        map.put("id", find_arraylist2);

        List<String> arraylist1 = new ArrayList<>();
        arraylist1.add(date);
        map.put("date", arraylist1);

        List<String> arraylist2 = new ArrayList<>();
        arraylist2.add(shared);
        map.put("shared", arraylist2);

        List<String> arraylist3 = new ArrayList<>();
        int temp = Integer.parseInt(like);
        arraylist3.add(String.valueOf(temp+1));
        map.put("like", arraylist3);

        String[] subtokens2 = coordinates.split(", ");
        List<String> arraylist4 = new ArrayList<>();
        for(int i=0; i<subtokens2.length; i++){
            arraylist4.add(subtokens2[i]);
        }
        map.put("coordinates", arraylist4);

        String[] subtokens3 = lat.split(", ");
        List<String> arraylist5 = new ArrayList<>();
        for(int i=0; i<subtokens3.length; i++){
            arraylist5.add(subtokens3[i]);
        }
        map.put("lat", arraylist5);

        String[] subtokens4 = lng.split(", ");
        List<String> arraylist6 = new ArrayList<>();
        for(int i=0; i<subtokens4.length; i++){
            arraylist6.add(subtokens4[i]);
        }
        map.put("lng", arraylist6);

        String[] subtokens5 = likeids.split(", ");
        List<String> arraylist7 = new ArrayList<>();
        for(int i=0; i<subtokens5.length; i++){
            arraylist7.add(subtokens4[i]);
        }
        if(arraylist7.get(0).equals("null")) {
            arraylist7.remove(0);
            arraylist7.add(id);
        } else{
            arraylist7.add(id);
        }
        map.put("likeids", arraylist7);

        update_journey(map);
    }

    private void update_journey(HashMap<String, List<String>> map){
        Call<Void> call = LoginActivity.retrofitInterface.updateTravel(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    //refresh gridview
                    show_items();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
