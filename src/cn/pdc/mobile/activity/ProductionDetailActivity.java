package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.ProductionAdapter;
import cn.pdc.mobile.entity.Production;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;

public class ProductionDetailActivity extends Activity {

	private Context mContext = ProductionDetailActivity.this;
	private List<Production> list_production;
	private ListView lv_production;
	private ProductionAdapter adapter;
	private Production production;

	private ImageView btn_back;
	private ImageView btn_tao;
	private TextView tv_title;

	private String title_activity;
	private String uid;
	private String title;
	private String type;
	private String description;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.setNotTitleScreen(ProductionDetailActivity.this);
		setContentView(R.layout.activity_production_detail);

		initData();
		tv_title = (TextView) findViewById(R.id.production_title);
		if ("Goods".equals(title_activity)) {
			tv_title.setText(getString(R.string.Have));
		} else if ("WishItem".equals(title_activity)) {
			tv_title.setText(getString(R.string.Want));
		}
		initViews();
		new getBasicInfoTask().execute("");
	}

	private void initViews() {
		lv_production = (ListView) findViewById(R.id.production_list);
		adapter = new ProductionAdapter(mContext, list_production);
		lv_production.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		btn_back = (ImageView) findViewById(R.id.back_btn);
		btn_tao = (ImageView) findViewById(R.id.tao_btn);
		btn_back.setOnClickListener(listener);
		btn_tao.setOnClickListener(listener);
	}

	private void initData() {
		Intent intent = getIntent();
		uid = intent.getStringExtra("uid");
		title_activity = intent.getStringExtra("item");

		list_production = new ArrayList<Production>();
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				finish();
				break;
			case R.id.tao_btn:
				AppUtil.openApp(mContext, "com.taobao.taobao");
				break;
			default:
				break;
			}
		}
	};

	private class getBasicInfoTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			Log.e("url",
					Config.GET_PROPERTITY.replace("$classname", title_activity)
							+ uid);
			return HttpUtil.doGet(Config.GET_PROPERTITY.replace("$classname",
					title_activity) + uid);
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
				for (Iterator<?> i = jo.keys(); i.hasNext();) {
					String key = (String) i.next();
					JSONObject tmp = jo.getJSONObject(key);
					if (!tmp.isNull("title")) {
						title = tmp.getString("title");
					} else {
						title = getString(R.string.undefined);
					}
					if (!tmp.isNull("goods_type")) {
						type = tmp.getString("goods_type");
					} else {
						type = getString(R.string.undefined);
					}
					if (!tmp.isNull("description")) {
						description = tmp.getString("description");
					} else {
						description = getString(R.string.undefined);
					}
					production = new Production(title, type, description);
					list_production.add(production);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				adapter.notifyDataSetChanged();
			}
		}
	}
}
