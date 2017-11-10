$(function(){
    var originNum = 100;  //一开始加载的个数
    var newNum = 0;
    //查询按钮
    $("#searchBtn").hover(function(){ $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");})
        .on("click",function(){
            searchAndCreate();
        });
    //下载按钮
    $("#loadBtn").hover(function(){ $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");})
        .on("click",function(){
            //ExcelDownload();
            console.log(111);
        });
    //查询时输出传输
    function searchAndCreate(){
        $.ajax({
            url : "operation/listOperationReport",
            type : "post",
            dataType : "json",
            data : {
                type : $("#operationIPQC option:selected").val(),
                client :  $("#clientName").val(),
                line : $("#line option:selected").val(),
                workOrderNo : $("#workOrderNum").val(),
                startTime : $("#startTime").val(),
                endTime : $("#endTime").val()
            },
            success : function(data){
                var dataLength  = data.length ; //获取数据长度
                autoCreateTable(data);
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
        $("#clientMainTable").empty();
        for( var i = 0 ; i < 100 ; i++){
            CreateOneTable(i ,data)
        }
    }

    //    上传参数并下载Excel文件
    function ExcelDownload(){
        var form   = $("<form>");
        $(document.body).append(form);
        var url = "operation/downloadOperationReport";
        form.attr("style","display : none").attr("target","").attr("method","post").attr("action",url).attr("enctype","multipart/form-data");

        var input1 = $("<input>");     //存储客户名
        input1.attr("type","hidden")
            .attr("value",$("#clientName").val());

        var input2 = $("<input>");     //存储类型
        input2.attr("type","hidden")
            .attr("value",$("#operationIPQC option:selected").val());

        var input3 = $("<input>");     //存储线别
        input3.attr("type","hidden")
            .attr("value",$("#line option:selected").val());

        var input4 = $("<input>");     //存储工单号
        input4.attr("type","hidden")
            .attr("value",$("#workOrderNum").val());

        var input5 = $("<input>");     //存储开始时间
        input5.attr("type","hidden")
            .attr("value",$("#startTime").val());

        var input6 = $("<input>");   //存储截止时间
        input5.attr("type","hidden")
            .attr("value",$("#endTime").val());
        form.append(input1)
            .append(input2)
            .append(input3)
            .append(input4)
            .append(input5)
            .append(input6);
        form.submit();
        form.remove();
    }
});