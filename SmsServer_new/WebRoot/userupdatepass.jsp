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
		var pass=$("#pass").val();
		var passed=$("#passed").val();
		if(pass.length<6){
			alert("密码长度不能小于6位");
			return;
		}
		if(passed!=pass){
			alert("确认密码不正确");
			return;
		}
		$.post("<%=basePath%>control/server!UpdatePassword.action",{"mapparam.pass":pass},function(msg){
			alert(msg.msg);
			window.open ("<%=basePath%>control/server!GetClientList.action", "_self");
		}, "json");
	}
	
	
	function reset(){
		$("#AdminName").val('');
		$("#AdminPass").val('');
		
	}
	
</script>
<%
	Object oc = session.getAttribute("userid");
	if (oc == null) {
%>
<script type="text/javascript">
		alert("登录超时,请重新登录");
		window.open('<%=basePath%>userlogin.jsp', '_parent');
</script>
<%
	}
%>
</head>

<body>
	<div id="LoginLogo"></div>
	<div id="LoginTop">
		<div class="BarLeft"></div>
		<div id="LoginTopText">密码修改</div>
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
					<td height="35" align="right" class="FontWeight">密码：</td>
					<td><input name="pass" type="password" class="Input"
						id="pass" /></td>
				</tr>
				<tr>
					<td height="35" align="right" class="FontWeight">确认密码：</td>
					<td><input name="passed" type="password" class="Input"
						id="passed" /></td>
				</tr>
				<tr>
					<td height="10" colspan="2" align="center"></td>
				</tr>
				<tr>
					<td height="40" colspan="2" align="center"><input
						name="Submits" type="button" class="Button" value="修改"
						onclick="login()" /> <input name="ReSet" type="button"
						class="Button" id="ReSet" value="重置" onclick="reset()" /></td>
				</tr>
				<tr>
					<td height="25" colspan="2" align="right" class="Font999">
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
