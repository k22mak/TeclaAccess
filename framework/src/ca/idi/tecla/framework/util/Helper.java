package ca.idi.tecla.framework.util;

import ca.idi.tecla.framework.TeclaApp;
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
				TeclaApp.TAG);
		mKeyguardManager = (KeyguardManager) context
				.getSystemService(keyguardService);
		mKeyguardLock = mKeyguardManager
				.newKeyguardLock(TeclaApp.TAG);
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
			if (TeclaApp.DEBUG)
				Log.d(TeclaApp.TAG, "Aquiring temporal wake lock...");
			mWakeLock.acquire(length);
		} else {
			if (TeclaApp.DEBUG)
				Log.d(TeclaApp.TAG, "Aquiring wake lock...");
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
		if (TeclaApp.DEBUG)
			Log.d(TeclaApp.TAG, "Acquiring keyguard lock...");
		mKeyguardLock.disableKeyguard();
	}

	public static void releaseWakeLock() {
		if (TeclaApp.DEBUG)
			Log.d(TeclaApp.TAG, "Releasing wake lock...");
		mWakeLock.release();
	}

	public static void releaseKeyguardLock() {
		if (TeclaApp.DEBUG)
			Log.d(TeclaApp.TAG, "Releasing keyguard lock...");
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
