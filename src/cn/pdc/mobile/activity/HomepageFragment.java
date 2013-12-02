package cn.pdc.mobile.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.dina.ui.model.BasicItem;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;
import cn.pdc.mobile.R;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;
import cn.pdc.mobile.utils.ToastUtil;

public class HomepageFragment extends Fragment {

	private Context mContext;
	private View mainView;
	private UITableView mTableView;
	private UITableView mButtonView;

	private String nickname;
	private String birthday;
	private String location;
	private String interesting;

	private BasicItem bi_nickname;
	private BasicItem bi_birthday;
	private BasicItem bi_location;
	private BasicItem bi_interesting;
	private BasicItem bi_wish;
	private BasicItem bi_have;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_homepage, null);
		mContext = getActivity();

		initData();
		initViews();
		return mainView;
	}

	/**
	 * init data
	 */
	private void initData() {
		new getBasicInfoTask().execute();
	}

	/**
	 * init views
	 */
	private void initViews() {
		mTableView = (UITableView) mainView.findViewById(R.id.tableView);

		mButtonView = (UITableView) mainView.findViewById(R.id.tableBtn);
		createBtnList();
		mButtonView.commit();
	}

	/**
	 * create menu list without img
	 */
	private void createList() {
		MenuClickListener listener = new MenuClickListener();
		mTableView.setClickListener(listener);

		bi_nickname = new BasicItem(getString(R.string.Nickname), nickname);
		bi_birthday = new BasicItem(getString(R.string.Birthday), birthday);
		bi_location = new BasicItem(getString(R.string.Location), location);
		bi_interesting = new BasicItem(getString(R.string.Interesting),
				interesting);

		mTableView.addBasicItem(bi_nickname);
		mTableView.addBasicItem(bi_birthday);
		mTableView.addBasicItem(bi_location);
		mTableView.addBasicItem(bi_interesting);
	}

	/**
	 * create menu list with img
	 */
	private void createBtnList() {
		bi_wish = new BasicItem(R.drawable.ic_launcher,
				getString(R.string.Wish), getString(R.string.undefined));
		mButtonView.addBasicItem(bi_wish);

		bi_have = new BasicItem(R.drawable.ic_launcher,
				getString(R.string.Wish), getString(R.string.undefined));
		mButtonView.addBasicItem(bi_have);
	}

	private class MenuClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			switch (index) {
			case 0:
				ToastUtil.showShortToast(mContext, "get");
				break;

			default:
				break;
			}
		}
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
				JSONObject jo = new JSONObject(result);
				nickname = jo.getString("nick");
				birthday = jo.getString("birthday");
				location = jo.getString("hometown");
				interesting = getString(R.string.undefined);
				Log.e("data", nickname + "-" + birthday + "-" + location + "-"
						+ interesting);

				createList();
				mTableView.commit();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
