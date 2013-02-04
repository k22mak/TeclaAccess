package ca.idi.tecla.framework;

import ca.idi.tecla.framework.util.Persistence;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.PowerManager;

public class TeclaApp extends Application
{
	
	//Constants

	/**
	 * Tag used for logging in the whole framework
	 */
	public static final String TAG = "TeclaFramework";
	
	/**
	 * Main debug switch, turns on/off debugging for the whole app
	 */
	public static final boolean DEBUG = true	;

	
	public static Persistence persistence;
	private PowerManager power_manager;
	private Boolean screen_on;

	@Override
	public void onCreate()
	{
		super.onCreate();
		init();
	}
	
	private void init()
	{
		TeclaStatic.logD("TECLA FRAMEWORK STARTING ON " + Build.MODEL + " BY " + Build.MANUFACTURER);
		
		power_manager = (PowerManager) getSystemService(Context.POWER_SERVICE);

		screen_on = true;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1)
		{
			screen_on = getScreenState();
		}
		TeclaStatic.logD("Screen on? " + screen_on);
		
		//Intents & Intent Filters
		registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
		
		persistence = new Persistence(this);
		
		TeclaStatic.startTeclaService(this);
	}

	// All intents will be processed here
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				screen_on = false;
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1)
				{
					screen_on = getScreenState();
				}
				TeclaStatic.logD("Screen on? " + screen_on);
			}
			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				screen_on = true;
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1)
				{
					screen_on = getScreenState();
				}
				TeclaStatic.logD("Screen on? " + screen_on);
			}
		}

	};
	
	public static Persistence getPersistence(){
		return persistence;
	}
	
	@TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
	private Boolean getScreenState() {
		return power_manager.isScreenOn();		
	}

}
