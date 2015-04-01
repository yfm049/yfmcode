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
<script language="javascript" type="text/javascript"
	src="<%=basePath%>My97DatePicker/WdatePicker.js"></script>

<base href="<%=basePath%>">

<title>My JSP 'right.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="manager/js/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="manager/css/jxc.css">
<script type="text/javascript">
	function update(){
		var pass=$("#pass").val();
		var cpass=$("#cpass").val();
		if(pass.length<=0){
			alert("密码不能为空");
			return;
		}
		if(pass!=cpass){
			alert("确认密码不正确");
			return;
		}
		$.getJSON("<%=basePath%>manager/user!update.action",{'user.pass':pass},function(msg){
			alert(msg.msg);
			window.close();
		});
	}
</script>
</head>

<body>
	<div>
		<table class="table" style="margin-top: 10px">
			<tbody>
				<tr>
					<td style="text-align: center">密码</td>
					<td><input id="pass" type="text" style="width: 100px;"></td>
				</tr>
				<tr>
					<td style="text-align: center">确认密码</td>
					<td><input id="cpass" type="text" style="width: 100px;"></td>
				</tr>
				<tr>
					<td colspan="2">
						<button onclick="update()">确定</button>
					</td>
				</tr>
			</tbody>
		</table>
	</div>



</body>
</html>
