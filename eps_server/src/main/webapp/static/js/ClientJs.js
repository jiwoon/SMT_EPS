$(function(){
    //鼠标放置时增加样式
    //查询按钮事件
    var originNum = 100;  //一开始加载的个数
    var newNum = 0;
    var dataLength = 0;
    $("#searchBtn").hover(function(){
            $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");
        })
        .on("click",function(){
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
                }
            );
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
            for( var i = 0 ; i < 100 ; i++){
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
            .attr("value",$("#line option:selected").val());

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
});
