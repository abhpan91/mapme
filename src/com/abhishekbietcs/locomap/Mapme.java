package com.abhishekbietcs.locomap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Mapme extends FragmentActivity implements LocationListener {
	GoogleMap googleMap;
	SharedPreferences pfr;
	double latitude;
	double longitude;
	String googlegives = "https://maps.googleapis.com/maps/api/place/search/json?";
	Double lon;
	Double lat;
	String provider;
	Criteria criteria;
	LocationManager locationManager;
	String lctn;
	String key;
	String type;
	String radius;
	String sensor;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("create", "0");
		setContentView(R.layout.activity_mapme);
		boolean b = isOnline();
		if (b == false) {
			Toast.makeText(getApplicationContext(),
					"please connect yor device to internet", Toast.LENGTH_LONG)
					.show();
		} else {
			SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			Log.d("create", "1");
			googleMap = mapFragment.getMap();
			Log.d("create", "2");
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.setMyLocationEnabled(true);
			Log.d("create", "3");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		pfr = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String view = pfr.getString("list", "abcd");
		if (view.contentEquals("satalite")) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		}
		if (view.contentEquals("trrain")) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		}
		if (view.contentEquals("hybrid")) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}
		if (view.contentEquals("normal")) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_mapme, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.MAPme:
			
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(50);
			int status = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getBaseContext());

			// Showing status
			if (status != ConnectionResult.SUCCESS) { // Google Play Services
														// are not available

				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
						this, requestCode);
				dialog.show();

			} else { // Google Play Services are available

				// Enabling MyLocation Layer of Google Map
				

				// Getting LocationManager object from System Service
				// LOCATION_SERVICE
				 locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

				// Creating a criteria object to retrieve provider
				 criteria = new Criteria();

				// Getting the name of the best provider
				 provider=locationManager.getBestProvider(criteria,false);

				// Getting Current Location
				Location location = locationManager.getLastKnownLocation(provider);

				if (location != null) {
					onLocationChanged(location);
				}

				locationManager
						.requestLocationUpdates(provider,
								Long.parseLong(pfr.getString("time", "20000")),
								1, this);

			}
			new Background().execute("abc");
			
			break;
		case R.id.aboutus:
			Intent i = new Intent("android.intent.action.ABOUT");
			startActivity(i);

			break;
		case R.id.prefrences:

			Intent s = new Intent("android.intent.action.VIEWPREFRENCE");
			startActivity(s);
			break;
		}
		return true;
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// Getting latitude of the current location
		 latitude = location.getLatitude();

		// Getting longitude of the current location
		 longitude = location.getLongitude();

		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);

		// Showing the current location in Google Map
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		// Zoom in the Google Map
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		googleMap.addMarker(new MarkerOptions().position(latLng)
				.title(pfr.getString("name", "abhishek").toString())
				.snippet("this is your location"));

	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()
				&& cm.getActiveNetworkInfo().isAvailable()) {
			return true;
		} else {
			return false;
		}
	}
 
public class Background extends AsyncTask<String,Integer,String>{

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		pfr = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		 lctn = "location="+latitude+","+longitude;
		 String api="AIzaSyDcLMS0IKQL3N76rO-aD2thfO46r96OCQI";
		 key = "&key="+api;
		 type = "&types="+pfr.getString("type","food");
		 radius = "&radius="+pfr.getString("radius","500");
		 sensor="&sensor=false";
		StringBuilder requesturl = new StringBuilder(googlegives);
		requesturl.append(lctn);
		requesturl.append(radius);
		requesturl.append(type);
		requesturl.append(sensor);
		requesturl.append(key);
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet req = new HttpGet(requesturl.toString());
		
		Log.d("create", requesturl.toString());

		try {
			HttpResponse res = client.execute(req);
			HttpEntity jsonentity = res.getEntity();
			String data = EntityUtils.toString(jsonentity);
			JSONObject jsonobj = new JSONObject(data);
			JSONArray resarray = jsonobj.getJSONArray("results");
			if (resarray.length() == 0) {

				Toast.makeText(getApplicationContext(), "nothing found",
						Toast.LENGTH_LONG).show();

			}

			else {

				int len = resarray.length();

				for (int j = 0; j < len; j++)

				{

					lon = resarray.getJSONObject(j)
							.getJSONObject("geometry")
							.getJSONObject("location").getDouble("lng");
					lat = resarray.getJSONObject(j)
							.getJSONObject("geometry")
							.getJSONObject("location").getDouble("lat");
					LatLng latLng = new LatLng(lat, lon);
					googleMap.addMarker(new MarkerOptions()
							.position(latLng)
							.title(resarray.getJSONObject(j)
									.getJSONObject("name").toString())
							.snippet(
									resarray.getJSONObject(j)
											.getJSONObject(
													"formatted_address")
											.toString()));

				}

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return null;
	}
	
}











}
