package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.DetailAdapter;
import cn.pdc.mobile.entity.Pair;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.view.CornerListView;

public class FriendDetailActivity extends Activity {

	private Context mContext = FriendDetailActivity.this;
	private CornerListView cornerListView;
	private DetailAdapter adapter;
	private List<Pair> nameList;
	private Pair pair;

	private ImageView btn_back;

	private String birthday;
	private String location;
	private String interesting;
	private String gender;
	private String want;
	private String have;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.setNotTitleScreen(FriendDetailActivity.this);
		setContentView(R.layout.activity_friend_detail);

		initData();
		initViews();
	}

	private void initData() {
		birthday = getString(R.string.undefined);
		location = getString(R.string.undefined);
		interesting = getString(R.string.undefined);
		gender = getString(R.string.undefined);
		want = getString(R.string.undefined);
		have = getString(R.string.undefined);

		nameList = new ArrayList<Pair>();

		pair = new Pair(getString(R.string.Gender), gender);
		nameList.add(pair);
		pair = new Pair(getString(R.string.Birthday), birthday);
		nameList.add(pair);
		pair = new Pair(getString(R.string.Location), location);
		nameList.add(pair);
		pair = new Pair(getString(R.string.Interesting), interesting);
		nameList.add(pair);
		pair = new Pair(getString(R.string.Want), want);
		nameList.add(pair);
		pair = new Pair(getString(R.string.Have), have);
		nameList.add(pair);
	}

	private void initViews() {
		cornerListView = (CornerListView) findViewById(R.id.friend_detail_list);
		adapter = new DetailAdapter(mContext, nameList, 1);
		cornerListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		btn_back = (ImageView) findViewById(R.id.back_btn);
		btn_back.setOnClickListener(btnClickListener);
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
}
