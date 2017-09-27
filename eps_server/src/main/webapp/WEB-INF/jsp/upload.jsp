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
				$("#upload-btn").text("请稍等....30秒左右");
				$("#upload-btn").attr("disabled","disabled");
				var formData = new FormData($("#file")[0]);  
				$.ajax({
				     url: '<%=basePath%>program/upload',
				     type: 'POST',  
				     data: formData,  
				     cache: false,  
				     contentType: false,  
				     processData: false,  
				     success: function (returndata) {
				    	 $("#upload-btn").removeAttr("disabled");
				    	 $("#upload-btn").text("上传");
				         if(returndata.result == "succeed"){
							alert("上传成功");
						 }else{
							 alert(returndata.result);
						 }
				     },  
				     error: function (returndata) {  
				    	 $("#upload-btn").removeAttr("disabled");
				    	 $("#upload-btn").text("上传");
				         alert("上传失败，请检查网络");
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
		<button id="upload-btn" onclick="upload()">上传</button>
	</body>
</html>
