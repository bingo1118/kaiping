package com.smart.cloud.fire.global;

import com.smart.cloud.fire.view.BingoViewModel;

public class Point implements BingoViewModel{
	private String pid;
	private String name;
	private String longtitude;
	private String latitude;
	private String address;
	private String status;
	private String areaid;
	private String areaName;
	private int protectionLevel;//保护级别 0市保1省保2国保
	private String protectionLevelName;//保护级别名称

	public int getProtectionLevel() {
		return protectionLevel;
	}

	public void setProtectionLevel(int protectionLevel) {
		this.protectionLevel = protectionLevel;
	}

	public String getProtectionLevelName() {
		return protectionLevelName;
	}

	public void setProtectionLevelName(String protectionLevelName) {
		this.protectionLevelName = protectionLevelName;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Override
	public String getModelId() {
		return pid;
	}

	@Override
	public String getModelName() {
		return name;
	}
}
