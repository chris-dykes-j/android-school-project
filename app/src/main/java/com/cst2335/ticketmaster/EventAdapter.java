package com.cst2335.ticketmaster;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter {

    public ArrayList<Event> events;
    Context context;

    public EventAdapter(ArrayList<Event> events, Context context) {
        super(context, -1, events);
        this.events = events;
        this.context = context;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int pos) {
        return events.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return Long.parseLong((events.get(pos).getId()));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.event_item, parent, false);
        TextView txt = convertView.findViewById(R.id.searchEventTitle);
        Event event = events.get(position);
        // set parameters, textview etc..
        txt.setText(event.getName());
        return convertView;
    }

    public void updateAdapter(ArrayList<Event> events) {
        this.events = events;
    }
}
