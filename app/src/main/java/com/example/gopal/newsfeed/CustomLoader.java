package com.example.gopal.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Gopal on 11/1/2018.
 */

public class CustomLoader extends AsyncTaskLoader<List<Event>> {
    private String mUrl;

    CustomLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    public List<Event> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        //performing network operation & extracting data
        List<Event> arrayList = Helper.fetchNewsData(mUrl);
        return arrayList;
    }
}
