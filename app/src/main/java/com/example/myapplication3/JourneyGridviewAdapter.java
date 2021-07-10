package com.example.myapplication3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JourneyGridviewAdapter extends BaseAdapter {
    private Context context;
    private List<TravelResult> travelresults;
    private Activity parentActivity;

    public JourneyGridviewAdapter(List<TravelResult> travelresults, Context context, Activity parentActivity){
        this.travelresults = travelresults;
        this.context = context;
        this.parentActivity = parentActivity;
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

        final TravelResult thisTravelResult = travelresults.get(i);

        String s_jny_name = thisTravelResult.getJourneyName();
        String s_date = thisTravelResult.getDate();
        String s_cpns = thisTravelResult.getCompanions();
        String id = thisTravelResult.getId();

        gv_jny_name.setText(s_jny_name);
        gv_date.setText(s_date);
        gv_cpns.setText(s_cpns);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), thisTravelResult.getCoordinates(), Toast.LENGTH_SHORT).show();

                //to MapActivity
//                Intent intent = new Intent(context, MapActivity.class);
//                intent.putExtra("coordinates", thisTravelResult.getCoordinates());
//                context.startActivity(intent);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context.getApplicationContext(), thisTravelResult.getId(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setTitle("Delete Journey");
                builder.setMessage("Will you delete this journey?");
                builder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,"Chose yes",Toast.LENGTH_LONG).show();

                                //delete item in DB(jny_name is s_jny_name, cpns is s_cpns)
                                HashMap<String, String> map = new HashMap<>();
                                map.put("journey_name", s_jny_name);
                                map.put("id", id);

                                Call<Void> call = LoginActivity.retrofitInterface.removeTravel(map);

                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.code() == 200) {
                                            Toast.makeText(context, "remove success",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(context, t.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });
                builder.setNegativeButton("no",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,"Chose no",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();
                return true;

            }
        });


        return view;
    }
}
