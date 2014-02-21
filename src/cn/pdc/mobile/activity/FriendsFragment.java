package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
import android.widget.ListView;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.FriendAdapter;
import cn.pdc.mobile.entity.User;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;

public class FriendsFragment extends Fragment {

	private Context mContext;

	private View mainView;
	private ListView lv_friend;
	private FriendAdapter adapter;

	private List<User> list_friend;

	private String nickname;
	private String location;
	private String birthday;
	private String interesting;
	private Boolean gender;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_friends, null);
		mContext = getActivity();

		initList();
		initViews();
		new getBasicInfoTask().execute("");

		return mainView;
	}

	private void initViews() {
		lv_friend = (ListView) mainView.findViewById(R.id.friends_list);
		lv_friend.setOnItemClickListener(listener);
		adapter = new FriendAdapter(mContext);
		adapter.setFriendsList(list_friend);
		lv_friend.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void initList() {
		list_friend = new ArrayList<User>();
	}

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

	private class getBasicInfoTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			Log.e("get", "follow");
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
				for (Iterator<?> i = jo.keys(); i.hasNext();) {
					String key = (String) i.next();
					JSONObject user = jo.getJSONObject(key);
					if (!user.isNull("nick")) {
						nickname = user.getString("nick");
					} else {
						nickname = getString(R.string.undefined);
					}
					if (!user.isNull("gender")) {
						gender = user.getBoolean("gender");
					} else {
						gender = true;
					}
					if (!user.isNull("birthday")) {
						birthday = user.getString("birthday");
					} else {
						birthday = getString(R.string.undefined);
					}
					if (!user.isNull("current_location")) {
						location = user.getString("current_location");
					} else {
						location = getString(R.string.undefined);
					}
					if (!user.isNull("interest")) {
						interesting = user.getString("interest");
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
			}
		}
	}
}
