$(function(){
    var originNum = 100;  //一开始加载的个数
    var newNum = 0;
    var array = [] ; //用于存储工单号
    //查询按钮
    $("#searchBtn").hover(function(){ $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");})
        .on("click",function(){
            var stime = $("#startTime").val() == "" ? $("#startTime").val() : $("#startTime").val()+ " 00:00:00";
            var etime = $("#endTime").val() == "" ? $("#endTime").val() : $("#endTime").val() + " 23:59:59";
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
                        $("#showWaiting").css("display" , "block");
                        searchAndCreate(stime,etime);
                    }
                }
            }
            //开始时间和终止时间都没有输入
            else if($("#startTime").val() == "" && $("#endTime").val() == ""){
                $("#showWaiting").css("display" , "block");
                searchAndCreate(stime,etime);
            }
        });

    //版面类型改变时其他选项置空
    $("#operationIPQC").on("change",function(){
        $("#clientName,#workOrderNum,#startTime,#endTime").val("");
        $("#line").find('option[value = ""]').attr("selected",true);
    });
    //下载按钮
    $("#loadBtn").hover(function(){ $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");})
        .on("click",function(){
            ExcelDownload1();
        });
    //查询时输出传输
    function searchAndCreate(a,b){
        $.ajax({
            url : "operation/listOperationReport",
            type : "post",
            dataType : "json",
            data : {
                type : $("#operationIPQC option:selected").val(),
                client :  $("#clientName").val(),
                line : $("#line option:selected").val(),
                workOrderNo : $("#workOrderNum").val(),
                startTime : a,
                endTime : b
            },
            success : function(data){
                $("#showWaiting").css("display" , "none");
                var dataLength  = data.length ; //获取数据长度
                array=[];
                for(var i = 0;i<data.length;i++){
                    var $json = {};
                    $json.label = data[i].workOrderNo;
                    array.push($json);
                }
                $("#clientMainTable").empty();
                autoCreateTable(data);
                autoComplete("workOrderNum",array,searchCallBack);
                    $(window).on("scroll",function(){
                        if(dataLength > 100){
                            newNum = originNum ;
                            if(newNum < dataLength){
                                originNum += 3;
                                originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加3后是否长度大于数据长度
                                for(var de = newNum ; de < originNum ; de++){
                                    CreateOneTable(de ,data);
                                }
                            }
                        }
                    });
            },
            error : function(){
                console.log("数据传输失败");
            }
        });
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

    //    动态生成多行表格
    function  autoCreateTable(data){
        var originLength  = (data.length > 100 ? 100 : data.length) ;
        for( var i = 0 ; i < originLength ; i++){
            CreateOneTable(i ,data);
        }
    }

    //    上传参数并下载Excel文件
    function ExcelDownload1(){
        var url = "operation/downloadOperationReport";
        var form1 = $("<form>");
        $(document.body).append(form1);
        form1.attr("style","display : none")
             .attr("target","")
             .attr("method","post")
            .attr("action",url);
        var input1 = $("<input>");
        input1.attr("style","display : none")           //类型
              .attr("name","type")
              .attr("value" ,$("#operationIPQC option:selected").val());

        var input2 = $("<input>");   //客户名
        input2.attr("style","display : none")
            .attr("name","client")
              .attr("value",$("#clientName").val());

        var input3 = $("<input>");   //线别
        input3.attr("style","display : none")
            .attr("name","line")
            .attr("value",$("#line option:selected").val());

        var input4 = $("<input>");
        input4.attr("style","display :none")
            .attr("name","workOrderNo")
              .attr("value" ,$("#workOrderNum").val());
        form1.append(input1);
        form1.append(input2);
        form1.append(input3);
        form1.append(input4);

        form1.submit();
        form1.remove();
    }

    //自动补全功能
    function autoComplete(id,array,fn){
        $("#"+id).autocompleter({
            highlightMatches : true,
            source : array,
            template : '{{ label }}',
            empty : false,
            limit : 5,
            callback : function(index ,value , selected){
                fn(selected.label);
            }
        });
    }

//    回调函数
    function searchCallBack (a){
        $(window).unbind();
        var stime = $("#startTime").val() == "" ? $("#startTime").val() : $("#startTime").val()+ " 00:00:00";
        var etime = $("#endTime").val() == "" ? $("#endTime").val() : $("#endTime").val() + " 23:59:59";
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
                    $("#showWaiting").css("display" , "block");
                    ajaxCreate(a,stime,etime);
                }
            }
        }
        //开始时间和终止时间都没有输入
        else if($("#startTime").val() == "" && $("#endTime").val() == ""){
            $("#showWaiting").css("display" , "block");
            ajaxCreate(a,stime,etime);
        }


    }
    //ajax 传输
    function ajaxCreate(a , st , et){
        $.ajax({
            url : "operation/listOperationReport",
            type : "post",
            dataType : "json",
            data : {
                type : $("#operationIPQC option:selected").val(),
                client :  $("#clientName").val(),
                line : $("#line option:selected").val(),
                workOrderNo : a,
                startTime :st,
                endTime : et
            },
            success : function(data){
                $("#showWaiting").css("display" , "none");
                var dataLength  = data.length ; //获取数据长度
                $("#clientMainTable").empty();
                autoCreateTable(data);
                $(window).on("scroll",function(){
                    if(dataLength > 100){
                        newNum = originNum ;
                        if(newNum < dataLength){
                            originNum += 3;
                            originNum = (originNum >= dataLength ? dataLength : originNum);  //判断加3后是否长度大于数据长度
                            for(var de = newNum ; de < originNum ; de++){
                                CreateOneTable(de ,data);
                            }
                        }
                    }
                });

            },
            error : function(){
                console.log("数据传输失败");
            }
        });
    }
});