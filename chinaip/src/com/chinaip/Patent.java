package com.chinaip;

import javax.swing.JCheckBox;

public class Patent {

	private JCheckBox chckbxNewCheckBox;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		return name+"--"+checked+"--"+value;
	}
	public JCheckBox GetCheckbox(){
		chckbxNewCheckBox = new JCheckBox(name);
		
		return chckbxNewCheckBox;
	}
	
	private boolean checked=false;
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
		chckbxNewCheckBox.setSelected(checked);
	}
	private String value;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String name;
}
