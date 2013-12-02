package cn.pdc.mobile.activity;

import cn.pdc.mobile.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FriendsFragment extends Fragment {

	private Context mContext;

	private View mainView;
	private ListView lv_friend;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_friends, null);
		mContext = getActivity();

		initViews();

		return mainView;
	}

	private void initViews() {
		lv_friend = (ListView) mainView.findViewById(R.id.friends_list);
	}
}
