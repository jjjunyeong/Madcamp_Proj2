package com.example.myapplication3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TravelResult {

    @SerializedName("journey_name")
    @Expose
    private List<String> journey_name;
    @SerializedName("date")
    @Expose
    private List<String> date;
    @SerializedName("id")
    @Expose
    private List<String> id;


    public TravelResult(List<String> journey_name, List<String> date, List<String> id){
        this.journey_name = journey_name;
        this.date = date;
        this.id = id;
    }

    public String getJourneyName() {
        return journey_name.get(0);
    }

    public String getDate() {

        return date.get(0);
    }

    public String getCompanions() {
        String ret = id.get(0);
        for(int i=1; i<id.size(); i++){
            ret = ret + ", " + id.get(i);
        }
        return ret;
    }

}