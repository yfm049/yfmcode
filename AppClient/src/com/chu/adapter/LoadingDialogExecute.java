package com.chu.adapter;

/**
 * 进度对话框代理方法类，需要由用户实现execute、executeSuccess、executeFailure三个方法。 execute方法由子线程异步执行，不允许更新主线程UI；executeSuccess、executeFailure两方法由主线程执行，可以更新主线程UI
 * 
 * @see LoadingDialog
 * @author 优化设计
 * @version 0.2
 */
public abstract class LoadingDialogExecute {
	private String errorInfo = "发生错误";

	/**
	 * 获取最后一次错误信息描述
	 * 
	 * @return
	 */
	public String getErrorInfo() {
		return errorInfo;
	}

	/**
	 * 设置最后一次错误信息描述
	 * 
	 * @param errorInfo
	 */
	public void setErrorInfo(String errorInfo) {
		if (errorInfo == null)
			return;
		this.errorInfo = errorInfo;
	}

	/**
	 * 异步执行方法，不允许用户在此方法中更新主线程UI，需要用户自行对异常捕获处理
	 * 
	 * @return
	 */
	public abstract boolean execute();

	/**
	 * 异步方法执行成功时执行的方法，可在此方法中更新主线程UI
	 */
	public abstract void executeSuccess();

	/**
	 * 异步方法执行失败时执行的方法，可在此方法中更新主线程UI
	 */
	public abstract void executeFailure();
}
