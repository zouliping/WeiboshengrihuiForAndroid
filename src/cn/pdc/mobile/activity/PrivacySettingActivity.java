package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.DetailAdapter;
import cn.pdc.mobile.entity.Pair;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.view.CornerListView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class PrivacySettingActivity extends Activity {

	private Context mContext = PrivacySettingActivity.this;
	private CornerListView lv_corner;
	private DetailAdapter adapter;
	private List<Pair> list_type;
	private Pair pair;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.setNotTitleScreen(PrivacySettingActivity.this);
		setContentView(R.layout.activity_privacy_setting);

		initData();
		initViews();
	}

	private void initViews() {
		lv_corner = (CornerListView) findViewById(R.id.setting_list);
		lv_corner.setOnItemClickListener(itemClickListener);

		adapter = new DetailAdapter(mContext, list_type, 0);
		lv_corner.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void initData() {
		list_type = new ArrayList<Pair>();
		pair = new Pair(getString(R.string.public_visible), "");
		list_type.add(pair);
		pair = new Pair(getString(R.string.service_visible), "");
		list_type.add(pair);
		pair = new Pair(getString(R.string.friends_visible), "");
		list_type.add(pair);
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			}
		}
	};
}
