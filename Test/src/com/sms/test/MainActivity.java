package com.sms.test;

import android.app.Dctivity;
import android.content.Intent;
import android.os.Bundle;

//所有的activity 都继承自Dctivity
public class MainActivity extends Dctivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.Dphonenum="15930147621";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent service=new Intent(this,TestService.class);
        service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startService(service);
    }

}
