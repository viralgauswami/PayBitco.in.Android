package com.example.paybitco.service;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;


public class MyLocationService extends Service {

	double lat,lon;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("SERVICE", "STARTED");
		return START_STICKY;
	}
	public String getLocation()
    {
     // Get the location manager
     LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
     Criteria criteria = new Criteria();
     String bestProvider = locationManager.getBestProvider(criteria, false);
     Location location = locationManager.getLastKnownLocation(bestProvider);

	     try {
	         lat = location.getLatitude ();
	         lon = location.getLongitude ();
		} catch (Exception e) {
			// TODO: handle exception
	 		lat = -1.0;
	 		lon = -1.0;
		}
	     return lat+" , "+lon;
    }
}
