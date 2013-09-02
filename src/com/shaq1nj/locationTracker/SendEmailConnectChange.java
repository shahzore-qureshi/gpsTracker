//package com.shaq1nj.locationTracker;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.location.Location;
//import android.util.Log;
////import android.widget.Toast;
//
//public class SendEmailConnectChange extends BroadcastReceiver
//{
//	private String username2;
//	private String password2;
//	private String subject2;
//	private String sendTo2;
//	private String smtp2;
//	private String port2;
//	private boolean ssl2;
//	
//	private Location newLocation2;
//	private double latitude2;
//	private double longitude2;
//	
////	private boolean isOnline = false;
//	
//	private static final String LOG_TAG = "shaq1nj";
//	
//	public void onReceive(Context context, Intent intent) {
////		if (intent.getAction().equals(ConnectionChangeReceiver.CONNECTION_INTENT))
////		{
////			Log.v(LOG_TAG, "GOT CONNECTION INTENT");
////			Toast.makeText(context, "CONNECTION INTENT", Toast.LENGTH_LONG).show();
////
////			if (Preferences.hasOldEmail) //isOnline
////			{
////				Toast.makeText(context, "Old Mail being Sent", Toast.LENGTH_LONG).show();
////				Log.v(LOG_TAG, "OLD MAIL BEING SENT");
////				getOldPrefs();
////				sendEmail();
////				Preferences.hasOldEmail = false;
////			}
////			
////		}
////		this.abortBroadcast();
//	}
//	
//	/*protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		checkInternetConnection();
//		Log.v(LOG_TAG, "GOT CONNECTION INTENT");
//		Toast.makeText(this, "CONNECTION INTENT", Toast.LENGTH_LONG).show();
//
//		if (Preferences.hasOldEmail) //isOnline
//		{
//			Toast.makeText(this, "Old Mail being Sent", Toast.LENGTH_LONG).show();
//
//			getOldPrefs();
//			sendEmail();
//			Preferences.hasOldEmail = false;
//		}
//		finish();
//	}*/
//	/*public void checkInternetConnection(Context context) {
//	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//	    // test for connection
//	    if (cm.getActiveNetworkInfo() != null
//	            && cm.getActiveNetworkInfo().isAvailable()
//	            && cm.getActiveNetworkInfo().isConnected()) {
//	        isOnline = true;
//	    } else {
//	        Log.v(LOG_TAG, "Internet Connection Not Present");
//			Toast.makeText(context, "No Internet", Toast.LENGTH_LONG).show();
//
//	        isOnline = false;
//	    }
//	}*/
//
////	private void getOldPrefs()
////	{
////		username2 = SendEmail.username2;
////        password2 = SendEmail.password2;
////        subject2 = SendEmail.subject2;
////        sendTo2 = SendEmail.sendTo2;
////        smtp2 = SendEmail.smtp2;
////        port2 = SendEmail.port2;
////        ssl2 = SendEmail.ssl2;
////        
////        newLocation2 = SendEmail.newLocation2;
////        latitude2 = SendEmail.latitude2;
////        longitude2 = SendEmail.longitude2;
////	}
//	
//	public void sendEmail()
//	{
//		if (newLocation2 != null)
//		{
//  			Mail m = new Mail(username2, password2);
//  			m.setHost(smtp2);
//  			m.setPort(port2);
//  			m.setSocks(port2);
//  			m.setAuth(ssl2);
//  			
//  			String[] toArr = {sendTo2};
//  			m.setTo(toArr); 
//  			m.setFrom(username2); 
//  			m.setSubject(subject2); 
//  			m.setBody("Latitude: " + latitude2 + "\nLongitude: " + longitude2
//  					+ "\nMAP: http://www.google.com/search?hl=en&source=hp&biw=1366&bih=642&q="
//  					+ latitude2 + "%2C+" + longitude2 + "&aq=f&aqi=&aql=&oq="); 
//
//  			try 
//  			{ 
//  				//    m.addAttachment("/sdcard/filelocation"); 
//  				if(m.send()) 
//  				{
//  					//sent
//  				} 
//  				else 
//  				{
//  				}
//  			} 
//  			catch(Exception e) 
//  			{ 
//  				Log.e(LOG_TAG, "Could not send email", e); 
//  			}
//		}
//	}
//}
