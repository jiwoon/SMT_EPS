<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<base href="/eps_server/">
    <meta charset="UTF-8">
    <title>配置页面</title>
    <link rel="stylesheet" href="static/css/configuration.css">
    <script src="static/js/jquery-3.1.1.min.js"></script>
    <script src="static/js/configuration.js"></script>
</head>
<body>
    <section class="mainBox">
        <!--版头部分-->
        <div class="banner" id="banner">
            <i>配置页面</i>
            <div class="choice">
                <span>产线设置筛选：</span>
                <select id="lineChoice">
                    <option value=100>统一</option>
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
            <button id="searchBtn" class="searchBtn ui-state-default ui-corner-all ui-corner-top">产线查询</button>
            <button id="saveBtn" class="saveBtn ui-state-default ui-corner-all ui-corner-top">保存</button>
        </div>
        <!--主表格-->
        <div id="mainTable" class="mainTable">
            <table>
                <thead>
                    <tr>
                        <td>线号</td>
                        <td>别名</td>
                        <td>值</td>
                        <td style="position: relative; display: none">报警状态<br /><i style="font-size:12px ;">(打勾为开启)</i></td>
                        <td>描述</td>
                    </tr>
                </thead>
                <tbody id="main" class="mainBody"></tbody>
            </table> 
        </div>
    </section>
</body>
</html>