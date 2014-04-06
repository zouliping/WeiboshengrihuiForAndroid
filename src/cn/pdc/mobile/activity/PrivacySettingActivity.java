package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.DetailAdapter;
import cn.pdc.mobile.entity.Pair;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.view.CornerListView;

public class PrivacySettingActivity extends Activity {

	private Context mContext = PrivacySettingActivity.this;
	private CornerListView lv_corner;
	private DetailAdapter adapter;
	private List<Pair> list_type;
	private Pair pair;
	private ImageView btn_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.setNotTitleScreen(PrivacySettingActivity.this);
		setContentView(R.layout.activity_privacy_setting);

		initData();
		initViews();
	}

	private void initViews() {
		lv_corner = (CornerListView) findViewById(R.id.privacy_type_list);
		lv_corner.setOnItemClickListener(itemClickListener);

		adapter = new DetailAdapter(mContext, list_type, 0);
		lv_corner.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		btn_back = (ImageView) findViewById(R.id.back_btn);
		btn_back.setOnClickListener(btnClickListener);
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

	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				finish();
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
			Intent intent = new Intent(mContext,
					PrivacyClassSettingActivity.class);
			switch (position) {
			case 0:
				startActivity(intent);
				break;
			case 1:
				startActivity(intent);
				break;
			case 2:
				startActivity(intent);
				break;
			}
		}
	};
}
