package shidian.tv.sntv.tools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class DialogUtils {
	
	private static Dialog dialog;
	private static Toast toast;

	public static void showDialog(Context context,String text){
		ProgressDialog pd=new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
		pd.setMessage(text);
		pd.show();
		dialog=pd;
	}
	
	public static void closeDialog(){
		if(dialog!=null){
			dialog.dismiss();
		}
	}
	
	public static void ShowToast(Context context,String text){
		if(toast==null){
			toast=Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_LONG);
		}
		toast.setText(text);
		toast.show();
	}
}
