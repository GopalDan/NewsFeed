package com.example.gopal.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Event>>, OnSharedPreferenceChangeListener{

   private CustomArrayAdapter mCustomAdapter;
   private ProgressBar mProgressbar;
   private TextView textView;
   private String Url = "https://content.guardianapis.com/search?&api-key=d71aed14-2fe8-42ca-b962-a9c3794f5049&show-fields=thumbnail,byline";
   private int id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressbar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.no_network);
        ListView list = findViewById(R.id.list);

        mCustomAdapter = new CustomArrayAdapter(this,new ArrayList<Event>());
        list.setAdapter(mCustomAdapter);
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nif = cm.getActiveNetworkInfo();
        if(nif!=null && nif.isConnected()) {
            getLoaderManager().initLoader(id, null, this).forceLoad();
        }
        else {
            mProgressbar.setVisibility(View.GONE);
            textView.setText("Oops! No Network");

        }
        // click listener for list item
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Event currentNews = mCustomAdapter.getItem(position);
                Uri webPage = Uri.parse(currentNews.getmWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public Loader<List<Event>> onCreateLoader(int i, Bundle bundle) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

            String categorySelected = sharedPrefs.getString(
                    getString(R.string.set_category_key),
                    getString(R.string.set_category_default_value)
            );

            String countrySelected = sharedPrefs.getString(
                    getString(R.string.set_country_key),
                    getString(R.string.set_country_default_value)
            );
            String date = sharedPrefs.getString(
                    getString(R.string.set_date_key),
                    getString(R.string.set_date_default_value));
            Log.v("MainActivity", "Dated Valued Received " + date);
            // parse breaks apart the URI string that's passed into its parameter
            Uri baseUri = Uri.parse(Url);

            // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
            Uri.Builder uriBuilder = baseUri.buildUpon();

            // Append query parameter and its value. For example, the `format=geojson`
            String pageSize = String.valueOf(10);
            String orderBy = "newest";

            uriBuilder.appendQueryParameter("q", categorySelected);
            uriBuilder.appendQueryParameter("page-size", pageSize);
            //uriBuilder.appendQueryParameter("q", countrySelected);
            uriBuilder.appendQueryParameter("order-by", orderBy);

            //uriBuilder.appendQueryParameter("from-date", date);


            // Return the completed uri `http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=minMagnitude&orderby=time


        return new CustomLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> newsList) {
        mProgressbar.setVisibility(View.GONE);

        if (newsList!=null && !newsList.isEmpty()){
            mCustomAdapter.addAll(newsList);
        }
        else{
            textView.setText("No news to show ");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
        mCustomAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main,menu);
       return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.setting){
            Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        getLoaderManager().restartLoader(id,null,this);
    }


}
