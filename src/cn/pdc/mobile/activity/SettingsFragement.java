package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.List;

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
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.UserDetailAdapter;
import cn.pdc.mobile.entity.Pairs;
import cn.pdc.mobile.view.CornerListView;

public class SettingsFragement extends Fragment {

	private Context mContext;

	private View mainView;
	private Button logout_btn;
	private CornerListView cornerListView;
	private UserDetailAdapter adapter;
	private List<Pairs> nameList;
	private Pairs pairs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_settings, null);
		mContext = getActivity();

		initView();
		return mainView;
	}

	public void initView() {
		cornerListView = (CornerListView) mainView
				.findViewById(R.id.setting_list);
		nameList = new ArrayList<Pairs>();

		pairs = new Pairs(getString(R.string.pdc_setting), "");
		nameList.add(pairs);
		pairs = new Pairs(getString(R.string.privacy), "");
		nameList.add(pairs);
		pairs = new Pairs(getString(R.string.clear_cache), "");
		nameList.add(pairs);
		pairs = new Pairs(getString(R.string.about), "");
		nameList.add(pairs);

		adapter = new UserDetailAdapter(mContext,nameList);
		cornerListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

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
