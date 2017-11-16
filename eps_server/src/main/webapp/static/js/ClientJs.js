$(function(){
    //鼠标放置时增加样式
    //查询按钮事件
    var originNum = 100;  //一开始加载的个数
    var newNum = 0;
    var dataLength = 0;
    var array1 = [] ;  //存储订单号
    var array2 = []; //存储工单号
    $("#searchBtn").hover(function(){
            $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");
        })
        .on("click",function(){

            var sT = $("#startTime").val() == "" ? $("#startTime").val() : $("#startTime").val()+" 00:00:00";
            var eT = $("#endTime").val() == "" ? $("#endTime").val() : $("#endTime").val() + " 23:59:59";
            //开始和结束时间都有输入时进行判断
            if($("#startTime").val() != "" && $("#endTime").val() != ""){
                var aa = $("#startTime").val().split("-");
                var bb = $("#endTime").val().split("-");
                for(var i = 0;i<aa.length;i++){
                    aa[i] = parseInt(aa[i]);
                    bb[i] = parseInt(bb[i])
                    if(aa[i]>bb[i]){
                        alert("时间输入错误！请重新输入");
                        $("#startTime").val("");
                        $("#endTime").val("");
                    }
                    else if( i == 2){
                        $("#showWaiting").css("display","block");
                        searchAjax(sT,eT);
                    }
                }
            }
            //开始时间和终止时间都没有输入
            else if($("#startTime").val() == "" && $("#endTime").val() == ""){
                searchAjax(sT,eT);
            }
    });
    //下载按钮事件
    $("#downloadBtn").hover(function(){
            $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");
        })
        .on("click",function(){
            ExcelDownload();
        });

//    动态生成多行表格
    function  autoCreateTable(data){
        var originLength = data.length > 100 ? 100 : data.length;
            for( var i = 0 ; i < originLength ; i++){
                 CreateOneTable(i ,data)
        }
    }
//    动态生成一行表格
    function CreateOneTable(k ,data){
        var html = "";
        html += "<tr>";
        html += "<td>" + data[k].line + "</td>"
        html += "<td>" + data[k].workOrderNo + "</td>"
        html += "<td>" + data[k].orderNo + "</td>"
        html += "<td>" + data[k].lineseat + "</td>"
        html += "<td>" + data[k].materialNo + "</td>"
        html += "<td>" + data[k].materialDescription + "</td>"
        html += "<td>" + data[k].materialSpecitification + "</td>"
        html += "<td>" + data[k].operationType + "</td>"
        html += "<td>" + data[k].operator + "</td>"
        html += "<td>" + data[k].time + "</td>"
        html += "</tr>";
        $("#clientMainTable").append(html);
    }

//    上传参数并下载Excel文件
    function ExcelDownload(){

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

        form.append(input1)
            .append(input2)
            .append(input3)
            .append(input4)
            .append(input5);
        form.submit();
        form.remove();
    }

//    调用ajax函数
    function  searchAjax(sT,eT){
        $.ajax({
            url : "operation/listClientReport",
            type : "post",
            dataType : "json",
            data :{
                client :  $("#client").val(),
                programNo : $("#programNum").val(),
                line : $("#line option:selected").text() == "不限" ? null : $("#line option:selected").text() ,
                orderNo : $("#OrderNum").val(),
                workOrderNo : $("#workOrderNum").val(),
                startTime : sT,
                endTime : eT
            },
            success : function(data){
                $("#showWaiting").css("display","none");
                dataLength  = data.length ; //获取数据长度
                for(var i = 0;i<data.length;i++){
                    var $json = {};
                    $json.label = data[i].orderNo;
                    array1.push($json);

                    var $json1 = {};
                    $json1.label = data[i].workOrderNo;
                    array2.push($json1);
                }
                autoComplete("OrderNum",array1,orderCallBack);
                autoComplete("workOrderNum",array2,workOrderCallBack);
                $("#clientMainTable").empty();
                if(dataLength != 0){
                    autoCreateTable(data,dataLength);
                    $(window).on("scroll",function(){
                        newNum = originNum ;
                        if(newNum < dataLength){
                            originNum += 3;
                            originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加3后是否长度大于数据长度
                            for(var de = newNum ; de < originNum ; de++){
                                CreateOneTable(de ,data);
                            }
                        }
                    });
                }
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
                fn(selected.label);
            }
        });
    }
//    订单回调函数
    function orderCallBack(a){
        $.ajax({
                url : "operation/listClientReport",
                type : "post",
                dataType : "json",
                data :{
                    client :  $("#client").val(),
                    programNo : $("#programNum").val(),
                    line : $("#line option:selected").text() == "不限" ? null : $("#line option:selected").text() ,
                    orderNo : a,
                    workOrderNo : $("#workOrderNum").val(),
                    startTime : $("#startTime").val(),
                    endTime : $("#endTime").val()
                },
                success : function(data){
                    dataLength  = data.length ; //获取数据长度
                    $("#clientMainTable").empty();
                    if(dataLength != 0){
                        autoCreateTable(data,dataLength);
                        $(window).on("scroll",function(){
                            newNum = originNum ;
                            if(newNum < dataLength){
                                originNum += 3;
                                originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加3后是否长度大于数据长度
                                for(var de = newNum ; de < originNum ; de++){
                                    CreateOneTable(de ,data);
                                }
                            }
                        });
                    }
                },
                error : function(){
                    console.log("数据传输失败！");
                }
            });
    }

//    工单回调函数
    function workOrderCallBack(a){
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
                startTime : $("#startTime").val(),
                endTime : $("#endTime").val()
            },
            success : function(data){
                dataLength  = data.length ; //获取数据长度
                $("#clientMainTable").empty();
                if(dataLength != 0){
                    autoCreateTable(data,dataLength);
                    if(dataLength > 100){
                        $(window).on("scroll",function(){
                            newNum = originNum ;
                            if(newNum < dataLength){
                                originNum += 3;
                                originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加3后是否长度大于数据长度
                                for(var de = newNum ; de < originNum ; de++){
                                    CreateOneTable(de ,data);
                                }
                            }
                        });
                    }
                }
            },
            error : function(){
                console.log("数据传输失败！");
            }
        });
    }
});
