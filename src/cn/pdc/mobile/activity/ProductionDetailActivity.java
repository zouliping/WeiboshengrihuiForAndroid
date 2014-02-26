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
import android.widget.ListView;
import android.widget.TextView;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.ProductionAdapter;
import cn.pdc.mobile.entity.Production;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;
import cn.pdc.mobile.utils.ToastUtil;

public class ProductionDetailActivity extends Activity {

	private Context mContext = ProductionDetailActivity.this;
	private List<Production> list_production;
	private Production production;
	private List<String> list_friends;
	private CharSequence[] cs_friends;

	private ListView lv_production;
	private ProductionAdapter adapter;
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
		initViews();

		new getBasicInfoTask().execute("");
		new getFriendsInfoTask().execute("");
	}

	private void initViews() {
		lv_production = (ListView) findViewById(R.id.production_list);
		adapter = new ProductionAdapter(mContext, list_production);
		lv_production.setAdapter(adapter);
		lv_production.setOnItemClickListener(itemClickListener);
		adapter.notifyDataSetChanged();

		btn_back = (ImageView) findViewById(R.id.back_btn);
		btn_tao = (ImageView) findViewById(R.id.tao_btn);
		btn_back.setOnClickListener(listener);
		btn_tao.setOnClickListener(listener);

		tv_title = (TextView) findViewById(R.id.production_title);
		if ("Goods".equals(title_activity)) {
			tv_title.setText(getString(R.string.Have));
			btn_tao.setVisibility(View.VISIBLE);
		} else if ("WishItem".equals(title_activity)) {
			tv_title.setText(getString(R.string.Want));
			btn_tao.setVisibility(View.GONE);
		}
	}

	private void initData() {
		Intent intent = getIntent();
		uid = intent.getStringExtra("uid");
		title_activity = intent.getStringExtra("item");

		list_production = new ArrayList<Production>();
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ToastUtil.showShortToast(mContext, position + "---");
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(getString(R.string.sendTo));
			builder.setSingleChoiceItems(cs_friends, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							ToastUtil.showShortToast(mContext, "test");
						}
					});
			builder.setPositiveButton(getString(R.string.yes), null);
			builder.setNegativeButton(getString(R.string.cancel), null);
			builder.show();
		}
	};

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				finish();
				break;
			case R.id.tao_btn:
				AppUtil.openApp(mContext, Config.TAOBAO_PACKAGE_NAME);
				break;
			default:
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
					list_friends.add((String) i.next());
				}
				cs_friends = list_friends.toArray(new CharSequence[list_friends
						.size()]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * get production list
	 * 
	 * @author zouliping
	 * 
	 */
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
