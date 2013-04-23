package com.prcse.jamjar;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

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

//Custom grid adapter for the artist grid
public class ArtistGridAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater layoutInflater;
	private ArrayList<Artist> artists = null;
	private JarLid appState;
	private HashMap<Long, Boolean> setImages;

    public ArtistGridAdapter(Context c, JarLid appState) {
        mContext = c;
        layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.appState = appState;
        setImages = new HashMap<Long, Boolean>();
    }
    
    public void setArtists(ArrayList<Artist> artists) {
    	this.artists = artists;
    	setImages = new HashMap<Long, Boolean>();
    	
    	for(Artist a : artists) {
        	setImages.put(a.getId(), false);
        }
    	
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
        
        Log.i("Backup Grid Image Loader", "Cached images: \n" + appState.getImages().get(artist.getId()));
        
    	//TODO get these to load in when image resource changes
        if(setImages.get(artist.getId()).equals(false)) {
        	// if image cached
        	if(appState.getImages() != null && appState.getImages().get(artist.getId()) != null) 
        	{
        		Bitmap temp = Bitmap.createBitmap((Bitmap)appState.getImages().get(artist.getId()),
        											0,
        											0,
        											((Bitmap)appState.getImages().get(artist.getId())).getWidth(),
        											((Bitmap)appState.getImages().get(artist.getId())).getHeight());
        		
        		holder.image.setImageBitmap(temp);
        		setImages.put(artist.getId(), true);
        	}
        	else if(artist.getThumb() != null) {
        		try {
        			String url = appState.getImage_base() + artist.getThumb();
                	new DownloadImageTask(holder.image, artist).execute(url);
        		}
        		catch(Exception e) {
        			holder.image.setImageResource(R.drawable.artist_venue_placeholder);
        		}
        	}
        	else {
        		holder.image.setImageResource(R.drawable.artist_venue_placeholder);
            }
        }
        
        holder.text.setText(artist.getName());
        return convertView;
    }
    
    static class ViewHolder {
    	RelativeLayout rl;
    	ImageView image;
    	TextView text;
    }
    
    private class DownloadImageTask extends AsyncTask<String, Void, Object[]> {
        ImageView bmImage;
        Artist a;
        Object[] result;

        public DownloadImageTask(ImageView bmImage, Artist a) {
            this.bmImage = bmImage;
            this.a = a;
            result = new Object[2];
        }

        protected Object[] doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                Log.i("Backup Grid Image Loader", "Getting image for " + a.getName());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                Log.e("Backup Grid Image Loader", "Failed to get image for Image set for " + a.getName());
            };
            result[0] = mIcon11;
            result[1] = a;
            return result;
        }

        protected void onPostExecute(Object[] result) {
	        bmImage.setImageBitmap((Bitmap)result[0]);
	        setImages.put(((Artist)result[1]).getId(), true);
	        Log.i("Backup Grid Image Loader", "Image set for " + a.getName());
        }
    }
}