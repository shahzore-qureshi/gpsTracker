package com.shaq1nj.locationTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver
{
	private String messageBody;
	private static String incomingNumber;
	private String keyemail;
	private String keysms;
	public static final String EMAIL_INTENT = "com.shaq1nj.locationtracker.SendEmail";
	public static final String SMS_INTENT = "com.shaq1nj.locationtracker.SendSMS";
	private static final String LOG_TAG = "shaq1nj";
		
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		Object messages[] = (Object[]) bundle.get("pdus");
		SmsMessage smsMessage[] = new SmsMessage[messages.length];
		
		for (int n = 0; n < messages.length; n++) 
		{
			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		}
		
		messageBody = smsMessage[0].getMessageBody();
		incomingNumber = smsMessage[0].getOriginatingAddress();
		
		
		getPrefs();
		
		Log.v(LOG_TAG,"SMS body: " + messageBody);
		Log.v(LOG_TAG,"SMS number: " + incomingNumber);
		Log.v(LOG_TAG,"Secret email keyword: " + getKeyemail());
		Log.v(LOG_TAG,"Secret sms keyboard: " + getKeysms());

		if (messageBody.equals(getKeyemail()))
		{
			Intent email = new Intent();
			email.setAction(EMAIL_INTENT);
			context.sendBroadcast(email);
	        Log.v(LOG_TAG, "EMAIL TIME");
		}
		else if (messageBody.equals(getKeysms()))
		{
			Intent sms = new Intent();
			sms.setAction(SMS_INTENT);
			context.sendBroadcast(sms);
	        Log.v(LOG_TAG, "SMS TIME");
		}
		
		this.abortBroadcast();
	}

	private void getPrefs() {
        //keyemail = MainPreferencesActivity.getKeyemail();
        //keysms = MainPreferencesActivity.getKeysms();
	}
	
	public String getMessageBody()
	{
		return messageBody;
	}
	
	public static String getIncomingNumber()
	{
		return incomingNumber;
	}
	
	public String getKeyemail()
	{
		return keyemail;
	}

	public String getKeysms()
	{
		return keysms;
	}
}
