package cn.pdc.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import cn.pdc.mobile.R;
import cn.pdc.mobile.utils.Config;

/**
 * Splash
 * 
 * @author zouliping
 * 
 */
public class SplashScreen extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		// splash screen 2 seconds
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				SharedPreferences data = getSharedPreferences("data", 0);
				Intent intent = null;
				String uname = data.getString("uname", null);
				String uid = data.getString("uid", null);
				if (uname == null || uid == null) {
					intent = new Intent(SplashScreen.this, LoginActivity.class);
				} else {
					Config.uname = uname;
					Config.uid = uid;
					intent = new Intent(SplashScreen.this, MainActivity.class);
				}
				SplashScreen.this.startActivity(intent);
				SplashScreen.this.finish();
				// overridePendingTransition(R.anim.my_scale_action,
				// R.anim.my_alpha_action);
			}
		}, 2000);
	}
}
