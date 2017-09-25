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
		<script src="static/js/ajaxfileupload.js" ></script>
		<script type="text/javascript">
			function upload(){
				$.ajaxFileUpload({
					fileElementId:"file",
					type:"post",
					url:"<%=basePath%>program/upload",
					error:function(){
						alert("上传失败");
					},
					success:function(data){
						alert(data.result);
					}
				});
			}
		</script>
	</head>
	<body>
		上传排位表文件（Excel文件）
		<br />
		<form id="file">  
    	  	<input type="file"/>
		</form>  
		<br />
		<button onclick="upload()">上传</button>
	</body>
</html>
