package com.prcse.jamjar;


import java.util.ArrayList;
import com.prcse.datamodel.Artist;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater layoutInflater;
	private ArrayList artists = null;

    public GridAdapter(Context c) {
        mContext = c;
        layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public void setArtists(ArrayList artists) {
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
    	
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialise some attributes
            convertView = layoutInflater.inflate(R.layout.artist_venue_tile, parent, false);
            
            holder = new ViewHolder();
        	holder.rl = (RelativeLayout) convertView.findViewById(R.id.artist_venue_grid);
    		holder.image = (ImageView) convertView.findViewById(R.id.thumbnail);
    		holder.text = (TextView) convertView.findViewById(R.id.title);
    		
    		convertView.setTag(holder);
        	
        	//imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }

        Artist artist = (Artist) artists.get(position);
        holder.image.setImageResource(R.drawable.asap_rocky);
        holder.text.setText(artist.getName());
        //imageView.setImageResource(mThumbIds[position]);
        return convertView;
    }
    
    static class ViewHolder {
    	RelativeLayout rl;
    	ImageView image;
    	TextView text;
    }
}
