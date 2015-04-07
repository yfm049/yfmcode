package com.pro.vidio;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.iptv.season.R;
import com.pro.adapter.MainAdapter;
import com.pro.adapter.TypeAdapter;
import com.pro.pojo.Category;
import com.pro.pojo.DialogUtls;
import com.pro.pojo.HttpUtils;
import com.pro.pojo.Type;
import com.pro.pojo.VideoFile;
import com.pro.pojo.VideoType;

@Fullscreen
@EActivity(R.layout.video_activity_main)
public class MainActivity extends Activity implements OnItemClickListener,
		OnScrollListener {

	private Category cg;
	private VideoType vt;
	private View view;

	private PopupWindow pw;

	private List<VideoFile> files = new ArrayList<VideoFile>();

	private List<Type> types = new ArrayList<Type>();

	private MainAdapter madapter;
	private TypeAdapter tadapter;
	@ViewById
	public TextView video_page;
	@ViewById
	public TextView video_tpage;

	private ListView video_typelist;
	@ViewById
	public ImageButton video_zjnew;
	@ViewById
	public ImageButton video_xztype;
	@ViewById
	public GridView video_gridview;

	public boolean isload = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			super.openOptionsMenu();
			showPopwindow();
		}else if(keyCode == KeyEvent.KEYCODE_BACK){
			onBackPressed();
		}
		return false;
	}

	@AfterViews
	public void datainit() {
		madapter = new MainAdapter(this, files);
		video_gridview.setAdapter(madapter);
		view = LayoutInflater.from(this).inflate(R.layout.window_type, null);
		tadapter = new TypeAdapter(this, types);
		video_typelist = (ListView) view.findViewById(R.id.video_typelist);
		video_typelist.setAdapter(tadapter);
		video_typelist.setOnItemClickListener(this);
		DialogUtls.show(this);
		getCategory("http://115.29.168.51:88/vod/main.xml");
		getType();
		video_gridview.setOnScrollListener(this);
	}

	@Background
	public void getType() {
		vt = HttpUtils.XmlToBean(VideoType.class,
				"http://115.29.168.51:88/vod/category.xml");
		setType();
	}

	@Background
	public void getCategory(String url) {
		if (!isload){
			isload = true;
			System.out.println(url);
			cg = HttpUtils.XmlToBean(Category.class, url);
			setCategory();
		}
	}

	@Click(R.id.video_zjnew)
	public void zjnewClick() {
		DialogUtls.show(this);
		files.clear();
		madapter.notifyDataSetChanged();
		getCategory("http://115.29.168.51:88/vod/main.xml");
		video_page.setText("1");
	}

	@UiThread
	public void setCategory() {
		DialogUtls.dismiss();
		if (cg != null) {
			video_tpage.setText("/"+cg.page);
			files.addAll(cg.files);
			madapter.notifyDataSetChanged();
		}
		isload = false;
	}

	@UiThread
	public void setType() {
		if (vt != null) {
			types.clear();
			types.addAll(vt.types);
			tadapter.notifyDataSetChanged();
		}

	}

	@ItemClick(R.id.video_gridview)
	public void gridviewIterClick(int pos) {
		VideoFile f = files.get(pos);
		Intent intent = new Intent(this, DetailActivity_.class);
		intent.putExtra("vf", f);
		startActivity(intent);

	}

	@Click(R.id.video_xztype)
	public void showPopwindow() {
		if (pw == null) {
			pw = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, true);
			pw.setBackgroundDrawable(new ColorDrawable(0));
			pw.setOutsideTouchable(true);
			pw.setFocusable(true);
		}
		pw.showAsDropDown(video_xztype);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Type t = types.get(position);
		DialogUtls.show(this);
		files.clear();
		madapter.notifyDataSetChanged();
		getCategory(t.url);
		video_page.setText("1");
		pw.dismiss();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if(firstVisibleItem>0){
			int cpage=((firstVisibleItem+12)/12);
			video_page.setText(cpage+"");
		}
		if (totalItemCount > 0&& (visibleItemCount + firstVisibleItem) == totalItemCount) {
			if (cg!=null&&cg.nextpage != null && !"".equals(cg.nextpage)) {
				getCategory(cg.nextpage);
			}

		}
	}

}
