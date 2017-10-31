<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="/eps_server/">
    <meta charset="UTF-8">
    <title>管理系统</title>
    <link rel="stylesheet" href="static/css/manageSystem.css">
    <script src="static/js/jquery-1.11.3.min.js"></script>
    <script src="static/js/manageSystem.js"></script>
</head>
<body>
<div class="manageSystem">
    <!--版头部分-->
    <div class="banner" id="banner">
        <span class="name">管理系统</span>
        <span class="showUser"></span>
        <span class="logout"></span>
    </div>
    <!--主要显示框-->
    <div class="main" id="main">
        <!--左侧导航栏-->
        <div class="nav" id="nav">
            <!--  //管理导航栏 -->
            <section id="nav-manage" class="ui-accordion-header ui-state-default ui-state-active ui-corner-top">
                管理
                <span id="triangle1" class="ui-icon ui-icon-triangle-1-s"></span>
            </section>
            <!--"管理"内容-->
            <div id="manage" class="manage-text">
                <ul>
                    <li>人员管理</li>
                    <li>站位表管理</li>
                </ul>
            </div>

            <!--//报表导航栏-->
            <section id="nav-report" class="ui-accordion-header ui-state-default ui-corner-all ui-corner-top">
                报表
                <span id="triangle2" class="ui-icon ui-icon-triangle-1-e"></span>
            </section>
            <div id="report" class="manage-text   yinCan">
                <ul>
                    <li>客户报表</li>
                    <li>IPQC</li>
                    <li>仓库报表</li>
                </ul>
            </div>

        </div>
        <!--右侧主要显示框-->
        <div class="main-text" id="main-text">
            <!--员工表-->
            <section class="staff" id="staff">
                <span class="manage-staff">人员管理</span>
                <!--搜索框-->
                <section class="search" id="search">
                    <span>输入或者选择相关信息进行查询：</span>
                    工号：<input type="text" id="id">
                    姓名：<input type="text" id="name">
                    岗位：<select id="type">
                            <option value =null>不限</option>
                            <option value ="0">0、仓库操作员</option>
                            <option value ="1">1、厂线操作员</option>
                            <option value="2">2、IPQC</option>
                            <option value="3">3、管理员</option>
                          </select>
                    班别：<select id="classType">
                                 <option value =null>不限</option>
                                <option value ="0">白班</option>
                                <option value ="1">夜班</option>
                           </select>
                    在职：<select id="enabled">
                                 <option value =null>不限</option>
                                <option value ="0">是</option>
                                <option value ="1">否</option>
                           </select>
                    <button id="find" class="ui-button ui-corner-all ui-state-default locative">查询</button>
                </section>
                <!--新增按钮-->
                <section class="addNew" id="addNew">
                    点击新增
                    <button id="addBtn" class="ui-accordion-header ui-state-default ui-corner-all ui-corner-top">增加</button>
                </section>
                <!--表格显示内容-->
                <section id="mainTable" class="mainTable">
                    <table>
                        <thead>
                            <tr>
                                <td>工号</td>
                                <td>姓名</td>
                                <td>岗位</td>
                                <td>班别</td>
                                <td>在职</td>
                                <td>入职时间</td>
                            </tr>
                        </thead>
                        <tbody id="ShowTable"></tbody>
                    </table>
<!--                     <button id="modify" class="operateBtn ui-state-default ui-corner-all ui-corner-top">修改</button>
                    <button id="delete" class="operateBtn ui-state-default ui-corner-all ui-corner-top delete">删除</button> -->
                </section>
            </section>
        </div>
    </div>
</div>
</body>
</html>