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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ActivityArtistsGrid extends Activity {

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
        setContentView(R.layout.activity_artists_grid); 
        
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
		
		RelativeLayout button = (RelativeLayout) menu_tray.findViewById(R.id.profile);
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(ActivityArtistsGrid.this, "yo yo yo", Toast.LENGTH_SHORT).show();
				//Intent intent = new Intent(v.getContext(), ActivityArtistsList.class);
				//startActivity(intent);
			}
		});

        gridview = (GridView) findViewById(R.id.artists_grid);
        gridview.setAdapter(new GridAdapter(this, this.image_base));

        gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(ActivityArtistsGrid.this, "" + position, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(v.getContext(), ActivityArtistDetail.class);
				intent.putExtra("artist", artists.get(position));
				startActivity(intent);
			}
        });
        
        connection = new PrcseConnection(host, port);
        new Connector().execute(connection);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.artists, menu);
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
}
