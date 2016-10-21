package com.example.bustrack;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends Activity {
	Button reg;
    EditText rollno ;
    EditText fname;
    EditText busno;
    EditText mobileno;
    String div;
    String std;
    Spinner standard;
    Spinner division;

    

    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        setTitle("Registering");
           StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            
        //spinner for standard
            standard = (Spinner) findViewById(R.id.spinner1);
            final String[] stds={"1","2","3","4","5","6","7","8","9","10"};
            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,R.layout.my_spinner, stds);
            standard.setAdapter(adp1);
            standard.setOnItemSelectedListener(new OnItemSelectedListener() {
            	 
        		public void onItemSelected(AdapterView<?> arg0, View arg1,
        				int arg2, long arg3) {
        			std=stds[arg2];
        		}
         
        		public void onNothingSelected(AdapterView<?> arg0) {
        			// TODO Auto-generated method stub
        		    		}
        	});
            
          //spinner for division
            division=(Spinner) findViewById(R.id.spinner2);
            final String[] divs={"a","b","c"};
            ArrayAdapter<String> adp2 = new ArrayAdapter<String>(this,R.layout.my_spinner, divs);
            division.setAdapter(adp2);
            division.setOnItemSelectedListener(new OnItemSelectedListener() {
            	 
        		public void onItemSelected(AdapterView<?> arg0, View arg1,
        				int arg2, long arg3) {
        			div=divs[arg2];
        		}
         
        		public void onNothingSelected(AdapterView<?> arg0) {
        			// TODO Auto-generated method stub
        			}
        	});
            
          // getting all the fields entered
         rollno =((EditText)findViewById(R.id.editText1));
         fname =((EditText)findViewById(R.id.editText2));
         busno =((EditText)findViewById(R.id.editText3));
        mobileno =((EditText)findViewById(R.id.editText4));
        reg = (Button)findViewById(R.id.button1);
        reg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // declare parameters that are passed to PHP script 
            	 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            	// define the parameter
                postParameters.add(new BasicNameValuePair("rollno",rollno.getText().toString()));
                postParameters.add(new BasicNameValuePair("fname",fname.getText().toString()));
                postParameters.add(new BasicNameValuePair("busno",busno.getText().toString()));
                postParameters.add(new BasicNameValuePair("mobileno",mobileno.getText().toString()));
                postParameters.add(new BasicNameValuePair("std",std));
                postParameters.add(new BasicNameValuePair("div",div));
                 
                String response = null;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
        	        alertDialogBuilder.setTitle("Registred");
        	        alertDialogBuilder
        	                .setMessage("Your child has been registered successfully tagno will be provided by school")
        	                .setIcon(R.drawable.tick)
        	                .setPositiveButton("OK",
        	                        new DialogInterface.OnClickListener() {
        	                            public void onClick(DialogInterface dialog, int id) {
        	                            	dialog.cancel();
        	                            	Intent i=new Intent(Register.this,MainActivity.class);
        	                        		startActivity(i);
        	                            }
        	                        });

        	        AlertDialog alertDialog = alertDialogBuilder.create();
        	        
        	   // call executeHttpPost method passing necessary parameters
                try {
                		if(rollno.getText().toString().equalsIgnoreCase("")||fname.getText().toString().equalsIgnoreCase("")||busno.getText().toString().equalsIgnoreCase("")||mobileno.getText().toString().equalsIgnoreCase(""))
                		{
                			Toast.makeText(Register.this,"all fields required", Toast.LENGTH_SHORT).show();	
                		}
                		if((mobileno.getText().toString()).length()!=10)
                		
                		{
                			Toast.makeText(Register.this,"enter valid mobile no", Toast.LENGTH_SHORT).show();	
                		}
                		else
                			{
                			response = CustomHttpClient.executeHttpPost(MainActivity.mainurl+"script/studentform.php",postParameters);
                			alertDialog.show();
                			//Toast.makeText(Register.this,response, Toast.LENGTH_SHORT).show();
                			}
             
                }
                catch (Exception e) {
                	Toast.makeText(Register.this, "Please check internet connection", Toast.LENGTH_LONG).show();
                    Log.e("log_tag","Error in http connection!!" + e.toString());
                   }
            	 }
            });
        }
		
}

