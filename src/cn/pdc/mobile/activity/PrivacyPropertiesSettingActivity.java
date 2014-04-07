package cn.pdc.mobile.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewSwitcher;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.PrivacyPropertyAdapter;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;

public class PrivacyPropertiesSettingActivity extends Activity {

	private Context mContext = PrivacyPropertiesSettingActivity.this;
	private ArrayList<String> list_pro;
	private ArrayList<Boolean> list_selected;

	private ViewSwitcher vs_pro;
	private ListView lv_pro;
	private PrivacyPropertyAdapter adapter;
	private ImageView btn_back;

	private String classname;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.setNotTitleScreen(PrivacyPropertiesSettingActivity.this);
		setContentView(R.layout.activity_privacy_pro_setting);

		initData();
		initViews();
	}

	private void initViews() {
		vs_pro = (ViewSwitcher) findViewById(R.id.pro_vs);
		lv_pro = new ListView(mContext);
		lv_pro.setCacheColorHint(Color.argb(0, 0, 0, 0));
		lv_pro.setDivider(getResources().getDrawable(
				R.drawable.list_divider_line));
		lv_pro.setDividerHeight(1);
		lv_pro.setSelector(R.drawable.list_item_selector);
		lv_pro.setOnItemClickListener(itemClickListener);

		adapter = new PrivacyPropertyAdapter(mContext, list_pro, list_selected);
		lv_pro.setAdapter(adapter);

		vs_pro.addView(lv_pro);
		vs_pro.addView(getLayoutInflater().inflate(
				R.layout.layout_progress_page, null));
		vs_pro.showNext();

		btn_back = (ImageView) findViewById(R.id.back_btn);
		btn_back.setOnClickListener(listener);
	}

	private void initData() {
		classname = getIntent().getStringExtra("classname");
		list_pro = new ArrayList<String>();
		list_selected = new ArrayList<Boolean>();
		new getClassInfoTask().execute("");
	}

	private OnClickListener listener = new OnClickListener() {

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
			if (position == 0) {
				Integer len = list_selected.size();
				Boolean tmp = !list_selected.get(0);
				list_selected.set(0, tmp);
				for (int i = 1; i < len; i++) {
					list_selected.set(i, tmp);
				}
				adapter.notifyDataSetChanged();
			} else {
				// if it is true, turn it to false. if it is false, turn it to
				// true.
				list_selected.set(position, !list_selected.get(position));
				adapter.notifyDataSetChanged();
			}
		}
	};

	private class getClassInfoTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				JSONObject jo = new JSONObject(
						HttpUtil.doGet(Config.GET_PROPERTIES_INFO + classname));
				Log.e("get all class", jo.toString());
				JSONArray ja = jo.getJSONArray(classname);
				Integer len = ja.length();
				// it means user selected all properties
				list_pro.add("All Properties");
				list_selected.add(false);
				for (int i = 0; i < len; i++) {
					list_pro.add(ja.getString(i));
					list_selected.add(false);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			adapter.notifyDataSetChanged();
			vs_pro.showNext();
		}
	}

}
