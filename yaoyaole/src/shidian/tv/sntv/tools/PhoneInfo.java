package shidian.tv.sntv.tools;

public class PhoneInfo {

	private String phone;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	private String uid;
	private String uuid;
	private boolean run=false;
	
	public boolean isRun() {
		return run;
	}
	public void setRun(boolean run) {
		this.run = run;
	}
	private String uterm="0";
	public String getUterm() {
		return uterm;
	}
	public void setUterm(String uterm) {
		this.uterm = uterm;
	}
	
	private String state;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
