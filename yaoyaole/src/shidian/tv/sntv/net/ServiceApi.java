package shidian.tv.sntv.net;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import shidian.tv.sntv.tools.PhoneInfo;
import shidian.tv.sntv.tools.Result;
import shidian.tv.sntv.tools.Utils;
import android.content.Context;

public class ServiceApi {
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public String login(Context context, String name, String pass) {
		try {
			int i = (int) (100.0D + 899.0D * Math.random());
			JSONObject jsonparam = new JSONObject();
			jsonparam.put("m", name);
			jsonparam.put("p", pass);
			jsonparam.put("key", String.valueOf(i));
			jsonparam.put("t", "1");
			jsonparam.put("pushid","");
			jsonparam.put("uid", "");
			jsonparam.put("uuid", "");
			jsonparam.put("v", "a");
			jsonparam.put("time", String.valueOf(System.currentTimeMillis()));
			jsonparam.put("skey", Utils.SKEY);
			return new HttpUtils().dopost(context, "/user/checkuser2.php", jsonparam, "/user/checkuser2.php");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Result checkSign(Context context,PhoneInfo pi){
		try {
			JSONObject jsonparam=new JSONObject();
			String l = String.valueOf(System.currentTimeMillis());
			String str4 = Utils.MD5(l);
			jsonparam.put("uid", pi.getUid());
			jsonparam.put("uuid", pi.getUuid());
			jsonparam.put("uterm", pi.getUterm());
			jsonparam.put("t", l);
			jsonparam.put("tcode", str4);
			jsonparam.put("v", "a");
			jsonparam.put("time", String.valueOf(System.currentTimeMillis()));
			jsonparam.put("skey", Utils.SKEY);
			String rt=new HttpUtils().dopost(context, "/award/adcheck107.php", jsonparam, "/award/adcheck107.php");
			Result result=new Result();
			JSONObject jo=new JSONObject(rt);
			result.setAwardtype(jo.getInt("awardtype"));
			result.setTid(jo.getString("tid"));
			result.setState("未领取");
			result.setTime(sdf.format(new Date()));
			if(result.getAwardtype()==99){
				result.setTmsga(jo.getString("tmsga"));
				result.setTmsgb(jo.getString("tmsgb"));
				result.setGiftname(jo.getString("giftname"));
			}else{
				result.setTmsga("谢谢参与");
				result.setTmsgb("");
				result.setGiftname("");
			}
			return result;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
