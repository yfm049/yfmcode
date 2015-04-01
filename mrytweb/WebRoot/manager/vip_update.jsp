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
		var end=$("#end").val();
		var img=$("#img").val();
		var state=$("#state").val();
		$.getJSON("<%=basePath%>manager/vip!update.action",{id:'${id}',end:end,currid:img,zt:state},function(msg){
			alert(msg.msg);
			window.close();
		});
	}
</script>
</head>

<body>
	<div>
		<table class="table" style="margin-top: 10px">
			<tbody id="data">
				<tr>
					<td style="text-align: center">截止日期</td>
					<td><input id="end" type="text" style="width: 150px;" class="username" value="${map.end }" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="readonly"></td>
				</tr>
				<tr>
					<td style="text-align: center">图片编号</td>
					<td><select id="img" name="img" style="width: 150px;" disabled="disabled">
							<c:forEach items="${lmo }" var="mo">
								<option value="${mo.img }">${mo.img }</option>
							</c:forEach>
							
					</select></td>
				</tr>
				<tr>
					<td>状态</td>
					<td><select id="state" name="state" style="width: 150px;">
							<option value="1">可用</option>
							<option value="0">禁用</option>
							<option value="2">加黑</option>
					</select></td>
				</tr>
				<tr>
					<td colspan="2">
						<button onclick="update()">确定</button>
					</td>
				</tr>
			</tbody>
		</table>
		<script>
			$("#img").val('${map.currid}');
			$("#state").val('${map.state}');
		</script>
	</div>



</body>
</html>
