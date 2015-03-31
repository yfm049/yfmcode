package com.chu;

import com.chu.adapter.LoadingDialog;
import com.chu.adapter.LoadingDialogExecute;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class FankuiActivity extends Activity {
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		loadingDialog = new LoadingDialog(this, new LoadingDialogExecute() {

			@Override
			public void executeSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(FankuiActivity.this, "123", Toast.LENGTH_LONG)
						.show();
				//loadingDialog.stop();
			}

			@Override
			public void executeFailure() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean execute() {
				// TODO Auto-generated method stub
				System.out.println("123");
				MeThread meThread = new MeThread();
				Thread thread = new Thread(meThread);
				thread.start();
				return true;
			}
		});
		loadingDialog.start();

	}

	public class MeThread implements Runnable {

		public MeThread() {
			super();
			// TODO Auto-generated constructor stub
		}

		public void run() {
			try {
				System.out.println("me");
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
