$(function(){
    //鼠标放置样式
    $("#pcBtn,#fileBtn").hover(function(){
        $(this).addClass("ui-state-hover")
    },function(){
        $(this).removeClass("ui-state-hover");
    });
//    查询按钮事件
    $("#pcBtn").on("click",function(){
        var standPosition = $("#stand-position").val() =="" ? null:$("#stand-position").val();     //站位的输入内容   对应fileName
        var workNum = $("#work-num").val() == "" ? null : $("#work-num").val();     //工单的输入内容  workOrder
        var state = $("#state option:selected").val();   //状态  state
        var line = $("#line-num option:selected").text() == "不限" ? null : $("#line-num option:selected").text() ;  //线号 line
        $.ajax({
            url :"program/list",
            type: "post",
            dataType: "json",
            data :{
                fileName : standPosition,
                workOrder : workNum ,
                state :state,
                line : line
            },
            success :function(data){
                console.log(data.length);
                autoCreateTable(data);
                var totalHeight = 0;
                for(var a = 0;a<10;a++){
                    totalHeight += $("#positionTable td").eq(3+4*a).height()+26 ;
                    $("#"+a).css("top",totalHeight);
                    $("#0"+a).css("top",totalHeight);
                }
            },
            error : function(){
                console.log("数据传输失败");
            }
        });
    });

//    上传文件部分
    $("#fileUpload").on("change",function(){
        var $path = $(this).val();
        var $name = $path.substr($path.lastIndexOf(".")+1); //获取后缀名
        if($name != "xls"){
            $(".show-warm").css("display","block");
            $("#fileBtn").attr("disabled",true)
                .removeClass("ui-state-default");
        }
        if($name == "xls"){
            $(".show-warm").css("display","none");
            $("#fileBtn").attr("disabled",false)
                .addClass("ui-state-default");
        }
    });
    //上传按钮事件
    $("#fileBtn").on("click",function(){
        var banMianVal = $("#banmian option:selected").val();
        var formData =  new FormData();
        formData.append('programFile',$('#fileUpload')[0].files[0]);
        formData.append('boardType',banMianVal);
        $.ajax({
            url : "program/upload",
            type : "post" ,
            cache : false,
            data : formData,
            processData : false,
            contentType : false,
            success : function (data) {
                    alert(data.result);

            },
            error :function(){
                console.log("数据传输失败！");
            }

        });
    });

//    生成表格部分
    $.ajax({
        url :"program/list",
        type: "post",
        dataType: "json",
        data :{},
        success :function(data){
            autoCreateTable(data);
            var totalHeight = 0;
            for(var a = 0 ; a < data.length ; a++){
              totalHeight += $("#positionTable td").eq(3+4*a).height()+26 ;
                $("#"+a).css("top",totalHeight);
                $("#0"+a).css("top",totalHeight);
            }
        },
        error : function(){
            console.log("数据传输失败");
        }
    });


//    动态生成表格
    function autoCreateTable(data){
        var stateChange = "";    //用于显示
        var stateChange1 = 0 ;   //用于传递到后台
        var gdh = "";          //传递工单号到后台
        var modifyUrl ="";     //用于传递链接
        var originState = "";  //用于存储原先的状态
        var originStateVal = 0;  //用于比较是否允许进行保存
        $("#positionTable").empty();
        $("#stateModify").empty();
        var dataLength = data.length;     //获取数据长度
        var html = "";
        for(var i = 0;i<dataLength;i++){
            html += "<tr>";
            html += "<td>" + data[i].fileName + "</td>"
            html += "<td>" + data[i].workOrder + "</td>"
            html += "<td>" + data[i].stateName + "</td>"
            html += "<td>" + data[i].line + "</td>"
            html += "</tr>";
            $("#positionTable").html(html);
            //增加按钮
        var btn = $("<button id='"+ i +"' class='ui-button ui-corner-all ui-state-default stateModifyCommon stateModifyBtn'>修改状态</button>");
            //判断状态是否为已作废
            if(data[i].stateName == "已作废"  || data[i].stateName == "已完成"){
                btn.attr("disabled", "disabled")
                    .removeClass("ui-state-default ui-state-hover");
            }
            //鼠标放置事件
       btn.hover(function(){
           $(this).addClass("ui-state-hover")
       },function(){
           $(this).removeClass("ui-state-hover");
       });
            //按键点击事件
            btn.on("click",function(){
                var id = $(this).attr("id");
                if ($(this).text() == "修改状态") {
                    $(this).text("保存");
                     gdh =  $("#positionTable td").eq(1+4*id).text();  //获取工单号
                    originState =  $("#positionTable td").eq(2+4*id).text();  //获取原先状态
                    switch (originState){
                        case "未开始" : originStateVal = 0;
                            break;
                        case "进行中" : originStateVal = 1;
                            break;
                        case "已完成" : originStateVal = 2;
                            break;
                    }
                    $("#0"+id).css("display","block");
                }else if($(this).text() == "保存"){
                    if(stateChange1 > originStateVal)
                    {
                        //允许修改则进行数据的传输
                        $.ajax({
                            url : modifyUrl,
                            type : "post",
                            dataType : "json",
                            data : {
                                workOrder :  gdh,
                                line : data[id].line,
                                boardType : data[id].boardType
                            },
                            success : function(data){
                                console.log(data);
                                if(data.result == "succeed"){
                                    alert("修改状态成功");
                                    $(this).text("修改状态");
                                    $("#0" + id).css("display", "none");
                                    $("#positionTable td").eq(2 + 4 * id).text(stateChange);
                                    if (stateChange == "已作废") {
                                        $(this).attr("disabled", "disabled")
                                            .removeClass("ui-state-default ui-state-hover");
                                    }
                                }
                                else if(data.result == "failed"){
                                    alert("当前处于“已完成状态”，无法作废，请重新选择。");
                                }
                            },
                            error : function(){
                                console.log("数据传输失败");
                            }
                        });

                    }
                    else{
                        alert("您选择的状态不符合，请重新选择！");
                    }
                }
            });
        //    放置下拉选项框
            var $sel = $("<select id='0"+ i +"' class='stateModifyCommon stateModifySel' style='display: none'></select>");
            $sel.append("<option value=0>未开始</option>");
            $sel.append("<option value=1>进行中</option>");
            $sel.append("<option value=2>已完成</option>");
            $sel.append("<option value=3>已作废</option>");
            //下拉事件
            $sel.on("change",function(){
                var sd = $(this).attr("id");
                
                stateChange1 = parseInt($(this).val());    //获取修改后的状态对应的value值
                stateChange =  $("#"+sd+"  option:selected").text();
                switch (stateChange1){                //传递链接
                    case 1 : modifyUrl = "program/start";
                        break;
                    case 2 : modifyUrl = "program/finish";
                        break;
                    case 3 : modifyUrl = "program/cancel";
                        break;
                }
            });
            $("#stateModify").append(btn)
                             .append($sel);

    }
    }
});