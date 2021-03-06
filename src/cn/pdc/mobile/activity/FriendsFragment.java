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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.FriendAdapter;
import cn.pdc.mobile.entity.User;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;
import cn.pdc.mobile.utils.StringUtil;
import cn.pdc.mobile.utils.ToastUtil;

/**
 * Friends
 * 
 * @author zouliping
 * 
 */
public class FriendsFragment extends Fragment {

	private Context mContext;

	private View mainView;
	private ViewSwitcher vs_friend;
	private ListView lv_friend;
	private FriendAdapter adapter;

	private View v_more;
	private Button btn_more;
	private ProgressBar pb_load;

	private List<User> list_friend;

	private String nickname;
	private String location;
	private String birthday;
	private String interesting;
	private Boolean gender;

	private Integer page = 0;
	private Integer num = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_friends, null);
		v_more = inflater.inflate(R.layout.load_more, null);
		mContext = getActivity();

		initData();
		initViews();
		new getBasicInfoTask().execute(page, num);

		return mainView;
	}

	private void initData() {
		list_friend = new ArrayList<User>();
	}

	private void initViews() {
		vs_friend = (ViewSwitcher) mainView.findViewById(R.id.friends_vs);
		lv_friend = new ListView(mContext);
		lv_friend.setCacheColorHint(Color.argb(0, 0, 0, 0));
		lv_friend.setDivider(getResources().getDrawable(
				R.drawable.list_divider_line));
		lv_friend.setDividerHeight(1);
		lv_friend.setSelector(R.drawable.list_item_selector);
		lv_friend.setOnItemClickListener(listener);
		lv_friend.setOnItemLongClickListener(longClickListener);
		lv_friend.addFooterView(v_more);

		adapter = new FriendAdapter(mContext);
		lv_friend.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		vs_friend.addView(lv_friend);
		vs_friend.addView(getActivity().getLayoutInflater().inflate(
				R.layout.layout_progress_page, null));

		vs_friend.showNext();

		btn_more = (Button) v_more.findViewById(R.id.btn_load_more);
		btn_more.setOnClickListener(btnClickListener);
		pb_load = (ProgressBar) v_more.findViewById(R.id.pb_load);
	}

	private OnClickListener btnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
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

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(mContext, FriendDetailActivity.class);
			User user = list_friend.get(position);
			Bundle bundle = new Bundle();
			bundle.putString("uid", user.getUid());
			bundle.putString("nickname", user.getNickname());
			bundle.putString("location", user.getLocation());
			bundle.putString("interesting", user.getInteresting());
			bundle.putString("birthday", user.getBirthday());
			bundle.putBoolean("gender", user.getGender());
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	private OnItemLongClickListener longClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Delete Friend");
			builder.setMessage("Are you sure to delete "
					+ list_friend.get(position).getNickname() + "?");
			builder.setPositiveButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							new removeFriendTask().execute(list_friend.get(
									position).getUid());
						}
					});
			builder.setNegativeButton(getString(R.string.cancel), null);
			builder.show();
			return false;
		}
	};

	/**
	 * remove friend relationship
	 * 
	 * @author zouliping
	 * 
	 */
	private class removeFriendTask extends AsyncTask<String, String, Boolean> {

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
			String friend = params[0];
			JSONObject jo = new JSONObject();
			try {
				jo.put("indiv1", Config.uid);
				jo.put("indiv2", friend);
				jo.put("relation", "follow");
				jo.put("uid", Config.uid);

				JSONObject result0 = new JSONObject(HttpUtil.doPut(
						Config.REMOVE_RELATION_URL, jo));
				JSONObject result = new JSONObject(HttpUtil.doPut(
						Config.REMOVE_RELATION_URL_API, jo));
				return (Boolean) result.get("result")
						&& result0.getBoolean("result");
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
				vs_friend.setDisplayedChild(1);
				list_friend = new ArrayList<User>();
				new getBasicInfoTask().execute(0, num);
			}
		}

	}

	private class getBasicInfoTask extends AsyncTask<Integer, String, String> {

		@Override
		protected String doInBackground(Integer... params) {

			// List<NameValuePair> list_params = new
			// LinkedList<NameValuePair>();
			// list_params.add(new BasicNameValuePair("classname", "User"));
			// list_params.add(new BasicNameValuePair("uid", Config.uid));
			// list_params.add(new BasicNameValuePair("uname", Config.uname));
			// list_params.add(new BasicNameValuePair("sid", Config.sid));
			// String query = URLEncodedUtils.format(list_params, "utf-8");
			// return HttpUtil.doGet(Config.GET_FRIENDS_LIST + query);

			Integer page = params[0];
			Integer num = params[1];
			List<NameValuePair> list_params = new LinkedList<NameValuePair>();
			list_params.add(new BasicNameValuePair("page", page + ""));
			list_params.add(new BasicNameValuePair("num", num + ""));
			list_params.add(new BasicNameValuePair("uid", Config.uid));
			String query = URLEncodedUtils.format(list_params, "utf-8");

			return HttpUtil.doGet(Config.GET_FRIENDS_LIST + query);
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
					JSONObject user = jo.getJSONObject(key);
					if (!user.isNull("-u_nick")) {
						nickname = StringUtil.removeSpecialChar(user
								.getString("-u_nick"));
					} else {
						nickname = getString(R.string.undefined);
					}
					if (!user.isNull("-u_gender")) {
						gender = user.getBoolean("-u_gender");
					} else {
						gender = true;
					}
					if (!user.isNull("-u_birthday")) {
						birthday = StringUtil.removeSpecialChar(user
								.getString("-u_birthday"));
					} else {
						birthday = getString(R.string.undefined);
					}
					if (!user.isNull("-u_current_location")) {
						location = StringUtil.removeSpecialChar(user
								.getString("-u_current_location"));
					} else {
						location = getString(R.string.undefined);
					}
					if (!user.isNull("-u_interest")) {
						interesting = StringUtil.removeSpecialChar(user
								.getString("-u_interest"));
					} else {
						interesting = getString(R.string.undefined);
					}
					User tmp = new User(key, nickname, location, birthday,
							interesting, gender);
					list_friend.add(tmp);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				adapter.setFriendsList(list_friend);
				adapter.notifyDataSetChanged();
				vs_friend.setDisplayedChild(0);
				btn_more.setVisibility(View.VISIBLE);
				pb_load.setVisibility(View.GONE);
			}
		}
	}
}
