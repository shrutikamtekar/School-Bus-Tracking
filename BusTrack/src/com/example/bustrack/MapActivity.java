package com.example.bustrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapActivity extends FragmentActivity {

	GoogleMap map;
	ArrayList<LatLng> markerPoints;
	TextView tvDistanceDuration;
	String result=null,response=null,busno=null,buslatitude=null,buslongitude=null;
	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	public String distance = "",duration = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setTitle("Map");
		
		tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);
		
		// Initializing 
		markerPoints = new ArrayList<LatLng>();
		
		// Getting reference to SupportMapFragment of the activity_main
		SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
		
		// Getting Map for the SupportMapFragment
		map = fm.getMap();
		
		// Enable MyLocation Button in the Map
		map.setMyLocationEnabled(true);	
		
		//changing map type
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		//disabling my location
		map.setMyLocationEnabled(false);
		
		Intent intent = getIntent();
		String busno=intent.getStringExtra("busno");
		try {
		result=null;
    	response=null;
       	postParameters.add(new BasicNameValuePair("busno",busno));
       	response = CustomHttpClient.executeHttpPost(MainActivity.mainurl+"script/locatebus.php", postParameters); 
	    result = response.toString();
          
       
           // Getting JSON Array from URL
           JSONArray jArray = new JSONArray(result);
      
           buslatitude=     jArray.getJSONObject(0).getString("latitude");
           buslongitude=     jArray.getJSONObject(0).getString("longitude");
		} catch (Exception e) {
	           // TODO Auto-generated catch block
	        e.printStackTrace();
		}
		String point1= ""+intent.getStringExtra("stoplatitude")+","+intent.getStringExtra("stoplongitude");
		String point2= ""+buslatitude+","+buslongitude;
		   Log.d("Map Activity",point2+" fetched");
      
		
		
		
		MarkerOptions options = new MarkerOptions();
		
		options.position(StringtoLatlang(point1));
		options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		map.addMarker(options);
		
		options.position(StringtoLatlang(point2));
		options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		map.addMarker(options);
		
	    CameraUpdate center=
	            CameraUpdateFactory.newLatLng(StringtoLatlang(point1));
	        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

	        map.moveCamera(center);
	        map.animateCamera(zoom);
		
		// Getting URL to the Google Directions API
		String url = getDirectionsUrl(StringtoLatlang(point1), StringtoLatlang(point2));				
		
		DownloadTask downloadTask = new DownloadTask();
		
		// Start downloading json data from Google Directions API
		downloadTask.execute(url);
		
	}
	private LatLng StringtoLatlang(String point){
		String[] latlong =  point.split(",");
		double latitude = Double.parseDouble(latlong[0]);
		double longitude = Double.parseDouble(latlong[1]);
		LatLng locationpoint = new LatLng(latitude, longitude);
		return locationpoint;
	}
	
	private String getDirectionsUrl(LatLng origin,LatLng dest){
					
		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		
		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;		
		
					
		// Sensor enabled
		String sensor = "sensor=false";			
					
		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
					
		// Output format
		String output = "json";
		
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		
		
		return url;
	}
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

	
	
	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String>{			
				
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {
				
			// For storing data from web service
			String data = "";
					
			try{
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;		
		}
		
		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			
			
			ParserTask parserTask = new ParserTask();
			
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
				
		}		
	}
	
	/** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
    	
    	// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			
			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           
            
            try{
            	jObject = new JSONObject(jsonData[0]);
            	DirectionsJSONParser parser = new DirectionsJSONParser();
            	
            	// Starts parsing data
            	routes = parser.parse(jObject);    
            }catch(Exception e){
            	e.printStackTrace();
            }
            return routes;
		}
		
		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();
			
			if(result.size()<1){
				Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
				return;
			}
				
			
			// Traversing through all the routes
			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();
				
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);
				
				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);	
					
					if(j==0){	// Get distance from the list
						distance = (String)point.get("distance");						
						continue;
					}else if(j==1){ // Get duration from the list
						duration = (String)point.get("duration");
						continue;
					}
					
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	
					
					points.add(position);						
				}
				
				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.RED);	
				
			}
			AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();

			// Setting Dialog Title
			alertDialog.setTitle("ETA");


			// Setting Dialog Message
			alertDialog.setMessage("Distance:"+distance+ ", Duration:"+duration);

			// Setting Icon to Dialog
			alertDialog.setIcon(R.drawable.location);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	dialog.cancel();
	        }
			});


			// Showing Alert Message
			alertDialog.show();
			tvDistanceDuration.setText("To refresh press back and click on show map");
			
			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions);							
		}			
    }   
   			
    	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
}