<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<base href="/eps_server/">
	<meta charset="UTF-8">
	<title>登录</title>
	<script src="static/js/jquery-2.1.1.js" ></script>
	<script type="text/javascript">
		function login(){
			$.ajax({
			     url: 'user/login',
			     type: 'POST',  
			     data: {
			    	 id:$("#id").val(),
			    	 password:$("#password").val()
			     },  
			     success: function (returndata) {
			    	 $("#upload-btn").removeAttr("disabled");
					 alert(returndata.result);
					 history.go(0);
			     },  
			     error: function (returndata) {  
			    	 $("#upload-btn").removeAttr("disabled");
			         alert("失败，请检查网络");
			     }  
			});  
		}
	</script>
</head>
<body>
	ID:<input id="id" value="S444222"/>
	密码：<input type="password" id="password" value="12345678">
	<button id="upload-btn" onclick="login()">登录</button>
</body>
</html>