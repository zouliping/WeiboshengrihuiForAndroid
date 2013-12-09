package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.UserDetailAdapter;
import cn.pdc.mobile.entity.Pairs;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;
import cn.pdc.mobile.utils.ToastUtil;
import cn.pdc.mobile.view.CornerListView;

public class HomepageFragment extends Fragment {

	private Context mContext;
	private View mainView;

	private String birthday;
	private String location;
	private String interesting;
	private String gender;

	private CornerListView cornerListView = null;
	private List<Pairs> listData = null;
	private Pairs pairs = null;
	private UserDetailAdapter adapter = null;

	private AlertDialog.Builder builder;
	private EditText et;

	private Integer index = 0;
	private String attrvalue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_homepage, null);
		mContext = getActivity();

		initData();
		initViews();

		new getBasicInfoTask().execute();

		return mainView;
	}

	/**
	 * init data
	 */
	private void initData() {
		birthday = getString(R.string.undefined);
		location = getString(R.string.undefined);
		interesting = getString(R.string.undefined);
		gender = getString(R.string.undefined);
		listData = new ArrayList<Pairs>();

		pairs = new Pairs(getString(R.string.Gender), gender);
		listData.add(pairs);
		pairs = new Pairs(getString(R.string.Birthday), birthday);
		listData.add(pairs);
		pairs = new Pairs(getString(R.string.Location), location);
		listData.add(pairs);
		pairs = new Pairs(getString(R.string.Interesting), interesting);
		listData.add(pairs);
	}

	/**
	 * init views
	 */
	private void initViews() {
		cornerListView = (CornerListView) mainView
				.findViewById(R.id.detail_list);
		adapter = new UserDetailAdapter(mContext, listData);
		cornerListView.setAdapter(adapter);
		cornerListView.setOnItemClickListener(new ListItemClickListener());
		adapter.notifyDataSetChanged();
	}

	private class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
			switch (position) {
			case 0:
				ToastUtil.showShortToast(mContext, "gender");
				break;
			case 1:
				ToastUtil.showShortToast(mContext, "birthday");

				break;
			case 2:
				ToastUtil.showShortToast(mContext, "location");
				builder = getBuilder(getString(R.string.Location), location);
				builder.setPositiveButton(getString(R.string.yes),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								index = position;
								attrvalue = et.getText().toString();
								new PutDataTask().execute("nick");
							}
						});
				builder.show();
				break;
			case 3:
				ToastUtil.showShortToast(mContext, "interesting");
				builder = getBuilder(getString(R.string.Interesting),
						interesting);
				builder.setPositiveButton(getString(R.string.yes), null);
				builder.show();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * get the builder
	 * 
	 * @param name
	 * @return
	 */
	private AlertDialog.Builder getBuilder(String name, String value) {
		builder = new AlertDialog.Builder(mContext);
		builder.setTitle(name);
		et = new EditText(mContext);
		if (!(value.equals(getString(R.string.undefined)))) {
			et.setText(value);
			et.setSelection(value.length());
		}
		builder.setView(et);
		builder.setNegativeButton(getString(R.string.cancel), null);
		return builder;
	}

	/**
	 * get user info
	 * 
	 * @author zouliping
	 * 
	 */
	private class getBasicInfoTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			return HttpUtil.doGet(Config.GET_USER_INFO.replace("$indivname",
					Config.uid) + Config.uid);
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
				if (!jo.isNull("gender")) {
					if (jo.getBoolean("gender")) {
						gender = getString(R.string.Male);
					} else {
						gender = getString(R.string.Female);
					}
				}
				if (!jo.isNull("birthday")) {
					birthday = jo.getString("birthday");
				}
				if (!jo.isNull("hometown")) {
					location = jo.getString("hometown");
				}
				if (!jo.isNull("interesting")) {
					interesting = jo.getString("interesting");
				}
				Log.e("data", gender + "-" + birthday + "-" + location + "-"
						+ interesting);
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				listData.get(0).setValue(gender);
				listData.get(1).setValue(birthday);
				listData.get(2).setValue(location);
				listData.get(3).setValue(interesting);

				adapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * put data to server
	 * 
	 * @author zouliping
	 * 
	 */
	private class PutDataTask extends AsyncTask<String, Integer, Boolean> {

		ProgressDialog dlg;

		@Override
		protected void onPreExecute() {
			dlg = new ProgressDialog(mContext);
			dlg.setTitle("Sending Data");
			dlg.setMessage("Please wait for a monent...");
			dlg.setCancelable(false);
			dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dlg.show();

		}

		@Override
		protected Boolean doInBackground(String... params) {
			JSONObject jo = new JSONObject();
			try {
				jo.put("classname", "User");
				jo.put("individualname", Config.uid);
				jo.put("uid", Config.uid);
				Log.e("param", params[0]);
				jo.put(params[0], attrvalue);

				JSONObject result = new JSONObject(HttpUtil.doPut(
						Config.UPDATE_USER_INFO, jo));
				return (Boolean) result.get("result");
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
			} else if (result == true) {
				ToastUtil.showShortToast(mContext, "Success");
				listData.get(index).setValue(attrvalue);
				adapter.setDetailList(listData);
				adapter.notifyDataSetChanged();
			}
		}
	}
}
