<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

<title>用户登录</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-1.8.3.js"></script>
<script type="text/javascript">
	function login(){
		var name=$("#AdminName").val();
		var pass=$("#AdminPass").val();
		$.post("<%=basePath%>control/server!ServerLogin.action",{"mapparam.name":name,"mapparam.pass":pass},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				window.open ("<%=basePath%>control/server!GetClientList.action", "_self");
			}
		}, "json");
	}
	
	
	function reset(){
		$("#AdminName").val('');
		$("#AdminPass").val('');
		
	}
	
</script>
</head>

<body>
	<div id="LoginLogo"></div>
	<div id="LoginTop">
		<div class="BarLeft"></div>
		<div id="LoginTopText">用戶登录</div>
		<div class="BarRight"></div>
		<div class="Cal"></div>
	</div>
	<div id="LoginBox">
		<table width="90%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td height="25">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td height="35" align="right" class="FontWeight">用户名：</td>
				<td><input name="AdminName" type="text" class="Input"
					id="AdminName" /></td>
			</tr>
			<tr>
				<td height="35" align="right" class="FontWeight">密码：</td>
				<td><input name="AdminPass" type="password" class="Input"
					id="AdminPass" /></td>
			</tr>
			<tr>
				<td height="10" colspan="2" align="center"></td>
			</tr>
			<tr>
				<td height="40" colspan="2" align="center">
				<input
					name="Submits" type="button" class="Button" value="登录" onclick="login()"/>
					<input name="ReSet" type="button" class="Button"
					id="ReSet" value="重置" onclick="reset()"/></td>
			</tr>
			<tr>
				<td height="25" colspan="2" align="right" class="Font999">
			</tr>
		</table>
	</div>
</body>
</html>
