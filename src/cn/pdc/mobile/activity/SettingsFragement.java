package cn.pdc.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import br.com.dina.ui.widget.UITableView;
import cn.pdc.mobile.R;

public class SettingsFragement extends Fragment {

	private Context mContext;

	private View mainView;
	private UITableView settings_tableView;
	private Button logout_btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_settings, null);
		mContext = getActivity();

		initView();
		return mainView;
	}

	public void initView() {
		settings_tableView = (UITableView) mainView
				.findViewById(R.id.settings_table);
		settings_tableView.addBasicItem(getString(R.string.pdc_setting));
		settings_tableView.addBasicItem(getString(R.string.privacy));
		settings_tableView.addBasicItem(getString(R.string.clear_cache));
		settings_tableView.addBasicItem(getString(R.string.about));
		settings_tableView.commit();

		logout_btn = (Button) mainView.findViewById(R.id.logout_btn);
		logout_btn.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.logout_btn:
				SharedPreferences data = mContext.getSharedPreferences("data",
						0);
				Editor editor = data.edit();
				editor.clear();
				editor.commit();

				Intent i = new Intent(mContext, LoginActivity.class);
				startActivity(i);
				break;
			default:
				break;
			}
		}
	};
}
