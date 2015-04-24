package com.pro.base;

import com.pro.hsh.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class BaseFragment extends Fragment {

	
	public void pushFragment(Fragment fragment){
		
		FragmentManager fm=this.getActivity().getSupportFragmentManager();
		if(fm.findFragmentByTag(fragment.getClass().getName())==null){
			FragmentTransaction ft=fm.beginTransaction();
			ft.replace(R.id.content, fragment,fragment.getClass().getName());
			ft.commit();
		}
	}
	
	public void pushFragmentAddBack(Fragment fragment){
		
		FragmentManager fm=this.getActivity().getSupportFragmentManager();
		if(fm.findFragmentByTag(fragment.getClass().getName())==null){
			FragmentTransaction ft=fm.beginTransaction();
			ft.replace(R.id.content, fragment,fragment.getClass().getName());
			ft.addToBackStack(fragment.getClass().getName());
			ft.commit();
		}
	}
	
	public void popBackStack(){
		FragmentManager fm=this.getActivity().getSupportFragmentManager();
		if(fm.getBackStackEntryCount()>0){
			fm.popBackStack();
		}else{
			getActivity().finish();
		}
		
	}
	public void popBackStackAll(){
		FragmentManager fm=this.getActivity().getSupportFragmentManager();
		fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	
	public void finishActivity(){
		getActivity().finish();
	}
	
	
	private Toast toast;
	public void showToast(String msg){
		if(toast==null){
			toast=Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
		}else{
			toast.setText(msg);
		}
		toast.show();
	}
	public void showToast(int msgres){
		if(toast==null){
			toast=Toast.makeText(getActivity(), msgres, Toast.LENGTH_SHORT);
		}else{
			toast.setText(msgres);
		}
		toast.show();
	}
}
