$(function(){
    //鼠标放置时增加样式
    //查询按钮事件
	var currentPage=1;  //当前页码，默认为第一页
	var totalPage=0;  //当前总页数
    var originNum = 100;  //一开始加载的个数
    var newNum = 0;
    var dataLength = 0;
    var array2 = []; //存储工单号
    $("#searchBtn").hover(function(){
            $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");
        })
        .on("click",function(){
        	currentPage = 1;
            timeCheck(searchAjax);
    });
    //下一页按钮事件
    $('.down').on("click",function(){
    	if(currentPage == totalPage){
    		alert("这是最后一页");
    		return false;
    	}else{
    		currentPage++;
    	}
    	goTop();
    	timeCheck(searchAjax);
    })
    //上一页按钮事件
    $('.up').on("click",function(){
    	if(currentPage == 1){
    		alert("当前是第一页");
    	}else{
    		currentPage--;
    	}
    	goTop();
    	timeCheck(searchAjax);
    })
    //跳转按钮事件
        $('.jump').on("click",function(){
    	var jumpPage = $('.jumpPage').val();
    	if(jumpPage==""||jumpPage==null){
    		alert("请输入要跳转的页面");
    	}else if(!isNumber($('.jumpPage'))){
    		alert("跳转页面必须要正整数");
    	}else if(jumpPage>totalPage){
    		alert("跳转页码不能超过总页码");
    	}
    	else{
    		currentPage = jumpPage;
        	goTop();
        	timeCheck(searchAjax);
    	}
    	$('.jumpPage').val("");
    })
    //默认先下载两天
   setInitialTime();
    //下载按钮事件
    $("#downloadBtn").hover(function(){
            $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");
        })
        .on("click",function(){
            timeCheck(ExcelDownload);
        });

//    动态生成多行表格
    function  autoCreateTable(data){
        var originLength = data.list.length > 100 ? 100 : data.list.length;
            for( var i = 0 ; i < originLength ; i++){
                 CreateOneTable(i ,data)
        }
    }
//    动态生成一行表格
    function CreateOneTable(k ,data){
        var html = "";
        var list = data.list;
        html += "<tr>";
        html += "<td>" + list[k].line + "</td>"
        html += "<td>" + list[k].workOrderNo + "</td>"
        html += "<td>" + list[k].lineseat + "</td>"
        html += "<td>" + list[k].materialNo + "</td>"
        html += "<td>" + list[k].materialDescription + "</td>"
        html += "<td>" + list[k].materialSpecitification + "</td>"
        html += "<td>" + list[k].operationType + "</td>"
        html += "<td>" + list[k].operator + "</td>"
        html += "<td>" + list[k].time + "</td>"
        html += "</tr>";
        $("#clientMainTable").append(html);
    }

//    上传参数并下载Excel文件
    function ExcelDownload(sT ,eT){
        var lineVal = $("#line option:selected").text() == "不限" ? null : $("#line option:selected").text();
        var form   = $("<form>");
        $(document.body).append(form);
        var url = "operation/downloadClientReport";
        form.attr("style","display : none").attr("target","").attr("method","post").attr("action",url).attr("enctype","multipart/form-data");

        var input1 = $("<input>");     //存储客户名
        input1.attr("type","hidden")
              .attr("name","client")
              .attr("value",$("#client").val());

        var input2 = $("<input>");     //存储程序表名
             input2.attr("type","hidden")
                   .attr("name","programNo")
                   .attr("value",$("#programNum").val());

        var input3 = $("<input>");     //存储线别
        input3.attr("type","hidden")
            .attr("name","line")
            .attr("value",lineVal);

        var input4 = $("<input>");     //存储订单号
        input4.attr("type","hidden")
            .attr("name","orderNo")
            .attr("value",$("#OrderNum").val());

        var input5 = $("<input>");     //存储订单号
        input5.attr("type","hidden")
            .attr("name","workOrderNo")
            .attr("value",$("#workOrderNum").val());

        var input6 = $("<input>");     //存储开始时间
            input6.attr("type","hidden")
                .attr("name","startTime")
                .attr("value",sT);

        var input7 = $("<input>");     //存储结束时间
        input7.attr("type","hidden")
            .attr("name","endTime")
            .attr("value",eT);
        form.append(input1)
            .append(input2)
            .append(input3)
            .append(input4)
            .append(input5)
            .append(input6)
            .append(input7);
        form.submit();
        form.remove();
    }

    //调用ajax函数
    function  searchAjax(sT,eT){
        $("#showWaiting").css("display","block");
        $.ajax({
            url : "operation/listClientReport",
            type : "post",
            data :{
                client :  $("#client").val(),
                programNo : $("#programNum").val(),
                line : $("#line option:selected").text() == "不限" ? null : $("#line option:selected").text() ,
                orderNo : $("#OrderNum").val(),
                workOrderNo : $("#workOrderNum").val(),
                startTime : sT,
                endTime : eT,
                currentPage:currentPage
            },
            success : function(data){
            	currentPage = data.page.currentPage;
            	totalPage = data.page.totallyPage;
                $("#showWaiting").css("display","none");
                $('.pageNow').text(data.page.currentPage);
                $('.pageAll').text(data.page.totallyPage);
                if(data.result){
                    alert("您没有权限！");
                    window.location.href = "/eps_server/user/goLogin";

                }else{
                    dataLength  = data.list.length ; //获取数据长度
                	var list = data.list;
                    for(var i = 0;i<data.list.length;i++){
                        var $json1 = {};
                        $json1.label =list[i].workOrderNo;
                        array2.push($json1);
                    }
                    autoComplete("workOrderNum",array2,workOrderCallBack);
                    $("#clientMainTable").empty();
                    if(dataLength != 0){
                        autoCreateTable(data,dataLength);

                        // $(window).on("scroll",function(){
                        //     newNum = originNum ;
                        //     if(newNum < dataLength){
                        //         originNum += 3;
                        //         originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加3后是否长度大于数据长度
                        //         for(var de = newNum ; de < originNum ; de++){
                        //             CreateOneTable(de ,data);
                        //         }
                        //     }
                        // });
                        //超过100个数据时边滚动边加载

                        originNum = 100;
                        newNum = 0;
                        if (dataLength > 100) {
                            $(window).on("scroll", debounce(function () {
                                var scrollTop = $(this).scrollTop();
                                var scrollHeight = $(document).height();
                                var windowHeight = $(this).height();
                                if (scrollTop + windowHeight >= scrollHeight - 20) {
                                    newNum = originNum;
                                    if (newNum < dataLength) {
                                        originNum += 100;
                                        originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加100后是否长度大于数据长度
                                        for (var de = newNum; de < originNum; de++) {
                                            CreateOneTable(de, data);
                                        }
                                    }
                                }
                            }));
                        }
                    }
                }
                $('#pagination').css("display","block");
               
            },
            error : function(){
                console.log("数据传输失败！");
            }
        });
    }

//    自动补全函数
    function autoComplete(id,array,fn){
        $("#"+id).autocompleter({
            highlightMatches : true,
            source : array,
            template : '{{ label }}',
            empty : false,
            limit : 5 ,
            callback : function( index ,value,selected){
            	currentPage=1;
                fn(selected.label);
            }
        });
    }


    //工单回调函数
    function workOrderCallBack(a){
        var sT = $("#startTime").val() == "" ? $("#startTime").val() : $("#startTime").val()+" 00:00:00";
        var eT = $("#endTime").val() == "" ? $("#endTime").val() : $("#endTime").val() + " 23:59:59";
        $("#showWaiting").css("display","block");
        $.ajax({
            url : "operation/listClientReport",
            type : "post",
            dataType : "json",
            data :{
                client :  $("#client").val(),
                programNo : $("#programNum").val(),
                line : $("#line option:selected").text() == "不限" ? null : $("#line option:selected").text() ,
                orderNo : $("#OrderNum").val(),
                workOrderNo : a,
                startTime : sT,
                endTime :eT,
                currentPage:currentPage
            },
            success : function(data){
            	currentPage = data.page.currentPage;
            	totalPage = data.page.totallyPage;
            	$("#showWaiting").css("display","none");
                $('.pageNow').text(data.page.currentPage);
                $('.pageAll').text(data.page.totallyPage);
                dataLength  = data.list.length ; //获取数据长度
                $("#clientMainTable").empty();
                if(dataLength !== 0){
                    autoCreateTable(data,dataLength);
                    // if(dataLength > 100){
                    //     $(window).on("scroll",function(){
                    //         newNum = originNum ;
                    //         if(newNum < dataLength){
                    //             originNum += 3;
                    //             originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加3后是否长度大于数据长度
                    //             for(var de = newNum ; de < originNum ; de++){
                    //                 CreateOneTable(de ,data);
                    //             }
                    //         }
                    //     });
                    // }
                    // 超过100个数据时边滚动边加载
                    originNum = 100;
                    newNum = 0;
                    if (data.list.length > 100) {
                        $(window).on("scroll", debounce(function () {
                            var scrollTop = $(this).scrollTop();
                            var scrollHeight = $(document).height();
                            var windowHeight = $(this).height();
                            if (scrollTop + windowHeight >= scrollHeight - 20) {
                                newNum = originNum;
                                if (newNum < dataLength) {
                                    originNum += 100;
                                    originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加100后是否长度大于数据长度
                                    for (var de = newNum; de < originNum; de++) {
                                        CreateOneTable(de, data);
                                    }
                                }
                            }
                        }));
                    }
                }
                $('#pagination').css("display","block");
            },
            error : function(){
                console.log("数据传输失败！");
            }
        });
    }

    //时间判断函数
    function timeCheck(fn){
        var sT = $("#startTime").val() == "" ? $("#startTime").val() : $("#startTime").val()+" 00:00:00";
        var eT = $("#endTime").val() == "" ? $("#endTime").val() : $("#endTime").val() + " 23:59:59";
        //开始和结束时间都有输入时进行判断
        if ($("#startTime").val() != "" && $("#endTime").val() != "") {
            var aa = $("#startTime").val().split("-");
            var bb = $("#endTime").val().split("-");
            var fromDate = "", toDate = "";
            for (var i = 0; i < aa.length; i++) {
                fromDate += aa[i];
                toDate += bb[i];
            }
            fromDate = parseInt(fromDate);
            toDate = parseInt(toDate);
            if (fromDate > toDate) {
                alert("时间输入错误！请重新输入");
                $("#startTime").val("");
                $("#endTime").val("");
                return
            }
            else {
                fn(sT, eT);
            }
        }
        //开始时间和终止时间都没有输入
        else if($("#startTime").val() == "" && $("#endTime").val() == ""){
            fn(sT,eT);
        }
    }

//调用时间函数
function setInitialTime(){
    var now = new Date();    //当前获得毫秒

    var nowYear = now.getFullYear();   ////得到年份
    var nowMonth = now.getMonth() + 1 ; // //得到月份
    nowMonth = nowMonth< 10 ? "0"+nowMonth : nowMonth ;
    var nowDay   = now.getDate();  // //得到日期
    nowDay = nowDay < 10 ? "0"+nowDay : nowDay ;
    var today = nowYear + "-" + nowMonth + "-" + nowDay ;   //  今天的年月份
    $("#endTime").val(today);

    var yesterdayMillion = now.getTime() - 1000*60*60*24 ;   //得到昨天的时间
    var yesterday = new Date(yesterdayMillion);
    var yesYear = yesterday.getFullYear();     //得到年份
    var yesMonth = yesterday.getMonth() + 1 ;  //得到月份
    yesMonth = yesMonth< 10 ? "0"+yesMonth : yesMonth ;
    var yesDay = yesterday.getDate();      //得到日期
    yesDay = yesDay < 10 ? "0"+yesDay : yesDay ;
    var preDay = yesYear + "-" + yesMonth + "-" + yesDay; //  昨天的年月份
    $("#startTime").val(preDay);
}
});
//判断是否为正整数
function isNumber(num){
	var val = num.val();
	var regu = /^[1-9]\d*$/;
	if(val!=""){
    	if(regu.test(val)){
    	    return true;
    	} else{
    	    return false;
    	}
	}
}
//跳转到顶端
function goTop() {
$('html, body').animate({scrollTop:0},0); 
}


