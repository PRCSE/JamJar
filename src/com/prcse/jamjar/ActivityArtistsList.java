package com.prcse.jamjar;

import java.util.ArrayList;
import com.prcse.datamodel.Artist;
import com.prcse.utils.PrcseConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class ActivityArtistsList extends Activity {
	
	private GridView gridView;
	private PrcseConnection connection;
	private String image_base = "https://dl.dropbox.com/u/63072480/JamJarPics/";
	private String host = "10.0.1.31"; // "192.168.1.155";
	private int port = 1234;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artists_list);
		
		
		gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new GridAdapter(this, this.image_base));
        
        gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(ActivityArtistsList.this, "" + position, Toast.LENGTH_SHORT).show();
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
    
    private class GetArtists extends AsyncTask<PrcseConnection, Integer, ArrayList<Artist>> {
    	@Override
		protected ArrayList<Artist> doInBackground(PrcseConnection... params) {
			PrcseConnection connection = params[0];
			ArrayList<Artist> result = null;
			try {
				result = connection.getFrontPage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
    	
    	protected void onPostExecute(ArrayList<Artist> result) {
    		((GridAdapter) gridView.getAdapter()).setArtists(result);
    	}
    }

}
