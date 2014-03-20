package cn.pdc.mobile.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.pdc.mobile.R;
import cn.pdc.mobile.utils.Config;

/**
 * Behind Menu
 * 
 * @author zouliping
 * 
 */
public class BehindMenuFragment extends Fragment {

	private RadioGroup menuGroup;
	private View mainView;
	private TextView tv_uname;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_left_menu, null);
		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		menuGroup = (RadioGroup) mainView.findViewById(R.id.main_menu);
		menuGroup.setOnCheckedChangeListener(menuCheckedChangeListener);

		tv_uname = (TextView) mainView.findViewById(R.id.username_menu);
		tv_uname.setText(Config.uname);
	}

	private OnCheckedChangeListener menuCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			Fragment mContent = null;
			int titleId = 0;
			switch (checkedId) {
			case R.id.homepage_menu:
				mContent = new HomepageFragment();
				titleId = R.string.Homepage;
				break;
			case R.id.friends_menu:
				mContent = new FriendsFragment();
				titleId = R.string.Friends;
				break;
			case R.id.settings_menu:
				mContent = new SettingsFragement();
				titleId = R.string.Settings;
				break;
			default:
				break;
			}

			if (mContent != null && titleId != 0) {
				switchFragment(mContent, titleId);
			}
		}
	};

	/**
	 * switch Fragment
	 * 
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, int titleId) {
		if (getActivity() == null) {
			return;
		}

		if (getActivity() instanceof MainActivity) {
			MainActivity main = (MainActivity) getActivity();
			main.switchContent(fragment, titleId);
		}
	}
}
