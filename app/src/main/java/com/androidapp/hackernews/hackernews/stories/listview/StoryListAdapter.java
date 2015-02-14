package com.androidapp.hackernews.hackernews.stories.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidapp.hackernews.hackernews.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jonwu on 2/13/15.
 */
public class StoryListAdapter extends BaseAdapter {
    public ArrayList<JSONObject> results;
    private static LayoutInflater inflater = null;
    public Context context;


    public StoryListAdapter(Context context, ArrayList<JSONObject> results) {
        this.context = context;
        this.results = results;
        inflater = LayoutInflater.from(context);
    }
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {
        public TextView tvTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.tvTitle.setText(results.get(position).getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
