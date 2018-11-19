package com.example.gopal.newsfeed;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.gopal.newsfeed.MainActivity.*;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
    }

    @Override public void onBackPressed()
    { super.onBackPressed();
    NavUtils.navigateUpFromSameTask(this);
    }

    public static class CustomPreferenceFragment extends PreferenceFragment
    implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
            //Updating category preference when app first time launches
            Preference category = findPreference(getString(R.string.set_category_key));
            bindPreferenceSummaryToValue(category);
            //Updating category preference when app first time launches
            Preference country = findPreference(getString(R.string.set_country_key));
            bindPreferenceSummaryToValue(country);
            //Updating date preference when app launches first time
           // Preference date = findPreference(getString(R.string.set_date_key));
           // bindPreferenceSummaryToValue(date);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if(preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference)preference;
                int index = listPreference.findIndexOfValue(stringValue);
                if(index>=0){
                    CharSequence[] labels = listPreference.getEntries();
                    listPreference.setSummary(labels[index]);
                }
                else{
                    listPreference.setSummary(stringValue);
                }
            }
           // finish();
          //  getLoaderManager().restartLoader(1,null,this);
            return true;
        }
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(),"");
            onPreferenceChange(preference, preferenceString);
        }



    }
}

