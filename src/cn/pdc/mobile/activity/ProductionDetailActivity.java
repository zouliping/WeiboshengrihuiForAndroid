package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.ProductionAdapter;
import cn.pdc.mobile.entity.Production;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;
import cn.pdc.mobile.utils.SHA1;
import cn.pdc.mobile.utils.StringUtil;
import cn.pdc.mobile.utils.ToastUtil;

/**
 * Production Detail
 * 
 * @author zouliping
 * 
 */
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
	private View v_more;

	private Button btn_more;
	private ProgressBar pb_load;

	private String title_activity;
	private String uname;
	private String title;
	private String type;
	private String description;

	private Integer page = 0;
	private Integer num = 2;
	private Integer sendToIndex = 0;

	private Boolean isMe = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.setNotTitleScreen(ProductionDetailActivity.this);
		setContentView(R.layout.activity_production_detail);

		initData();
		initViews();

		new getBasicInfoTask().execute(page, num);

	}

	private void initViews() {
		v_more = getLayoutInflater().inflate(R.layout.load_more, null);

		vs_production = (ViewSwitcher) findViewById(R.id.production_vs);
		lv_production = new ListView(mContext);
		lv_production.setCacheColorHint(Color.argb(0, 0, 0, 0));
		lv_production.setDivider(getResources().getDrawable(
				R.drawable.list_divider_line));
		lv_production.setDividerHeight(1);
		lv_production.setSelector(R.drawable.list_item_selector);
		lv_production.addFooterView(v_more);

		adapter = new ProductionAdapter(mContext);
		lv_production.setAdapter(adapter);

		vs_production.addView(lv_production);
		vs_production.addView(getLayoutInflater().inflate(
				R.layout.layout_progress_page, null));
		vs_production.showNext();

		btn_back = (ImageView) findViewById(R.id.back_btn);
		btn_tao = (ImageView) findViewById(R.id.tao_btn);
		btn_more = (Button) v_more.findViewById(R.id.btn_load_more);
		btn_back.setOnClickListener(listener);
		btn_tao.setOnClickListener(listener);
		btn_more.setOnClickListener(listener);

		pb_load = (ProgressBar) v_more.findViewById(R.id.pb_load);

		tv_title = (TextView) findViewById(R.id.production_title);
		if ("Goods".equals(title_activity)) {
			tv_title.setText(getString(R.string.Have));
			btn_tao.setVisibility(View.VISIBLE);
			if (isMe) {
				lv_production.setOnItemClickListener(itemClickListener);
			} else {
				btn_tao.setVisibility(View.GONE);
			}
		} else if ("WishItem".equals(title_activity)) {
			tv_title.setText(getString(R.string.Want));
			btn_tao.setVisibility(View.GONE);
		}
	}

	private void initData() {
		Intent intent = getIntent();
		uname = intent.getStringExtra("uname");
		title_activity = intent.getStringExtra("item");
		isMe = intent.getBooleanExtra("isMe", true);

		list_production = new ArrayList<Production>();
		list_production_name = new ArrayList<String>();

		if ("Goods".equals(title_activity) && isMe) {
			new getFriendsInfoTask().execute("");
		}
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
			case R.id.btn_load_more:
				btn_more.setVisibility(View.GONE);
				pb_load.setVisibility(View.VISIBLE);

				page++;

				new getBasicInfoTask().execute(page, num);
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

			JSONObject sjo = new JSONObject();
			JSONObject result0 = null;
			try {
				sjo.put("from", Config.uid);
				sjo.put("to", to);
				sjo.put("pid", present);
				result0 = new JSONObject(HttpUtil.doPut(
						Config.SEND_PRESENT_URL, sjo));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				jo.put("indivname", present);
				jo.put("uid", Config.uid);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				JSONObject reJsonObject = new JSONObject(HttpUtil.doPut(
						Config.REMOVE_URL_API, jo));
				if ((Boolean) reJsonObject.get("result")) {
					jo.put("classname", "Goods");
					jo.put("individualname",
							present + System.currentTimeMillis());
					jo.put("uid", SHA1.getSHA1String(to));
					jo.put("g_title", list_production.get(index).getTitle());
					jo.put("g_goods_type", list_production.get(index).getType());
					jo.put("g_description", list_production.get(index)
							.getDescription());

					Log.e("send present", jo.toString());

					reJsonObject = new JSONObject(HttpUtil.doPut(
							Config.UPDATE_INFO_API, jo));
					return (Boolean) reJsonObject.get("result")
							&& result0.getBoolean("result");
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
			//
			// List<NameValuePair> list_params = new
			// LinkedList<NameValuePair>();
			// list_params.add(new BasicNameValuePair("classname", "User"));
			// list_params.add(new BasicNameValuePair("uid", Config.uid));
			// list_params.add(new BasicNameValuePair("uname", Config.uname));
			// list_params.add(new BasicNameValuePair("sid", Config.sid));
			// String query = URLEncodedUtils.format(list_params, "utf-8");
			//
			// return HttpUtil.doGet(Config.GET_FRIENDS_LIST + query);
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
					list_friends.add(StringUtil.removeSpecialChar(tmp
							.getString("-u_nick")));
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
	private class getBasicInfoTask extends AsyncTask<Integer, String, String> {
		@Override
		protected String doInBackground(Integer... params) {

			// List<NameValuePair> list_params = new
			// LinkedList<NameValuePair>();
			// list_params
			// .add(new BasicNameValuePair("classname", title_activity));
			// list_params.add(new BasicNameValuePair("uid", Config.uid));
			// list_params.add(new BasicNameValuePair("uname", uname));
			// list_params.add(new BasicNameValuePair("sid", Config.sid));
			// String query = URLEncodedUtils.format(list_params, "utf-8");
			//
			// Log.e("get production url", Config.GET_PROPERTITY + query);
			// return HttpUtil.doGet(Config.GET_PROPERTITY + query);

			Integer page = params[0];
			Integer num = params[1];

			List<NameValuePair> list_params = new LinkedList<NameValuePair>();
			list_params.add(new BasicNameValuePair("type", title_activity));
			list_params.add(new BasicNameValuePair("uid", SHA1
					.getSHA1String(uname)));
			list_params.add(new BasicNameValuePair("page", page + ""));
			list_params.add(new BasicNameValuePair("num", num + ""));
			String query = URLEncodedUtils.format(list_params, "utf-8");
			return HttpUtil.doGet(Config.GET_PROPERTITY + query);
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				// to avoid that server does not work or network is not
				// connected
				if (result == null) {
					return;
				}

				if (result.equals("{}")) {
					ToastUtil.showShortToast(mContext, "no more data");
					btn_more.setEnabled(false);
				}

				JSONObject jo = new JSONObject(result);

				for (Iterator<?> i = jo.keys(); i.hasNext();) {
					String key = (String) i.next();
					list_production_name.add(key);
					JSONObject tmp = jo.getJSONObject(key);
					String prefix = null;

					if ("Goods".equals(title_activity)) {
						prefix = "-g_";
					} else if ("WishItem".equals(title_activity)) {
						prefix = "-w_";
					}

					if (!tmp.isNull(prefix + "title")) {
						title = StringUtil.removeSpecialChar(tmp
								.getString(prefix + "title"));
					} else {
						title = getString(R.string.undefined);
					}
					if (!tmp.isNull(prefix + "goods_type")) {
						type = StringUtil.removeSpecialChar(tmp
								.getString(prefix + "goods_type"));
					} else {
						type = getString(R.string.undefined);
					}
					if (!tmp.isNull(prefix + "description")) {
						description = StringUtil.removeSpecialChar(tmp
								.getString(prefix + "description"));
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
				btn_more.setVisibility(View.VISIBLE);
				pb_load.setVisibility(View.GONE);
			}
		}
	}
}
