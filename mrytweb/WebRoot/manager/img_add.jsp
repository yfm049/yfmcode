<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="y" uri="/yfm-page"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<base href="<%=basePath%>">

<title>My JSP 'right.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="manager/js/jquery.js"></script>
<script src="manager/js/uploadify.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="manager/css/uploadify.css">
<script type="text/javascript">
$(function() {
	$("#xzwj").uploadify({
		height        : 30,
		fileSizeLimit :'4096kb',
		removeCompleted:false,
		buttonText    :'选择文件',
		auto     	  : false,
		queueID  	  : 'fileform',
		fileTypeExts  :'*.gif',
		swf           : '<%=basePath%>/manager/js/uploadify.swf',
		uploader      : '<%=basePath%>/manager/img!upload.action',
		width         : 150
	});
});
function uploadfile(){
	$("#xzwj").uploadify('upload','*');
}
function closedialog(){
	window.close();
}
</script>
</head>

<body style="margin: 0px;padding: 0px">
		<table class="table">
			<tr>
				<td  style="width: 70%">
					<div id="fileform" style="width: 100%;height: 400px;overflow: visible;">
						
					</div>
				</td>
				<td style="width: 30%" valign="top">
					<div id="xzwj">选择文件</div>
					<div id="scwj" onclick="uploadfile()" class="uploadify-button " style="height: 30px; line-height: 30px; width: 150px;cursor: pointer;"><span class="upladify-button-text">上传文件</span></div>
					<div id="close" onclick="closedialog()" class="uploadify-button " style="height: 30px; line-height: 30px; width: 150px;cursor: pointer;margin-top: 12px"><span class="upladify-button-text">关闭窗口</span></div>
				</td>
			</tr>
		</table>
</body>
</html>
