package com.example.myapplication3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.time.chrono.JapaneseDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private Button btn_add, btn_submit;
    private EditText et_jny_name, date, cpn1, cpn2, cpn3;
    private String id, s_jny_name, s_date, s_cpn1, s_cpn2, s_cpn3;
    private ImageView check1, check2, check3, check4, check5;
    private GridView gridView;
    private List<Journey> items;
    //boolean check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        gridView = findViewById(R.id.gridView);

        add_items();

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJourney();
            }
        });
    }


    private void add_items(){
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);

        Call<List<TravelResult>> call = LoginActivity.retrofitInterface.getTravel(map);

        call.enqueue(new Callback<List<TravelResult>>() {
            @Override
            public void onResponse(Call<List<TravelResult>> call, Response<List<TravelResult>> response) {
                if (response.code() == 200) {
                    List<TravelResult> result = response.body();

                    JourneyGridviewAdapter adapter = new JourneyGridviewAdapter(result, getApplicationContext(), MenuActivity.this);
                    gridView.setAdapter(adapter);

                } else if (response.code() == 404) {
                    Toast.makeText(getApplicationContext(), "Let's start our first journey!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<TravelResult>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public class Journey {
        String journey_name;
        String date;
        String companions;

        Journey(String journey_name, String date, String companions){
            this.journey_name = journey_name;
            this.date = date;
            this.companions = companions;
        }
    }

    private void addJourney(){
        View view = getLayoutInflater().inflate(R.layout.add_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view);

        et_jny_name = (EditText) view.findViewById(R.id.et_jny_name);
        date = (EditText) view.findViewById(R.id.et_date);
        cpn1 = (EditText) view.findViewById(R.id.et_cpn1);
        cpn2 = (EditText) view.findViewById(R.id.et_cpn2);
        cpn3 = (EditText) view.findViewById(R.id.et_cpn3);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        check1 = (ImageView) view.findViewById(R.id.img_check1);
        check2 = (ImageView) view.findViewById(R.id.img_check2);
        check3 = (ImageView) view.findViewById(R.id.img_check3);
        check4 = (ImageView) view.findViewById(R.id.img_check4);
        check5 = (ImageView) view.findViewById(R.id.img_check5);

        final AlertDialog dialog = builder.create();
        dialog.show();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_jny_name = et_jny_name.getText().toString();
                s_cpn1 = cpn1.getText().toString();
                s_cpn2 = cpn2.getText().toString();
                s_cpn3 = cpn3.getText().toString();
//                Toast.makeText(getApplicationContext(), s_jny_name, Toast.LENGTH_SHORT).show();
                //check = true;
                if(s_jny_name.replace(" ", "").equals("")){
                    //Toast.makeText(getApplicationContext(), "Need journey name", Toast.LENGTH_SHORT).show();
                    check1.setVisibility(v.VISIBLE);
                } else {
                    check1.setVisibility(v.INVISIBLE);
                }

                if(s_date == null){
                    //Toast.makeText(getApplicationContext(), "Need date", Toast.LENGTH_SHORT).show();
                    check2.setVisibility(v.VISIBLE);
                } else {
                    check2.setVisibility(v.INVISIBLE);
                }

                if(!s_cpn1.replace(" ", "").equals("")){
                    //check database to see if s_cpn1 id exists
                    check_user(s_cpn1, check3, v);
                } else {
                    check3.setVisibility(v.INVISIBLE);
                }

                if(!s_cpn2.replace(" ", "").equals("")){
                    //check database to see if s_cpn2 id exists
                    check_user(s_cpn2, check4, v);
                } else {
                    check4.setVisibility(v.INVISIBLE);
                }

                if(!s_cpn3.replace(" ", "").equals("")){
                    //check database to see if s_cpn3 id exists
                    check_user(s_cpn3, check5, v);
                } else {
                    check5.setVisibility(v.INVISIBLE);
                }

                if(check1.getVisibility() == v.INVISIBLE && check2.getVisibility() == v.INVISIBLE && check3.getVisibility() == v.INVISIBLE && check4.getVisibility() == v.INVISIBLE && check5.getVisibility() == v.INVISIBLE) {
                    HashMap<String, List<String>> map = new HashMap<String, List<String>>();

                    List<String> arraylist1 = new ArrayList<>();
                    arraylist1.add(s_jny_name);
                    map.put("journey_name", arraylist1);

                    List<String> arraylist2 = new ArrayList<>();
                    arraylist2.add(s_date);
                    map.put("date", arraylist2);

                    List<String> arraylist3 = new ArrayList<>();
                    arraylist3.add(id);
                    if(!s_cpn1.replace(" ", "").equals("")){
                        arraylist3.add(s_cpn1);
                    }
                    if(!s_cpn2.replace(" ", "").equals("")){
                        arraylist3.add(s_cpn2);
                    }
                    if(!s_cpn3.replace(" ", "").equals("")){
                        arraylist3.add(s_cpn3);
                    }
                    map.put("id", arraylist3);

                    List<String> arraylist4 = new ArrayList<>();
                    arraylist4.add(36.3721+"");
                    arraylist4.add(127.3604+"");
                    map.put("coordinates", arraylist4);

                    //should also provide default value of route later

                    //upload on database
                    add_travel(map);
                    dialog.dismiss();

                    add_items();
                }
            }
        });
    }

    public void add_travel(HashMap<String, List<String>> map){
        Call<Void> call = LoginActivity.retrofitInterface.addTravel(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Upload success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void check_user(String user, ImageView imageView, View view){
        if(user.equals(id)){
            imageView.setVisibility(view.VISIBLE);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("id", user);
        Log.d("checkUser", user);

        Call<Void> call = LoginActivity.retrofitInterface.checkUser(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.d("checkUser", response.code()+"");

                if (response.code() == 200) {
                    imageView.setVisibility(view.INVISIBLE);
                    Log.d("checkUser", "invisible");
                    //Toast.makeText(getApplicationContext(), "User exist", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 400) {
                    imageView.setVisibility(view.VISIBLE);
                    Log.d("checkUser", "visible");
                    //Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        s_date = (year_string + "/" + month_string + "/" + day_string);
        date.setText(s_date);
    }
}
