package chu.server.bean;

import java.io.Serializable;

public class PlaneInfo implements Serializable{
	private String _from;
	private String _to;
	private String time;
	private String price;
	private String airport;
	private String level;
	private String num;
	
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlaneInfo(String from, String to, String time, String price,
			String airport, String level, String num) {
		super();
		_from = from;
		_to = to;
		this.time = time;
		this.price = price;
		this.airport = airport;
		this.level = level;
		this.num = num;
	}

	
    public PlaneInfo() {
		super();
	}


	public String get_from() {
		return _from;
	}


	public void set_from(String from) {
		_from = from;
	}


	public String get_to() {
		return _to;
	}


	public void set_to(String to) {
		_to = to;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String getAirport() {
		return airport;
	}


	public void setAirport(String airport) {
		this.airport = airport;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}

	public String getNum() {
		return num;
	}


	public void setNum(String num) {
		this.num = num;
	}


	@Override
	public String toString() {
		return "PlaneInfo [_from=" + _from + ", _to=" + _to + ", time=" + time
				+ ", price=" + price + ", airport=" + airport + ", level="
				+ level + ", num=" + num + "]";
	}

    
}
