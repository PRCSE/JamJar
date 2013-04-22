package com.prcse.jamjar;


import java.util.ArrayList;

import com.prcse.datamodel.Artist;
import com.prcse.datamodel.Billing;
import com.prcse.datamodel.Event;
import com.prcse.datamodel.Tour;
import com.prcse.datamodel.Venue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

//Custom adapter for events
public class EventGridAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater layoutInflater;
	private Artist artist;
	//private ArrayList<Tour> tours = new ArrayList<Tour>();
	private ArrayList<Billing> billings = new ArrayList<Billing>();
	private ArrayList<Event> events = new ArrayList<Event>();
	private Venue venue = null;

    public EventGridAdapter(Context c, Artist artist) {
        mContext = c;
        this.artist = artist;
        billings = artist.getBillings();
        for (Billing b : billings)
        {
        	events.add(b.getEvent());
        }
        layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public void setArtist(Artist artist) {
    	this.artist = artist;
    	billings = artist.getBillings();
        for (Billing b : billings)
        {
        	events.add(b.getEvent());
        }
    	this.notifyDataSetChanged();
    }

    public int getCount() {
        if(events != null){
        	return events.size();
        }
    	return 0;
    }

    public Event getItem(int position) {
        return events.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

    	ViewHolder holder;
    	
        if (convertView == null) {  // if it's not recycled, initialise some attributes
            convertView = layoutInflater.inflate(R.layout.event_tile, parent, false);
            
            holder = new ViewHolder();
        	holder.ticketGrid = (RelativeLayout) convertView.findViewById(R.id.ticket_grid);
        	holder.ticketGrid = (RelativeLayout) convertView.findViewById(R.id.thumbnail);
    		holder.year = (TextView) convertView.findViewById(R.id.year);
    		holder.day = (TextView) convertView.findViewById(R.id.day);
    		holder.month = (TextView) convertView.findViewById(R.id.month);
    		holder.ticketGrid = (RelativeLayout) convertView.findViewById(R.id.act);
    		holder.artist = (TextView) convertView.findViewById(R.id.artist);
    		holder.tour = (TextView) convertView.findViewById(R.id.tour);
    		holder.location = (RelativeLayout) convertView.findViewById(R.id.location);
    		holder.city = (TextView) convertView.findViewById(R.id.city);
    		holder.venue = (TextView) convertView.findViewById(R.id.venue);
    		
    		convertView.setTag(holder);
    		
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }

        Event event = (Event) events.get(position);
        venue = event.getSeatingPlan().getVenue();
        
        
        holder.day.setText(event.getDayToString());
        holder.month.setText(event.getMonthToString());
        holder.year.setText(event.getYearToString());
        holder.artist.setText(artist.getName());
        holder.tour.setText(event.getTourName());
        holder.city.setText(venue.getName());
        holder.venue.setText(venue.getPostcode());

        return convertView;
    }
    
    static class ViewHolder {
    	RelativeLayout ticketGrid;
    	RelativeLayout thumbnail;
    	TextView year;
    	TextView day;
    	TextView month;
    	RelativeLayout act;
    	TextView artist;
    	TextView tour;
    	RelativeLayout location;
    	TextView city;
    	TextView venue;
    }
    
    static class EventByTour {
    	Tour tour;
    	ArrayList<Event> events;
    }
}
