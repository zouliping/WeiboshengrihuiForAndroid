package cn.pdc.mobile.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import cn.pdc.mobile.R;

import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Basic Activity
 * 
 * @author zouliping
 * 
 */
public class BaseActivity extends SlidingFragmentActivity {

	private int titleRes; // titlebar

	public BaseActivity(int titleRes) {
		this.titleRes = titleRes;
	}

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(titleRes);

		// sliding menu
		SlidingMenu sm = getSlidingMenu();
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		// set android icon to null
		getActionBar().setIcon(android.R.color.transparent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
