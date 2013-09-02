package com.shaq1nj.locationTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
//import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver
{
	
  public static final String CONNECTION_INTENT = "com.shaq1nj.locationtracker.SendEmailConnectChange";
  private static final String LOG_TAG = "shaq1nj";

  public void onReceive( Context context, Intent intent )
  {
    /*ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
    NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(     ConnectivityManager.TYPE_MOBILE );
    
    if ( activeNetInfo != null )
    {
      Toast.makeText( context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
      Intent email1 = new Intent();
      email1.setAction(CONNECTION_INTENT);
      context.sendBroadcast(email1);
      
      Intent email = new Intent(context, SendEmailConnectChange.class);
      email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(email);
    }
    else if( mobNetInfo != null )
    {
      Toast.makeText( context, "Mobile Network Type : " + mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
      Intent email2 = new Intent();
      email2.setAction(CONNECTION_INTENT);
      context.sendBroadcast(email2);
      
      Intent email = new Intent(context, SendEmailConnectChange.class);
      email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(email);
    }*/
	  
	  ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    // test for connection
	    if (cm.getActiveNetworkInfo() != null
	            && cm.getActiveNetworkInfo().isAvailable()
	            && cm.getActiveNetworkInfo().isConnected()) {
	    	Log.v(LOG_TAG, "SHAQ: INTERNET CONNECTION FOUND");
	//    	Toast.makeText(context, "Internet found", Toast.LENGTH_SHORT).show();
	    	
	        Intent connect = new Intent();
	        connect.setAction(CONNECTION_INTENT);
	        context.sendBroadcast(connect);
	        Log.v(LOG_TAG, "BROADCAST CONNECT SENT");
	//        Toast.makeText(context, "BROADCAST CONNECT SENT", Toast.LENGTH_SHORT).show();
	    } else {
	        Log.v(LOG_TAG, "SHAQ: INTERNET NOT PRESENT");
	//		Toast.makeText(context, "No Internet", Toast.LENGTH_LONG).show();

	    }
  }
}