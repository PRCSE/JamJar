package com.prcse.jamjar;


import java.io.InputStream;
import java.util.ArrayList;

import com.prcse.datamodel.Artist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ArtistGridAdapter extends BaseAdapter {
	private Context mContext;
	private String image_base;
	private LayoutInflater layoutInflater;
	private ArrayList<Artist> artists = null;

    public ArtistGridAdapter(Context c, String image_base) {
        mContext = c;
        this.image_base = image_base;
        layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        
        if(artist.getThumb() != "NO SET IMAGE" || artist.getThumb() != null) {
        	String url = this.image_base + artist.getThumb();
        	new DownloadImageTask(holder.image).execute(url);
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
    
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
