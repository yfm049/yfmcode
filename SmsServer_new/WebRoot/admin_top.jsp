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

<title>顶部</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="images/skin.css" rel="stylesheet" type="text/css">
<script language=JavaScript>
	function logout() {
		if (confirm("您确定要退出控制面板吗？"))
			top.location = "out.asp";
		return false;
	}
	function showsubmenu(sid) {
		var whichEl = eval("submenu" + sid);
		var menuTitle = eval("menuTitle" + sid);
		if (whichEl.style.display == "none"){
			eval("submenu" + sid + ".style.display=\"\";");
		}else{
			eval("submenu" + sid + ".style.display=\"none\";");
		}
	}
</script>
<%
	Object oc=session.getAttribute("adminuser");
	if(oc==null){
%>
	<script type="text/javascript">
		window.open('<%=basePath%>login.jsp','_parent');
	</script>
<%} %>
</head>

<body leftmargin="0" topmargin="0">
	<table width="100%" height="64" border="0" cellpadding="0"
		cellspacing="0" class="admin_topbg">
		<tr>
			<td width="61%" height="64"><img src="images/logo.gif"
				width="262" height="64"></td>
			<td width="39%" valign="top"><table width="100%" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td width="74%" height="38" class="admin_txt">管理员：<b>${sessionScope.adminuser }</b>
							您好,感谢登陆使用！
						</td>
						<td width="22%"><a href="#" target="_self"
							onClick="logout();"><img src="images/out.gif" alt="安全退出"
								width="46" height="20" border="0"></a></td>
						<td width="4%">&nbsp;</td>
					</tr>
					<tr>
						<td height="19" colspan="3">&nbsp;</td>
					</tr>
				</table></td>
		</tr>
	</table>
</body>
</html>
