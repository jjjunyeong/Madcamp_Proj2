package com.example.myapplication3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class JourneyGridviewAdapter extends BaseAdapter {
    Context context;
    List<TravelResult> travelresults;

    public JourneyGridviewAdapter(List<TravelResult> travelresults, Context context){
        this.travelresults = travelresults;
        this.context = context;
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
        gv_jny_name.setText(thisTravelResult.getJourneyName());
        gv_date.setText(thisTravelResult.getDate());
        gv_cpns.setText(thisTravelResult.getCompanions());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), thisTravelResult.getJourneyName(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
