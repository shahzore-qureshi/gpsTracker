package com.shaq1nj.locationTrackerComplete;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
 
public class Preferences extends PreferenceActivity
{
	private TrackLocation locator;
	private static Location newLocation;
	private static double latitude;
	private static double longitude;
	private Context context = this;
	private static final String LOG_TAG = "shaq1nj";
	private Mail m;
	private final SmsManager sms = SmsManager.getDefault();
	private static String username;
	private static String password;
	private static String subject;
	private static String sendTo;
	private static String smtp;
	private static String port;
	private static boolean ssl;
	private static String smsTo;
	
	private String sendEmail;
	private String sendSMS;
	private String turnOff;
	private String clearAll;
	
	private static String keyemail;
	private static String keysms;
	
	private String toast1 = "";
	private String toast2 = "";
	
	private static class IncomingHandler extends Handler {
		//No overrides.
	};
	
	private IncomingHandler handler1;
	private IncomingHandler handler2;
	
	public static boolean isOnline;
	public static boolean hasOldEmail;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		getPrefs();
		
		
		Preference sendEmail = (Preference) findPreference("sendEmail");
        sendEmail.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        	public boolean onPreferenceClick(Preference preference) {
        		
        		final ProgressDialog dialog1 = ProgressDialog.show(Preferences.this, "", 
                        "Sending Email. Please wait 60 seconds...", true, false);
        		
        		

        		handler1 = new IncomingHandler() {
                    public void handleMessage(Message msg) {
                            dialog1.dismiss();
                            Toast.makeText(Preferences.this, toast1, Toast.LENGTH_LONG).show();
                    }
        		};
        		
        		new Thread(new Runnable(){
        			public void run(){
        				Looper.prepare();
        				sendEmail();
        				handler1.sendEmptyMessage(0);
        			}
        			}).start();
        		
                SharedPreferences customSharedPreference = getSharedPreferences(
                		"myCustomSharedPrefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = customSharedPreference.edit();
                editor.putString("sendEmailPref", "The Email preference has been clicked");
                editor.commit();
                return true;
            }

        });
        
        Preference sendSMS = (Preference) findPreference("sendSMS");
        sendSMS.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        	public boolean onPreferenceClick(Preference preference) {
        		
        		final ProgressDialog dialog1 = ProgressDialog.show(Preferences.this, "", 
                        "Sending SMS. Please wait 60 seconds...", true, false);
        		
        		handler2 = new IncomingHandler() {
                    public void handleMessage(Message msg) {
                            dialog1.dismiss();
                            Toast.makeText(Preferences.this, toast2, Toast.LENGTH_LONG).show();
                    }
        		};
        		
        		new Thread(new Runnable(){
        			public void run(){
        				Looper.prepare();
        				sendSMS();
        				handler2.sendEmptyMessage(0);
        			}
        			}).start();
        		
                SharedPreferences customSharedPreference = getSharedPreferences(
                		"myCustomSharedPrefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = customSharedPreference.edit();
                editor.putString("sendSMSPref", "The SMS preference has been clicked");
                editor.commit();
                return true;
            }

        });
        
        Preference turnOff = (Preference) findPreference("turnOff");
        turnOff.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        	public boolean onPreferenceClick(Preference preference) {
        		try{
	        		//locator.removeUpdates();
	        		finish();
        		}
        		catch (Exception e)
        		{
        			Log.e(LOG_TAG, "Problem with canceling updates", e);
        		}
        		
                SharedPreferences customSharedPreference = getSharedPreferences(
                		"myCustomSharedPrefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = customSharedPreference.edit();
                editor.putString("turnOffPref", "The TurnOff preference has been clicked");
                editor.commit();
                return true;
            }

        });
        
        Preference clearAll = (Preference) findPreference("clearAll");
        clearAll.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        	public boolean onPreferenceClick(Preference preference) {
        		try{
        			SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());
        			
        			Editor editor = prefs.edit();
        			editor.clear();
        			editor.commit();
        			finish();
        		}
        		catch (Exception e)
        		{
        			Log.e(LOG_TAG, "Problem with clearing settings", e);
        		}
        		
                SharedPreferences customSharedPreference = getSharedPreferences(
                		"myCustomSharedPrefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = customSharedPreference.edit();
                editor.putString("clearAllPref", "The clearAll preference has been clicked");
                editor.commit();
                return true;
            }

        });
	}
	protected void onStop()
	{
		super.onStop();
		getPrefs();
	}
	
	protected void OnDestroyed()
	{
		getPrefs();
//		locator.removeUpdates();
	}
	
	public void checkInternetConnection() {
	    ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
	    // test for connection
	    if (cm.getActiveNetworkInfo() != null
	            && cm.getActiveNetworkInfo().isAvailable()
	            && cm.getActiveNetworkInfo().isConnected()) {
	        isOnline = true;
	    } else {
	        Log.v(LOG_TAG, "Internet Connection Not Present");
	        isOnline = false;
	    }
	}
	
	public void getPrefs() {
        // Get the xml/preferences.xml preferences
        SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
        
        username = (prefs.getString("username", ""));
        password =(prefs.getString("password", ""));
        subject =(prefs.getString("subject", ""));
        sendTo =(prefs.getString("sendTo", ""));
        smtp = (prefs.getString("smtp", ""));
        port =(prefs.getString("port", ""));
        ssl =(prefs.getBoolean("ssl", false));
        smsTo =(prefs.getString("smsTo", ""));
        keyemail =(prefs.getString("keyemail", ""));
        keysms =(prefs.getString("keysms", ""));

        SharedPreferences mySharedPreferences = getSharedPreferences(
                "myCustomSharedPrefs", Activity.MODE_PRIVATE);
        sendEmail = mySharedPreferences.getString("sendEmail", "");
        sendSMS = mySharedPreferences.getString("sendSMS", "");
        turnOff = mySharedPreferences.getString("turnOff", "");
        clearAll = mySharedPreferences.getString("clearAll", "");
        
        
        
	}
	
	private void getLocation()
	{
		try{
			locator = new TrackLocation(context);
			locator.findLocation();
			
			Log.v(LOG_TAG, "waiting 60 seconds...");
			
			Thread.sleep(60000);
		                
			Log.v(LOG_TAG, "done waiting 60 seconds...");
			
			newLocation = locator.getSlowLocation();
			
			if (newLocation == null)
				newLocation = locator.getQuickLocation();
			
			if (newLocation != null)
			{
				latitude = newLocation.getLatitude();
		      	longitude = newLocation.getLongitude();
			}
		}
		catch (Exception e)
		{
			Log.e(LOG_TAG, "Location Update Failed", e);
		}
	}
	
	public void sendEmail()
	{
		getLocation();
		getPrefs();
		checkInternetConnection();
		if (newLocation != null && isOnline)
		{
  			m = new Mail(getUsername(), getPassword());
  			m.setHost(getSMTP());
  			m.setPort(getPort());
  			m.setSocks(getPort());
  			m.setAuth(isSsl());
  			
  			String[] toArr = {getSendTo()};
  			m.setTo(toArr); 
  			m.setFrom(getUsername()); 
  			m.setSubject(getSubject()); 
  			m.setBody("Latitude: " + latitude + "\nLongitude: " + longitude
  					+ "\nMAP: http://www.google.com/search?hl=en&source=hp&biw=1366&bih=642&q="
  					+ latitude + "%2C+" + longitude + "&aq=f&aqi=&aql=&oq="); 

  			try 
  			{ 
  				//    m.addAttachment("/sdcard/filelocation"); 
  				if(m.send()) 
  				{ 
  					toast1 = "Email was sent successfully.";
  				} 
  				else 
  				{ 
  					toast1 = "Email was not sent.";
  				}
  			} 
  			catch(Exception e) 
  			{ 
  				toast1 = "There was a problem sending the email.";
  				Log.e(LOG_TAG, "Could not send email", e); 
  			}
		}
		else
			toast1 = "No Internet Connection. Please enable internet.";
	}
	
	public void sendSMS()
	{
		getLocation();
		getPrefs();
		if (newLocation != null)
		{
			try{
				if (!getSmsTo().equals(""))
				{
					sms.sendTextMessage(getSmsTo(), null, 
							"MAP: http://www.google.com/search?hl=en&source=hp&biw=1366&bih=642&q="
							+ latitude + "%2C+" + longitude + "&aq=f&aqi=&aql=&oq=", null, null);
					toast2 = "SMS was sent successfully";
				}
				else
					toast2 = "SMS was not sent";
			}
			catch (Exception e)
			{
  				toast2 = "There was a problem sending the SMS.";
				Log.e(LOG_TAG, "SMS Crash", e);
			}
		}
	}

	public static Location getNewLocation()
	{
		return newLocation;
	}
	
	public static double getLatitude()
	{
		return latitude;
	}
	
	public static double getLongitude()
	{
		return longitude;
	}
	
	public static String getUsername()
	{
		return username;
	}
	public static String getPassword()
	{
		return password;
	}
	public static String getSubject()
	{
		return subject;
	}
	public static String getSendTo()
	{
		return sendTo;
	}
	public static String getSMTP()
	{
		return smtp;
	}
	public static String getPort()
	{
		return port;
	}
	public static boolean isSsl()
	{
		return ssl;
	}
	public static String getSmsTo()
	{
		return smsTo;
	}

	public String getSendEmail()
	{
		return sendEmail;
	}

	public String getSendSMS()
	{
		return sendSMS;
	}

	public String getTurnOff()
	{
		return turnOff;
	}
	
	public static String getKeyemail()
	{
		return keyemail;
	}

	public static String getKeysms()
	{
		return keysms;
	}
	
	public String getClearAll()
	{
		return clearAll;
	}
	
	public static boolean isOnline()
	{
		return isOnline;
	}
}