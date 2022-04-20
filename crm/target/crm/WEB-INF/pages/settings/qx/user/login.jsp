<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String basePath = request.getScheme() + "://" + request.getServerName()
 					 + ":" + request.getServerPort()
					 + request.getContextPath() + "/"; %>
<html>
<head>
	<base href="<%= basePath %>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="tex/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function (){
        //给整个窗口添加键盘按下事件
        $(window).keydown(function (e){
            //当按下回车键时，提交请求
            if(e.keyCode == 13){
                //模拟单击事件
                $("#loginBtn").click();
            }
        });

		//使用id选择器，给登录按钮绑定单击事件
		$("#loginBtn").click(function (){
			//发请求，先获取页面的参数
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());
			//prop获取元素指定的属性值（true/false）
			var isRem = $("#isRem").prop("checked");
			//验证账号和密码不为空
			if(loginAct == ""){
				alert("用户名不能为空");
				return;
			}
			if(loginPwd == ""){
				alert("密码不能为空");
				return;
			}
            //显示正在验证
            $("#msg").html("正在验证账号...");
			//向后台发送异步请求
			$.ajax({
				//发送的地址
				url:'settings/qx/user/login',
				//传输的数据
				data:{
					loginAct:loginAct,
					loginPwd:loginPwd,
					isRem:isRem
				},
				//请求方式
				type:'post',
				//响应信息的类型
				dataType:'json',
				//回调函数处理响应，data接收后台的响应信息
				success:function (data){
					if(data.code == "1"){
						//登陆成功，跳转到业务主页面
						window.location.href = "/crm/workbench/index";
					}else {
						//登录失败，提示错误信息
						$("#msg").html(data.message);
					}
				}
			});
		});
	});
</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;"></span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input id="loginAct" class="form-control" type="text" value="${cookie.loginAct.value}" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input id="loginPwd" class="form-control" type="password" value="${cookie.loginPwd.value}" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
                            <c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">
                                <input id="isRem" type="checkbox" checked> 十天内免登录
                            </c:if>
                            <c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">
                                <input id="isRem" type="checkbox"> 十天内免登录
                            </c:if>
						</label>
						&nbsp;&nbsp;
						<span id="msg"></span>
					</div>
					<button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>