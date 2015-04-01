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
		<link rel="stylesheet" type="text/css" href="manager/css/jxc.css">
		<script type="text/javascript">
			function updatezt(id,zt){
				$.getJSON("<%=basePath%>manager/vip!upzt.action",{id:id,zt:zt},function(msg){
					alert(msg.msg);
					window.location.reload();
				});
			}
			function update(id){
				window.showModalDialog("<%=basePath%>manager/vip!toupdatePage.action?id="+id,window,"toolbar=0,menubar=0,resizable=0,width=380,height=300,center:yes");
				window.location.reload();
			}
			function dele(id){
				$.getJSON("<%=basePath%>manager/vip!dele.action",{id:id},function(msg){
					alert(msg.msg);
					window.location.reload();
				});
			}
			function gopage(p){
				$("#cpage").val(p);
				$("form").submit();
			}
		</script>
	</head>

	<body>
		<div class="menu">
			<form action="manager/vip!viplist.action" method="post" style="margin: 0px;padding: 0px;" target="_self">
					<input id="cpage" type="hidden" name="page.cpage" value="1">
					IMEI：<input type="text" id="imei" name="imei" value="${imei }">&nbsp;
					状态：<select id="zt" name="zt">
						<option value="-1">全部</option>
						<option value="1">可用</option>
						<option value="0">禁用</option>
						<option value="2">加黑</option>
					</select><input type="submit" value="查询">
			</form>
		</div>
		
		<div>
			<table class="table" style="margin-top: 10px">
				<tr>
					<th>
						IMEI码
					</th>
					<th>
						认证码
					</th>
					<th>
						创建时间
					</th>
					<th>
						截止时间
					</th>
					<th>
						图片名称
					</th>
					<th>
						最后查看时间
					</th>
					<th>
						状态
					</th>
					<th>
						操作
					</th>
				</tr>
				<c:forEach items="${lmo}" var="mo">
				<tr>
					<td>
						${mo.imei }
					</td>
					<td>
						${mo.code }
					</td>
					<td>
						${mo.start }
					</td>
					<td>
						${mo.end }
					</td>
					<td>
						${mo.currid }
					</td>
					<td>
						${mo.last }
					</td>
					<td>
						${mo.zt }
					</td>
					<td style="text-align: center;width: 240px">
						<c:if test="${mo.state==0||mo.state==2 }"><a href="javascript:void(0)" onclick="updatezt('${mo.id }','1')">可用</a></c:if>
						<c:if test="${mo.state==1 }"><a href="javascript:void(0)" onclick="updatezt('${mo.id }','0')">禁用</a></c:if>
						<a href="javascript:void(0)" onclick="updatezt('${mo.id }','2')">加黑</a>
						<a href="javascript:void(0)" onclick="update('${mo.id }')">修改</a>
						<a href="javascript:void(0)" onclick="dele('${mo.id }')">删除</a>
					</td>
				</tr>
				</c:forEach>
			</table>
		</div>
		<div class="page">
			<y:ypage tsize="${page.tsize}" cpage="${page.cpage}" psize="${page.psize}"/>
		</div>
	</body>
	<script type="text/javascript">
			$("#zt").val('${zt}');
			$("#imei").val('${imei}');
		</script>
</html>
