package cn.pdc.mobile.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class AppUtil {

	/**
	 * set no title and no status bar
	 * 
	 * @param activity
	 */
	public static void setNoTitleAndNoStatusBarScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * set no title
	 * 
	 * @param activity
	 */
	public static void setNotTitleScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
}
