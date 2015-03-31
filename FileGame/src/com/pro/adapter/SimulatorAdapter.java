package com.pro.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pro.game.R;
import com.pro.pojo.Simulator;

public class SimulatorAdapter extends BaseAdapter{

	private List<Simulator> Simulatorlist;
	private Context context;
	private AssetManager am;
	public SimulatorAdapter(Context context,List<Simulator> Simulatorlist){
		this.context=context;
		this.Simulatorlist=Simulatorlist;
		am=context.getAssets();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Simulatorlist.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return Simulatorlist.get(pos);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Simulator slr=Simulatorlist.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.simulator_item, null);
		}
		TextView slrname=(TextView)view.findViewById(R.id.slrname);
		ImageView logo=(ImageView)view.findViewById(R.id.logo);
		slrname.setText(slr.getName());
		logo.setImageBitmap(SimulatorLogo(slr));
		return view;
	}

	public Bitmap SimulatorLogo(Simulator slr) { 
		InputStream in=null;
		try {
			in = am.open(slr.getLogo());
			return BitmapFactory.decodeStream(in); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        return null;
    }   
}
