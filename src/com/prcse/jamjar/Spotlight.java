package com.prcse.jamjar;

import java.util.ArrayList;


import com.prcse.datamodel.Artist;
import com.prcse.utils.PrcseConnection;
import com.slidingmenu.lib.SlidingMenu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Spotlight extends Activity implements OnClickListener {

	private ActionBar actionBar;
	private SlidingMenu menu_tray;
	private PrcseConnection connection;
	private GridView gridview;
	private String image_base = "https://dl.dropbox.com/u/63072480/JamJarPics/";
	private String host = "10.0.1.31"; // "192.168.1.155";
	private int port = 1234;
	private ArrayList<Artist> artists;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotlight); 
        setTitle(R.string.title_activity_spotlight);
        
        actionBar = getActionBar();
		Display display = getWindowManager().getDefaultDisplay();
		Point point = new Point();

		actionBar.setDisplayHomeAsUpEnabled(true);
		display.getSize(point);
		int width = point.x;

		SlidingMenu menu_tray = new SlidingMenu(this);
		menu_tray.setMode(SlidingMenu.LEFT);
		menu_tray.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu_tray.setBehindOffset(width / 2);
		menu_tray.setFadeDegree(0.35f);
		menu_tray.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu_tray.setMenu(R.layout.menu_tray);
		
		RelativeLayout menu_profile_btn = (RelativeLayout) menu_tray.findViewById(R.id.profile);
		RelativeLayout menu_spotlight_btn = (RelativeLayout) menu_tray.findViewById(R.id.spotlight);
		RelativeLayout menu_artists_btn = (RelativeLayout) menu_tray.findViewById(R.id.artists);
		RelativeLayout menu_venues_btn = (RelativeLayout) menu_tray.findViewById(R.id.venues);
		RelativeLayout menu_tours_btn = (RelativeLayout) menu_tray.findViewById(R.id.tours);
		menu_profile_btn.setOnClickListener(this);
		menu_spotlight_btn.setOnClickListener(this);
		menu_artists_btn.setOnClickListener(this);
		menu_venues_btn.setOnClickListener(this);
		menu_tours_btn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spotlight, menu);
        return true;
    }
    
    private class Connector extends AsyncTask<PrcseConnection, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(PrcseConnection... params) {
			PrcseConnection connection = params[0];
			try {
				connection.connect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return connection.isConnected();
		}
		
		protected void onPostExecute(Boolean result) {
    		if(result == true) {
    			new GetArtists().execute(connection);
    		}
    	}
    }
    
    private class GetArtists extends AsyncTask<PrcseConnection, Integer, ArrayList> {
    	@Override
		protected ArrayList doInBackground(PrcseConnection... params) {
			PrcseConnection connection = params[0];
			artists = null;
			try {
				artists = connection.getFrontPage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return artists;
		}
    	
    	protected void onPostExecute(ArrayList result) {
    		((GridAdapter) gridview.getAdapter()).setArtists(result);
    	}
    }
    
    @Override
    public void onClick(View v)
    {
    	switch(v.getId()){
    	
    	case R.id.profile:
    		Toast.makeText(Spotlight.this, "profile", Toast.LENGTH_SHORT).show();
    		break;
    		
    	case R.id.spotlight:
    		Toast.makeText(Spotlight.this, "spotlight", Toast.LENGTH_SHORT).show();
    		break;
    		
    	case R.id.artists:
    		Toast.makeText(Spotlight.this, "artists", Toast.LENGTH_SHORT).show();
    		break;
    		
    	case R.id.venues:
    		Toast.makeText(Spotlight.this, "venues", Toast.LENGTH_SHORT).show();
    		break;
    			
    	case R.id.tours:
    		Toast.makeText(Spotlight.this, "tours", Toast.LENGTH_SHORT).show();
    		break;
    	}
    }
}
