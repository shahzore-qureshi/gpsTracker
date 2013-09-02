package com.shaq1nj.locationTracker;

import org.jraf.android.backport.switchwidget.SwitchPreference;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
 
public class MainPreferencesActivity extends PreferenceActivity
{
	private static Location newLocation;
	private static final String TAG = "MainPreferences";
	private final SmsManager sms = SmsManager.getDefault();
	
	//A simple class for a static handler.
	private static class StaticHandler extends Handler{
		
	}
	
	private static StaticHandler smsHandler = null;
	
	private String smsResultToast;
	
	public static boolean isOnline;
	public static boolean hasOldEmail;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
        
        Preference sendSMS = (Preference) findPreference("sendSMS");
        sendSMS.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        	public boolean onPreferenceClick(Preference preference) {
        		
        		final ProgressDialog dialog1 = ProgressDialog.show(MainPreferencesActivity.this, "", 
                        "Getting location and sending SMS. Please wait 20 seconds for accuracy purposes...", true, false);
        		
        		smsHandler = new StaticHandler() {
                    public void handleMessage(Message msg) {
                            dialog1.dismiss();
                            Toast.makeText(MainPreferencesActivity.this, smsResultToast, Toast.LENGTH_LONG).show();
                    }
                    
        		};
        		
        		new Thread(new Runnable(){
        			public void run(){
        				Looper.prepare();
        				sendSMS();
        				smsHandler.sendEmptyMessage(0);
        			}
        			}).start();

                return true;
            }

        });
        
        SwitchPreference switchPreference = (SwitchPreference) findPreference("switch");
        switchPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Boolean newBool = (Boolean) newValue;
				try
				{
					if (newBool)
		        		startService(new Intent(getApplicationContext(), LocationTrackingService.class));
					else
						stopService(new Intent(getApplicationContext(), LocationTrackingService.class));
				}
        		catch (Exception e)
        		{
        			Log.e(TAG, "Problem with turning on/off updates", e);
        		}
				
				return true;
			}
		});
        
	}

	public void sendSMS()
	{
		getLocation();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String smsTo = prefs.getString("smsTo", "");
		
		if (newLocation != null)
		{
			try{
				if (!smsTo.equals(""))
				{
					sms.sendTextMessage(smsTo, null, 
							"MAP: http://www.google.com/search?hl=en&source=hp&biw=1366&bih=642&q="
							+ newLocation.getLatitude() + "%2C+" + newLocation.getLongitude() + "&aq=f&aqi=&aql=&oq=", null, null);
					smsResultToast = "SMS was sent successfully";
				}
				else
					smsResultToast = "SMS was not sent";
			}
			catch (Exception e)
			{
				smsResultToast = "There was a problem sending the SMS.";
				Log.e(TAG, "SMS Crash", e);
			}
		}
	}
	
	private void getLocation()
	{
		try{
			newLocation = LocationTrackingManager.getActiveInstance(getApplicationContext()).getCurrentLocation(20);
		}
		catch (Exception e)
		{
			newLocation = null;
			Log.e(TAG, "Location Update Failed", e);
		}
	}
}