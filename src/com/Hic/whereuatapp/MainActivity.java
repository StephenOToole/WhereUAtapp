package com.Hic.whereuatapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;

public class MainActivity extends FragmentActivity implements View.OnClickListener, LocationListener{

	//Variables
	
	GoogleMap Gmap;								//Create variable of type GoogleMap.
	Context context = this; 					//context for Layout inflater
	private Connection con;						//Connection variable to connect to database;
	private Statement st;						//Holds an sql statement
	private ResultSet rs;						//Result from database
	LatLng latlon;								//Latitude object
	LatLng latlon1;									
	Polyline line;								//Used to draw line between two points
	double lat1;
	double lon1;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (TestGPlay() == true){							//Check to see if user has Google Play Services
		setContentView(R.layout.activity_main);				//Start main activity
		FindLocation();										//Run FindLocation method
		
		}
		
		Gmap.setOnMarkerDragListener(new OnMarkerDragListener(){		//Checks to see if a marker has been dragged on the map.

			@Override
			public void onMarkerDrag(Marker latlon1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				// TODO Auto-generated method stub
				 
			}

			@Override
			public void onMarkerDragStart(Marker latlon1) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		Gmap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){			//Listener for when user holds down finger on map.
			
			@Override
			public void onMapLongClick(final LatLng latlng1) {
				// TODO Auto-generated method stub
				LayoutInflater li = LayoutInflater.from(context);	//Layout Inflater for alert dialog
				final View v = li.inflate(R.layout.alert, null);	//set alert dialog view using alert.xml
				AlertDialog.Builder build = new AlertDialog.Builder(context);	//Build alert dialog
				build.setView(v);												//set the dialog builder to view v
				build.setCancelable(false);			//Makes sure user can't cancel the builder.
				
				build.setPositiveButton("Create", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						EditText title = (EditText) v.findViewById(R.id.edt1);		//Get text from id edt1
						EditText Snippet = (EditText) v.findViewById(R.id.eds1);	//Get text from eds1
						//Add marker
						Gmap.addMarker(new MarkerOptions()	
						.title(title.getText().toString())							//Title is text taken from XML file
						.snippet(Snippet.getText().toString())						//Snippet is the same
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))		//Set icon colour to red
						.position(latlng1)											//Set position
						.draggable(true)											//Make it moveable
						);
					}
					
					
				});
				
				build.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){		//Set the cancel button to close when clicked

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();			//Cancel dialog option
						
						
					}
					
				});
				
				AlertDialog al = build.create();		//Show dialog 
				al.show();
			}
		});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void DBConnect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
		} catch(Exception e){
			System.out.println("Error connecting to database");
		}
	}
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	public void ButtonA(View v){			//events that occur when button is pressed
		AlertDialog.Builder build2 = new AlertDialog.Builder(context);
		build2.setTitle("Welcome to WhereuAtapp");
		LayoutInflater li = LayoutInflater.from(context);
		final View v2 = li.inflate(R.layout.help, null);
		build2.setView(v2);
		
		TextView TextView1 = (TextView) v2.findViewById(R.id.help1);	//Same as above
		TextView1.setText("-To add a marker, hold down your finger on the location you wish to place it");
		
		TextView TextView2 = (TextView) v2.findViewById(R.id.help2);
		TextView2.setText("-Use the Find Friend button to enter their latitude and longitude and find them in the world");
		
		TextView TextView3 = (TextView) v2.findViewById(R.id.help3);
		TextView3.setText("-You can drag a red marker by holding your finger down on the marker and dragging it to a new location");
		
		build2
		.setCancelable(false)
		.setPositiveButton("Okay", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
			
		});
		
		AlertDialog alertDialog = build2.create();
		 
		// show it
		alertDialog.show();
	}
	
	public void ButtonB(View v){			//events that occur when button is pressed

		
		AlertDialog.Builder build1 = new AlertDialog.Builder(context);	//Same method for building alert dialog for long click
		build1.setTitle("Enter latitude and longitude");
		LayoutInflater li = LayoutInflater.from(context);
		final View v1 = li.inflate(R.layout.place, null);
		build1.setView(v1);
		
		build1						
		.setCancelable(false)
		.setPositiveButton("Create",new DialogInterface.OnClickListener() {		//Set positive button actions
			public void onClick(DialogInterface dialog,int id) {
				
				
				
				EditText etLat = (EditText) v1.findViewById(R.id.edlat);		//Get text from id
				 EditText etlon = (EditText) v1.findViewById(R.id.edlon);
				 EditText name = (EditText) v1.findViewById(R.id.edname);		//Get text from id edt1
				 
				
				 
				 if(etlon.getText().toString().isEmpty()){			//Check if field is empty
					 Toast.makeText(getBaseContext(), "No Longitude entered", Toast.LENGTH_SHORT).show();
					 
					 etlon.requestFocus();
					 
					 return;			//Start from beginning
				 }
				 
				 else{
					 
					 lon1 = Double.parseDouble(etlon.getText().toString());
					 
				 }
				 
				 if(etLat.getText().toString().isEmpty()){
					 Toast.makeText(getBaseContext(), "No Latitude entered", Toast.LENGTH_SHORT).show();
					 
					 etLat.requestFocus();
					 
					 return;
				 } else{
					 lat1 = Double.parseDouble(etLat.getText().toString());
				 }
				
					
				 
				 latlon1 = new LatLng(lat1, lon1);			//Set latitude-longitude object
				 
			
				Gmap.addMarker(new MarkerOptions()				//Same as previous marker
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
				.title(name.getText().toString())
				.position(latlon1)
				.draggable(false)
				);
				
				 line = Gmap.addPolyline(new PolylineOptions()	//Set polyline...
			     .add(latlon, latlon1)							//draw line between two points
			     .width(5)										//Set width
			     .color(Color.RED));							//And colour
				}
				
			
		  })
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});
		
		AlertDialog alertDialog = build1.create();
		 
		// show it
		alertDialog.show();

	
	}

	
	private boolean TestGPlay(){			//Method to test if user as Google play services
		int stat = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);	//Integer will store the test
		if(stat == ConnectionResult.SUCCESS){
			
			 return(true);		//if the connection is a success, return true.
		 }else{
				Toast.makeText(this, "No Google Play Services available", Toast.LENGTH_SHORT).show(); 	//Display toast message
				
			}
		 return(false);
	}
	
	private void FindLocation(){			//Method used for finding user's location
		if(Gmap == null){
			Gmap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap(); //Store our map into variable Gmap
		 if(Gmap != null){		//If Gmap isn't empty
			 
			Gmap.setMyLocationEnabled(true);			
			Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);			
			
			 LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);		//Create new location manager
		
			 String provider = lm.getBestProvider(new Criteria(), true); //This is for finding the best provider
			 
			 if(provider == null){						//If there is no provider
				 onProviderDisabled(provider);			//Disable provider
			 }
			 
			 Location loc = lm.getLastKnownLocation(provider);			//Get current location
			 
			 if(loc != null){			//If current location is not null
				 onLocationChanged(loc);		//Change location to this location
			 }
			
			 
		 }
		}
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		//Latitude and Longitude
		double lat = location.getLatitude();
		double lon = location.getLongitude();
				
		latlon = new LatLng(lat, lon);
		
		Gmap.moveCamera(CameraUpdateFactory.newLatLng(latlon));		//Move camera to current position
		Gmap.animateCamera(CameraUpdateFactory.zoomTo(10));			//Zoom in
		Gmap.getUiSettings().setMyLocationButtonEnabled(true);		//Enable Google's built-in relocator button.
		
		TextView TextView = (TextView) findViewById(R.id.textview);		//Get text from textview id
		TextView.setText("Latitude:" + lat);							//Set the textview to be the current user's latitude
		
		TextView TextView2 = (TextView) findViewById(R.id.textview2);	//Same as above
		TextView2.setText("Longitude:" + lon);
		
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
