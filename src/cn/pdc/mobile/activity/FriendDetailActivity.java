package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.view.CornerListView;

/**
 * Friend Detail
 * 
 * @author zouliping
 * 
 */
public class FriendDetailActivity extends Activity {

	private Context mContext = FriendDetailActivity.this;
	private CornerListView cornerListView;
	private DetailAdapter adapter;
	private List<Pair> nameList;
	private Pair pair;

	private ImageView btn_back;
	private ImageView btn_calendar;

	// private String uid;
	private String nickname;
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
		Intent intent = getIntent();
		// uid = intent.getStringExtra("uid");
		birthday = intent.getStringExtra("birthday");
		location = intent.getStringExtra("location");
		interesting = intent.getStringExtra("interesting");
		nickname = intent.getStringExtra("nickname");
		if (intent.getBooleanExtra("gender", true)) {
			gender = getString(R.string.Male);
		} else {
			gender = getString(R.string.Female);
		}

		want = ">";
		have = ">";

		nameList = new ArrayList<Pair>();

		pair = new Pair(getString(R.string.Nickname), nickname);
		nameList.add(pair);
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
		cornerListView.setOnItemClickListener(itemClickListener);
		adapter = new DetailAdapter(mContext, nameList, 1);
		cornerListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		btn_back = (ImageView) findViewById(R.id.back_btn);
		btn_back.setOnClickListener(btnClickListener);
		btn_calendar = (ImageView) findViewById(R.id.back_calendar);
		btn_calendar.setOnClickListener(btnClickListener);
	}

	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				finish();
				break;
			case R.id.back_calendar:
				// AppUtil.openApp(FriendDetailActivity.this,
				// "cn.pdc.calendar");
				String tmp = birthday.substring(birthday.indexOf("-"));
				int year = Calendar.getInstance().get(Calendar.YEAR);
				JSONObject jo = new JSONObject();
				try {
					jo.put("a_title", "To celebrate " + nickname
							+ "'s birthday");
					jo.put("a_start", year + tmp);
					jo.put("a_end", year + tmp);
					jo.put("a_description", "To celebrate " + nickname
							+ "'s birthday");
					jo.put("a_location", location);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// MyActivity myActivity = new MyActivity("To celebrate "
				// + nickname + "'s birthday", year + tmp, year + tmp,
				// "To celebrate " + nickname + "'s birthday", location);
				AppUtil.openActivity(mContext, Config.CALENDAR_PACKAGE_NAME,
						Config.CALENDAR_ACTIVITY_NAME, jo.toString());
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
			case 5:
				Intent intent = new Intent(mContext,
						ProductionDetailActivity.class);
				intent.putExtra("uname", nickname);
				intent.putExtra("item", "WishItem");
				startActivity(intent);
				break;
			case 6:
				Intent intent2 = new Intent(mContext,
						ProductionDetailActivity.class);
				intent2.putExtra("uname", nickname);
				intent2.putExtra("item", "Goods");
				intent2.putExtra("isMe", false);
				startActivity(intent2);
				break;
			default:
				break;
			}
		}
	};
}
