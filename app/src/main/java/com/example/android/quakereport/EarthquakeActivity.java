/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;

import static com.example.android.quakereport.R.string.settings_min_magnitude_key;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {

    //public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String EARTHQUAKE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private ListView earthquakeListView = null;
    private CutomAdapter adapter = null;
    private TextView NodataFoundView = null;
    private View progressbarView = null;
    private  int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
    /*
        earthquakes.add(new Earthquake("Feb 2,2016","7.2","San Francisco"));
        earthquakes.add(new Earthquake("Feb 3,2016","5.1","London"));
        earthquakes.add(new Earthquake("Feb 2,2016","3.0","Tokyo"));
        earthquakes.add(new Earthquake("Feb 3,2016","5.5","Mexico City"));
        earthquakes.add(new Earthquake("Feb 4,2016","6.9","Moscow"));
        earthquakes.add(new Earthquake("Feb 5,2016","4.5","Rio de Janeiro"));
        earthquakes.add(new Earthquake("Feb 1,2016","3.5","Paris"));
    */

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);

        //progress Bar View
        progressbarView = (ProgressBar)findViewById(R.id.progress);

        NodataFoundView = (TextView)findViewById(R.id.dataNotFound);
        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new CutomAdapter(EarthquakeActivity.this,new ArrayList<Earthquake>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        //if custom adapter is empty then NodataFound view will be Active
        earthquakeListView.setEmptyView(NodataFoundView);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake obj = (Earthquake) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(obj.getUrl()));
                startActivity(intent);
            }
        });
        //getting connetivity manager ref from System service to know if there is an active network
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            //start the loader with loader manager
            getLoaderManager().initLoader(1/*Loader id*/, null/*Additional information*/, this/* an object that should receive the result when load will finish. this is the Activity object*/);
            //Log.d("Load Manager","LOader manager init the loader");
        }else{
            //setting progress bar visibility gone
            progressbarView.setVisibility(View.GONE);
            NodataFoundView.setText(R.string.no_internet);
        }
    }

    @Override
    public android.content.Loader<ArrayList<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        /*SharedPreferences is like Map in c++.it store the preference value with a key in a XML file */
        SharedPreferences sharedobject = PreferenceManager.getDefaultSharedPreferences(this);//getDefaultSharedPreferences gives you instance of SharedPreference of default xml file.
        String min_magnitude = sharedobject.getString(getString(R.string.settings_min_magnitude_key)/*Key of SharedPreference*/,getString(R.string.settings_min_magnitude_default)/*Default value*/);
        String orderByValue = sharedobject.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));
        String limitValue = sharedobject.getString(getString(R.string.settings_limit_key),getString(R.string.settings_limit_default));
        //Log.d("OnCreateLoader","CALLED");
        /* URI is nothing its resource locator.
         * URI can identify a much broader range of things, from files and email mailboxes, to physical objects like books.
         * URL is a subset of URI
        */
        //uri.parse creating a new Uri object from url string.
        Uri uri = Uri.parse(EARTHQUAKE_URL);
        /* Uri.Builder let you add your query parameter to your url.
         * uri.buildupon Constructs a new builder, copying the attributes from this Uri.
         */
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("limit",limitValue);
        builder.appendQueryParameter("minmag",min_magnitude);
        builder.appendQueryParameter("orderby",orderByValue);

        return new EarthquakeLoader(this,builder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes) {
        //Log.d("Load Manager","On Load Finished has Called");

        //setting progress bar visibility gone
        progressbarView.setVisibility(View.GONE);
        NodataFoundView.setText(R.string.no_Edata_found);
        if(earthquakes == null) {
            return;
        }
        // set Earthquake data to adapter
        adapter.setData(earthquakes);

    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<Earthquake>> loader) {
        //Log.d("Load Manager","On Load Reset Has called");
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings_menu:
                Intent i = new Intent(this,SettingActivity.class);
                startActivityForResult(i,REQUEST_CODE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data.getBooleanExtra("needToLoad",false)){
               // Log.d("on Activity Result -","Called");
                getLoaderManager().restartLoader(1,null,this);
                progressbarView.setVisibility(View.VISIBLE);
            }
        }
    }
}
