package ca.idi.tecla.framework.util;

import ca.idi.tecla.framework.SwitchEventProvider;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

public class Helper {
	private static PowerManager mPowerManager;
	private static WakeLock mWakeLock;
	private static KeyguardLock mKeyguardLock;
	private static Handler mHandler;
	private static KeyguardManager mKeyguardManager;
	private static AudioManager mAudioManager;
	private static final int WAKE_LOCK_TIMEOUT = 5000;

	public static void initManagers(Context context, String powerService,
			String keyguardService, String audioService) {
		mAudioManager = (AudioManager) context.getSystemService(audioService);
		mPowerManager = (PowerManager) context.getSystemService(powerService);
		mWakeLock = mPowerManager.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.FULL_WAKE_LOCK
						| PowerManager.ON_AFTER_RELEASE,
				SwitchEventProvider.TAG);
		mKeyguardManager = (KeyguardManager) context
				.getSystemService(keyguardService);
		mKeyguardLock = mKeyguardManager
				.newKeyguardLock(SwitchEventProvider.TAG);
	}

	public static void cancelFullReset(Runnable mFullResetRunnable) {
		mHandler.removeCallbacks(mFullResetRunnable);
	}

	/**
	 * Hold wake lock until releaseWakeLock() is called.
	 */
	public static void holdWakeLock() {
		holdWakeLock(0);
	}

	/**
	 * Hold wake lock for the number of seconds specified by length
	 * 
	 * @param length
	 *            the number of seconds to hold the wake lock for
	 */
	public static void holdWakeLock(long length) {
		if (length > 0) {
			if (SwitchEventProvider.DEBUG)
				Log.d(SwitchEventProvider.TAG, "Aquiring temporal wake lock...");
			mWakeLock.acquire(length);
		} else {
			if (SwitchEventProvider.DEBUG)
				Log.d(SwitchEventProvider.TAG, "Aquiring wake lock...");
			mWakeLock.acquire();
		}
		pokeUserActivityTimer();
	}

	/**
	 * Wakes and unlocks the screen for a minimum of {@link WAKE_LOCK_TIMEOUT}
	 * miliseconds
	 */
	public static void wakeUnlockScreen() {
		holdKeyguardLock();
		holdWakeLock(WAKE_LOCK_TIMEOUT);
	}

	public static void holdKeyguardLock() {
		if (SwitchEventProvider.DEBUG)
			Log.d(SwitchEventProvider.TAG, "Acquiring keyguard lock...");
		mKeyguardLock.disableKeyguard();
	}

	public static void releaseWakeLock() {
		if (SwitchEventProvider.DEBUG)
			Log.d(SwitchEventProvider.TAG, "Releasing wake lock...");
		mWakeLock.release();
	}

	public static void releaseKeyguardLock() {
		if (SwitchEventProvider.DEBUG)
			Log.d(SwitchEventProvider.TAG, "Releasing keyguard lock...");
		mKeyguardLock.reenableKeyguard();
	}

	public static void pokeUserActivityTimer() {
		mPowerManager.userActivity(SystemClock.uptimeMillis(), true);
	}

	public static String byte2Hex(int bite) {
		return String.format("0x%02x", bite);
	}

	public void showToast(String msg, Context context) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void showToast(int resid, Context context) {
		Toast.makeText(context, resid, Toast.LENGTH_LONG).show();
	}
	
	public static void useSpeakerphone() {
		mAudioManager.setMode(AudioManager.MODE_IN_CALL);
		mAudioManager.setSpeakerphoneOn(true);
	}
	
	public void stopUsingSpeakerPhone() {
		mAudioManager.setMode(AudioManager.MODE_NORMAL);
		mAudioManager.setSpeakerphoneOn(false);
	}


}
