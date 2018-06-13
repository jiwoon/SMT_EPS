$(function () {
    var originNum = 100;  //一开始加载的个数
    var newNum = 0;
    var array = []; //用于存储工单号


    var tempStartTime = "";
    var tempEndTime = "";
    //优先设置两天
    setInitialTime();

    //返回按钮
    $("#returnBtn").on("click", function () {
        $("#detailsTable").css("display", "none");
        $("#clientDetailsBody").empty();

        $("#main-table").css("display", "block");
    });


    //查询按钮
    $("#searchBtn").hover(function () {
        $(this).addClass("ui-state-hover")
    }, function () {
        $(this).removeClass("ui-state-hover");
    })
        .on("click", function () {
            checkTime(searchAndCreate);
        });

    //版面类型改变时其他选项置空
    $("#operationIPQC").on("change", function () {
        $("#clientName,#workOrderNum,#startTime,#endTime").val("");
        $("#line").find('option[value = ""]').attr("selected", true);
        setInitialTime();

    });
    //下载按钮
    $("#loadBtn").hover(function () {
        $(this).addClass("ui-state-hover")
    }, function () {
        $(this).removeClass("ui-state-hover");
    })
        .on("click", function () {
            checkTime(ExcelDownload1);
        });

    //查询时输出传输
    function searchAndCreate(a, b) {
        $("#showWaiting").css("display", "block");
        $.ajax({
            url: "operation/listOperationReportSummary",
            type: "post",
            dataType: "json",
            data: {
                type: $("#operationIPQC option:selected").val(),
                //client: $("#clientName").val(),
                line: $("#line option:selected").text() == "不限" ? "" : $("#line option:selected").text(),
                workOrderNo: $("#workOrderNum").val(),
                startTime: a,
                endTime: b
            },
            success: function (data) {
                $("#showWaiting").css("display", "none");
                var oprTypeText = $("#operationIPQC option:selected").text();
                var oprTypeVal = $("#operationIPQC option:selected").val();
                if (data.result) {
                    alert("您没有权限");
                    window.location.href = "/eps_server/user/goLogin";
                } else {
                    var dataLength = data.length; //获取数据长度
                    array = [];
                    for (var i = 0; i < data.length; i++) {
                        var $json = {};
                        $json.label = data[i].workOrderNo;
                        array.push($json);
                    }
                    $("#clientMainTable").empty();
                    autoCreateTable(data, oprTypeText, oprTypeVal);
                    autoComplete($("#workOrderNum").val(), array, searchCallBack);
                    // $(window).on("scroll",function(){
                    //     if(dataLength > 100){
                    //         newNum = originNum ;
                    //         if(newNum < dataLength){
                    //             originNum += 3;
                    //             originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加3后是否长度大于数据长度
                    //             for(var de = newNum ; de < originNum ; de++){
                    //                 CreateOneTable(de ,data);
                    //             }
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
                                        CreateOneTable(de, data, oprTypeText);
                                        bindEvent(de, data, oprTypeText, oprTypeVal);
                                    }
                                }
                            }
                        }));
                    }
                }
            },
            error: function () {
                console.log("数据传输失败");
            }
        });
    }

    //绑定事件
    function bindEvent(index, data, typeText, typeVal) {
        $("#detail-" + index + "").unbind().on("click", function () {
            $("#showWaiting").css("display", "block");
            $.ajax({
                url: "operation/listOperationReport",
                type: "post",
                dataType: "json",
                data: {
                    line: data[index].line,
                    workOrderNo: data[index].workOrderNo,
                    operator: data[index].operator,
                    type: typeVal,
                    startTime: tempStartTime,
                    endTime: tempEndTime
                },
                success: function (data) {
                    $("#showWaiting").css("display", "none");
                    $("#detailsTable").css("display", "block");
                    $("#main-table").css("display", "none");

                    $(".details-table-header p").eq(0).text("线号：" + data[0].line);
                    $(".details-table-header p").eq(1).text("工单号：" + data[0].workOrderNo);
                    $(".details-table-header p").eq(2).text("操作员：" + data[0].operator);
                    $(".details-table-header p").eq(3).text("操作类型：" + typeText);


                    for (var i = 0; i < data.length; i++) {
                        createDetailsTable(data, i)
                    }

                },
                error: function () {
                    alert("数据传输失败, 请尝试刷新页面");
                }
            })

        })
    }

//详情表格创建
    function createDetailsTable(data, index) {
        var html = ""
        html += "<tr>"
        html += "<td>" + data[index].lineseat + "</td>"
        html += "<td>" + data[index].materialNo + "</td>"
        html += "<td>" + data[index].materialDescription + "</td>"
        html += "<td>" + data[index].materialSpecitification + "</td>"
        html += "<td>" + data[index].result + "</td>"
        html += "<td>" + data[index].time + "</td>"
        html += "</tr>"

        $("#clientDetailsTable").append(html);
    }

//    动态生成一行表格
    function CreateOneTable(k, data, type) {
        var html = "";
        html += "<tr>";
        html += "<td>" + data[k].line + "</td>"
        html += "<td>" + data[k].workOrderNo + "</td>"
        // html += "<td>" + data[k].orderNo + "</td>"
        // html += "<td>" + data[k].lineseat + "</td>"
        // html += "<td>" + data[k].materialNo + "</td>"
        // html += "<td>" + data[k].materialDescription + "</td>"
        // html += "<td>" + data[k].materialSpecitification + "</td>
        html += "<td>" + data[k].operator + "</td>"
        html += "<td>" + type + "</td>"
        // html += "<td>" + data[k].time + "</td>"
        html += "<td> <span style='color: green'>成功次数：" + data[k].passCount + "  </span><span style='color: red'>失败次数：" + data[k].failCount + "  </span></td>"
        html += "<td>总数：" + eval(data[k].passCount + data[k].failCount) + "</td>"
        html += "<td><button class='ui-button ui-corner-all ui-state-default stateModifyCommon stateModifyBtn' id='detail-" + k + "'>详情</button></td>"
        html += "</tr>";
        $("#clientMainTable").append(html);


    }

    //    动态生成多行表格
    function autoCreateTable(data, typeText, typeVal) {
        var originLength = (data.length > 100 ? 100 : data.length);
        for (var i = 0; i < originLength; i++) {
            CreateOneTable(i, data, typeText);
            bindEvent(i, data, typeText, typeVal)

        }
    }

    //    上传参数并下载Excel文件
    function ExcelDownload1(sT, eT) {
        var url = "operation/downloadOperationReport";
        var form1 = $("<form>");
        $(document.body).append(form1);
        form1.attr("style", "display : none")
            .attr("target", "")
            .attr("method", "post")
            .attr("action", url);
        var input1 = $("<input>");
        input1.attr("style", "display : none")           //类型
            .attr("name", "type")
            .attr("value", $("#operationIPQC option:selected").val());

        var input2 = $("<input>");   //客户名
        input2.attr("style", "display : none")
            .attr("name", "client")
            .attr("value", $("#clientName").val());

        var input3 = $("<input>");   //线别
        input3.attr("style", "display : none")
            .attr("name", "line")
            .attr("value", $("#line option:selected").text() == "不限" ? "" : $("#line option:selected").text());

        var input4 = $("<input>");    //工单
        input4.attr("style", "display :none")
            .attr("name", "workOrderNo")
            .attr("value", $("#workOrderNum").val());

        var input5 = $("<input>");    //开始时间
        input5.attr("style", "display :none")
            .attr("name", "startTime")
            .attr("value", sT);

        var input6 = $("<input>");    //结束时间
        input6.attr("style", "display :none")
            .attr("name", "endTime")
            .attr("value", eT);

        form1.append(input1);
        form1.append(input2);
        form1.append(input3);
        form1.append(input4);
        form1.append(input5)
        form1.append(input6)

        form1.submit();
        form1.remove();
    }

    //自动补全功能
    function autoComplete(id, array, fn) {
        $("#" + id).autocompleter({
            highlightMatches: true,
            source: array,
            template: '{{ label }}',
            empty: false,
            limit: 5,
            callback: function (index, value, selected) {
                fn(selected.label);
            }
        });
    }

//    回调函数
    function searchCallBack(a) {
        $(window).unbind();
        var stime = $("#startTime").val() == "" ? $("#startTime").val() : $("#startTime").val() + " 00:00:00";
        var etime = $("#endTime").val() == "" ? $("#endTime").val() : $("#endTime").val() + " 23:59:59";
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
                $("#showWaiting").css("display", "block");
                ajaxCreate(a, stime, etime);
            }
        }
        //开始时间和终止时间都没有输入
        else if ($("#startTime").val() == "" && $("#endTime").val() == "") {
            $("#showWaiting").css("display", "block");
            ajaxCreate(a, stime, etime);
        }


    }

    //ajax 传输
    function ajaxCreate(a, st, et) {
        $.ajax({
            url: "operation/listOperationReportSummary",
            type: "post",
            dataType: "json",
            data: {
                type: $("#operationIPQC option:selected").val(),
                // client: $("#clientName").val(),
                line: $("#line option:selected").val(),
                workOrderNo: a,
                startTime: st,
                endTime: et
            },
            success: function (data) {
                $("#showWaiting").css("display", "none");
                var dataLength = data.length; //获取数据长度
                var oprTypeText = $("#operationIPQC option:selected").text();
                var oprTypeVal = $("#operationIPQC option:selected").val();
                $("#clientMainTable").empty();
                autoCreateTable(data, oprTypeText, oprTypeVal);
                // $(window).on("scroll",function(){
                //     if(dataLength > 100){
                //         newNum = originNum ;
                //         if(newNum < dataLength){
                //             originNum += 3;
                //             originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加3后是否长度大于数据长度
                //             for(var de = newNum ; de < originNum ; de++){
                //                 CreateOneTable(de ,data);
                //             }
                //         }
                //     }
                // });
                // 超过100个数据时边滚动边加载
                originNum = 100;
                newNum = 0;
                if (data.length > 100) {
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
                                    CreateOneTable(de, data, oprTypeText);
                                    bindEvent(de, data, oprTypeText, oprTypeVal)
                                }
                            }
                        }
                    }));
                }
            },
            error: function () {
                console.log("数据传输失败");
            }
        });
    }

//    时间检查函数
    function checkTime(fn) {
        tempStartTime = tempEndTime = "";
        console.log($("#startTime").val())
        var stime = tempStartTime = $("#startTime").val() == "" ? $("#startTime").val() : $("#startTime").val() + " 00:00:00";
        var etime = tempEndTime = $("#endTime").val() == "" ? $("#endTime").val() : $("#endTime").val() + " 23:59:59";
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
                fn(stime, etime);
            }
        }
        //开始时间和终止时间都没有输入
        else if ($("#startTime").val() == "" && $("#endTime").val() == "") {
            fn(stime, etime);
        }
    }

//    调用时间函数
    function setInitialTime() {
        var now = new Date();    //当前获得毫秒

        var nowYear = now.getFullYear();   ////得到年份
        var nowMonth = now.getMonth() + 1; // //得到月份
        nowMonth = nowMonth < 10 ? "0" + nowMonth : nowMonth;
        var nowDay = now.getDate();  // //得到日期
        nowDay = nowDay < 10 ? "0" + nowDay : nowDay;
        var today = nowYear + "-" + nowMonth + "-" + nowDay;   //  今天的年月份
        $("#endTime").val(today);

        var yesterdayMillion = now.getTime() - 1000 * 60 * 60 * 24;   //得到昨天的时间
        var yesterday = new Date(yesterdayMillion);
        var yesYear = yesterday.getFullYear();     //得到年份
        var yesMonth = yesterday.getMonth() + 1;  //得到月份
        yesMonth = yesMonth < 10 ? "0" + yesMonth : yesMonth;
        var yesDay = yesterday.getDate();      //得到日期
        yesDay = yesDay < 10 ? "0" + yesDay : yesDay;
        var preDay = yesYear + "-" + yesMonth + "-" + yesDay; //  昨天的年月份
        $("#startTime").val(preDay);
    }
});

function debounce(func, wait, immediate) {
    var timeout;
    return function () {
        var context = this, args = arguments;
        var later = function () {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        var callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
}