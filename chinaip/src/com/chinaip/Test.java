package com.chinaip;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		replace re=new replace();
		int i=1;
		while(true){
			System.out.println((Runtime.getRuntime().freeMemory()/1048576f)+"M--"+(i++));
			String html=re.loadfile("D://mm1374641484945.html");
			String phtml=re.replace(html);
			re.parse(phtml);
		}
	}

}
