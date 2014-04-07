package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import cn.pdc.mobile.utils.HttpUtil;
import cn.pdc.mobile.view.CornerListView;

public class PrivacySettingActivity extends Activity {

	private Context mContext = PrivacySettingActivity.this;

	private List<String> list_friends;
	private CharSequence[] cs_friends;
	private List<Pair> list_type;
	private Pair pair;

	private CornerListView lv_corner;
	private DetailAdapter adapter;
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

		new getFriendsInfoTask().execute("");
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
			final Intent intent = new Intent(mContext,
					PrivacyClassSettingActivity.class);
			switch (position) {
			case 0:
				startActivity(intent);
				break;
			case 1:
				startActivity(intent);
				break;
			case 2:
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Choose Friends");
				builder.setMultiChoiceItems(cs_friends, null,
						new DialogInterface.OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								// TODO Auto-generated method stub

							}
						});
				builder.setPositiveButton(getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(intent);
							}
						});
				builder.setNegativeButton(getString(R.string.cancel), null);
				builder.show();
				break;
			}
		}
	};

	/**
	 * get friends list
	 * 
	 * @author zouliping
	 * 
	 */
	private class getFriendsInfoTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			return HttpUtil.doGet(Config.GET_FRIENDS_LIST + Config.uid);
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				// to avoid that server does not work or network is not
				// connected
				if (result == null) {
					return;
				}

				JSONObject jo = new JSONObject(result);
				list_friends = new ArrayList<String>();
				for (Iterator<?> i = jo.keys(); i.hasNext();) {
					JSONObject tmp = jo.getJSONObject((String) i.next());
					list_friends.add(tmp.getString("nick"));
				}
				Log.e("friends size", list_friends.size() + "");
				cs_friends = list_friends.toArray(new CharSequence[list_friends
						.size()]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
