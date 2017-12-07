$(function(){
    var originNum = 100;  //一开始加载的个数
    var newNum = 0;
    //鼠标放置样式
    $("#pcBtn,#fileBtn").hover(function(){
        $(this).addClass("ui-state-hover")
    },function(){
        $(this).removeClass("ui-state-hover");
    });

    var array1 = [];  //放置站位表名
    var array2 = []; //放置工单号
//    查询按钮事件
    $("#pcBtn").on("click",function(){
        $(window).unbind();
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
                line : line,
                ordBy : "state"
            },
            success :function(data){
                autoCreateTable(data);
                bindEvent(data);
                //超过100个数据时边滚动边加载
                if(data.length > 100){
                    $(window).on("scroll",function(){
                        newNum = originNum ;
                        if(newNum < data.length){
                            originNum += 3;
                            originNum = (originNum >= data.length ? data.length : originNum);  //判断加3后是否长度大于数据长度
                            for(var de = newNum ; de < originNum ; de++){
                                CreateOneTable(de ,data);
                            }
                        }
                    });
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
        data :{
            ordBy : "state"
        },
        success :function(data){
            autoCreateTable(data);
            bindEvent(data);
            completeAuto("stand-position",array1,fileCallBack);
            completeAuto("work-num",array2,workOrderCallBack);
            if(data.length > 100){
                $(window).on("scroll",function(){
                    newNum = originNum ;
                    if(newNum < data.length){
                        originNum += 3;
                        originNum = (originNum >= data.length ? data.length : originNum);  //判断加3后是否长度大于数据长度
                        for(var de = newNum ; de < originNum ; de++){
                            CreateOneTable(de ,data);
                        }
                    }
                });
            }
        },
        error : function(){
            console.log("数据传输失败");
        }
    });

//    动态生成表格
    function autoCreateTable(data){
        $("#positionTable").empty();
        $("#stateModify").empty();
        var dataLength = data.length;     //获取数据长度
        for(var i = 0;i<dataLength;i++){
            var $json = {};
            $json.label = data[i].fileName;
            array1.push($json);

            var $json1 = {};
            $json1.label = data[i].workOrder;
            array2.push($json1);
        }
        createOneTr(data);
    }

    //生成表格
    function  createOneTr (data){
        var originLength = data.length > 100 ? 100 : data.length;  //判断数据个数是否大于100，大于100个先加载100个
        for(var q = 0; q < originLength ; q++){
            var html = "";
            html += "<tr>";
            html += "<td>" + data[q].fileName + "</td>"
            html += "<td>" + data[q].workOrder + "</td>"
            html += "<td>" + data[q].stateName + "</td>"
            html += "<td>" + data[q].line + "</td>"
            html += "<td>" + "<div class='commonDiv'><button id='" + q + "' class='ui-button ui-corner-all ui-state-default stateModifyCommon stateModifyBtn'>修改状态</button>" +
                "<select id='0" + q + "' class='stateModifyCommon stateModifySel'style='display: none'><option value=0>未开始</option><option value=1>进行中</option><option value=2>已完成</option> <option value=3>已作废</option></select>"+
                "<button id='00" + q + "' class='ui-button ui-corner-all ui-state-default stateModifyCommon stateModifyBtn' style='display: none'>取消</button></div>"+"</td>"
            html += "</tr>";
            $("#positionTable").append(html);
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

    //            绑定事件
    function bindEvent(data){
        var stateChange = "";    //用于显示
        var stateChange1 = 0 ;   //用于传递到后台
        var gdh = "";          //传递工单号到后台
        var modifyUrl ="";     //用于传递链接
        var originState = "";  //用于存储原先的状态
        var originStateVal = 0;  //用于比较是否允许进行保存
        var $id = "";
        for( var i = 0 ; i < $(".commonDiv") .length; i++) {
            //修改按钮事件
            $("#" + i).hover(function () {
                    $(this).addClass("ui-state-hover")
                }, function () {
                    $(this).removeClass("ui-state-hover");
                })
                .on("click", function () {
                     $id = $(this).attr("id");
                    var $numId = parseInt($id);
                    dataId = data[$numId].id;
                    if ($(this).text() == "修改状态") {
                        $(this).text("保存");
                        gdh = $("#positionTable td").eq(1 + 5 * $id).text();  //获取工单号
                        originState = $("#positionTable td").eq(2 + 5 * $id).text();  //获取原先状态

                        //将状态转换为可以作为对比条件的数字
                        switch (originState) {
                            case "未开始" :
                                originStateVal = 0;
                                break;
                            case "进行中" :
                                originStateVal = 1;
                                break;
                            case "已完成" :
                                originStateVal = 2;
                                break;
                        }
                        $("#0" + $id).css("display", "block");
                        $("#00" + $id).css("display", "block");
                    }
                    else {
                        if (stateChange1 > originStateVal) {
                            //进行数据传输
                            $.ajax({
                                url: modifyUrl,
                                type: "post",
                                dataType: "json",
                                data: {
                                    workOrder: gdh,
                                    line: data[$id].line,
                                    boardType: data[$id].boardType,
                                    id : dataId
                                },
                                success: function (data) {
                                    if (data.result == "succeed") {
                                        alert("修改状态成功");
                                        $("#" + $id).text("修改状态");
                                        $("#0" + $id).css("display", "none");
                                        $("#00" + $id).css("display", "none");
                                        $("#positionTable td").eq(2 + 5 * $id).text(stateChange);
                                        if (stateChange == "已作废" || stateChange == "已完成") {
                                            $("#"+$id).attr("disabled", "disabled")
                                                .removeClass("ui-state-default ui-state-hover");
                                        }
                                    }
                                    else if (data.result == "failed") {
                                        alert("当前处于“已完成状态”，无法作废，请重新选择。");
                                    }
                                },
                                error: function () {
                                    console.log("数据传输失败");
                                }
                            });
                        }
                        else {
                            alert("您选择的状态不符合，请重新选择！");
                        }
                    }});
            var completeState = $("#positionTable td").eq(2+5*i).text();
            if(completeState == "已完成" || completeState == "已作废"){
                $("#" + i).attr("disabled","disabled")
                          .removeClass("ui-state-default");
            }
            //下拉框事件
            $("#0" + i).on("change", function () {
                        var $thisId = $(this).attr("id");
                        stateChange1 = parseInt($(this).val());    //获取修改后的状态对应的value值
                        stateChange = $("#" + $thisId + " option:selected").text();
                        switch (stateChange1) {                //传递链接
                            case 1 :
                                modifyUrl = "program/start";
                                break;
                            case 2 :
                                modifyUrl = "program/finish";
                                break;
                            case 3 :
                                modifyUrl = "program/cancel";
                                break;
                        }
                    });
            //取消按钮事件
            $("#00" + i).hover(function () {
                            $(this).addClass("ui-state-hover")
                        }, function () {
                            $(this).removeClass("ui-state-hover");
                        })
                        .on("click", function () {
                            var cancelId = parseInt($(this).attr("id"));
                            $("#" + cancelId).text("修改状态");
                            $("#0" + cancelId).css("display", "none");
                            $(this).css("display", "none");
                        });


        }}

//   自动补全函数
    function completeAuto(id,array,fn){
        $("#"+id).autocompleter({
            highlightMatches : true,
            source : array,
            template : '{{ label }}',
            empty : false,
            limit : 5,
            callback : function(index ,value ,selected){
                fn(selected.label);
            }
        });
    }

//    站位表回调函数
    function fileCallBack(a){
        $(window).unbind();
        var workNum = $("#work-num").val() == "" ? null : $("#work-num").val();     //工单的输入内容  workOrder
        var state = $("#state option:selected").val();   //状态  state
        var line = $("#line-num option:selected").text() == "不限" ? null : $("#line-num option:selected").text() ;  //线号 line
        $.ajax({
            url :"program/list",
            type: "post",
            dataType: "json",
            data :{
                fileName : a,
                workOrder : workNum ,
                state :state,
                line : line
            },
            success :function(data){
                autoCreateTable(data);
                bindEvent(data);
            },
            error : function(){
                console.log("数据传输失败");
            }
        });
    }

//    工单回调函数
    function  workOrderCallBack(a){
        $(window).unbind();
        var standPosition = $("#stand-position").val() =="" ? null:$("#stand-position").val();     //站位的输入内容   对应fileName
        var state = $("#state option:selected").val();   //状态  state
        var line = $("#line-num option:selected").text() == "不限" ? null : $("#line-num option:selected").text() ;  //线号 line
        $.ajax({
            url :"program/list",
            type: "post",
            dataType: "json",
            data :{
                fileName : standPosition,
                workOrder : a ,
                state :state,
                line : line
            },
            success :function(data){
                autoCreateTable(data);
                bindEvent(data);
            },
            error : function(){
                console.log("数据传输失败");
            }
    });
    }
});