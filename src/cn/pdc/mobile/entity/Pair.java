package cn.pdc.mobile.entity;

/**
 * Entity Pair
 * 
 * @author zouliping
 * 
 */
public class Pair {

	String name;
	String value;

	public Pair(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
