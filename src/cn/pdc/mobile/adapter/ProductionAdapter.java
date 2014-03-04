package cn.pdc.mobile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.pdc.mobile.R;
import cn.pdc.mobile.entity.Production;

public class ProductionAdapter extends BaseAdapter {

	private List<Production> productionList = new ArrayList<Production>();
	private Context mContext;

	public ProductionAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public ProductionAdapter(Context mContext, List<Production> productionList) {
		super();
		this.productionList = productionList;
		this.mContext = mContext;
	}

	public void setProductionList(List<Production> productionList) {
		this.productionList = productionList;
	}

	@Override
	public int getCount() {
		return productionList.size();
	}

	@Override
	public Object getItem(int position) {
		return productionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProductionItemHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_production_detail, null);
			holder = new ProductionItemHolder();
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.production_title);
			holder.tv_type = (TextView) convertView
					.findViewById(R.id.production_tpye);
			holder.tv_description = (TextView) convertView
					.findViewById(R.id.production_description);
			convertView.setTag(holder);
		} else {
			holder = (ProductionItemHolder) convertView.getTag();
		}

		Production production = productionList.get(position);
		holder.tv_title.setText(production.getTitle());
		holder.tv_type.setText(production.getType());
		holder.tv_description.setText(production.getDescription());

		return convertView;
	}

	private class ProductionItemHolder {
		TextView tv_title;
		TextView tv_type;
		TextView tv_description;
	}

}
