package com.example.bustrack;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Locate extends Activity {
	Button showmap;
    Spinner busstop;
    EditText buses;
    Button find;
	String check=null;
	String response=null;
	String[] stringbusstop=null;
	String result=null;
	String stopname=null;
	String busno=null;

	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		  	 
	 protected void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	        setContentView(R.layout.find);
	        setTitle("Searching for bus location");
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
	      //assign address
	        find= (Button)findViewById(R.id.button1);
	        busstop= (Spinner) findViewById(R.id.spinner1);
	        buses = (EditText)findViewById(R.id.editText1);
	        showmap = (Button)findViewById(R.id.button2);

 }
	 public void findstops(View v){
		   new JSONParse().execute();
           busstop.setEnabled(true);
	 }
	 public void locatebus(View v){
		 Toast.makeText(Locate.this,"displaying map", Toast.LENGTH_SHORT).show();
		 	postParameters.add(new BasicNameValuePair("busstop",busstop.getSelectedItem().toString()));
		 	
            try {
            	result=null;
            	response=null;
				response = CustomHttpClient.executeHttpPost(MainActivity.mainurl+"script/locatestop.php", postParameters);
			       String result = response.toString();
             
                   
                   // Getting JSON Array from URL
                   JSONArray jArray = new JSONArray(result);
                  
                   String stoplatitude=     jArray.getJSONObject(0).getString("latitude");
                   String stoplongitude=     jArray.getJSONObject(0).getString("longitude");
                   
               	     Intent i = new Intent(Locate.this, MapActivity.class);   
                  
                   i.putExtra("stoplatitude", stoplatitude);
                   i.putExtra("stoplongitude", stoplongitude);
                   i.putExtra("busno", busno);
                   startActivity(i);
                   } catch (Exception e) {
				           // TODO Auto-generated catch block
				        e.printStackTrace();
			       }
		 	
	 }
	
	// getting the stops
	private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Locate.this);
            pDialog.setMessage("Getting Bus Stops");
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
                	busstop.setAdapter(null);
                	busno=buses.getText().toString();
                    postParameters.add(new BasicNameValuePair("busno",busno));
                    response = CustomHttpClient.executeHttpPost(MainActivity.mainurl+"script/busstops.php", postParameters);

                    // store the result returned by PHP script that runs MySQL query
                    String result = response.toString();
                    Log.i("response",result);
                    
                    // Getting JSON Array from URL
                    JSONArray jArray = new JSONArray(result);
                    stringbusstop= new String[jArray.length()];
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Locate.this,R.layout.my_spinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    for (int i = 0; i < jArray.length(); i++) {
                         adapter.add(jArray.getJSONObject(i).getString("stopname"));
                    }
                  busstop.setAdapter(adapter);
                    
                }catch (JSONException e) {
                	Toast.makeText(Locate.this,"no entry for this bus,enter a valid bus no", Toast.LENGTH_SHORT).show();	
                	
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           
            pDialog.dismiss();


        }
    }
}
