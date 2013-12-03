package cn.pdc.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.pdc.mobile.R;
import cn.pdc.mobile.entity.User;

public class FriendAdapter extends BaseAdapter {

	private List<User> friendsList;
	private Context mContext;

	public FriendAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public FriendAdapter(List<User> friendsList, Context mContext) {
		super();
		this.friendsList = friendsList;
		this.mContext = mContext;
	}

	public List<User> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(List<User> friendsList) {
		this.friendsList = friendsList;
	}

	@Override
	public int getCount() {
		return friendsList.size();
	}

	@Override
	public Object getItem(int position) {
		return friendsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendItemHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.card_friend, null);
			holder = new FriendItemHolder();
			holder.card_avatar = (ImageView) convertView
					.findViewById(R.id.card_avatar);
			holder.card_birthday = (TextView) convertView
					.findViewById(R.id.card_birthday);
			holder.card_gender = (ImageView) convertView
					.findViewById(R.id.card_gender);
			holder.card_location = (TextView) convertView
					.findViewById(R.id.card_location);
			holder.card_nick = (TextView) convertView
					.findViewById(R.id.card_nick);
			convertView.setTag(holder);
		} else {
			holder = (FriendItemHolder) convertView.getTag();
		}

		holder.card_birthday.setText(friendsList.get(position).getBirthday());
		holder.card_location.setText(friendsList.get(position).getLocation());
		holder.card_nick.setText(friendsList.get(position).getNickname());
		if (friendsList.get(position).getGender()) {
			holder.card_gender.setImageResource(R.drawable.male);
		} else {
			holder.card_gender.setImageResource(R.drawable.female);
		}
		// to set avatar
		holder.card_avatar.setImageResource(R.drawable.avatar_default);

		return convertView;
	}

	private class FriendItemHolder {
		public ImageView card_avatar;
		public TextView card_nick;
		public ImageView card_gender;
		public TextView card_birthday;
		public TextView card_location;
	}

}
