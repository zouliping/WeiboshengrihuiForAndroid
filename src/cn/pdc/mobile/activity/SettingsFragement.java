package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.DetailAdapter;
import cn.pdc.mobile.entity.Pair;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.view.CornerListView;

/**
 * Settings
 * 
 * @author zouliping
 * 
 */
public class SettingsFragement extends Fragment {

	private Context mContext;

	private View mainView;
	private Button logout_btn;
	private CornerListView cornerListView;
	private DetailAdapter adapter;
	private List<Pair> nameList;
	private Pair pair;

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
		cornerListView.setOnItemClickListener(itemClickListener);
		nameList = new ArrayList<Pair>();

		pair = new Pair(getString(R.string.pdc_setting), "");
		nameList.add(pair);
		pair = new Pair(getString(R.string.developer), "");
		nameList.add(pair);
		pair = new Pair(getString(R.string.github), "");
		nameList.add(pair);
		pair = new Pair(getString(R.string.feedback), "");
		nameList.add(pair);

		adapter = new DetailAdapter(mContext, nameList, 0);
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
				SharedPreferences data = mContext.getSharedPreferences("wdata",
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

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				Intent i = new Intent(mContext, PrivacySettingActivity.class);
				startActivity(i);
				break;
			case 1:
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle(getString(R.string.developer));
				builder.setMessage(Config.DEVELOPER_EMAIL);
				builder.setNegativeButton(getString(R.string.cancel), null);
				builder.show();
				break;
			case 2:
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(Config.URL_GITHUB));
				startActivity(intent);
				break;
			case 3:
				AppUtil.openWeiciyuanActivity(mContext,
						Config.WEICIYUAN_PACKAGE_NAME,
						Config.WEICIYUAN_ACTIIVTY_NAME,
						"#Feedback of Weiboshengrihui# " + Build.BRAND + " "
								+ Build.MODEL + ", Android "
								+ Build.VERSION.RELEASE);
				break;
			default:
				break;
			}
		}
	};
}
