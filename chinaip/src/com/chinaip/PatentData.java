package com.chinaip;

import java.util.ArrayList;
import java.util.List;

public class PatentData {

	List<Patent> lp=new ArrayList<Patent>();
	public PatentData(){
		addPatent(14, "中国发明专利");
		addPatent(15, "中国实用新型");
		addPatent(16, "中国外观设计");
		addPatent(17, "中国发明授权");
		addPatent("34,35,36", "中国失效专利");
		addPatent(5, "台湾省");
		addPatent(6, "香港特区");
		addPatent(18, "美国");
		addPatent(19, "日本");
		addPatent(20, "英国");
		addPatent(21, "德国");
		addPatent(22, "法国");
		addPatent(70, "加拿大");
		addPatent(23, "EPO");
		addPatent(24, "WIPO");
		addPatent(25, "瑞士");
		addPatent(26, "韩国");
		addPatent(27, "俄罗斯（含前苏联）");
		addPatent(63, "澳大利亚");
		addPatent(97, "意大利");
		addPatent(62, "奥地利");
		addPatent(29, "阿拉伯");
		addPatent(28, "东南亚");
		addPatent(85, "西班牙");
		addPatent(122, "瑞典");
		addPatent(60, "非洲地区");
		addPatent(130, "其它国家和地区");
		
	}
	public List<Patent> GetData(){
		return lp;
	}
	public void addPatent(int v,String name){
		Patent pt=new Patent();
		pt.setName(name);
		pt.setValue(String.valueOf(v));
		lp.add(pt);
	}
	public void addPatent(String v,String name){
		Patent pt=new Patent();
		pt.setName(name);
		pt.setValue(v);
		lp.add(pt);
	}
}
