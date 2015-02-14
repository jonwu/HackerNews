package com.androidapp.hackernews.hackernews.stories;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.androidapp.hackernews.hackernews.interfaces.AsyncParent;
import com.androidapp.hackernews.hackernews.singletons.UrlsSingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jonwu on 2/13/15.
 */

public class RequestStories extends AsyncTask<String, String, ArrayList<JSONObject>> {

    private RequestQueue mRequestQueue;
    private ArrayList<String> result;
    public AsyncParent delegate = null;

    public RequestStories(RequestQueue mRequestQueue, ArrayList<String> result){
        this.mRequestQueue = mRequestQueue;
        this.result = result;
    }
    @Override
    protected ArrayList<JSONObject> doInBackground(String... params) {
        ArrayList<JSONObject> stories = new ArrayList<JSONObject>();
        for (int i = 0; i < result.size(); i++) {
            String id = result.get(i);
            String url = UrlsSingleton.item+id+".json";

            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(url, null, future, future);
            mRequestQueue.add(request);

            try {
                JSONObject object = future.get(30, TimeUnit.SECONDS);
                stories.add(object);
            } catch (InterruptedException e) {
                // exception handling
            } catch (ExecutionException e) {
                // exception handling
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return stories;


    }

    @Override
    protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
        if (result != null){
            delegate.onPostRequestStories(jsonObjects);
        }
    }
}
