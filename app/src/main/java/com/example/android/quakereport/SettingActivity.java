package com.example.android.quakereport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


public class SettingActivity extends AppCompatActivity{
    public static Boolean needToLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        FragmentPreference obj = new FragmentPreference();
        android.app.FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.setting_activity,obj,"FragmentPreference").commit();
        needToLoad = false;
    }


    /*
     * if you want to go back to parent activity from child activity you have to add meta tag to manifest file then
     * you have to override the onOptionItemSelected method and have to put the code into it. otherwise the back arrow
     * animation wont work.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class FragmentPreference extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        private Boolean verify;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference magPref = findPreference(getString(R.string.settings_min_magnitude_key));
            bindSummaryToValue(magPref);
            Preference orderByPref = findPreference(getString(R.string.settings_order_by_key));
            bindSummaryToValue(orderByPref);
            Preference limitPref = findPreference(getString(R.string.settings_limit_key));
            bindSummaryToValue(limitPref);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            needToLoad = true;
            if(verify){
                verify = false;
                needToLoad = false;
            }
            String sample = o.toString();
            if(preference instanceof ListPreference){
                CharSequence entries[] = ((ListPreference) preference).getEntries();
                int index = ((ListPreference) preference).findIndexOfValue(sample);
                if(index>=0){
                    preference.setSummary(entries[index]);
                }
            }else{
                int data = Integer.parseInt(sample);
                if(data < 0 || data > 10)
                    return false;
                preference.setSummary(sample);
            }
            return true;
        }

        private void bindSummaryToValue(Preference preference){
            verify = true;
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String min_mag = sharedPreferences.getString(preference.getKey(),getString(R.string.settings_min_magnitude_default));
            onPreferenceChange(preference,min_mag);
        }
    }

    @Override
    public void finish() {
     //   Log.e("Finish Call",""+needToLoad);
        Intent i = new Intent();
        i.putExtra("needToLoad",needToLoad);
        setResult(RESULT_OK,i);
        super.finish();
    }
}
