package com.example.drive;

import java.util.ArrayList;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;

import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GPS extends Service implements ConnectionCallbacks,
OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

// for the bus to store location of
String busno;

//for Database
String response=null;
ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

private Location mLastLocation;

// Google client to interact with Google API
private GoogleApiClient mGoogleApiClient;

private LocationRequest mLocationRequest;

// Location updates intervals in sec
private static int UPDATE_INTERVAL = 60000*4; // 1 min
private static int FATEST_INTERVAL =30000; // 30 sec
private static int DISPLACEMENT =0; // 10 meters

//latitude and longitude
String latitude,longitude;

	@Override
	public void onCreate() {
 		// First we need to check availability of play services
        if (checkPlayServices()) {
 
            // Building the GoogleApi client
            buildGoogleApiClient();
            
            createLocationRequest();
        }
 
	}
	
	@Override
    public void onStart(Intent intent, int startId) {
    	// For time consuming an long tasks you can launch a new thread here...
        Log.d("GPS","Service Started");
       busno=intent.getStringExtra("busno");
       Log.d("GPS","bus no is "+busno);
       if (mGoogleApiClient != null) {
           mGoogleApiClient.connect();
       }
       

    }
	/* adding location into database*/
	void Database()
	{
		 postParameters.add(new BasicNameValuePair("busno",busno));
 		postParameters.add(new BasicNameValuePair("lat",latitude));
         postParameters.add(new BasicNameValuePair("long",longitude));

         try {
        	response = CustomHttpClient.executeHttpPost(MainActivity.mainurl+"script/driverform.php",postParameters);
         	Toast.makeText(this,"current data added", Toast.LENGTH_SHORT).show();
         	
        	 }
         catch (Exception e) {
             e.printStackTrace();
             Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();                        
         }

	}
	/**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }
    
    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {
 
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        latitude = String.valueOf(mLastLocation.getLatitude());
    	longitude = String.valueOf(mLastLocation.getLongitude());
          // Displaying the new location on UI
        Database();
    }
    
	/**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    
    
    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode,(Activity) getApplicationContext(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }
    /**
     * Google api callback methods
     */
   
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("GPS", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }
 
    @Override
    public void onConnected(Bundle arg0) {
 
        // Once connected with google api, get the location
    	// Starting the location updates
        startLocationUpdates();
    	Log.d("GPS", "Periodic location updates started!");
    	   
    }
 
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	 @Override
	    public void onDestroy() {
		          if (mGoogleApiClient.isConnected()) {
	            mGoogleApiClient.disconnect();
	        }
	        Log.d("GPS", "Periodic location updates stopped!");
	    }
	
	
	}
