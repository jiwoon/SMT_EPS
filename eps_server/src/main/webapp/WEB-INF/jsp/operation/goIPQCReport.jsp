<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<base href="/eps_server/">
    <meta charset="UTF-8">
    <title>操作报表</title>
    <link rel="stylesheet" href="static/css/operationCss.css">
    <link rel="stylesheet" href="static/css/jquery.autocompleter.css" />
    <link rel="stylesheet" href="static/css/positionCss.css">
    <script src="static/js/jquery-1.11.3.min.js"></script>
    <script src="static/js/jquery.autocompleter.js"></script>
    <script src="static/js/operationJs.js"></script>

</head>
<body>
    <section class="position-table" id="operationReport">
        <span class="manage-staff">操作报表</span>
        <div class="operation-search" id="operation-search">
        	<span class="span">
        		请选择相应类型：<select id="operationIPQC">
        						<option value=0>上料</option>
        						<option value=1>换料</option>
        						<option value=2>抽检</option>
        						<option value=3>全检</option>
        						<option value=4>仓库</option>
        			       </select>
        	</span><br />
        	<div>
        		<input type="text" readonly = "readonly" class="youhua"/>
        		客户名 ：<input id="clientName" type="text" />
     		线号 ：<select id="line">
	        		<option value=>不限</option>
	        		<option value=0>301</option>
	        		<option value=1>302</option>
	        		<option value=2>303</option>
	        		<option value=3>304</option>
	        		<option value=4>305</option>
	        		<option value=5>306</option>
	        		<option value=6>307</option>
	        		<option value=7>308</option>
        	    </select>
        	</div>
     		<br />
        	    <div class="autoCom">
        	    	<p style="float: left;">工单号 ：</p><input id = "workOrderNum" type="text" class="autoInput"/>
        	    </div>
        	    
     		起止时间 ：<input id="startTime" type="date" class="location"/> <em>--</em> <input id="endTime" type="date" />
     		<button id="searchBtn" class = "ui-accordion-header ui-state-default ui-corner-all ui-corner-top commonBtn btnSearch" >查询</button>
     		<button id="loadBtn" class = "ui-accordion-header ui-state-default ui-corner-all ui-corner-top commonBtn btnLoad">下载报表</button>
        </div>
    	 <!-- 表格显示 -->
        <div class="operationTable">
            <table style=word-break:break-all>
                <thead>
                    <tr>
                        <td width = 60>线号</td>
                        <td width = 60>工单号</td>
                        <td width = 60>客户订单号</td>
                        <td width = 60>槽位</td>
                        <td width = 220>物料编号</td>
                        <td width = 50>物料描述</td>
                        <td width = 330>物料规格</td>
                        <td width = 60>操作类型</td>
                        <td width = 110>操作者</td>
                        <td width = 100>操作时间</td>
                    </tr>
                </thead>
                <tbody id="clientMainTable"></tbody>
        </table>
        </div>
    </section>
</body>
</html>