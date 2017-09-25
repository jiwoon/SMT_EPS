<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<meta charset="UTF-8">
		<title>上传排位表</title>
		<script src="static/js/jquery-2.1.1.js" ></script>
		<script type="text/javascript">
			function upload(){
				var formData = new FormData($("#file")[0]);  
				$.ajax({
				     url: '<%=basePath%>program/upload',
				     type: 'POST',  
				     data: formData,  
				     async: false,  
				     cache: false,  
				     contentType: false,  
				     processData: false,  
				     success: function (returndata) {  
				         if(returndata.result == "succeed"){
							alert("上传成功");
						 }else{
							 alert(returndata.result);
						 }
				     },  
				     error: function (returndata) {  
				         alert("上传失败");
				     }  
				});  
			}
		</script>
	</head>
	<body>
		上传排位表文件（Excel文件）
		<br />
		<form id="file">  
    	  	<input type="file" name="programFile"/>
		</form>  
		<br />
		<button onclick="upload()">上传</button>
	</body>
</html>
