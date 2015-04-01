import shidian.tv.sntv.tools.DbUtils;
import shidian.tv.sntv.tools.Result;
import android.test.AndroidTestCase;


public class Test extends AndroidTestCase {

	public void testLogin() throws Exception {  
		Result rt=new Result();
		rt.setPhone("13473405826");
		rt.setState("未领取");
		rt.setTmsga("谢谢参与");
		rt.setTmsgb("食用油");
		rt.setGiftname("");
		DbUtils.getInstance(this.getContext()).addGift(rt);
    }  
}
