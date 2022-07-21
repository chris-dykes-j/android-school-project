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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        Event event = events.get(position);
        int layout = R.layout.event_item;
        convertView = inflater.inflate(layout, parent, false);
        // set parameters, textview etc..
        ImageView img = convertView.findViewById(R.id.searchEventImage);
        TextView txt = convertView.findViewById(R.id.searchEventTitle);
        txt.setText(event.getName());
        return convertView;
    }
}
