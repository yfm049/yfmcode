package com.pro.vidio;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.forcetech.android.ForceTV;
import com.iptv.pro.PlayerActivity;
import com.iptv.season.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pro.adapter.PartAdapter;
import com.pro.pojo.DialogUtls;
import com.pro.pojo.Forcetv;
import com.pro.pojo.HttpUtils;
import com.pro.pojo.VideoCategory;
import com.pro.pojo.VideoFile;
import com.pro.pojo.link;

@Fullscreen
@EActivity(R.layout.activity_detail)
public class DetailActivity extends Activity {

	
	public List<link> links=new ArrayList<link>();
	
	private VideoCategory vc;
	
	private VideoFile vf;
	
	public PartAdapter padapter;
	@ViewById
	public TextView name;
	@ViewById
	public TextView des;
	@ViewById
	public ImageView vimg;
	@ViewById
	public GridView gridpart;
	@ViewById
	public TextView jianjie;
	
	@AfterViews
	public void datainit(){
		ForceTV.initForceClient();
		vf=(VideoFile)this.getIntent().getSerializableExtra("vf");
		padapter=new PartAdapter(this, links);
		gridpart.setAdapter(padapter);
		DialogUtls.show(this);
		GetVideoCategory();
	}
	@Background
	public void GetVideoCategory(){
		if(vf!=null){
			vc=HttpUtils.XmlToBean(VideoCategory.class, vf.url);
			Setdata();
		}
	}
	
	@UiThread
	public void Setdata(){
		DialogUtls.dismiss();
		links.addAll(vc.file.links);
		padapter.notifyDataSetChanged();
		StringBuffer sb=new StringBuffer();
		sb.append("类型:"+vc.file.v_type+"  ");
		sb.append("导演:"+vc.file.director+"  ");
		sb.append("地区:"+vc.file.country+"  ");
		sb.append("语言:"+vc.file.language+"\r\n");
		sb.append("主演:"+vc.file.actor);
		jianjie.setText(sb.toString());
		name.setText(vf.name);
		des.setText(vc.file.description);
		ImageLoader.getInstance().displayImage(vc.file.Img, vimg);
	}
	
	@ItemClick(R.id.gridpart)
	public void gridpartItemClick(int pos){
		link lk=links.get(pos);
		switch_chan(lk.filmid,vc.file.Server,lk.type);
		
		
	}
	
	@Background
	public void switch_chan(String videoId,String server,String hotlink){
			String param="http://127.0.0.1:9898/cmd.xml?cmd=switch_chan&";
			param+="id="+videoId;
			param+="&server="+server;
			param+="&link="+hotlink;
			String playurl="http://127.0.0.1:9898/"+videoId+".ts";
			Forcetv forcetv=HttpUtils.XmlToBean(Forcetv.class, param);
			if(forcetv!=null&&"0".equals(forcetv.result.ret)){
				play(playurl);
			}
			
	}
	@UiThread
	public void play(String url){
		Intent intent = new Intent(this,PlayerActivity.class);
		intent.putExtra("prg", url);
        startActivity(intent);
//        PackageManager pm = this.getPackageManager();
//        List<ResolveInfo> rilist=pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        if(rilist!=null&&rilist.size()>0){
//        	StringBuffer sb=new StringBuffer();
//        	for(ResolveInfo ri:rilist){
//        		sb.append(ri.activityInfo.packageName+"--"+ri.activityInfo.name+"\r\n");
//        	}
//        	AlertDialog.Builder builer=new AlertDialog.Builder(this);
//        	builer.setTitle("bofangqi");
//        	builer.setMessage(sb.toString());
//        	builer.create().show();
//        }
        
        
        
        //startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ForceTV.stop();
	}
	
}
