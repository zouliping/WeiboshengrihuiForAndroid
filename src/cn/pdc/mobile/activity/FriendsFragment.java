package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.FriendAdapter;
import cn.pdc.mobile.entity.User;

public class FriendsFragment extends Fragment {

	private Context mContext;

	private View mainView;
	private ListView lv_friend;
	private FriendAdapter adapter;

	private List<User> list_friend;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_friends, null);
		mContext = getActivity();

		initList();
		initViews();

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
		for (int i = 0; i < 10; i++) {
			User user = new User("nick" + i, "location" + i, "1992-01-0" + i,
					(i % 2 == 0), "");
			list_friend.add(user);
		}
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(mContext, FriendDetailActivity.class);
			startActivity(intent);
		}
	};
}
