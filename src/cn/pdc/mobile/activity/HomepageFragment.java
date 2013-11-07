package cn.pdc.mobile.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.pdc.mobile.R;

public class HomepageFragment extends Fragment {

	private Context mContext;

	private View mainView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_homepage, null);
		mContext = getActivity();

		return mainView;
	}
}
