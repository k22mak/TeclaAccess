package ca.idi.tecla.framework;

import ca.idi.tecla.framework.util.Helper;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class TeclaActivity extends Activity {
	
	private static final String ACTION_DISABLE_TECLA_IME = "ca.idi.tekla.sdk.action.DISABLE_TECLA_IME";
	private static final String ACTION_ENABLE_TECLA_IME = "ca.idi.tekla.sdk.action.ENABLE_TECLA_IME";

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
	
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle switchIntent = intent.getExtras();
			String tempIntent = switchIntent
					.getString(SwitchEvent.EXTRA_INTENT);
			Log.i(TeclaApp.TAG, " " + tempIntent);

			switch (Integer.parseInt(tempIntent)) {
			case 1:
				intentDown();
				break;
			case 2:
				intentUp();
				break;
			case 3:
				intentLeft();
				break;
			case 4:
				intentRight();
				break;
			default:
				break;
			}

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(
				SwitchEvent.ACTION_SWITCH_EVENT_RECEIVED));
		Log.i("Registering Receiver", "Registering Receiver");
		broadcastDisableTeclaIME();
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);	
		broadcastEnableTeclaIME();
	}

	public void intentLeft() {
	}

	public void intentRight() {
	}

	public void intentUp() {
	}

	public void intentDown() {
	}
	
	private void broadcastDisableTeclaIME(){
		Intent intent = new Intent(ACTION_DISABLE_TECLA_IME);
		sendBroadcast (intent);
	}
	private void broadcastEnableTeclaIME(){
		Intent intent = new Intent(ACTION_ENABLE_TECLA_IME);
		sendBroadcast (intent);
	}

}
