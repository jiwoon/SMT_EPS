<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<base href="/eps_server/">
		<meta charset="UTF-8">
		<meta name="renderer" content="webkit">
		<title>上传排位表</title>
		<script src="static/js/jquery-2.1.1.js" ></script>
		<script type="text/javascript">
			function upload(){
				$("#upload-btn").text("请稍等....30秒左右");
				$("#upload-btn").attr("disabled","disabled");
				var formData = new FormData($("#file")[0]);  
				$.ajax({
				     url: 'program/upload',
				     type: 'POST',  
				     data: formData,  
				     cache: false,  
				     contentType: false,  
				     processData: false,  
				     success: function (returndata) {
				    	 $("#upload-btn").removeAttr("disabled");
				    	 $("#upload-btn").text("上传");
						 alert(returndata.result);
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
    	  	<select name="boardType">
    	  		<option value="0">默认</option>
				<option value="1">AB面</option>
				<option value="2">A面</option>
				<option value="3">B面</option>
    	  	</select>
		</form>  
		<br />
		<button id="upload-btn" onclick="upload()">上传</button>
		<br />
		<br />
		<a href="static/standard.docx">点击下载：排位表格式规范文档</a>
	</body>
</html>
