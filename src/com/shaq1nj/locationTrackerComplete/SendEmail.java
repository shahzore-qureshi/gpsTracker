package com.shaq1nj.locationTrackerComplete;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.util.Log;

public class SendEmail extends BroadcastReceiver
{
	private String username;
	private String password;
	private String subject;
	private String sendTo;
	private String smtp;
	private String port;
	private boolean ssl;
	
	private TrackLocation locator;
	private Location newLocation;
	private double latitude;
	private double longitude;
	
	public static String username2;
	public static String password2;
	public static String subject2;
	public static String sendTo2;
	public static String smtp2;
	public static String port2;
	public static boolean ssl2;
	
	public static Location newLocation2;
	public static double latitude2;
	public static double longitude2;
	
	private boolean isOnline = false;
//	public static boolean hasOldEmail = false;
	
	private static final String LOG_TAG = "shaq1nj";
	
	private Context context;
	
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMSReceiver.EMAIL_INTENT))
		{
			this.context = context;
			checkInternetConnection();
			
			Log.v(LOG_TAG, "GOT EMAIL INTENT");
			getPrefs();
			getLocation(context);
			
			if (isOnline)
			{
				sendEmail();
				locator.removeUpdates();
			}
			else
			{
				setOldPrefs();
				//Preferences.hasOldEmail = true;

			}
				
				
		}
		this.abortBroadcast();

	}

	public void checkInternetConnection() {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
	
	private void getLocation(Context context)
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

	private void getPrefs()
	{
        username = Preferences.getUsername();
        password = Preferences.getPassword();
        subject = Preferences.getSubject();
        sendTo = Preferences.getSendTo();
        smtp = Preferences.getSMTP();
        port = Preferences.getPort();
        ssl = Preferences.isSsl();
	}
	
	private void setOldPrefs()
	{
		 username2 = Preferences.getUsername();
		 password2 = Preferences.getPassword();
		 subject2 = Preferences.getSubject();
		 sendTo2 = Preferences.getSendTo();
		 smtp2 = Preferences.getSMTP();
		 port2 = Preferences.getPort();
		 ssl2 = Preferences.isSsl();
	}
	
	public void sendEmail()
	{
		if (newLocation != null)
		{
  			Mail m = new Mail(username, password);
  			m.setHost(smtp);
  			m.setPort(port);
  			m.setSocks(port);
  			m.setAuth(ssl);
  			
  			String[] toArr = {sendTo};
  			m.setTo(toArr); 
  			m.setFrom(username); 
  			m.setSubject(subject); 
  			m.setBody("Latitude: " + latitude + "\nLongitude: " + longitude
  					+ "\nMAP: http://www.google.com/search?hl=en&source=hp&biw=1366&bih=642&q="
  					+ latitude + "%2C+" + longitude + "&aq=f&aqi=&aql=&oq="); 

  			try 
  			{ 
  				//    m.addAttachment("/sdcard/filelocation"); 
  				if(m.send()) 
  				{
  					//sent
  				} 
  				else 
  				{
  				}
  			} 
  			catch(Exception e) 
  			{ 
  				Log.e(LOG_TAG, "Could not send email", e); 
  			}
  			
  			
		}
	}
}
