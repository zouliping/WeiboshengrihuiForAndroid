package cn.pdc.mobile.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import cn.pdc.mobile.R;

import com.slidingmenu.lib.SlidingMenu;

/**
 * Main Activity
 * 
 * @author zouliping
 * 
 */
public class MainActivity extends BaseActivity {

	private Fragment mContent;
	private Fragment menuFragment;

	public MainActivity() {
		super(R.string.Homepage);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager manager = getSupportFragmentManager();

		if (savedInstanceState != null) {
			mContent = manager.getFragment(savedInstanceState, "mContent");
			menuFragment = manager.findFragmentById(R.id.menu_frame);
		}
		if (mContent == null) {
			mContent = new HomepageFragment();
			menuFragment = new BehindMenuFragment();
		}

		// above view
		setContentView(R.layout.frame_content);
		manager.beginTransaction().replace(R.id.content_frame, mContent)
				.commit();

		// behind view
		setBehindContentView(R.layout.frame_menu);
		manager.beginTransaction().replace(R.id.menu_frame, menuFragment)
				.commit();

		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	/**
	 * switch the fragment
	 * 
	 * @param fragment
	 */
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

	/**
	 * switch the fragment
	 * 
	 * @param fragment
	 * @param title
	 */
	public void switchContent(Fragment fragment, int title) {
		switchContent(fragment);
		setTitle(title);
	}

}
