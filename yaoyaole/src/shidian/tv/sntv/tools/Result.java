package shidian.tv.sntv.tools;

public class Result {

	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String phone;
	private int awardtype;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getAwardtype() {
		return awardtype;
	}
	public void setAwardtype(int awardtype) {
		this.awardtype = awardtype;
	}
	public String getTmsga() {
		return tmsga;
	}
	public void setTmsga(String tmsga) {
		this.tmsga = tmsga;
	}
	public String getTmsgb() {
		return tmsgb;
	}
	public void setTmsgb(String tmsgb) {
		this.tmsgb = tmsgb;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	private String tmsga;
	private String tmsgb;
	private String tid;
	
	private String time;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	private String giftname;
	public String getGiftname() {
		return giftname;
	}
	public void setGiftname(String giftname) {
		this.giftname = giftname;
	}
	
	private String state;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public boolean iswin(){
		if(!"".equals(tmsgb)){
			return true;
		}
		return false;
	}
}
