package ca.idi.tecla.framework;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public abstract class TeclaActivity extends Activity {

	private static final String ACTION_DISABLE_TECLA_IME = "ca.idi.tekla.sdk.action.DISABLE_TECLA_IME";
	private static final String ACTION_ENABLE_TECLA_IME = "ca.idi.tekla.sdk.action.ENABLE_TECLA_IME";

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle switchIntent = intent.getExtras();

			switch (switchIntent.getInt(SwitchEvent.EXTRA_INTENT)) {
			case 1:
				intentDown();
				break;
			case 2:
				intentUp();
				break;
			case 4:
				intentLeft();
				break;
			case 8:
				intentRight();
				break;
			case 16:
				intentButtonOne();
				break;
			case 32:
				intentButtonTwo();
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

	public abstract void intentLeft();

	public abstract void intentRight();

	public abstract void intentUp();

	public abstract void intentDown();

	public abstract void intentButtonOne();

	public abstract void intentButtonTwo();

	private void broadcastDisableTeclaIME() {
		Intent intent = new Intent(ACTION_DISABLE_TECLA_IME);
		sendBroadcast(intent);
	}

	private void broadcastEnableTeclaIME() {
		Intent intent = new Intent(ACTION_ENABLE_TECLA_IME);
		sendBroadcast(intent);
	}

}
