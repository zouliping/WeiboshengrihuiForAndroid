package cn.pdc.mobile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HttpUtil {

	/**
	 * do get
	 * 
	 * @param uri
	 * @return
	 */
	public static String doGet(String uri) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(uri);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = response.getEntity().getContent();
				return StringUtil.inputStream2String(is);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * do post
	 * 
	 * @param uri
	 * @param jo
	 * @return
	 */
	public static String doPost(String uri, JSONObject jo) {

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(uri);
			StringEntity se = new StringEntity(jo.toString());
			request.setEntity(se);
			HttpResponse response = client.execute(request);
			return EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
