package com.example.android.quakereport;


import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.ArrayList;

/**
 * Created by rdas6313 on 25/5/17.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {
    private String url = null;
    EarthquakeLoader(Context context,String url_link){
        super(context);
        url = url_link;
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
       // Log.d("Load Manager","Load in Background Called");
        if(url == null)
            return null;
        ArrayList<Earthquake> earthquakes = queryutils.extractEarthquake(url);
        return earthquakes;
    }

    @Override
    protected void onStartLoading() {
     //   Log.d("Load Manager","On Start Loading Called");
        forceLoad();
    }
}
