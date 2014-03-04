package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.ViewSwitcher;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.ProductionAdapter;
import cn.pdc.mobile.entity.Production;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;
import cn.pdc.mobile.utils.StringUtil;
import cn.pdc.mobile.utils.ToastUtil;

public class ProductionDetailActivity extends Activity {

	private Context mContext = ProductionDetailActivity.this;
	private List<Production> list_production;
	private Production production;
	private List<String> list_friends;
	private CharSequence[] cs_friends;
	private List<String> list_production_name;

	private ViewSwitcher vs_production;
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

	private Integer sendToIndex = 0;

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
		vs_production = (ViewSwitcher) findViewById(R.id.production_vs);
		lv_production = new ListView(mContext);
		lv_production.setCacheColorHint(Color.argb(0, 0, 0, 0));
		lv_production.setDivider(getResources().getDrawable(
				R.drawable.list_divider_line));
		lv_production.setDividerHeight(1);
		lv_production.setSelector(R.drawable.list_item_selector);

		adapter = new ProductionAdapter(mContext);
		lv_production.setAdapter(adapter);

		vs_production.addView(lv_production);
		vs_production.addView(getLayoutInflater().inflate(
				R.layout.layout_progress_page, null));
		vs_production.showNext();

		btn_back = (ImageView) findViewById(R.id.back_btn);
		btn_tao = (ImageView) findViewById(R.id.tao_btn);
		btn_back.setOnClickListener(listener);
		btn_tao.setOnClickListener(listener);

		tv_title = (TextView) findViewById(R.id.production_title);
		if ("Goods".equals(title_activity)) {
			tv_title.setText(getString(R.string.Have));
			btn_tao.setVisibility(View.VISIBLE);
			lv_production.setOnItemClickListener(itemClickListener);
		} else if ("WishItem".equals(title_activity)) {
			tv_title.setText(getString(R.string.Want));
			btn_tao.setVisibility(View.GONE);
		}
	}

	private void initData() {
		Intent intent = getIntent();
		uid = intent.getStringExtra("uid");
		title_activity = intent.getStringExtra("item");
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(getString(R.string.sendTo));
			builder.setSingleChoiceItems(cs_friends, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							sendToIndex = which;
							Log.e("selected index", sendToIndex + "");
						}
					});
			builder.setPositiveButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.e("in btn --- selected index", sendToIndex + "");
							new sendGoodsTask().execute(
									list_production_name.get(position),
									position + "",
									cs_friends[sendToIndex].toString());
						}
					});
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
	 * remove spec goods
	 * 
	 * @author zouliping
	 * 
	 */
	private class sendGoodsTask extends AsyncTask<String, String, Boolean> {

		ProgressDialog dlg;

		@Override
		protected void onPreExecute() {
			dlg = new ProgressDialog(mContext);
			dlg.setTitle("Sending");
			dlg.setMessage("Please wait for a monent...");
			dlg.setCancelable(false);
			dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dlg.show();

		}

		@Override
		protected Boolean doInBackground(String... params) {
			String present = params[0];
			Integer index = Integer.parseInt(params[1]);
			String to = params[2];
			JSONObject jo = new JSONObject();

			try {
				jo.put("indivname", present);
				jo.put("uid", Config.uid);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				JSONObject reJsonObject = new JSONObject(HttpUtil.doPut(
						Config.REMOVE_URL, jo));
				if ((Boolean) reJsonObject.get("result")) {
					jo.put("classname", "Goods");
					jo.put("individualname",
							present + System.currentTimeMillis());
					jo.put("uid", to);
					jo.put("title", list_production.get(index).getTitle());
					jo.put("goods_type", list_production.get(index).getType());
					jo.put("description", list_production.get(index)
							.getDescription());

					Log.e("send present", jo.toString());

					reJsonObject = new JSONObject(HttpUtil.doPut(
							Config.UPDATE_INFO, jo));
					return (Boolean) reJsonObject.get("result");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dlg.dismiss();
			if (result == false) {
				ToastUtil.showShortToast(mContext, "Failed");
			} else {
				ToastUtil.showShortToast(mContext, "Success");
				ProductionDetailActivity.this.finish();
			}
		}
	}

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
				Log.e("friends size", list_friends.size() + "");
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

				list_production = new ArrayList<Production>();
				list_production_name = new ArrayList<String>();
				JSONObject jo = new JSONObject(result);
				for (Iterator<?> i = jo.keys(); i.hasNext();) {
					String key = (String) i.next();
					list_production_name.add(key);
					JSONObject tmp = jo.getJSONObject(key);
					if (!tmp.isNull("title")) {
						title = StringUtil.removeSpecialChar(tmp
								.getString("title"));
					} else {
						title = getString(R.string.undefined);
					}
					if (!tmp.isNull("goods_type")) {
						type = StringUtil.removeSpecialChar(tmp
								.getString("goods_type"));
					} else {
						type = getString(R.string.undefined);
					}
					if (!tmp.isNull("description")) {
						description = StringUtil.removeSpecialChar(tmp
								.getString("description"));
					} else {
						description = getString(R.string.undefined);
					}
					production = new Production(title, type, description);
					list_production.add(production);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				adapter.setProductionList(list_production);
				adapter.notifyDataSetChanged();
				vs_production.setDisplayedChild(0);
			}
		}
	}
}
