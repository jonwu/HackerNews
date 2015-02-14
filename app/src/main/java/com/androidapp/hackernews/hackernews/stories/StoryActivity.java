package com.androidapp.hackernews.hackernews.stories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.androidapp.hackernews.hackernews.interfaces.AsyncParent;
import com.androidapp.hackernews.hackernews.singletons.UrlsSingleton;
import com.androidapp.hackernews.hackernews.R;
import com.androidapp.hackernews.hackernews.stories.listview.StoryListAdapter;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.AdapterView.OnItemClickListener;


public class StoryActivity extends ActionBarActivity implements AsyncParent {
    private ListView lvStories;
    private ProgressBar progress;
    private ArrayList<String> result;
    private RequestQueue mRequestQueue;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        lvStories = (ListView) findViewById(R.id.lvStories);
        progress = (ProgressBar) findViewById(R.id.loadingPanel);
        //Queue is used to make synchronous calls.
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        //Begin tasks
        getIDs();

    }
    //Get all IDs
    public void getIDs(){
        Ion.with(this)
                .load(UrlsSingleton.topstories)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        ArrayList<String> stories = new ArrayList<String>();
                        for (int i = 0; i < result.size(); i++) {
                            stories.add(result.get(i).getAsString());

                        }
                        RequestStories requestStories = new RequestStories(mRequestQueue, stories);
                        requestStories.delegate = StoryActivity.this;
                        requestStories.execute();
                    }
                });
    }

    // This is a callback after RequestStories, where we assign functions to lvStories
    @Override
    public void onPostRequestStories(final ArrayList<JSONObject> jsonObjects) {
        StoryListAdapter storyListAdapter = new StoryListAdapter(this, jsonObjects);
        lvStories.setAdapter(storyListAdapter);
        progress.setVisibility(View.GONE);
        lvStories.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    //Open in WebView
                    String url = jsonObjects.get(position).getString("url");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
