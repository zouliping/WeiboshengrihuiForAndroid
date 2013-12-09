package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.List;

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
import br.com.dina.ui.widget.UITableView.ClickListener;
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

	private String nickname;
	private String birthday;
	private String location;
	private String interesting;
	private String gender;

	private CornerListView cornerListView = null;

	private List<Pairs> listData = null;
	private Pairs pairs = null;
	private UserDetailAdapter adapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_homepage, null);
		mContext = getActivity();

		initData();
		initViews();

//		new getBasicInfoTask().execute();

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
		listData = new ArrayList<Pairs>();

		pairs = new Pairs(getString(R.string.Nickname), nickname);
		listData.add(pairs);
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
		adapter.notifyDataSetChanged();
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
				listData.get(0).setValue(nickname);
				listData.get(1).setValue(gender);
				listData.get(2).setValue(birthday);
				listData.get(3).setValue(location);
				listData.get(4).setValue(interesting);

				adapter.notifyDataSetChanged();
			}
		}

	}
}
