package com.example.android.quakereport;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Quake>> {
    public EarthquakeLoader(Context context) {
        super(context);
    }

    @Override
    public List<Quake> loadInBackground() {
        List<Quake> earthquakes = QueryUtils.extractEarthquakes();

        return earthquakes;
    }
}
