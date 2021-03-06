package com.prcse.jamjar;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.prcse.datamodel.Artist;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//Custom grid adapter for the artist grid
public class ArtistGridAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater layoutInflater;
	private ArrayList<Artist> artists = null;
	private JarLid appState;

    public ArtistGridAdapter(Context c, JarLid appState) {
        mContext = c;
        layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.appState = appState;
        
        appState.getConnection().addObserver(new Observer() {

			@Override
			public void update(Observable arg0, Object arg1) {
				if(JarLid.IMAGES == arg1) {
					ArtistGridAdapter.this.notifyDataSetChanged();
				}
			}
        	
        });
    }
    
    public void setArtists(ArrayList<Artist> artists) {
    	this.artists = artists;
    	this.notifyDataSetChanged();
    }

    public int getCount() {
        if(artists != null){
        	return artists.size();
        }
    	return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

    	ViewHolder holder;
    	
        if (convertView == null) {  // if it's not recycled, initialise some attributes
            convertView = layoutInflater.inflate(R.layout.artist_venue_tile, parent, false);
            
            holder = new ViewHolder();
        	holder.rl = (RelativeLayout) convertView.findViewById(R.id.artist_venue_grid);
    		holder.image = (ImageView) convertView.findViewById(R.id.thumbnail);
    		holder.text = (TextView) convertView.findViewById(R.id.title);
    		
    		convertView.setTag(holder);
    		
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }

        Artist artist = (Artist) artists.get(position);
        
        if(appState.getImages() != null) {
        	//TODO get these to load in when image resource changes
        	if(appState.getImages().get((int)artist.getId()) != null) {
        		holder.image.setImageBitmap((Bitmap)appState.getImages().get((int)artist.getId()));
        	}
        	else {
            	holder.image.setImageResource(R.drawable.artist_venue_placeholder);
            }
        }
        else {
        	holder.image.setImageResource(R.drawable.artist_venue_placeholder);
        }

        holder.text.setText(artist.getName());
        return convertView;
    }
    
    static class ViewHolder {
    	RelativeLayout rl;
    	ImageView image;
    	TextView text;
    }
}