<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="/eps_server/">
    <meta charset="UTF-8">
    <title>登录界面</title>
    <link rel="stylesheet" href="static/css/loginCss.css">
    <link rel="stylesheet" href="static/css/manageSystem.css">
    <script src="static/js/jquery-1.11.3.min.js"></script>
    <script src="static/js/konva.min.js"></script>
    <script src="static/js/loginJs.js"></script>

</head>
<body>
    <div class="login-box">
        <div id="container" class="loginOutBox">
            <div class="login">
                <span class="title">系统登录</span>
              <section class="login-main">
		          <form action="goManage.jsp" id="login-form" method="post">     	
		                  	账号：<input id="user" type="text" name = "userid"><br>
		 						  <input id="transportName" type="text" name = "transportName" value = "123456" style="display: none">                  
		                  	密码：<input id="password" type="password">
		          </form>
              </section>
                <section>
                    <button id="denglu" class="ui-state-default ui-corner-all ui-corner-top login-btn">登录</button>
                </section>
            </div>
        </div>
        <div class="showError" id="showError" style="display: none">
            <span class="errorShowBox">您输入的账号或者密码错误，请重新输入
            <button id="errorShowBox-btn" class="errorShowBox-btn">确定</button>
            </span>
        </div>
    </div>
</body>
</html>
