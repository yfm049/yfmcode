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
<script src="js/jquery.validate.js"></script>
<script src="js/additional-methods.js"></script>
<script src="js/messages_zh.js"></script>

<script type="text/javascript">
	function adduser() {
		if ($("#adduserform").valid()) {
			var form = $("#adduserform").serialize();
			$.post($("#adduserform").attr("action"), form, function(msg) {
				if (msg.state == 0) {
					alert(msg.msg);
				} else {
					window.open("<%=basePath%>control/data!UserList.action", "_self");
				}

			}, "json");
		}
	}

	$(function() {
		$("#datepicker").datepicker({
			dateFormat: "yy-mm-dd",
			monthNames: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
		});
		$("#adduserform").validate();
	});
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
						<td height="31"><div class="titlebt">用户管理</div></td>
					</tr>
				</table></td>
			<td width="16" valign="top" background="images/mail_rightbg.gif"><img
				src="images/nav-right-bg.gif" width="16" height="29" /></td>
		</tr>
		<tr>
			<td valign="middle" background="images/mail_leftbg.gif">&nbsp;</td>
			<td valign="top" bgcolor="#F7F8F9" height="100%">
				<form action="<%=basePath%>control/data!UpdateUser.action" id="adduserform">
					<input type="hidden" name="mapparam.id" style="width: 30%" value="${map.id }">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						style="margin-top: 5px">
						<tr class="nowtable" height="30">
							<td class="left_txt2" style="padding-left: 15px">修改用户</td>
						</tr>
					</table>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">

						<tbody>
							<tr>
								<td class="left_txt2" style="width: 10%;text-align: right;">账号：</td>
								<td class="left_txt2">${map.name }</td>
							</tr>
							<tr>
								<td class="left_txt2" style="text-align: right;">密码：</td>
								<td class="left_txt2"><input type="password"
									name="mapparam.pass" style="width: 30%" class="text required" minlength="6" value="${map.pass }"></td>
							</tr>
							
							<tr>
								<td class="left_txt2" style="text-align: right;">手机号：</td>
								<td class="left_txt2"><input type="text"
									name="mapparam.phonenum" style="width: 30%" class="required" digits="true" minlength="6" value="${map.phonenum }"></td>
							</tr>
							<tr>
								<td class="left_txt2" style="text-align: right;">imei：</td>
								<td class="left_txt2"><input type="text"
									name="mapparam.imei" style="width: 30%" minlength="10" value="${map.imei }"></td>
							</tr>
							<tr>
								<td class="left_txt2" style="text-align: right;">截止时间：</td>
								<td class="left_txt2"><input type="text"
									name="mapparam.endtime" id="datepicker" readonly="readonly" style="width: 30%"
									class="required" value="${map.endtime }"></td>
							</tr>
							<tr>
								<td class="left_txt2" colspan="2" style="padding-left: 14%;">
									<input type="button" value="修改" onclick="adduser()">
								</td>

							</tr>
						</tbody>
					</table>
				</form>
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
