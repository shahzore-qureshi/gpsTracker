//package com.shaq1nj.locationTracker;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.location.Location;
//import android.telephony.SmsManager;
//import android.util.Log;
//
//public class SendSMS extends BroadcastReceiver
//{
//	private String incoming;
//	private final SmsManager sms = SmsManager.getDefault();
//	
//	private TrackLocation locator;
//	private Location newLocation;
//	private double latitude;
//	private double longitude;
//	
//	private static final String LOG_TAG = "shaq1nj";
//	
//	public void onReceive(Context context, Intent intent) {
//		Log.v(LOG_TAG,intent.getAction());
//		if (intent.getAction().equals(SMSReceiver.SMS_INTENT))
//		{
//			Log.v(LOG_TAG, "GOT SMS INTENT");
//
//			getLocation(context);
//			
//			incoming = SMSReceiver.getIncomingNumber();
//			sendSMS();
//		}
//	}
//	
//	private void getLocation(Context context)
//	{
//		try{
//			locator = new TrackLocation(context);
//			locator.findLocation();
//			
//			Log.v(LOG_TAG, "waiting 60 seconds...");
//			
//			Thread.sleep(60000);
//		                
//			Log.v(LOG_TAG, "done waiting 60 seconds...");
//			
//			newLocation = locator.getSlowLocation();
//			
//			if (newLocation == null)
//				newLocation = locator.getQuickLocation();
//			
//			if (newLocation != null)
//			{
//				latitude = newLocation.getLatitude();
//		      	longitude = newLocation.getLongitude();
//			}
//		}
//		catch (Exception e)
//		{
//			Log.e(LOG_TAG, "Location Update Failed", e);
//		}
//	}
//	
//	public void sendSMS()
//	{
//		if (newLocation != null)
//		{
//			try{
//				if (!incoming.equals(""))
//				{
//					sms.sendTextMessage(incoming, null, 
//							"MAP: http://www.google.com/search?hl=en&source=hp&biw=1366&bih=642&q="
//							+ latitude + "%2C+" + longitude + "&aq=f&aqi=&aql=&oq=", null, null);
//					locator.removeUpdates();
//				}
//			}
//			catch (Exception e)
//			{
//				Log.e(LOG_TAG, "SMS Crash", e);
//			}
//		}
//	}
//}
