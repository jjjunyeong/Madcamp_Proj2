package com.example.myapplication3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        gridView = findViewById(R.id.gridView);

        //show items on gridview
        show_items();

        //on button("NEW JOURNEY") click, add new journey
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_journey();
            }
        });
    }


    //get information from DB, show items on gridview
    //if there are at least one journey user participated, show on gridview
    //if user did not participate in any journey, show Toast message "Let's start our first journey"
    //on grid item click, go to MapActivity
    //on grid item long-click, show dialog to modify/delete item info
    private void show_items(){
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);

        Call<List<GetTravelResult>> call = LoginActivity.retrofitInterface.getTravel(map);

        call.enqueue(new Callback<List<GetTravelResult>>() {
            @Override
            public void onResponse(Call<List<GetTravelResult>> call, Response<List<GetTravelResult>> response) {
                if (response.code() == 200) {
                    List<GetTravelResult> result = response.body();

                    MyJourneyGridviewAdapter adapter = new MyJourneyGridviewAdapter(result, getApplicationContext());
                    gridView.setAdapter(adapter);

                    //on item click, go to MapActivity
                    //show item map, focus on coordinates, ...
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long o) {
                            String coordinates = adapter.getItemCoordinates(i);
                            String id = adapter.getItemUserId(i);
                            Intent intent = new Intent(MenuActivity.this, MapActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("coordinates", coordinates);
                            startActivity(intent);
                        }
                    });

                    //on item long-click, show new dialog
                    //can modify or delete item info
                    gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                            String shared = adapter.getItemShared(i);
                            builder.setTitle("Modify/Delete Journey");
                            builder.setMessage("what do you want to do with this journey?");
                            builder.setPositiveButton("delete",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String a = adapter.getItemJourneyName(i);
                                            String b = adapter.getItemUserId(i);
                                            delete_item(a, b);
                                            dialog.cancel();
                                        }
                                    });
                            builder.setNegativeButton("modify",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String a = adapter.getItemJourneyName(i);
                                            String b = adapter.getItemUserId(i);
                                            String c = adapter.getItemDate(i);
                                            String d = adapter.getItemCompanionIds(i);
                                            modify_item(a, b, c, d);
                                            //dialog.cancel();
                                        }
                                    });
                            if(shared.equals("false")){
                                builder.setNeutralButton("share",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                String a = adapter.getItemJourneyName(i);
                                                String b = adapter.getItemDate(i);
                                                String c = adapter.getItemCompanionIds(i);
                                                String d = adapter.getItemCoordinates(i);
                                                upload_share_item(a, b, c, d, false);
                                                dialog.cancel();
                                            }
                                        });
                            } else {
                                builder.setNeutralButton("unshare",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                String a = adapter.getItemJourneyName(i);
                                                String b = adapter.getItemDate(i);
                                                String c = adapter.getItemCompanionIds(i);
                                                String d = adapter.getItemCoordinates(i);
                                                upload_share_item(a, b, c, d, true);
                                                dialog.cancel();
                                            }
                                        });
                            }
                            builder.show();
                            return true;
                        }
                    });
                } else if (response.code() == 404) {
                    Toast.makeText(getApplicationContext(), "Let's start our first journey!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<GetTravelResult>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void upload_share_item(String share_journey_name, String share_date, String share_cpns, String share_coordinates, Boolean shared){
        HashMap<String, List<String>> map = new HashMap<>();

        List<String> arraylist1 = new ArrayList<>();
        arraylist1.add(share_journey_name);
        map.put("find_journey_name", arraylist1);

        List<String> arraylist2 = new ArrayList<>();
        String[] tokens = share_cpns.split(", ");
        for(int i=0; i<tokens.length; i++){
            arraylist2.add(tokens[i]);
        }
        map.put("find_id", arraylist2);

        map.put("journey_name", arraylist1);
        map.put("id", arraylist2);

        List<String> arraylist3 = new ArrayList<>();
        if(shared) { arraylist3.add("false"); }
        else { arraylist3.add("true"); }
        map.put("shared", arraylist3);

        List<String> arraylist4 = new ArrayList<>();
        arraylist4.add(share_date);
        map.put("date", arraylist4);

        Call<Void> call = LoginActivity.retrofitInterface.updateTravel(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "your journey is shared", Toast.LENGTH_SHORT).show();
                    show_items();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    //find and delete item in DB
    //find document with journey_name "journey_name", and id "id"
    //***if A goes to 2 journeys with same name, problem occurs***
    //delete that document from Travel collection
    private void delete_item(String journey_name, String id){
        HashMap<String, String> map = new HashMap<>();
        map.put("journey_name", journey_name);
        map.put("id", id);

        Call<Void> call = LoginActivity.retrofitInterface.removeTravel(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(),"deleted journey",Toast.LENGTH_LONG).show();
                    //refresh gridview
                    show_items();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //find and modify item in DB
    //show dialog with original items
    //let user modify items
    //update_journey()
    //find document with original journey_name and original ids(companions)
    //modify document with new input items
    private void modify_item(String org_journey_name, String org_id, String org_date, String org_cpns){
        HashMap<String, List<String>> map = new HashMap<>();

        List<String> find_arraylist1 = new ArrayList<>();
        find_arraylist1.add(org_journey_name);
        map.put("find_journey_name", find_arraylist1);
        Log.d("Modify", "find_journey_name: " + org_journey_name);

        String org_cpn1;
        String org_cpn2;
        String org_cpn3;

        String[] tokens = new String[4];
        String[] subtokens = org_cpns.split(", ");
        for(int i = 0; i<3; i++){
            if(i >= subtokens.length){
                tokens[i] = "";
            } else {
                tokens[i] = subtokens[i];
            }
        }
        if(tokens[0].equals(org_id)){
            org_cpn1 = tokens[1];
            org_cpn2 = tokens[2];
            org_cpn3 = tokens[3];
        } else if(tokens[1].equals(org_id)){
            org_cpn1 = tokens[0];
            org_cpn2 = tokens[2];
            org_cpn3 = tokens[3];
        } else if(tokens[2].equals(org_id)){
            org_cpn1 = tokens[0];
            org_cpn2 = tokens[1];
            org_cpn3 = tokens[3];
        } else{
            org_cpn1 = tokens[0];
            org_cpn2 = tokens[1];
            org_cpn3 = tokens[2];
        }

        List<String> find_arraylist2 = new ArrayList<>();
        for(int i=0; i<subtokens.length; i++){
            find_arraylist2.add(subtokens[i]);
        }
        //find_arraylist2.add(org_id);
        map.put("find_id", find_arraylist2);
        //Log.d("Modify", "find_id: " + org_id);

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

        et_jny_name.setText(org_journey_name);
        date.setText(org_date);
        cpn1.setText(org_cpn1);
        cpn2.setText(org_cpn2);
        cpn3.setText(org_cpn3);

        Log.d("Modify", org_journey_name + " / " + org_date + " / " + org_cpn1 + " / " + org_cpn2 + " / " + org_cpn3);

        final AlertDialog dialog = builder.create();
        dialog.show();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showDatePicker(v);}
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_jny_name = et_jny_name.getText().toString();
                s_date = date.getText().toString();
                s_cpn1 = cpn1.getText().toString();
                s_cpn2 = cpn2.getText().toString();
                s_cpn3 = cpn3.getText().toString();

                Log.d("Modify", "s_cpn1 / s_cpn2 / s_cpn3: " + s_cpn1 + " / " + s_cpn2 + " / " + s_cpn3);


                //journey name should be submitted
                if(!s_jny_name.replace(" ", "").equals("")){
                    List<String> arraylist1 = new ArrayList<>();
                    arraylist1.add(s_jny_name);
                    map.put("journey_name", arraylist1);
                    Log.d("Modify", "journey_name: " + s_jny_name);
                    check1.setVisibility(v.INVISIBLE);
                } else { check1.setVisibility(v.VISIBLE); }

                //date should be submitted
                if(!s_date.equals("")){
                    List<String> arraylist2 = new ArrayList<>();
                    arraylist2.add(s_date);
                    map.put("date", arraylist2);
                    Log.d("Modify", "date: " + s_date);
                    check2.setVisibility(v.INVISIBLE);
                } else { check2.setVisibility(v.VISIBLE); }

                //user is free to submit companion names or not
                //companion id should exist in DB
                //companion id should not be user id
                if(!s_cpn1.replace(" ", "").equals("")){ check_user(s_cpn1, check3, v); }
                else { check3.setVisibility(v.INVISIBLE); }

                if(!s_cpn2.replace(" ", "").equals("")){ check_user(s_cpn2, check4, v); }
                else { check4.setVisibility(v.INVISIBLE); }

                if(!s_cpn3.replace(" ", "").equals("")){ check_user(s_cpn3, check5, v); }
                else { check5.setVisibility(v.INVISIBLE); }


                //after all inputs are verified, put information into Hashmap, upload on DB
                if(check1.getVisibility() == v.INVISIBLE && check2.getVisibility() == v.INVISIBLE && check3.getVisibility() == v.INVISIBLE && check4.getVisibility() == v.INVISIBLE && check5.getVisibility() == v.INVISIBLE) {
                    List<String> arraylist3 = new ArrayList<>();
                    arraylist3.add(id);
                    if(!s_cpn1.replace(" ", "").equals("")){ arraylist3.add(s_cpn1); }
                    if(!s_cpn2.replace(" ", "").equals("")){ arraylist3.add(s_cpn2); }
                    if(!s_cpn3.replace(" ", "").equals("")){ arraylist3.add(s_cpn3); }
                    map.put("id", arraylist3);

                    //update on database
                    update_journey(map);

                    //close dialog
                    dialog.dismiss();
                }
            }
        });
        Toast.makeText(getApplicationContext(),"modified journey",Toast.LENGTH_LONG).show();

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


    //show dialog to get information from user
    //check if user submitted journey name and date
    //check if companion ids exist in DB, check if companion ids are not user id -> check_user
    //upload data on DB (coordinates are initialized to KAIST coordination) -> upload_journey
    private void add_journey(){
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
            public void onClick(View v) { showDatePicker(v);}
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_jny_name = et_jny_name.getText().toString();
                s_cpn1 = cpn1.getText().toString();
                s_cpn2 = cpn2.getText().toString();
                s_cpn3 = cpn3.getText().toString();

                //journey name should be submitted
                if(s_jny_name.replace(" ", "").equals("")){ check1.setVisibility(v.VISIBLE); }
                else { check1.setVisibility(v.INVISIBLE); }

                //date should be submitted
                if(s_date == null){ check2.setVisibility(v.VISIBLE); }
                else { check2.setVisibility(v.INVISIBLE); }

                //user is free to submit companion names or not
                //companion id should exist in DB
                //companion id should not be user id

                if(!s_cpn1.replace(" ", "").equals("")){ check_user(s_cpn1, check3, v); }
                else { check3.setVisibility(v.INVISIBLE); }

                if(!s_cpn2.replace(" ", "").equals("")){ check_user(s_cpn2, check4, v); }
                else { check4.setVisibility(v.INVISIBLE); }

                if(!s_cpn3.replace(" ", "").equals("")){ check_user(s_cpn3, check5, v); }
                else { check5.setVisibility(v.INVISIBLE); }

                //after all inputs are verified, put information into Hashmap, upload on DB
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
                    if(!s_cpn1.replace(" ", "").equals("")){ arraylist3.add(s_cpn1); }
                    if(!s_cpn2.replace(" ", "").equals("")){ arraylist3.add(s_cpn2); }
                    if(!s_cpn3.replace(" ", "").equals("")){ arraylist3.add(s_cpn3); }
                    map.put("id", arraylist3);

                    List<String> arraylist4 = new ArrayList<>();
                    arraylist4.add(36.3721+"");
                    arraylist4.add(127.3604+"");
                    map.put("coordinates", arraylist4);

                    List<String> arraylist5 = new ArrayList<>();
                    arraylist5.add("false");
                    map.put("shared", arraylist5);

                    //should also provide default value of route later

                    //upload on database
                    upload_journey(map);

                    //dismiss dialog
                    dialog.dismiss();
                }
            }
        });
    }

    public void upload_journey(HashMap<String, List<String>> map){
        Call<Void> call = LoginActivity.retrofitInterface.addTravel(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "upload success", Toast.LENGTH_SHORT).show();
                    show_items();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //check DB if user(companion id) exists in DB
    public void check_user(String user, ImageView imageView, View view){
        if(user.equals(id)){
            imageView.setVisibility(view.VISIBLE);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("id", user);

        Call<Void> call = LoginActivity.retrofitInterface.checkUser(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) { imageView.setVisibility(view.INVISIBLE); }
                else if (response.code() == 400) { imageView.setVisibility(view.VISIBLE); }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //helper function for date selection
    private void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    //helper function for date selection
    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        s_date = (year_string + "/" + month_string + "/" + day_string);
        date.setText(s_date);
    }

    //gridview adapter
    public class MyJourneyGridviewAdapter extends BaseAdapter{
        private Context context;
        private List<GetTravelResult> travelresults;

        public MyJourneyGridviewAdapter(List<GetTravelResult> travelresults, Context context){
            this.travelresults = travelresults;
            this.context = context;
        }

        public String getItemJourneyName(int position) {
            return travelresults.get(position).getJourneyName();
        }

        public String getItemDate(int position) {
            return travelresults.get(position).getDate();
        }

        public String getItemCompanionIds(int position) {
            return travelresults.get(position).getCompanions();
        }

        public String getItemUserId(int position) {
            return travelresults.get(position).getId();
        }

        public String getItemCoordinates(int position){
            return travelresults.get(position).getCoordinates();
        }

        public String getItemShared(int position){
            return travelresults.get(position).getShared();
        }

        @Override
        public int getCount() {
            return travelresults.size();
        }

        @Override
        public Object getItem(int position) {
            return travelresults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.griditem_journey, viewGroup, false);

            TextView gv_jny_name = view.findViewById(R.id.gv_jny_name);
            TextView gv_date = view.findViewById(R.id.gv_date);
            TextView gv_cpns = view.findViewById(R.id.gv_cpns);

            final GetTravelResult thisTravelResult = travelresults.get(i);

            String s_jny_name = thisTravelResult.getJourneyName();
            String s_date = thisTravelResult.getDate();
            String s_cpns = thisTravelResult.getCompanions();
            String id = thisTravelResult.getId();
            String shared = thisTravelResult.getShared();

            gv_jny_name.setText(s_jny_name);
            gv_date.setText(s_date);
            gv_cpns.setText(s_cpns);

            return view;


        }

    }

}
