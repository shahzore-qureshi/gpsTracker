package com.shaq1nj.locationTracker;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

/**
 * LocationTrackingManager is a simple way to force the {@link LocationManager} to update
 * itself to the users current position. Once 1 update occurs any further updates
 * are cancelled.
 *
 */
public class LocationTrackingManager {

	private static String TAG = "LocationTrackingManager";
	
	private static LocationTrackingManager instance = null;
	private static Context mContext = null;

	private static LocationManager locationManager;
	private static LocationListener locationListener;  

	// Holds the most up to date location.
	private volatile static Location mLocation;

	private static boolean locationAvailable = false;
	private static boolean isRegistered = false;

	public static synchronized LocationTrackingManager getActiveInstance(Context context){
		if (instance == null)
		{
			registerLocationListeners(context);
			instance = new LocationTrackingManager();
			mContext = context;
		}

		return instance;
	}

	private LocationTrackingManager(){

	}

	public synchronized boolean isRegistered(){
		return isRegistered;
	}

	public synchronized boolean hasLocation(){
		return mLocation != null;
	}

	/**
	 * Request location updates.
	 * 
	 * @param ctx The current Context
	 * @return True if we registered the location updates successfully. False on failure.
	 */
	private synchronized static boolean registerLocationListeners(Context ctx) {
		Log.d(TAG,"[LocationTrackingManager] register location listener");

		if(locationManager == null){
			locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
			Log.d(TAG,"[LocationTrackingManager] obtain service");
		}

		Criteria locationAccuracy = new Criteria();
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			locationAccuracy.setAccuracy(Criteria.ACCURACY_FINE);
		else
			locationAccuracy.setAccuracy(Criteria.ACCURACY_COARSE);
		
		if (locationListener == null)
			createLocationListeners();

		try{
			final String bestProvider = locationManager.getBestProvider(locationAccuracy, true);
			locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
			isRegistered = true;
			Log.d(TAG,"[LocationTrackingManager] registered using: "+bestProvider);
		}catch(IllegalArgumentException ill){
			//use location services is not turned on
			Log.e(TAG,"[LocationTrackingManager] location services not enabled "+ill.toString());
			isRegistered = false;
			return false;
		}catch(Exception e){
			Log.e(TAG,"[LocationTrackingManager] unable to obtain current location "+e.toString());
			e.printStackTrace();
			isRegistered = false;
			return false;
		}
		return true;
	}

	/**
	 * Unregister location updates.
	 * 
	 */
	private synchronized static void removeLocationListeners(){
		isRegistered = false;
		if(BuildConfig.DEBUG)Log.d(TAG, "[LocationTrackingManager] remove location listeners");

		if(locationListener != null){
			locationManager.removeUpdates(locationListener);
			locationListener = null;
		}
	}

	/**
	 *  Creates a LocationListener that will cancel itself after one update
	 */
	private synchronized static void createLocationListeners() {
		Log.d(TAG,"[LocationTrackingManager] create location listeners");
		locationListener = new LocationListener() {
			public void onStatusChanged(String provider, int status, Bundle extras) {

				Log.d(TAG,"[LocationTrackingManager] listener onStatusChange: "+provider+", status: "+status);

				switch(status) {
				case LocationProvider.OUT_OF_SERVICE:
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					locationAvailable = false;
					break;
				case LocationProvider.AVAILABLE:
					break;
				}
			}
			public void onProviderEnabled(String provider) {
			}
			public void onProviderDisabled(String provider) {
				Log.d(TAG,"[LocationTrackingManager] listener provider disabled: "+provider);
				locationAvailable = false;
			}
			public void onLocationChanged(Location location) {
				//got their location, remove the listener
				mLocation = location;
				Log.d(TAG, "[LocationTrackingManager] listener onLocationChanged: "+ mLocation.getProvider()+" updated: "+ new Date(mLocation.getTime())+" accuracy: "+mLocation.getAccuracy());
				locationAvailable = true;
				Log.i(TAG,"[LocationTrackingManager] listener removing updates");
				removeLocationListeners();
			}
		};
	}

	/**
	 * Waits for the registered provider to update by sleeping the currently executing thread
	 * in small incremental values. If the provider updates before our timeout is reached,
	 * then return that location. Otherwise return the last known location from all enabled
	 * providers.
	 * 
	 * @param timeOut In seconds, the maximum amount of time to wait before returning
	 * @return The current or last known location. Null on failure
	 */
	public Location getCurrentLocation(int timeOut){ //in seconds

		if(! isRegistered()) return getBestLastKnownLocation(mContext);


		final long lTimeOut = 1000*timeOut;

		//Wait for location provider to return an update
		Log.d(TAG,"[LocationTrackingManager] getCurrentLocation is waiting for location");
		long timeBegin = System.currentTimeMillis();
		long timeEnd = timeBegin;
		boolean flag = false;
		locationAvailable = false;
		Location bestLoc = null;

		try{
			while(!flag && !locationAvailable){
				//waiting...
				timeEnd = System.currentTimeMillis();

				if(timeEnd-timeBegin > lTimeOut){ 
					Log.i(TAG,"[LocationTrackingManager] exceeded timeout for getCurrentLocation, use last known");
					flag=true;

					Criteria criteria = new Criteria();

					List<String> providers = locationManager.getProviders(criteria, false);
					for(String provider : providers){
						Log.d(TAG, "[LocationTrackingManager] Checking Provider: "+ provider);

						Location location = locationManager.getLastKnownLocation(provider);
						if(location!=null){
							Log.d(TAG, "[LocationTrackingManager] Provider: "+ provider+" updated: "+new Date(location.getTime())+" accuracy: "+location.getAccuracy());

							if(bestLoc==null){
								bestLoc = location;
							}else{
								//is this location more recent?
								//do we care about accuracy here?
								if(location.getTime() > bestLoc.getTime()) 
									bestLoc = location;
							}
						}
					}

					if(locationManager == null){
						Log.e(TAG, "[LocationTrackingManager] locationManager == null");
					}

					if(bestLoc == null){
						Log.e(TAG, "[LocationTrackingManager] loc == null, use backup method");
						bestLoc = getBestLastKnownLocation(mContext);
					}
				}
				Thread.sleep(20); //sleep for 20ms, allow other work to be done while waiting 
			}

			if(!flag){
				Log.d(TAG, "[LocationTrackingManager] Found location, Provider: "+ mLocation.getProvider()+" updated: "+ new Date(mLocation.getTime())+" accuracy: "+mLocation.getAccuracy());
				bestLoc = mLocation;
			}

			removeLocationListeners();
			return bestLoc;

		}catch(Exception e){
			//null here in emulator. does it happen on a device?
			e.printStackTrace();
			Log.e(TAG,"[LocationTrackingManager] location is null " + e.toString());
			removeLocationListeners();
			return null;
		}
	}

	/**
	 * Gets the most recent location from all enabled providers.
	 * No accuracy requirements are imposed
	 * 
	 * @return The last known location of the device
	 */
	private static Location getBestLastKnownLocation(Context ctx){
		LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.NO_REQUIREMENT);

		Location bestLoc = null;

		List<String> providers = locationManager.getProviders(criteria, false);
		for(String provider : providers){
			Location location = locationManager.getLastKnownLocation(provider);
			if(location!=null){

				if(bestLoc==null){
					bestLoc = location;
				}else{
					//is this location more recent?
					//do we care about accuracy here?
					if(location.getTime() > bestLoc.getTime()) 
						bestLoc = location;
				}
			}
		}

		Log.d(TAG, "[LocationTrackingManager] getBestLastKnownLocation: "+ bestLoc.getProvider()+" updated: "+ new Date(bestLoc.getTime())+" accuracy: "+bestLoc.getAccuracy());

		removeLocationListeners();
		return bestLoc;
	}
}