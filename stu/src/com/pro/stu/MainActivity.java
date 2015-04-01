package com.pro.stu;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import com.pro.utils.Info;
import com.pro.utils.XmlParserUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {


	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			InputStream in=this.getAssets().open("main.xml");
			if(in!=null){
				List<Info> li=XmlParserUtils.ParseXmlToInfo(in);
				for(Info i:li){
					System.out.println(i.getChildName()+"---");
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
