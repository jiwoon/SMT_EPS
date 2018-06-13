<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="/eps_server/">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <title>人员加密二维码</title>
	<script src="static/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript">
		$(function(){
			$("#size").val(${size});
			$("#code").attr("src","static/temp.png?random="+Math.random());
			$("#code").attr("width", ${size});
			$("#code").attr("height", ${size});
		});
		function createCode(){
			var size = $("#size").val();
			window.location="/eps_server/user/getCodePic?id=${id}&size="+size;
		}
	</script>
</head>
<body>
	<img id="code"/>
	<br>二维码大小：<br>
	<input id="size" type="number" />
	<button onclick="createCode()">生成</button>
</body>
</html>
