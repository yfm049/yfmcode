package com.chu.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Message;

/**
 * 封装Thread和Handler，用户可以方便的调用进度对话框，<b>并异步执行代码</b>，在代码执行完成后，进行主线程UI的更新。 该类需配合
 * {@link LoadingDialogExecute}代理类使用。
 * 
 * @see LoadingDialogExecute
 * @author 优化设计
 * @version 0.2
 */
public class LoadingDialog {
	private ProgressDialog prDialog;
	private Context context;
	private String dialogTitle = "加载提示";
	private String dialogMessage = "正在加载，请稍候……";
	private LoadingDialogExecute loadingDialogExecute;
	private Thread thread;
	private boolean isShowDialog = true;

	/**
	 * 获取是否显示进度对话框
	 * 
	 * @return
	 */
	public boolean getIsShowDialog() {
		return isShowDialog;
	}

	/**
	 * 设置是否显示进度对话框，为false时，进度对话框不显示，但异步代码依然正常执行
	 * 
	 * @param isShowDialog
	 */
	public ProgressDialog setIsShowDialog(boolean isShowDialog) {
		this.isShowDialog = isShowDialog;
		return getProgressDialog();
	}

	/**
	 * 获取进度对话框
	 * 
	 * @return 进度对话框
	 */
	public ProgressDialog getProgressDialog() {
		return prDialog;
	}

	/**
	 * 获取Context
	 * 
	 * @return Context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * 设置Context
	 * 
	 * @param _c
	 */
	public void setContext(Context _c) {
		this.context = _c;
	}

	/**
	 * 获取Handler
	 * 
	 * @return
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * 设置对话框标题
	 * 
	 * @param _s
	 */
	public void setDialogTitle(String _s) {
		this.dialogTitle = _s;
		if (prDialog != null) {
			prDialog.setTitle(_s);
		}
	}

	/**
	 * 设置对话框内容
	 * 
	 * @param _s
	 */
	public void setDialogMessage(String _s) {
		this.dialogMessage = _s;
		if (prDialog != null) {
			prDialog.setMessage(_s);
		}
	}

	/**
	 * 获取Thread
	 * 
	 * @return
	 */
	public Thread getThread() {
		return this.thread;
	}

	private void initProgressDialog() {
		prDialog = new ProgressDialog(context);
		prDialog.setTitle(dialogTitle);
		prDialog.setMessage(dialogMessage);
		prDialog.setCanceledOnTouchOutside(false);
		prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prDialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				thread.interrupt(); // 停止线程
			}
		});
		prDialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				thread.interrupt(); // 停止线程
			}
		});
	}

	/**
	 * 进度对话框构造函数
	 * 
	 * @param context
	 *            Context
	 * @param loadingDialogExecute
	 *            需要实现LoadingDialogExecute类的三个方法
	 */
	public LoadingDialog(Context context, LoadingDialogExecute loadingDialogExecute) {
		this.context = context;
		this.loadingDialogExecute = loadingDialogExecute;
		initProgressDialog();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (prDialog != null && prDialog.isShowing()) {
				prDialog.dismiss();
			}
			switch (msg.what) {
			case 0:
				loadingDialogExecute.executeSuccess();
				break;
			case 1:
				loadingDialogExecute.executeFailure();
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 开始执行进度对话框
	 */
	public void start() {
		if (prDialog == null) {
			initProgressDialog();
		}
		if (getIsShowDialog()) {
			prDialog.show();
		}

		thread = new Thread() {
			public void run() {
				boolean b = loadingDialogExecute.execute();
				if (b) {
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}
			}
		};
		thread.start();
	}

	/**
	 * 停止执行进度对话框，对话框关闭，线程停止
	 */
	public void stop() {
		prDialog.dismiss();
		handler.sendEmptyMessage(1);
	}

}
