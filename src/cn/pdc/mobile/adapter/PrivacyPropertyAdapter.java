package cn.pdc.mobile.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.pdc.mobile.R;

public class PrivacyPropertyAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> list_pro;
	private ArrayList<Boolean> list_selected;

	public PrivacyPropertyAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public PrivacyPropertyAdapter(Context mContext, ArrayList<String> list_pro,
			ArrayList<Boolean> list_selected) {
		super();
		this.mContext = mContext;
		this.list_pro = list_pro;
		this.list_selected = list_selected;
	}

	@Override
	public int getCount() {
		return list_pro.size();
	}

	@Override
	public Object getItem(int position) {
		return list_pro.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PropertyHolder holder = null;
		if (convertView == null) {
			holder = new PropertyHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_privacy_property, null);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.pro_title);
			holder.iv_selected = (ImageView) convertView
					.findViewById(R.id.pro_selected);
			convertView.setTag(holder);
		} else {
			holder = (PropertyHolder) convertView.getTag();
		}

		holder.tv_title.setText(list_pro.get(position));
		if (list_selected.get(position)) {
			holder.iv_selected.setVisibility(View.VISIBLE);
		} else {
			holder.iv_selected.setVisibility(View.GONE);
		}

		return convertView;
	}

	public class PropertyHolder {
		TextView tv_title;
		ImageView iv_selected;
	}

}
