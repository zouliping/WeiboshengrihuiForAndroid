package cn.pdc.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.pdc.mobile.R;
import cn.pdc.mobile.entity.Pairs;

public class DetailAdapter extends BaseAdapter {

	private List<Pairs> detailList;
	private Context mContext;

	public DetailAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public DetailAdapter(Context mContext, List<Pairs> detailList) {
		super();
		this.detailList = detailList;
		this.mContext = mContext;
	}

	public List<Pairs> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<Pairs> detailList) {
		this.detailList = detailList;
	}

	@Override
	public int getCount() {
		return detailList.size();
	}

	@Override
	public Object getItem(int position) {
		return detailList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DetailItemHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_detail, null);
			holder = new DetailItemHolder();
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.detail_name);
			holder.tv_value = (TextView) convertView
					.findViewById(R.id.detail_value);
			convertView.setTag(holder);
		} else {
			holder = (DetailItemHolder) convertView.getTag();
		}

		holder.tv_name.setText(detailList.get(position).getName());
		holder.tv_value.setText(detailList.get(position).getValue());

		return convertView;
	}

	private class DetailItemHolder {
		private TextView tv_name;
		private TextView tv_value;
	}
}
