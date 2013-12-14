package cn.pdc.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.pdc.mobile.R;
import cn.pdc.mobile.adapter.ProductionAdapter;
import cn.pdc.mobile.entity.Production;
import cn.pdc.mobile.utils.AppUtil;

public class ProductionDetailActivity extends Activity {

	private Context mContext = ProductionDetailActivity.this;
	private List<Production> productionList;
	private ListView lv_production;
	private ProductionAdapter adapter;
	private Production production;

	private ImageView btn_back;
	private ImageView btn_tao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.setNotTitleScreen(ProductionDetailActivity.this);
		setContentView(R.layout.activity_production_detail);

		initData();
		initViews();
	}

	private void initViews() {
		lv_production = (ListView) findViewById(R.id.production_list);
		adapter = new ProductionAdapter(mContext, productionList);
		lv_production.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		btn_back = (ImageView) findViewById(R.id.back_btn);
		btn_tao = (ImageView) findViewById(R.id.tao_btn);
		btn_back.setOnClickListener(listener);
		btn_tao.setOnClickListener(listener);
	}

	private void initData() {
		productionList = new ArrayList<Production>();

		for (int i = 0; i < 10; i++) {
			production = new Production("title" + i, "type" + i, "description"
					+ i);
			productionList.add(production);
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				finish();
				break;
			case R.id.tao_btn:
				AppUtil.openApp(mContext, "com.taobao.taobao");
				break;
			default:
				break;
			}
		}
	};
}
