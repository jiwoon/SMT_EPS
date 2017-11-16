<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<base href="/eps_server/">
    <meta charset="UTF-8">
    <title>客户报表展示页面</title>
    <link rel="stylesheet" href="static/css/ClientCss.css">
    <link rel="stylesheet" href="static/css/positionCss.css">
    <link rel="stylesheet" href="static/css/jquery.autocompleter.css" />
    <script src="static/js/jquery-1.11.3.min.js"></script>
    <script src = "static/js/jquery.autocompleter.js"></script>
    <script src="static/js/ClientJs.js"></script>
</head>
<body>
	<div class="showWaiting" id="showWaiting">
		<span>数据加载中，请等待<i><img src="static/images/shalou.gif"/></i></span>
	</div>
    <section class="position-table" id="clientReport">
        <span class="manage-staff">客户报表</span>
        <!--搜索框-->
        <div class="search clearfix" id="search">
	        <div style="float: left; margin-right: 18px;">
	        	客户名：<input id= "client" type="text" />
	        	程序表编号：<input id="programNum" type="text" />
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
        	<div class="autoFinish">
        		订单号：<input id="OrderNum" type="text" />
        	</div>

        	<div class="autoFinish1"> 
        		工单号 ：<input id="workOrderNum" type="text" />
        	</div>
        	<div class="date">
        		起止时间：<input id="startTime" type="date" /> <em>--</em> <input id="endTime" type="date"/>
        	</div>
            <button id="searchBtn" class="ui-accordion-header ui-state-default ui-corner-all ui-corner-top clientSearchBtn btnCommon">查询</button>
            <button id = "downloadBtn" class="ui-accordion-header ui-state-default ui-corner-all ui-corner-top downloadBtn btnCommon">报表下载</button>
        </div>
        <!-- 表格显示 -->
        <div class="clientTable">
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