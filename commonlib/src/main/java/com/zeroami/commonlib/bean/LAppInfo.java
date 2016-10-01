package com.zeroami.commonlib.bean;

import android.graphics.drawable.Drawable;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：保存APP信息</p>
 */
public class LAppInfo {

	private String label;
	private Drawable icon;
	private String packageName;
	private String className;
	public LAppInfo() {
		super();
	}
	public LAppInfo(String label, Drawable icon, String packageName, String className) {
		super();
		this.label = label;
		this.icon = icon;
		this.packageName = packageName;
		this.className = className;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	@Override
	public String toString() {
		return "LAppInfo [label=" + label + ", icon=" + icon + ", packageName=" + packageName + ", className="
				+ className + "]";
	}
}
