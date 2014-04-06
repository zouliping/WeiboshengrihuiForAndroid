package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ViewSwitcher;
import cn.pdc.mobile.R;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;

public class PrivacyClassSettingActivity extends Activity {

	private Context mContext = PrivacyClassSettingActivity.this;
	private ArrayList<HashMap<String, String>> list_classes;

	private ViewSwitcher vs_class;
	private ListView lv_class;
	private SimpleAdapter adapter;
	private ImageView btn_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.setNotTitleScreen(PrivacyClassSettingActivity.this);
		setContentView(R.layout.activity_privacy_class_setting);

		initData();
		initViews();
	}

	private void initViews() {
		vs_class = (ViewSwitcher) findViewById(R.id.class_vs);
		lv_class = new ListView(mContext);
		lv_class.setCacheColorHint(Color.argb(0, 0, 0, 0));
		lv_class.setDivider(getResources().getDrawable(
				R.drawable.list_divider_line));
		lv_class.setDividerHeight(1);
		lv_class.setSelector(R.drawable.list_item_selector);

		adapter = new SimpleAdapter(mContext, list_classes,
				R.layout.list_item_privacy_class, new String[] { "title" },
				new int[] { R.id.title });
		lv_class.setAdapter(adapter);

		vs_class.addView(lv_class);
		vs_class.addView(getLayoutInflater().inflate(
				R.layout.layout_progress_page, null));
		vs_class.showNext();

		btn_back = (ImageView) findViewById(R.id.back_btn);
		btn_back.setOnClickListener(listener);
	}

	private void initData() {
		list_classes = new ArrayList<HashMap<String, String>>();
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

	private class getClassInfoTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				JSONObject jo = new JSONObject(
						HttpUtil.doGet(Config.GET_CLASS_INFO_INFO));
				Log.e("get all class", jo.toString());
				JSONArray ja = jo.getJSONArray("classes");
				Integer len = ja.length();
				HashMap<String, String> map = null;
				for (int i = 0; i < len; i++) {
					map = new HashMap<String, String>();
					map.put("title", ja.getString(i));
					list_classes.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			adapter.notifyDataSetChanged();
			vs_class.showNext();
		}
	}
}
