package com.prcse.jamjar;

import java.util.ArrayList;

import com.prcse.datamodel.Artist;
import com.prcse.utils.PrcseConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class Spotlight extends Activity {

	private ActionBar actionBar;
	private PrcseConnection connection;
	private GridView gridview;
<<<<<<< HEAD
	private String image_base = "https://dl.dropbox.com/u/63072480/JamJarPics/";
	private String host = "10.0.1.31"; // "192.168.1.155";
	private int port = 1234;
=======
	private ArrayList<Artist> artists;
>>>>>>> james
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotlight); 

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new GridAdapter(this, this.image_base));

        gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(Spotlight.this, "" + position, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(v.getContext(), ActivityArtistDetail.class);
				intent.putExtra("artist", artists.get(position));
				startActivity(intent);
			}
        });
        
<<<<<<< HEAD
        connection = new PrcseConnection(host, port);
=======
        connection = new PrcseConnection("10.0.1.31", 1234);
>>>>>>> james
        new Connector().execute(connection);
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
}
