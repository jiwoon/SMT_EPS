<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="/eps_server/">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <title>排位表管理页面</title>
    <link rel="stylesheet" href="static/css/positionCss.css">
    <link rel="stylesheet" href="static/css/jquery.autocompleter.css">
    <script src="static/js/jquery-1.11.3.min.js"></script>
    <script src="static/js/jquery.autocompleter.js" type="text/javascript"></script>
    <script src="static/js/editTable.js"></script>
    <script src="static/js/positionJs.js"></script>
</head>
<body>
<!--站位表管理-->
<div class="showWaiting" id="showWaiting">
    <span>数据加载中，请等待<i><img src="static/images/shalou.gif"/></i></span>
</div>
<section class="edit-table" id="edit-table" style="display: none;">
    <div class="smt-back">
        <a href="javascript:void(0);" >返回</a>
        <span>确认编辑完成请点击页底保存</span>
    </div>
    <div class="smt-table" data-example-id="hoverable-table">
        <div class="table-header">
            <p></p>
            <p></p>
            <p></p>
        </div>
        <table class="table table-hover editable" id="SMTTable">
            <tr>
                <th style="width: 4%;">序列号</th>
                <th style="width: 8%;">站位</th>
                <th style="width: 14%;">程序料号</th>
                <th style="width: 5%;">数量</th>
                <th style="width: 21%;">BOM料号/规格</th>
                <th style="width: 20%;">单板位置</th>
                <th style="width: 5%;">主替</th>
                <th style="width: 23%;">operation</th>
            </tr>
        </table>
        <div class="save-btn">
            <a href="javascript:void(0);" id="save-btn">保存</a>
        </div>
    </div>
</section>
<section class="position-table" id="position-table">
    <span class="manage-staff">站位表管理</span>
    <span class="position-table-warm">提醒：上传排位表时，若“未开始”的项目列表中存在“版面类型”、“工单号”、“线别”三者一致时
                ，将覆盖上一份“未开始”中的项目</span>
    <!--搜索框-->
    <div class="position-table-search">
        <div class="completeAuto">
            <p style="float: left;">站位表：</p><input id="stand-position" type="text">
        </div>
        <div class="completeAuto">
            工单：<input id="work-num" type="text">
        </div>
        状态：<select id="state">
        <option value=>不限</option>
        <option value=0>未开始</option>
        <option value=1>进行中</option>
        <option value=2>已完成</option>
        <option value=3>已作废</option>
    </select>
        线号：<select id="line-num">
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
        <button id="pcBtn" class="ui-button ui-corner-all ui-state-default ptsBtn">查询</button>
    </div>
    <!--上传文件-->
    <div class="upload" id="upload">
        <input id="fileUpload" name="programFile" type="file" class="fileUpload">
        <select name="boardType" id="banmian" class="banmian">
            <option value="0">默认</option>
            <option value="1">AB面</option>
            <option value="2">A面</option>
            <option value="3">B面</option>
        </select>
        <button id="fileBtn" class="ui-button ui-corner-all ui-state-default fileBtn">上传</button>
        <span class="show-warm">请选择xls格式的文件!!!</span>
    </div>
    <div class="mainTable positionTable">
        <table style="cellpadding=0 ;cellspacing=0">
            <thead id="positionThead">
            <tr id="positionTableTitle">
                <td style="width: 21%;">站位表</td>
                <td style="width: 21%;">工单</td>
                <td style="width: 7%;">版面</td>
                <td style="width: 21%;">上传时间</td>
                <td style="width: 10%;">状态</td>
                <td style="width: 5%;">线号</td>
                <td style="width: 15%;">修改状态</td>
            </tr>
            </thead>
            <tbody id="positionTable"></tbody>
        </table>
        <div id="stateModify">
        </div>
    </div>
</section>
</body>
</html>