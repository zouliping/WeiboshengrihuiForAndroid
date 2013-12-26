package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.DetailAdapter;
import cn.pdc.mobile.entity.Pair;
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
	private String want;
	private String have;

	private CornerListView cornerListView = null;
	private List<Pair> listData = null;
	private Pair pair = null;
	private DetailAdapter adapter = null;

	private AlertDialog.Builder builder;
	private EditText et;

	private Integer index = 0;
	private String attrvalue;
	private Boolean genderValue;

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
		want = getString(R.string.undefined);
		have = getString(R.string.undefined);
		listData = new ArrayList<Pair>();

		pair = new Pair(getString(R.string.Gender), gender);
		listData.add(pair);
		pair = new Pair(getString(R.string.Birthday), birthday);
		listData.add(pair);
		pair = new Pair(getString(R.string.Location), location);
		listData.add(pair);
		pair = new Pair(getString(R.string.Interesting), interesting);
		listData.add(pair);
		pair = new Pair(getString(R.string.Want), want);
		listData.add(pair);
		pair = new Pair(getString(R.string.Have), have);
		listData.add(pair);
	}

	/**
	 * init views
	 */
	private void initViews() {
		cornerListView = (CornerListView) mainView
				.findViewById(R.id.detail_list);
		adapter = new DetailAdapter(mContext, listData, 0);
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
				builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Gender");
				int choice = 0;
				if (gender.equals(getString(R.string.Female))) {
					choice = 1;
				}
				builder.setSingleChoiceItems(new String[] { "Male", "Female" },
						choice, new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								index = position;
								if (which == 0) {
									attrvalue = getString(R.string.Male);
									genderValue = true;
								} else if (which == 1) {
									attrvalue = getString(R.string.Female);
									genderValue = false;
								}
							}
						});
				builder.setPositiveButton(getString(R.string.yes),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								gender = attrvalue;
								new PutDataTask().execute("gender");
							}
						});
				builder.setNegativeButton(getString(R.string.cancel), null);
				builder.show();
				break;
			case 1:
				ToastUtil.showShortToast(mContext, "birthday");
				index = position;
				int year = 1990;
				int month = 01;
				int day = 01;
				if (birthday.equals(getString(R.string.undefined))) {
					Calendar calendar = Calendar.getInstance();
					year = calendar.get(Calendar.YEAR);
					month = calendar.get(Calendar.MONTH);
					day = calendar.get(Calendar.DAY_OF_MONTH);
				} else {
					String[] str = birthday.split("-");
					try {
						year = Integer.parseInt(str[0]);
						month = Integer.parseInt(str[1]) - 1;
						day = Integer.parseInt(str[2]);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				DatePickerDialog dialog = new DatePickerDialog(mContext,
						dateSetListener, year, month, day);
				dialog.setTitle("Birthday");
				dialog.show();
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
								location = attrvalue;
								new PutDataTask().execute("current_location");
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
			case 4:
			case 5:
				Intent intent = new Intent(mContext,
						ProductionDetailActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	}

	OnDateSetListener dateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			attrvalue = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
			birthday = attrvalue;
			new PutDataTask().execute("birthday");
		}
	};

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
				if (!jo.isNull("current_location")) {
					location = jo.getString("current_location");
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
				if (index == 0) {
					jo.put(params[0], genderValue);
				} else {
					jo.put(params[0], attrvalue);
				}

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
