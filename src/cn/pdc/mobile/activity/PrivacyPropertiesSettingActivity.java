package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.SimpleAdapter;
import android.widget.ViewSwitcher;
import cn.pdc.mobile.R;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;

public class PrivacyPropertiesSettingActivity extends Activity {

	private Context mContext = PrivacyPropertiesSettingActivity.this;
	private ArrayList<HashMap<String, String>> list_pros;

	private ViewSwitcher vs_pro;
	private ListView lv_pro;
	private SimpleAdapter adapter;
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

		adapter = new SimpleAdapter(mContext, list_pros,
				R.layout.list_item_privacy_class, new String[] { "title" },
				new int[] { R.id.title });
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
		list_pros = new ArrayList<HashMap<String, String>>();
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
			Intent intent = getIntent();
			startActivity(intent);
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
				HashMap<String, String> map = null;
				for (int i = 0; i < len; i++) {
					map = new HashMap<String, String>();
					map.put("title", ja.getString(i));
					list_pros.add(map);
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
