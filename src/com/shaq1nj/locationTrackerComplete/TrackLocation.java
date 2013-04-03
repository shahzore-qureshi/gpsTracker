package com.shaq1nj.locationTrackerComplete;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class TrackLocation
{
	boolean locationChange;
	Context myContext;
	Location newLocation;
	LocationManager locationManager;
	LocationListener locationListener;
	String whichProvider;
	private static final String LOG_TAG = "shaq1nj";

	
	public TrackLocation(Context myContext)
	{
		this.myContext = myContext;
	}
	
	public void findLocation()
	{
		locationManager =  (LocationManager) myContext.getSystemService(Context.LOCATION_SERVICE);
		
		locationListener = new LocationListener()
        {
        	public void onLocationChanged(Location location)
        	{
        		newLocation = location;
        		locationChange = true;
        	}
        	public void onStatusChanged(String provider, int status, Bundle extras) {}

        	public void onProviderEnabled(String provider)
        	{
        		
        	}

        	public void onProviderDisabled(String provider)
        	{
        		Log.v(LOG_TAG, provider);
        	}
        };
        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
//     	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
	public Location getSlowLocation()
	{
		if (locationChange == true)
		{
			return newLocation;
		}
		else
			return null;
	}
	
	public Location getQuickLocation()
	{
		List<String> providers = locationManager.getProviders(true);

        newLocation = null;
        for (int i = providers.size()-1; i >= 0; i--)
        {
                newLocation = locationManager.getLastKnownLocation(providers.get(i));
                if (newLocation != null) 
                	break;
        }
        
        return newLocation;
	}
	
	public void removeUpdates()
	{
		locationManager.removeUpdates(locationListener);
	}
	
}
