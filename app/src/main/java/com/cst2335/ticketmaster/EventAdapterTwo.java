package com.cst2335.ticketmaster;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapterTwo extends ArrayAdapter {
    public ArrayList<Event> events;
    public Context context;

    public EventAdapterTwo(ArrayList<Event> messageItems, Context context) {
        super(context, -1, messageItems);
        this.events = messageItems;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        Event message = events.get(position);
        int layout = R.layout.event_item;
        convertView = inflater.inflate(layout, parent, false);
        TextView textView = convertView.findViewById(R.id.searchEventTitle);
        textView.setText(message.getName());
        return convertView;
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
        return (long) pos;
    }

}

