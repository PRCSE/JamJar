package com.prcse.jamjar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.prcse.datamodel.Artist;
import com.prcse.datamodel.Customer;
import com.prcse.protocol.CustomerBooking;
import com.prcse.protocol.CustomerInfo;
import com.prcse.protocol.Request;
import com.prcse.protocol.FrontPage;
import com.prcse.utils.PrcseConnection;
import com.prcse.utils.ResponseHandler;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;



public class JarLid extends Application {
	
	// class constants
	private final static String customerCertificate = "customerCertificate";
	
	// Connection variables
	private String image_base = "https://dl.dropboxusercontent.com/u/6918192/University/PRCSE/";
	private String host = "80.235.137.15"; // 77.99.8.110  alternative server location
	private int port = 1234;
	private PrcseConnection connection;
	private Thread connThread;
	
	// global info
	private ArrayList<Object> artists;
	private CustomerInfo user;
	private Bitmap user_image;
	HashMap<Long, Bitmap> artist_images;
	HashMap<Long, CustomerBooking> bookings;

	public JarLid() {
		user = new CustomerInfo();
		
		// open connection thread to the database
        connection = new PrcseConnection(host, port);
        connection.addObserver(new Observer() {

			@Override
			public void update(Observable arg0, Object arg1) {
				// arg1 will be the requests that are pushed from the server
				if(arg1 == null && connection.isConnected()) {
					// if this is true we have just connected, so get front page
					connection.getFrontPage(new ResponseHandler() {
						// here we react to the response
						@Override
						public void handleResponse(Request response) {
							// to be called when response comes back
							artists = ((FrontPage)response).getArtists();
							
							// get images with async task
							new DownloadImageTask().execute();
						}
					});
				}
				else if(arg1 instanceof CustomerInfo) {
					// update CustomerInfo object
					JarLid.this.setUser((CustomerInfo) arg1);
					if(JarLid.this.getUser() != null) {
						// get image if exsists
						new DownloadUserImage().execute();
					}
				}
			}
        	
        });
        connThread = new Thread(connection);
        connThread.start();
	}
	
	public boolean isLoggedIn() {
		if(user.getCustomer() == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public Bitmap getUser_image() {
		return user_image;
	}

	public void setUser_image(Bitmap user_image) {
		this.user_image = user_image;
	}
	
	public PrcseConnection getConnection() {
		return connection;
	}
	
	public HashMap<Long, Bitmap> getImages() {
		return artist_images;
	}

	public ArrayList<Object> getArtists() {
		return artists;
	}

	public void setArtists(ArrayList<Object> artists) {
		this.artists = artists;
	}

	public CustomerInfo getUser() {
		return user;
	}

	public void setUser(CustomerInfo user) {
		this.user = user;
	}
	
	public String getImage_base() {
		return image_base;
	}

	public HashMap<Long, CustomerBooking> getBookings() {
		return bookings;
	}

	public void setBookings(HashMap<Long, CustomerBooking> bookings) {
		this.bookings = bookings;
	}
	
	//File streams to write and read a customer/user object, to/from storage
	public boolean getCustomerStorage() {
		try {
			FileOutputStream output = this.openFileOutput(customerCertificate, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(output);
			os.writeObject(user);
			
			os.close();
			return true;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setCustomerStorage() {
		try {
			FileInputStream input = this.openFileInput(customerCertificate);
			ObjectInputStream is = new ObjectInputStream(input);
			user = (CustomerInfo)is.readObject();
			
			is.close();
			return true;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void removeCustomerStorage()
	{
		try
		{
			new File(customerCertificate).delete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	 private class DownloadImageTask extends AsyncTask<Void, Void, Void> {
		 HashMap<Long, Bitmap> images;
	
	    public DownloadImageTask() {
	        this.images = new HashMap<Long, Bitmap>();
	    }

		@Override
		protected Void doInBackground(Void... params) {
			for(Object a : artists) {
				String url = image_base + ((Artist)a).getThumb();
				Bitmap mIcon = null;
				try {
					if(url != null) {
			            InputStream in = new java.net.URL(url).openStream();
			            mIcon = BitmapFactory.decodeStream(in);
			            images.put(((Artist)a).getId(), mIcon);
					}
		        } catch (Exception e) {
		            Log.e("Error", e.getMessage());
		            e.printStackTrace();
		        }
			}
			artist_images = images;
			return null;
		}
	}
	 
	private class DownloadUserImage extends AsyncTask<Void, Void, Bitmap> {
		Bitmap image;
		Customer customer;

		@Override
		protected Bitmap doInBackground(Void... params) {
			String url = customer.getThumb();
			try {
				if(url != null) {
		            InputStream in = new java.net.URL(url).openStream();
		            image = BitmapFactory.decodeStream(in);
				}
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
			return image;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			Bitmap retirevedImage = result;
			JarLid.this.setUser_image(retirevedImage);
		}
	}
}
