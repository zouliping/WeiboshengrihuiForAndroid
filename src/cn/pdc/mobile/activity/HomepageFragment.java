package cn.pdc.mobile.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
	private String gender;

	private BasicItem bi_nickname;
	private BasicItem bi_birthday;
	private BasicItem bi_location;
	private BasicItem bi_interesting;
	private BasicItem bi_gender;
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
		nickname = getString(R.string.undefined);
		birthday = getString(R.string.undefined);
		location = getString(R.string.undefined);
		interesting = getString(R.string.undefined);
		gender = getString(R.string.undefined);

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
		bi_gender = new BasicItem(getString(R.string.Gender), gender);
		bi_birthday = new BasicItem(getString(R.string.Birthday), birthday);
		bi_location = new BasicItem(getString(R.string.Location), location);
		bi_interesting = new BasicItem(getString(R.string.Interesting),
				interesting);

		mTableView.addBasicItem(bi_nickname);
		mTableView.addBasicItem(bi_gender);
		mTableView.addBasicItem(bi_birthday);
		mTableView.addBasicItem(bi_location);
		mTableView.addBasicItem(bi_interesting);
	}

	/**
	 * create menu list with img
	 */
	private void createBtnList() {
		MoreMenuClickListener moreMenuClickListener = new MoreMenuClickListener();
		mButtonView.setClickListener(moreMenuClickListener);

		bi_wish = new BasicItem(R.drawable.ic_launcher,
				getString(R.string.Wish), getString(R.string.undefined));
		mButtonView.addBasicItem(bi_wish);

		bi_have = new BasicItem(R.drawable.ic_launcher,
				getString(R.string.Have), getString(R.string.undefined));
		mButtonView.addBasicItem(bi_have);
	}

	/**
	 * basic detail menu click listener
	 * 
	 * @author zouliping
	 * 
	 */
	private class MenuClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			switch (index) {
			case 0:
				ToastUtil.showShortToast(mContext, "nick");
				new AlertDialog.Builder(mContext)
						.setTitle(getString(R.string.Nickname))
						.setView(new EditText(mContext))
						.setPositiveButton(getString(R.string.yes), null)
						.setNegativeButton(getString(R.string.cancel), null)
						.show();
				break;
			case 1:
				ToastUtil.showShortToast(mContext, "gender");
				break;
			case 2:
				ToastUtil.showShortToast(mContext, "birthday");
				break;
			case 3:
				ToastUtil.showShortToast(mContext, "location");
				new AlertDialog.Builder(mContext)
						.setTitle(getString(R.string.Location))
						.setView(new EditText(mContext))
						.setPositiveButton(getString(R.string.yes), null)
						.setNegativeButton(getString(R.string.cancel), null)
						.show();
				break;
			case 4:
				ToastUtil.showShortToast(mContext, "interesting");
				new AlertDialog.Builder(mContext)
						.setTitle(getString(R.string.Interesting))
						.setView(new EditText(mContext))
						.setPositiveButton(getString(R.string.yes), null)
						.setNegativeButton(getString(R.string.cancel), null)
						.show();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * more menu click listener
	 * 
	 * @author zouliping
	 * 
	 */
	private class MoreMenuClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			switch (index) {
			case 0:
				ToastUtil.showShortToast(mContext, "wish");
				break;
			case 1:
				ToastUtil.showShortToast(mContext, "have");
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
				// to avoid that server does not work or network is not
				// connected
				if (result == null) {
					return;
				}

				JSONObject jo = new JSONObject(result);
				if (!jo.isNull("nick")) {
					nickname = jo.getString("nick");
				}
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
				Log.e("data", nickname + "-" + gender + "-" + birthday + "-"
						+ location + "-" + interesting);
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				createList();
				mTableView.commit();
			}
		}

	}
}
