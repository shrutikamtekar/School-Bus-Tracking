package com.example.drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.api.GoogleApiClient;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Boardkid  extends Activity
{
	private static final String TAG = Boardkid.class.getSimpleName();
	private static final int REQUEST_DISCOVERY = 0x1;;
	private Handler _handler = new Handler();
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	private BluetoothSocket socket = null;
	private TextView sTextView;
	private OutputStream outputStream;
	private InputStream inputStream;
	private String str,busno,verfiybus;
	private StringBuffer sbu;
	Editor editor;
	String response=null;
    String tagno,message,name,mobileno,status=null;
    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    PendingIntent piSent,piDelivered;
    
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
		WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.boarding);
		setTitle("Boarding Kids");
		sTextView = (TextView)findViewById(R.id.sTextView);
		if (!_bluetooth.isEnabled()) {
			finish();
			return;
		}
		busno=getIntent().getStringExtra("busno");		
		Intent intent = new Intent(this, DiscoveryActivity.class);
		 
		/* Prompted to select a server to connect */
		Toast.makeText(this, "select device to connect", Toast.LENGTH_SHORT).show();
		
		/* Select device for list */
		startActivityForResult(intent, REQUEST_DISCOVERY);
		
		
		Log.d("EF-BTBee", ">>setOnKeyListener");
	}
	
	/* after select, connect to device */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != REQUEST_DISCOVERY) {
			finish();
			return;
		}
		if (resultCode != RESULT_OK) {
			finish();
			return;
		}
		final BluetoothDevice device = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		new Thread() {
			public void run() {
				connect(device);
			};
		}.start();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		try {
			if(socket!= null)
			socket.close();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("EF-BTBee", ">>"+e.toString());
		}
	}
	
	/*after connected,read tags*/
	protected void connect(BluetoothDevice device) {
		//BluetoothSocket socket = null;
		try {
			//Create a Socket connection: need the server's UUID number of registered
			socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			
			socket.connect();
			Log.d("EF-BTBee", ">>Client connectted");
		
			inputStream = socket.getInputStream();														
			outputStream = socket.getOutputStream();
			int read = -1;
			final byte[] bytes = new byte[2048];
			for (; (read = inputStream.read(bytes)) > -1;) {
				final int count = read;
				_handler.post(new Runnable() {
					public void run() {
						StringBuilder b = new StringBuilder();
						for (int i = 0; i < count; ++i) {
							String s = Integer.toString(bytes[i]);
							b.append(s);
							b.append(",");
						}
						String s = b.toString();
						String[] chars = s.split(",");
						sbu = new StringBuffer();  
						 for (int i = 0; i < chars.length; i++) {  
						        sbu.append((char) Integer.parseInt(chars[i]));  
						    }
						Log.d("EF-BTBee", ">>inputStream");
						if(sbu.length()>13)
						{
							sbu.delete(12,14);
							tagno=sbu.toString().replace("\n", "");
							Log.d("EF-BTBee",tagno);
							new JSONParse().execute();						 
						}
						else
						{
							Toast.makeText(Boardkid.this,"tag it again", Toast.LENGTH_SHORT).show();
						}
					}
				}); 
			}
			
		} catch (IOException e) {
			Log.e("EF-BTBee", ">>", e);
			finish();
			return ;
		} finally {
			if (socket != null) {
				try {
					Log.d("EF-BTBee", ">>Client Socket Close");
					socket.close();	
					finish();
					return ;
				} catch (IOException e) {
					Log.e("EF-BTBee", ">>", e);
				}
			}
		}
	}

	
	/*fetch the phone no*/
	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            /** Creating a pending intent which will be broadcasted when an sms message is successfully sent */
	        piSent = PendingIntent.getBroadcast(getBaseContext(), 0, new Intent("in.wptrafficanalyzer.sent") , 0);
	        
	        /** Creating a pending intent which will be broadcasted when an sms message is successfully delivered */
	        piDelivered = PendingIntent.getBroadcast(getBaseContext(), 0, new Intent("in.wptrafficanalyzer.delivered"), 0); 
	        pDialog = new ProgressDialog(Boardkid.this);
	        pDialog.setMessage("Verifying...");
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
                	postParameters.add(new BasicNameValuePair("tagno",tagno));
                	postParameters.add(new BasicNameValuePair("busno",busno));
                	response = CustomHttpClient.executeHttpPost(MainActivity.mainurl+"script/board.php", postParameters);
                	// store the result returned by PHP script that runs MySQL query
                    String result = response.toString();
                    Log.i("response",result);
                    // Getting JSON Array from URL
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++)
                    	{
                    		mobileno=  jArray.getJSONObject(i).getString("mobileno");
                    		status=jArray.getJSONObject(i).getString("boarded");
                    		name=jArray.getJSONObject(i).getString("name");
                    		verfiybus=jArray.getJSONObject(i).getString("busno");
                    	}
                    /** Getting an instance of SmsManager to sent sms message from the application*/
    		        SmsManager smsManager = SmsManager.getDefault();		        
    		       
    		        pDialog.dismiss();

    		        if(status.equalsIgnoreCase("0")&&busno.equalsIgnoreCase(verfiybus))
    		        {
    		        	
    		        	if(str != null)
						{
    		        		sTextView.setText(str+name+" boarded the bus");
    		        		str +=name+" boarded the bus";
						}
    		        	else
    		        	{
    		        		sTextView.setText(name+" boarded the bus");
    		        		str=name+" boarded the bus";
    		        	}
    		        	message=name+" boarded the bus";
    		        	Log.d("log_tag", "mobile no is "+mobileno+" status "+status+" "+message);
    		        	/** Sending the Sms message to the intended party */
    		        	smsManager.sendTextMessage(mobileno, null, message, piSent, piDelivered);
    		        }
    		        else if(status.equalsIgnoreCase("1")&&busno.equalsIgnoreCase(verfiybus))
    		        {
    		        	if(str != null)
						{
    		        		sTextView.setText(str+name+" has boarded the bus already");
    		        		str +=name+" has boarded the bus already";
						}
    		        	else
    		        	{
    		        		sTextView.setText(name+" has boarded the bus already");
    		        		str=name+" has boarded the bus already";
    		        	}
    		        }
    		        else
    		        {
    		        	if(str != null)
						{
    		        		sTextView.setText(str+name+" is in the wrong bus");
    		        		str +=name+" has boarded the wrong bus";
						}
    		        	else
    		        	{
    		        		sTextView.setText(name+" is in the wrong bus");
    		        		str=name+" has boarded the wrong bus";
    		        	}
    		        }
    		        str+='\n';
    		        
                }
                  catch (JSONException e) {
                    Log.e("log_tag", "Error parsing data " + e.toString());
                    pDialog.dismiss();
                  }

            } catch (Exception e) {
            	Log.e("log_tag", "Error parsing data " + e.toString());
                e.printStackTrace();
                pDialog.dismiss();
            }
      
         }
        
    }
}

