<!--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>-->
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="/eps_server/">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <title>仓库出入库报表</title>
    <link rel="stylesheet" href="static/css/storeCss.css">
    <link rel="stylesheet" href="static/css/jquery.autocompleter.css"/>
    <link rel="stylesheet" href="static/css/positionCss.css">
    <script src="static/js/jquery-1.11.3.min.js"></script>
    <script src="static/js/jquery.autocompleter.js"></script>
    <script src="static/js/storeJs.js"></script>

</head>
<body>
<div class="showWaiting" id="showWaiting">
    <span>数据加载中，请等待<i><img src="static/images/shalou.gif"/></i></span>
</div>
<section class="position-table" id="storeReport">
    <div id="main-table">
        <span class="manage-staff">仓库出入库报表</span>
        <div style="width: 100%;background-color: #ffffff;border-bottom: 5px solid #77d5f7;">
            <div class="operation-search" id="operation-search">


                <div class="userline">
                    <span><p>仓位：</p><input id="positionName" type="text"></span>
                    <span><p>供应商：</p><input id="customName" type="text"/></span>
                    <span><p>操作员：</p><input id="operatorName" type="text"></span>
                </div>
                <br/>
                <div class="autoCom">
                    <p style="float: left;">料号 ：</p><input id="materialNoName" type="text" class="autoInput"/>
                </div>
                <div class="seTime">
                    起止时间 ：<input id="startTime" type="date" class="location"/> <em>--</em> <input id="endTime"
                                                                                                  type="date"/>
                </div>
                <button id="storeSearchBtn" class="ui-accordion-header ui-state-default ui-corner-all ui-corner-top commonBtn
        btnSearch">查询
                </button>
                <%--<button id="loadBtn" class="ui-accordion-header ui-state-default ui-corner-all ui-corner-top commonBtn--%>
        <%--btnLoad">下载报表--%>
                <%--</button>--%>
            </div>
        </div>
        <!-- 表格显示 -->
        <div class="operationTable">
            <table>
                <thead>
                <tr>
                    <td width=100>时间戳</td>
                    <td width=120>料号</td>
                    <td width=50>数量</td>
                    <td width=50>操作员</td>
                    <td width=100>操作时间</td>
                    <td width=50>仓位</td>
                    <td width=50>供应商</td>
                    <td width=50></td>
                </tr>
                </thead>
                <tbody id="storeMainTable"></tbody>
            </table>
        </div>


    </div>
</section>
</body>
</html>