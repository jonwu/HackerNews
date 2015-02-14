package com.androidapp.hackernews.hackernews.interfaces;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jonwu on 2/13/15.
 */
public interface AsyncParent {
    void onPostRequestStories(ArrayList<JSONObject> jsonObjects);
}
