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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements QuakeAdapter.ListItemClickListener{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    RecyclerView quakeRecyclerView;
    QuakeAdapter quakeAdapter;

    ArrayList<Quake> earthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        quakeRecyclerView = (RecyclerView) findViewById(R.id.quake_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        quakeRecyclerView.setLayoutManager(layoutManager);

        // Create a fake list of earthquake locations.
//        ArrayList<Quake> earthquakes = new ArrayList<>();
//        earthquakes.add(new Quake(7.2, "San Francisco", "Feb 2, 2016"));
//        earthquakes.add(new Quake(6.1, "London", "July 20, 2015"));
//        earthquakes.add(new Quake(6.1, "Tokyo", "July 20, 2015"));
//        earthquakes.add(new Quake(6.1, "Mexico City", "July 20, 2015"));
//        earthquakes.add(new Quake(6.1, "Moscow", "July 20, 2015"));
//        earthquakes.add(new Quake(6.1, "Rio de Janeiro", "July 20, 2015"));
//        earthquakes.add(new Quake(1.6, "Paris", "Oct 30, 2011"));
        earthquakes = QueryUtils.extractEarthquakes();

        quakeAdapter = new QuakeAdapter(earthquakes, this, R.layout.quake_list_item, this);
        quakeRecyclerView.setAdapter(quakeAdapter);

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
}
