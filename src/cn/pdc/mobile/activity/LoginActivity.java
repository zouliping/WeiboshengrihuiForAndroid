package cn.pdc.mobile.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.pdc.mobile.R;
import cn.pdc.mobile.utils.AppUtil;
import cn.pdc.mobile.utils.Config;
import cn.pdc.mobile.utils.HttpUtil;
import cn.pdc.mobile.utils.ToastUtil;

public class LoginActivity extends Activity {

	private Context mContext = LoginActivity.this;

	private EditText et_name;
	private EditText et_pwd;

	private Button btn_login;

	private String uname;
	private String upwd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.setNoTitleAndNoStatusBarScreen(LoginActivity.this);
		setContentView(R.layout.login_layout);

		initViews();
	}

	/**
	 * init Views
	 */
	private void initViews() {
		et_name = (EditText) findViewById(R.id.login_name);
		et_pwd = (EditText) findViewById(R.id.login_pwd);
		btn_login = (Button) findViewById(R.id.login_btn);
		btn_login.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_btn:
				if (getNameAndPwd()) {
					new LoginTask().execute();
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * get uername and pwd
	 */
	private Boolean getNameAndPwd() {
		uname = et_name.getText().toString();
		upwd = et_pwd.getText().toString();

		if (("".equals(uname)) || ("".equals(upwd))) {
			Toast.makeText(mContext, "username or password can not be null",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * login task
	 * 
	 * @author zouliping
	 * 
	 */
	private class LoginTask extends AsyncTask<String, Integer, String> {

		ProgressDialog dlg;

		@Override
		protected void onPreExecute() {
			dlg = new ProgressDialog(mContext);
			dlg.setTitle("Login");
			dlg.setMessage("please wait for a monent...");
			dlg.setCancelable(false);
			dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dlg.show();

		}

		@Override
		protected String doInBackground(String... params) {
			JSONObject jo = new JSONObject();
			try {
				jo.put("id", uname);
				jo.put("password", upwd);

				JSONObject result = new JSONObject(HttpUtil.doPost(
						Config.LOGIN, jo));
				return (String) result.get("result");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dlg.dismiss();
			if ("false".equals(result)) {
				ToastUtil.showShortToast(mContext,
						"Login failed! Please try again");
			} else {
				// store uid in SharedPreferences
				SharedPreferences data = getSharedPreferences("data", 0);
				Editor editor = data.edit();
				editor.putString("uid", uname);
				editor.putString("uname", uname);
				editor.commit();

				Config.uid = uname;
				Config.uname = uname;

				ToastUtil.showShortToast(mContext, "Login successfully");
				LoginActivity.this.finish();
				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
			}
		}
	}
}
