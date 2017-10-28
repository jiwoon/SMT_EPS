<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<base href="/eps_server/">
    <meta charset="UTF-8">
    <title>表单</title>
    <link rel="stylesheet" href="static/css/tableCss.css">
    <script src="static/js/jquery-1.11.3.min.js"></script>
    <script src="static/js/table_js.js"></script>
</head>
<body>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
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
            <td>3</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
        </tr>
        <tr class="third">
            <td>失败数</td>
            <td>6</td>
            <td>7</td>
            <td>7</td>
            <td>7</td>
        </tr>
        <tr class="four">
            <td>总数</td>
            <td>9</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
        </tr>
    </table>
</div>
</body>
</html>