package com.shaq1nj.locationTracker;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.content.Intent;

public class Driver extends Activity 
{
	private Thread mSplashThread;
	
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
	    
	    // The thread to wait for splash screen events
	    mSplashThread =  new Thread(){
	        public void run(){
	            try {
	                synchronized(this){
	                    // Wait given period of time or exit on touch
	                    wait(5000);
	                }
	            

	            finish();
	            
	            // Run next activity
	            Intent settingsActivity = new Intent(getBaseContext(), Preferences.class);
	            startActivity(settingsActivity);
	            stop();
	            }
	            catch(Exception ex){                    
	            }
	        }
	    };
	    
	    mSplashThread.start();        
	}
    
/**
 * Processes splash screen touch events
 */
@Override
public boolean onTouchEvent(MotionEvent evt)
{
    if(evt.getAction() == MotionEvent.ACTION_DOWN)
    {
        synchronized(mSplashThread){
            mSplashThread.notifyAll();
        }
    }
    return true;
}    
}