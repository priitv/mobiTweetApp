package com.szcho.mobitweetapp;

import java.util.List;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class TimelineAdapter extends BaseAdapter {
 
    private Activity activity;
    private List<TweetData> statuses;
    private static LayoutInflater inflater=null;
 
    public TimelineAdapter(Activity a, List<TweetData> statuses) {
        activity = a;
        this.statuses = statuses;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public int getCount() {
        return statuses.size();
    }
    
    public void setStatuses(List<TweetData> statuses) {
        this.statuses = statuses;
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.listrow, null);
 
        TextView createdAt = (TextView)vi.findViewById(R.id.created_at);
        TextView userName = (TextView)vi.findViewById(R.id.user_name);
        TextView userPost = (TextView)vi.findViewById(R.id.text);
 
        TweetData status = statuses.get(position);
 
        createdAt.setText(status.getCreatedAt());
        userName.setText(status.getUserName());
        userPost.setText(status.getText());
        return vi;
    }
}
