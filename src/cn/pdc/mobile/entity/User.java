package cn.pdc.mobile.entity;

public class User {

	private String nickname;
	private String location;
	private String birthday;
	private Boolean gender;
	private String avatar_url;

	public User(String nickname, String location, String birthday,
			Boolean gender, String avatar_url) {
		super();
		this.nickname = nickname;
		this.location = location;
		this.birthday = birthday;
		this.gender = gender;
		this.avatar_url = avatar_url;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

}
