<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
%>
<%@taglib prefix="y" uri="/yfm-page"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>用户管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="images/skin.css" rel="stylesheet" type="text/css" />
	<link href="css/ui-darkness/jquery-ui-1.9.2.custom.css" rel="stylesheet">
	<script src="js/jquery-1.8.3.js"></script>
	<script src="js/jquery-ui-1.9.2.custom.js"></script>
<script type="text/javascript">
	function search() {
		var name = $("#name").val();
		if (name == "") {
			$("#msg").text("账号不能为空");
			$("#dialog").dialog();
		} else {
			gopage('1');
		}
	}
	function gopage(p) {
		$("#cpage").val(p);
		$("#userlist").submit();
	}
	function showaddDialog() {
		window.open("<%=basePath%>user/adduser.jsp", "_self");
	}
	function getUser(id){
		window.open("<%=basePath%>control/data!GetUser.action?mapparam.id="+id, "_self");
	}
	function deleteuser(id){
		$.post("<%=basePath%>control/data!DeleteUser.action",{"mapparam.id":id},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				window.location.reload(true);
			}
		});
	}
	function UpdateUserState(id,state){
		$.post("<%=basePath%>control/data!UpdateUserState.action",{"mapparam.id":id,"mapparam.state":state},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				window.location.reload(true);
			}
		});
	}
	function adddevice(id){
		window.open("<%=basePath%>device/adddevice.jsp?userid="+id, "_self");
	}
</script>
<%
	Object oc=session.getAttribute("adminuser");
	if(oc==null){
%>
	<script type="text/javascript">
		alert("登录超时,请重新登录");
		window.open('<%=basePath%>login.jsp','_parent');
	</script>
<%} %>
</head>

<body>
	<table width="100%" height="99%" border="0" cellpadding="0"
		cellspacing="0">
		<tr>
			<td width="17" valign="top" background="images/mail_leftbg.gif"><img
				src="images/left-top-right.gif" width="17" height="29" /></td>
			<td valign="top" background="images/content-bg.gif"><table
					width="100%" height="31" border="0" cellpadding="0" cellspacing="0"
					class="left_topbg" id="table2">
					<tr>
						<td height="31"><div class="titlebt">用戶管理</div></td>
					</tr>
				</table></td>
			<td width="16" valign="top" background="images/mail_rightbg.gif"><img
				src="images/nav-right-bg.gif" width="16" height="29" /></td>
		</tr>
		<tr>
			<td valign="middle" background="images/mail_leftbg.gif">&nbsp;</td>
			<td valign="top" bgcolor="#F7F8F9" height="100%">
				<form action="control/data!UserList.action" target="_self"
					id="userlist">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						style="margin-top: 5px">
						<tr class="nowtable" height="30">
							<td style="text-align: right;" class="left_txt2">账号:<input
								type="text" id="name" name="mapparam.name"
								value="${mapparam.name}"><input type="button"
								value="搜索用户" onclick="search()"> <input type="hidden"
								name="page.cpage" id="cpage" value="1"> <input
								type="button" value="添加用户" onclick="showaddDialog()">
							</td>
						</tr>
					</table>
				</form>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="margin-top: 5px">
					<thead>
						<tr class="nowtable" height="30">
							<th width="10%" class="left_bt2">账号</th>
							<th width="10%" class="left_bt2">手机号</th>
							<th width="10%" class="left_bt2">IMEI码</th>
							<th width="10%" class="left_bt2">截止时间</th>
							<th width="10%" class="left_bt2">操作</th>
						</tr>
					</thead>
					<tbody>
						<%
							Object object = request.getAttribute("lmo");
							if (object != null) {
								List<Map<String, Object>> lmo = (List<Map<String, Object>>) object;
								for (int i = 0; i < lmo.size(); i++) {
									Map<String, Object> mo = lmo.get(i);
						%>
						<tr <%=i % 2 == 0 ? "bgcolor=#f2f2f2" : ""%> height="30">
							<td class="left_txt2" style="text-align: center;"><a href="<%=basePath%>control/data!DeviceList.action?mapparam.name=<%=mo.get("name")%>"><%=mo.get("name")%></a></td>
							
							<td class="left_txt2" style="text-align: center;"><%=mo.get("phonenum")%></td>
							<td class="left_txt2" style="text-align: center;"><%=mo.get("imei")%></td>
							<td class="left_txt2" style="text-align: center;"><%=mo.get("endtime")%></td>
							<td class="left_txt2" style="text-align: center;">
							<a href="javascript:deleteuser('<%=mo.get("id")%>')">删除</a>&nbsp;&nbsp;
							<a href="javascript:getUser('<%=mo.get("id")%>')">修改</a>&nbsp;&nbsp;
							<a href="javascript:UpdateUserState('<%=mo.get("id")%>','<%="0".equals(mo.get("state").toString())?"1":"0"%>')"><%="1".equals(mo.get("state").toString())?"禁用":"启用"%></a>&nbsp;&nbsp;
							<a href="javascript:adddevice('<%=mo.get("id")%>')">添加客户端</a>
							</td>
						</tr>
						<%
							}
							}
						%>

					</tbody>
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr class="nowtable">
						<td style="text-align: right;"><y:ypage
								tsize="${page.tsize }" cpage="${page.cpage }" /></td>
					</tr>
				</table>
			</td>
			<td background="images/mail_rightbg.gif">&nbsp;</td>
		</tr>
		<tr>
			<td valign="bottom" background="images/mail_leftbg.gif"><img
				src="images/buttom_left2.gif" width="17" height="17" /></td>
			<td background="images/buttom_bgs.gif"><img
				src="images/buttom_bgs.gif" width="17" height="17"></td>
			<td valign="bottom" background="images/mail_rightbg.gif"><img
				src="images/buttom_right2.gif" width="16" height="17" /></td>
		</tr>
	</table>

</body>
</html>
