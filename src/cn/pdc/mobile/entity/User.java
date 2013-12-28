package cn.pdc.mobile.entity;

public class User {

	private String nickname;
	private String location;
	private String birthday;
	private String interesting;
	private Boolean gender;

	public User(String nickname, String location, String birthday,
			String interesting, Boolean gender) {
		super();
		this.nickname = nickname;
		this.location = location;
		this.birthday = birthday;
		this.interesting = interesting;
		this.gender = gender;
	}

	public User() {
		super();
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

	public String getInteresting() {
		return interesting;
	}

	public void setInteresting(String interesting) {
		this.interesting = interesting;
	}
}
