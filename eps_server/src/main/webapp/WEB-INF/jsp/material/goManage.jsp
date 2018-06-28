<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="/eps_server/">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <title>物料保质期管理页面</title>
    <link rel="stylesheet" href="static/css/positionCss.css">
    <link rel="stylesheet" href="static/css/jquery.autocompleter.css">
    <link rel="stylesheet" href="static/css/material.css">
    <script src="static/js/jquery-1.11.3.min.js"></script>
    <script src="static/js/jquery.autocompleter.js" type="text/javascript"></script>
    <script src="static/js/material.js" type="text/javascript"></script>
</head>
<body>
	<!--物料保质期管理-->
	<div class="showWaiting" id="showWaiting">
    	<span>数据加载中，请等待<i><img src="static/images/shalou.gif"/></i></span>
	</div>
	<section class="position-table" id="position-table">
    	<span class="manage-staff">物料保质期管理</span>
    	<!--搜索框-->
        <div class="search" id="search">
			<span>输入或者选择相关信息进行查询：</span>
			<div id="lengthDiv" style="width:800px">
				<div class="autoComplete">
					物料编号：<input type="text" id="materialNo">
				</div>  
				<div class="autoComplete">
					保质期(天)：<input type="text" id="perifdOfValidity">
				</div>                   	
				<button id="find" class="ui-state-default ui-corner-top ui-corner-all ptsBtn" style="margin-top:-10px;">查询</button>
				<!--新增按钮-->
				<div class="addNew" id="addNew">
					点击新增<button id="addBtn" class="ui-state-default ui-corner-top ui-corner-all ptsBtn">增加</button>
				</div>
			</div>
		</div>
		
		<!--表格显示内容-->
		<div id="mainTable" class="mainTable">
			<table width="600px" cellpadding="0" cellspacing="0">
				<thead>
					<tr id="tableTitle">
						<td>物料编号</td>
                        <td>保质期(天)</td>
                        <td>信息修改</td>
                    </tr>
                </thead>
                <tbody id="ShowTable"></tbody>
             </table>
             <div id="btnControl"></div>
        </div>
	</section>
	
	<!--物料保质期表增加具体操作页面-->
    <section class="material-operation" id="material-operation" style="display:none">
    	<div class="new-position">
                                物料编号：<input type="text" id="addMaterialNo"> <br>
                               保质期(天)：<input type="text" id="addPerifdOfValidity"><br>
           <button id="new-position-save" class="ui-button ui-corner-all ui-state-default new-position-save" >保存</button>
        </div>
        <button id="new-position-back" class="ui-button ui-corner-all ui-state-default new-position-back">返回</button>
    </section>
    
    <!--物料保质期表修改操作界面-->
    <section class="material-operation" id="material-modify"  style="display:none">
		<div class="new-position">
				<input type="hidden" id="modifyId">
             	物料编号：<input type="text" id="modifyMaterialNo"> <br>
				保质期(天)：<input type="text" id="modifyPerifdOfValidity"><br>
           		<button id="modify-position-save" class="ui-button ui-corner-all ui-state-default new-position-save" >保存</button>
        </div>
        <button id="modify-position-back" class="ui-button ui-corner-all ui-state-default new-position-back">返回</button>
	</section>
</body>
</html>