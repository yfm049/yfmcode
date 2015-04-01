package com.pro.rfidw;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebserviceUtils {

	
	private static String namespace="http://211.136.187.246:8080/location/services/locationService";
	private static String wsdl="http://211.136.187.246:8080/location/services/locationService?wsdl";

	public static String RFIDInfo(String uidNum,String password){
		SoapObject so=new SoapObject(namespace, "getRFIDInfo");
		try {
			so.addProperty("uidNum",uidNum);
			so.addProperty("password",password);
			SoapSerializationEnvelope sste=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			sste.dotNet=true;
			sste.setOutputSoapObject(so);
			HttpTransportSE htse=new HttpTransportSE(wsdl);
			htse.call(null, sste);
			System.out.println("调用"+so.toString()+"--返回:"+sste.getResponse().toString());
			return sste.getResponse().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String NotifyInfo(String uidNum){
		SoapObject so=new SoapObject(namespace, "notifyInfo");
		try {
			so.addProperty("uidNum",uidNum);
			SoapSerializationEnvelope sste=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			sste.dotNet=true;
			sste.setOutputSoapObject(so);
			HttpTransportSE htse=new HttpTransportSE(wsdl);
			htse.call(null, sste);
			System.out.println("调用"+so.toString()+"--返回:"+sste.getResponse());
			return sste.getResponse().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
