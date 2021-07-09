package com.example.myapplication3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private Button btn_add, btn_submit;
    private EditText et_jny_name, date, cpn1, cpn2, cpn3;
    private String id, s_jny_name, s_date, s_cpn1, s_cpn2, s_cpn3;
    private ImageView check1, check2, check3, check4, check5;
    //boolean check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJourney();
            }
        });

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
                    check_user(s_cpn1, check3, v);
                } else {
                    check3.setVisibility(v.INVISIBLE);
                }

                if(!s_cpn2.replace(" ", "").equals("")){
                    check_user(s_cpn2, check4, v);
                } else {
                    check4.setVisibility(v.INVISIBLE);
                }

                if(!s_cpn3.replace(" ", "").equals("")){
                    check_user(s_cpn3, check5, v);
                } else {
                    check5.setVisibility(v.INVISIBLE);
                }

                if(check1.getVisibility() == v.INVISIBLE && check2.getVisibility() == v.INVISIBLE && check3.getVisibility() == v.INVISIBLE && check4.getVisibility() == v.INVISIBLE && check5.getVisibility() == v.INVISIBLE) {
                    Toast.makeText(getApplicationContext(), s_jny_name + ", " + s_date + ", " + s_cpn1 + ", " + s_cpn2 + ", " + s_cpn3, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

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
                    //check = false;
                    Log.d("checkUser", "visible");
                    //Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //Toast.makeText(getApplicationContext(), result+"", Toast.LENGTH_SHORT).show();
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
//        Toast.makeText(getApplicationContext(),"Date: "+dateMessage,Toast.LENGTH_SHORT).show();
    }
}
