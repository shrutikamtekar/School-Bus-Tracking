package com.example.drive;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	public Button drive,board,unboard;
    public EditText busno;
    public static String bus;
    /* request BT discover */
   	private static final int	REQUEST_DISCOVERABLE	= 0x5;
   	/* Get Default Adapter */
   	private BluetoothAdapter	_bluetooth				= BluetoothAdapter.getDefaultAdapter();
   	public static String mainurl="http://schoolbustracker.comuv.com/SchoolBus/";
  //for Database
   	String response=null;
   	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
   	Boolean check=false;

  	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Main Menu");
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();

		// Setting Dialog Title
		alertDialog.setTitle("Information");

		// Setting Dialog Message
		alertDialog.setMessage("You need to start driving before boarding the kids");

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.alert);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        	dialog.cancel();
        }
		});


		// Showing Alert Message
		alertDialog.show();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
		drive=(Button)findViewById(R.id.button1);
        busno=(EditText)findViewById(R.id.editText1);
        board=(Button)findViewById(R.id.button2);
        unboard=(Button)findViewById(R.id.button3);
        
        board.setVisibility(View.INVISIBLE);
        unboard.setVisibility(View.INVISIBLE);
        
        
        
         drive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	verify();
            	if(check)
            	{
            		if(!_bluetooth.isEnabled())
                    _bluetooth.enable();
                    Intent intent = new Intent(MainActivity.this,GPS.class);
                    intent.putExtra("busno",bus);
                    startService(intent);
                    busno.setVisibility(View.INVISIBLE);
                	drive.setVisibility(View.INVISIBLE);
                	board.setVisibility(View.VISIBLE);
            	}
            }
           });
        

	}
  	/*verifying
  	 */
  	public void verify()
  	{
  		bus=busno.getText().toString();
    	if(busno.getText().toString().length()==1)
     	{
    		postParameters.add(new BasicNameValuePair("busno",bus));
    		try {
            	response = CustomHttpClient.executeHttpPost(MainActivity.mainurl+"script/verfiy.php",postParameters);
             	Log.d("Main Activity",response.toString());
            	if(response.toString().contains("yes"))
            		check=true;
            	else
            		Toast.makeText(this,"entered bus no does not exsits", Toast.LENGTH_SHORT).show();
            	 }
             catch (Exception e) {
                 e.printStackTrace();
                 Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();                        
             }
     	}
    	if(busno.getText().toString().equalsIgnoreCase("")) 
       	{
       		Toast.makeText(MainActivity.this,"all fields required", Toast.LENGTH_SHORT).show();
       	}
    	else if(busno.getText().toString().length()!=1)
    	{	
    		Toast.makeText(MainActivity.this,"enter valid bus no", Toast.LENGTH_SHORT).show();
    		
    	}
    	

  	}
	
	/* Boarding */
		public void boarding(View view)
		{
		   drive.setVisibility(View.INVISIBLE);
		   unboard.setVisibility(View.VISIBLE);
           Intent enabler = new Intent(this, Boardkid.class);
           enabler.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           enabler.putExtra("busno",bus);
           startActivity(enabler);
		}
		
		/* Unboarding */
		public void unboarding(View view)
		{
			Intent enabler = new Intent(this,Unboardkid.class);
			enabler.putExtra("busno",bus);
			enabler.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(enabler);
			
		}
		@Override
	    public void onBackPressed() {
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	        alertDialogBuilder.setTitle("Exit Application");
	        alertDialogBuilder
	                .setMessage("Are you sure you want to exit?")
	                .setCancelable(false)
	                .setPositiveButton("Yes",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int id) {
	                               moveTaskToBack(true);
	                               finish();
	                               android.os.Process.killProcess(android.os.Process.myPid());
	                                System.exit(1);
	                            }
	                        })

	                .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int id) {

	                        dialog.cancel();
	                    }
	                });

	        AlertDialog alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
	    }

}