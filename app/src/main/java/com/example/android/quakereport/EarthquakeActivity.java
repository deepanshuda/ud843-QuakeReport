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
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements QuakeAdapter.ListItemClickListener, android.support.v4.app.LoaderManager.LoaderCallbacks<List<Quake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    RecyclerView quakeRecyclerView;
    QuakeAdapter quakeAdapter;

    List<Quake> earthquakes;

    TextView noEarthQuakesView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        quakeRecyclerView = (RecyclerView) findViewById(R.id.quake_recyclerview);
        noEarthQuakesView = (TextView) findViewById(R.id.no_earthquakes_textview);
        progressBar = (ProgressBar) findViewById(R.id.earthquake_progressbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        quakeRecyclerView.setLayoutManager(layoutManager);
        earthquakes = new ArrayList<Quake>();
        // Create a fake list of earthquake locations.
//        ArrayList<Quake> earthquakes = new ArrayList<>();
//        earthquakes.add(new Quake(7.2, "San Francisco", "Feb 2, 2016"));
//        earthquakes.add(new Quake(6.1, "London", "July 20, 2015"));
//        earthquakes.add(new Quake(6.1, "Tokyo", "July 20, 2015"));
//        earthquakes.add(new Quake(6.1, "Mexico City", "July 20, 2015"));
//        earthquakes.add(new Quake(6.1, "Moscow", "July 20, 2015"));
//        earthquakes.add(new Quake(6.1, "Rio de Janeiro", "July 20, 2015"));
//        earthquakes.add(new Quake(1.6, "Paris", "Oct 30, 2011"));

        quakeAdapter = new QuakeAdapter(earthquakes, this, R.layout.quake_list_item, this);
        quakeRecyclerView.setAdapter(quakeAdapter);

//        EarthquakeAsyncTask earthquakeAsyncTask = new EarthquakeAsyncTask();
//        earthquakeAsyncTask.execute();

        getSupportLoaderManager().initLoader(1, null, this).forceLoad();

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Quake selectedQuake = earthquakes.get(clickedItemIndex);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(selectedQuake.getUrl()));
        startActivity(browserIntent);
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<List<Quake>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquakeLoader(EarthquakeActivity.this);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<List<Quake>> loader, List<Quake> data) {
        progressBar.setVisibility(View.INVISIBLE);

        if (data != null) {
            earthquakes = data;

            if (earthquakes.size() > 0) {
                quakeRecyclerView.setVisibility(View.VISIBLE);
                noEarthQuakesView.setVisibility(View.INVISIBLE);
                quakeAdapter.setQuakes(earthquakes);
                quakeAdapter.notifyDataSetChanged();
            } else {
                quakeRecyclerView.setVisibility(View.INVISIBLE);
                noEarthQuakesView.setVisibility(View.VISIBLE);
            }
        } else {
            quakeRecyclerView.setVisibility(View.INVISIBLE);
            noEarthQuakesView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<List<Quake>> loader) {
        quakeAdapter.setQuakes(new ArrayList<Quake>());
    }

//    private class EarthquakeAsyncTask extends AsyncTask<Void, Void, List<Quake>> {
//
//        @Override
//        protected List<Quake> doInBackground(Void... voids) {
//            earthquakes = QueryUtils.extractEarthquakes();
//
//            return earthquakes;
//        }
//
//        @Override
//        protected void onPostExecute(List<Quake> quakes) {
//            quakeAdapter.setQuakes(earthquakes);
//            quakeAdapter.notifyDataSetChanged();
//        }
//    }

}
