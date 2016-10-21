package com.example.bustrack;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Dmap extends Activity {


	 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	 String check=null;
	 String response=null;
	 String stoplat,stoplong;
	 String stopname,busno;
	protected void onCreate(Bundle savedInstanceState) {
    
        setContentView(R.layout.displaying);
       Intent in = getIntent();
      stopname=in.getStringExtra("stopname");
      busno=in.getStringExtra("busno");
      Log.d("dmap","in dmap");
      Toast.makeText(Dmap.this,"in dmap class", Toast.LENGTH_SHORT).show();
    //new JSONParse().execute();
     }
	
	// getting the latitude and longitude
		 private class JSONParse extends AsyncTask<String, String, JSONObject> {
			 private ProgressDialog pDialog;
		        @Override
		        protected void onPreExecute() {
		            super.onPreExecute();
		            pDialog = new ProgressDialog(Dmap.this);
		            pDialog.setMessage("Getting location");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(true);
		            pDialog.show();
		           }

		        @Override
		        protected JSONObject doInBackground(String... args) {
		            JSONParser jParser = new JSONParser();
		            // Getting JSON from URL
		            JSONObject json = jParser.getJSONFromUrl(MainActivity.mainurl);
		            return json;
		        }

		        @Override
		        protected void onPostExecute(JSONObject json) {

		            try {

		                try {
		                	postParameters.add(new BasicNameValuePair("busno", busno));
		                    response = CustomHttpClient.executeHttpPost(MainActivity.mainurl+"script/getlocation.php", postParameters);

		                    // store the result returned by PHP script that runs MySQL query
		                    String result = response.toString();
		                    // Getting JSON Array from URL

		                    JSONArray jArray = new JSONArray(result);
		                    for (int i = 0; i < jArray.length(); i++) {
		                       // JSONObject c = jArray.getJSONObject(i);
		                        // Storing  JSON item in a Variable
		                        stoplat=jArray.getJSONObject(i).getString("latitude");
		                        stoplong=jArray.getJSONObject(i).getString("longitude");
		                        }
		                	} 
		                catch (JSONException e) {
		                    Log.e("log_tag", "Error parsing data " + e.toString());
		                }
		            }
		            catch (Exception e) {
		                e.printStackTrace();
		            }
		        
		        }
		    }
		
}