<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<base href="/eps_server/">
    <meta charset="UTF-8">
    <title>表单</title>

    <link rel="stylesheet" href="static/css/TableCss.css">

    <script src="static/js/jquery-1.11.3.min.js"></script>
    <script src="static/js/table_js.js"></script>
</head>
<body>
	<section class="banner">
		<div class="banner-main">
			<p>实时显示表格</p>
			<span>
				请选择需要查看的线号
				<select name="" id="banner-line">
					<option value=0>301</option>
					<option value=1>302</option>
					<option value=2>303</option>
					<option value=3>304</option>
					<option value=4>305</option>
					<option value=5>306</option>
					<option value=6>307</option>
					<option value=7 selected="selected">308</option>
				</select>
			</span>
		</div>
	</section>
	<div class="box">
    	<table id="table">
        <tr id="top-left">
            <td >
                <div class="out">
                    <i>项目</i>
                    <em>时间</em>
                </div>
            </td>
            <td colspan="3" style="font-weight:900;font-size: 80px">操作员</td>
            <td colspan="2" style="font-weight:900;font-size: 80px">QC</td>
        </tr>
        <!--内容部分-->
        <!--//1-->
        <tr class="first">
            <td rowspan="4" class="current" width="100px">
                当前
                <span id="span">(17:00)</span>
            </td>
            <td id="kong" style="position: relative">
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <!--2-->
        <tr class="first">
            <td rowspan="4" class="time current" width="100px" >当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <!--3--->
        <tr class="first">
            <td rowspan="4" class="time current" width="100px">当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <!--4-->
        <tr class="first">
            <td rowspan="4" class="time current" width="100px">当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <!--5-->
        <tr class="first">
            <td rowspan="4" class="time current" width="100px">当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <!--6-->
        <tr class="first">
            <td rowspan="4" class="time current" width="100px">当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <!--7-->
        <tr class="first">
            <td rowspan="4" class="time current" width="100px">当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <!--8-->
        <tr class="first">
            <td rowspan="4" class="time current" width="100px">当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td class="suc">成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>

        <tr class="first">
            <td rowspan="4" class="time current" width="100px">当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <!--10-->
        <tr class="first">
            <td rowspan="4" class="time current" width="100px">当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <!--11-->
        <tr class="first">
            <td rowspan="4" class="time current" width="100px">当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <!--12-->
        <tr class="first">
            <td rowspan="4" class="time current" width="100px">当前</td>
            <td>
                <div class="common">
                    <i>项目</i>
                    <em>数据</em>
                </div>
            </td>
            <td>上料</td>
            <td>换料</td>
            <td>抽检</td>
            <td>全检</td>
        </tr>
        <tr class="second">
            <td>成功数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
    </table>
	</div>
</body>
</html>