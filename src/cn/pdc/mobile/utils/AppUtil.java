package cn.pdc.mobile.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class AppUtil {

	/**
	 * 设置全屏无标题栏且无状态栏
	 * 
	 * @param activity
	 */
	public static void setNoTitleAndNoStatusBarScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
