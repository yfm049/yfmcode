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
		gopage('1');
	}
	function gopage(p) {
		$("#cpage").val(p);
		$("#userlist").submit();
	}
	function pv_q(u, w, h){ 
		var pv=''; 
		pv += '<object width="'+w+'" height="'+h+'" classid="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B" codebase="http://www.apple.com/qtactivex/qtplugin.cab">'; 
		pv += '<param name="src" value="'+u+'">'; 
		pv += '<param name="controller" value="true">'; 
		pv += '<param name="type" value="video/quicktime">'; 
		pv += '<param name="autoplay" value="false">'; 
		pv += '<param name="target" value="myself">'; 
		pv += '<param name="bgcolor" value="black">'; 
		pv += '<param name="pluginspage" value="http://www.apple.com/quicktime/download/index.html">'; 
		pv += '<embed src="'+u+'" width="'+w+'" height="'+h+'" controller="true" align="middle" bgcolor="black" target="myself" autoplay="false" type="video/quicktime" pluginspage="http://www.apple.com/quicktime/download/index.html"></embed>'; 
		pv += '</object>'; 
		document.write(pv); 
		}
</script>
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
						<td height="31"><div class="titlebt">通话列表</div></td>
					</tr>
				</table></td>
			<td width="16" valign="top" background="images/mail_rightbg.gif"><img
				src="images/nav-right-bg.gif" width="16" height="29" /></td>
		</tr>
		<tr>
			<td valign="middle" background="images/mail_leftbg.gif">&nbsp;</td>
			<td valign="top" bgcolor="#F7F8F9" height="100%">
				<form action="control/data!PhoneList.action" target="_self"
					id="userlist">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						style="margin-top: 5px">
						<tr class="nowtable" height="30">
							<td style="text-align: right;" class="left_txt2">用户:<input
								type="text" id="name" name="mapparam.name"
								value="${mapparam.name}"> 号码:<input type="text"
								id="name" name="mapparam.phone" value="${mapparam.phone}">
								<input type="button" value="搜索" onclick="search()"> <input
								type="hidden" name="page.cpage" id="cpage" value="1">
							</td>
						</tr>
					</table>
				</form>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="margin-top: 5px">
					<thead>
						<tr class="nowtable" height="30">
							<th width="10%" class="left_bt2">所属用户</th>
							<th width="10%" class="left_bt2">客户端号码</th>
							<th width="10%" class="left_bt2">姓名</th>
							<th width="10%" class="left_bt2">号码</th>
							<th width="10%" class="left_bt2">类型</th>
							<th width="10%" class="left_bt2">时长</th>
							<th width="10%" class="left_bt2">通话录音</th>
							<th width="10%" class="left_bt2">时间</th>
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
							<td class="left_txt2" style="text-align: center;"><%=mo.get("name")%></td>
							<td class="left_txt2" style="text-align: center;"><%=mo.get("phone")%></td>
							<td class="left_txt2" style="text-align: center;"><%=mo.get("phonename")%></td>
							<td class="left_txt2" style="text-align: center;"><%=mo.get("phonenum")%></td>
							<td class="left_txt2" style="text-align: center;"><%=mo.get("type")%></td>
							<td class="left_txt2" style="text-align: center: ;"><%=mo.get("shichang")%></td>
							<td class="left_txt2" style="text-align: center;">
								<%if (!"".equals(mo.get("luyinfile"))) {%> 
								<object width="240" height="15" classid="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B" codebase="http://www.apple.com/qtactivex/qtplugin.cab"><param name="src" value="sound/<%=mo.get("luyinfile")%>"><param name="controller" value="true"><param name="type" value="video/quicktime"><param name="autoplay" value="false"><param name="target" value="myself"><param name="bgcolor" value="black"><param name="pluginspage" value="http://www.apple.com/quicktime/download/index.html"><embed src="sound/<%=mo.get("luyinfile")%>" width="240" height="15" controller="true" align="middle" bgcolor="black" target="myself" autoplay="false" type="video/quicktime" pluginspage="http://www.apple.com/quicktime/download/index.html"></object>
								<%} %>
							</td>
							<td class="left_txt2" style="text-align: center;"><%=mo.get("ctime")%></td>
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
