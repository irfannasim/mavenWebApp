package com.enums;

public enum ResponseEnum {
	DATA("data"), 
	STATUS("status"), 
	REASON("reason"),
	ERROR("Error"),
	SUCCESS("Sucess");

	String value;

	private ResponseEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
