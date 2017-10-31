<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<base href="/eps_server/">
		<meta charset="UTF-8">
		<title>上传报表</title>
		<script src="static/js/jquery-2.1.1.js" ></script>
		<script type="text/javascript">
			function downloadFileByForm() {
		        var url = "http://localhost:8080/eps_server/program/unfill";
		        var form = $("<form></form>").attr("action", url).attr("method", "post").attr("enctype", "multipart/form-data");
		        form.append($("#a"));
		        form.appendTo('body').submit().remove();
		    }
		</script>
	</head>
	<body>
		上传报表文件（Excel文件）
		<br />
    	 <input id="a" type="file" name="file"/>
		<br />
		<button id="upload-btn" onclick="downloadFileByForm()">上传</button>
	</body>
</html>
