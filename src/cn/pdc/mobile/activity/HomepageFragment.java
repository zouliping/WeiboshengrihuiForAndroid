package cn.pdc.mobile.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.dina.ui.model.BasicItem;
import br.com.dina.ui.widget.UITableView;
import cn.pdc.mobile.R;

public class HomepageFragment extends Fragment {

	private Context mContext;
	private View mainView;
	private UITableView mTableView;
	private UITableView mButtonView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_homepage, null);
		mContext = getActivity();

		mTableView = (UITableView) mainView.findViewById(R.id.tableView);
		createList();
		mTableView.commit();

		mButtonView = (UITableView) mainView.findViewById(R.id.tableBtn);
		createBtnList();
		mButtonView.commit();
		return mainView;
	}

	private void createList() {
		mTableView.addBasicItem(getString(R.string.Nickname), "test");
		mTableView.addBasicItem(getString(R.string.Birthday), "test");
		mTableView.addBasicItem(getString(R.string.Location), "test");
		mTableView.addBasicItem(getString(R.string.Interesting), "test");
	}

	private void createBtnList() {
		BasicItem bi1 = new BasicItem("Wish");
		bi1.setDrawable(R.drawable.ic_launcher);
		mButtonView.addBasicItem(bi1);

		BasicItem bi2 = new BasicItem("Have");
		bi2.setDrawable(R.drawable.ic_launcher);
		mButtonView.addBasicItem(bi2);
	}
}
