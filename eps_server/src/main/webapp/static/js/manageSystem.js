$(function(){


    //设置登录时人的账号
   var screenWidth = $(window).width();     //获取屏幕宽度
    var screenHeight = $(window).height(); //获取屏幕高度
    var markNum = 0;  //用于记录修改的是哪一行以便返回数据
    //根据屏幕设置宽高度
    var trLength = 0;   //存储显示表格tr的个数
    var array1 = [];   //用于存储工号
    var array2 = [];   //用于存储姓名
    $(".manageSystem #banner .main").css("width",screenWidth);    //版头的宽度
    $(".main").css("height",screenHeight-81);               //

//    设置导航栏鼠标放置的动作
    $("#nav-manage, #nav-report,#find ,#addBtn,#new-position-save,#new-position-back,#modify-position-back,#modify-position-save").hover(function(){
       $(this).addClass("ui-state-hover")
    },function(){
        $(this).removeClass("ui-state-hover");
    });
    //导航栏点击时出现相应的操作
    $("#nav-manage").on("click",function(){
        $(this).addClass("ui-state-active").removeClass("ui-corner-all").siblings("section").addClass("ui-corner-all").removeClass("ui-state-active");
        $("#manage").removeClass("yinCan");
        $("#report").addClass("yinCan");
        $("#triangle1").addClass("ui-icon-triangle-1-s").removeClass("ui-icon-triangle-1-e");
        $("#triangle2").addClass("ui-icon-triangle-1-e").removeClass("ui-icon-triangle-1-s");

    });
    $("#nav-report").on("click",function(){
        $(this).addClass("ui-state-active").removeClass("ui-corner-all").siblings("section").addClass("ui-corner-all").removeClass("ui-state-active");
        $("#report").removeClass("yinCan");
        $("#manage").addClass("yinCan");
        $("#triangle2").addClass("ui-icon-triangle-1-s").removeClass("ui-icon-triangle-1-e");
        $("#triangle1").addClass("ui-icon-triangle-1-e").removeClass("ui-icon-triangle-1-s");
    });
//    点击菜单栏的样式
     $(".manage-text li").on("click",function(){
         $(this).addClass("current").siblings("li").removeClass("current");
     });
    //  点击菜单右侧相应显示
    $("#manage li:eq(0)").on("click",function(){
        $.ajax({
            url: "user/list",
            type: "post",
            dataType: "json",
            data: {},
            success: function (data){
                autoCreateTable(data);
                btnBindEvent(data);
                autoComplete("workId",array1,IDSearch);
                autoComplete("name",array2,nameSearch);
            },
            error:function(){
                console.log("数据传输失败");
            }
        });
        $("#main-text section").css("display","none");
        $("#staff").css("display","block");
    });
    $("#manage li:eq(1)").on("click",function(){
        $("#main-text section").css("display","none");
        $("#positionManage").css("display","block");
    });
    $("#manage li:eq(2)").on("click",function(){
        $("#main-text section").css("display","none");
        $("#warmManage").css("display","block");
    });
    $("#report li:eq(0)").on("click",function(){
        $("#main-text section").css("display","none");
        $("#clientReport").css("display","block");
    });
    $("#report li:eq(1)").on("click",function(){
        $("#main-text section").css("display","none");
        $("#operationReport").css("display","block");
    });
    $("#report li:eq(2)").on("click",function(){
        window.location.href = "../eps_server/operation/goDisplayReport";
    });
    $("#report li:eq(3)").on("click",function(){
        window.location.href = "../eps_server/operation/goDisplayReport2";
    });

//    查询按钮点击事件
    $("#find").on("click",function(){
        $.ajax({
            url: "user/list",
            type: "post",
            dataType: "json",
            data:{
                id :$("#workId").val()==""?null:$("#workId").val(),
                name:$("#name").val()==""?null:$("#name").val(),
                type:$("#type").val(),
                classType:$("#classType").val(),
                enabled :$("#enabled").val()
            },
            success: function (data){
                autoCreateTable(data);
                btnBindEvent(data);
            },
            error:function(){
                console.log("数据传输失败!!!");
            }
        });

    });
//点击增加按钮的点击事件
    $("#addNew").on("click",function(){
        $("#staff").css("display","none");
        $("#staff-operation").css("display","block");
    });

//员工增加界面按钮事件
    var btnMoveRatio = 0 ;     //按键下滑的系数
    //点击保存按钮触发的事件
    $("#new-position-save").on("click",function(){
        btnMoveRatio++;
        $.ajax({
            url: "user/add",
            type: "post",
            dataType: "json",
            data:{
                id :$("#newId").val(),
                name:$("#newName").val(),
                type:$("#newType").val(),
                classType:$("#newClassType").val(),
            },
            success: function (data){
                if(data.result == "succeed"){
                    alert("保存成功！");
                    $.ajax({
                        url: "user/list",
                        type: "post",
                        dataType: "json",
                        data: {},
                        success: function (data){
                            autoCreateTable(data);
                        },
                        error:function(){
                            console.log("数据传输失败");
                        }
                    });
                    trLength++;
                    setBakcGroundColor();
                }
                else if(data.result == "failed_id_exist"){
                    alert("工号重复，请重新输入！");
                }
            },
            error:function(){
                console.log("数据传输失败");
            }
        });
    });

    $("#new-position-back").on("click",function(){
        $("#staff").css("display","block");
        $("#staff-operation").css("display","none");

    //    点击返回按键时将页面数据重置
        $("#newId").val("");
        $("#newName").val("");
        $("#newType").find('option[value= '+ $("#newType").val()+']').removeAttr("selected");
        $("#newClassType").find('option[value= '+ $("#newClassType").val()+']').removeAttr("selected");
    });

//    员工信息修改页面按钮操作
    $("#modify-position-back").on("click",function(){    //返回按钮
        $("#staff").css("display","block");
        $("#staff-modify").css("display","none");
    });
    $("#modify-position-save").on("click",function(){     //保存按钮
        var modifiedId = $("#modifyId").val();     //获取修改后工号文本框
        var modifiedName = $("#modifyName").val();  //获取修改后的名字文本框
        var modifiedType1 = $("#modifyType option:selected").text();  //岗位：用于前端显示
        var modifiedType2 = $("#modifyType option:selected").val();    //岗位：用于传送数据到后台
        var modifiedClassType1 = $("#modifyClassType option:selected").text();  //班别：用于前端显示
        var modifiedClassType2 = $("#modifyClassType option:selected").val();  //班别：用于传送数据到后台
        var modifiedEnabled1 = $("#modifyEnable option:selected").text();   // 在职状态：用于前台显示
        var modifiedEnabled12 = $("#modifyEnable option:selected").val();   // 在职状态：用于传数据到后台
        //console.log(markNum);
        $.ajax({
            url: "user/update",
            type: "post",
            dataType: "json",
            data: {
                id :modifiedId,
                name:modifiedName,
                type:modifiedType2,
                classType:modifiedClassType2,
                enabled :modifiedEnabled12
            },
            success: function (data){
                if(data.result == "succeed"){
                    alert("保存成功");
                    $("#ShowTable td").eq(7*markNum).text(modifiedId);
                    $("#ShowTable td").eq(7*markNum+1).text(modifiedName);
                    $("#ShowTable td").eq(7*markNum+2).text(modifiedType1);
                    $("#ShowTable td").eq(7*markNum+3).text(modifiedClassType1);
                    $("#ShowTable td").eq(7*markNum+4).text(modifiedEnabled1);
                }
            },
            error:function(){
                console.log("数据传输失败");
            }
        });
    });

    //    动态生成表格
    function autoCreateTable(data){
        array1 = [];
        array2 = [];
        $("#ShowTable").empty();
        $("#btnControl").empty();
        var staffNum = data.length;    //获取员工总人数
        var html = "";     //用于动态创建表格
        for(var i = 0;i<staffNum;i++) {
            var $json = {};
            $json.label = data[i].id;
            array1.push($json);

            var $json1 = {};
            $json1.label = data[i].name;
            array2.push($json1);

            //创建表格
            html += "<tr>";
            html += "<td>" + data[i].id + "</td>"
            html += "<td>" + data[i].name + "</td>"
            html += "<td>" + data[i].typeName + "</td>"
            html += "<td>" + data[i].classTypeName + "</td>"
            html += "<td>" + data[i].isEnabled + "</td>"
            html += "<td>" + data[i].createTimeString + "</td>"
            html += "<td>" + "<button id='" + i + "' class='operateBtn ui-state-default ui-corner-all ui-corner-top modify'>修改</button>" +
                "<button id='0" + i + "' class='operateBtn ui-state-default ui-corner-all ui-corner-top delete'>删除</button>"+"</td>"
            html += "</tr>";
            $("#ShowTable").html(html);
        }
        setBakcGroundColor();
    }
    
    //给按钮绑定事件
    function  btnBindEvent(data){
        for(var i = 0;i<data.length;i++){
            var workState = $("#ShowTable td").eq(4+7*i).text();
            //修改按钮
            $("#" + i).hover(function () {
                         $(this).addClass("ui-state-hover")
                        }, function () {
                         $(this).removeClass("ui-state-hover");
                        })
                .on("click",function(){
                    var d = parseInt($(this).attr("id"));     //id对应的按键的数据
                    markNum = d;
                    modifyBtn(d);
                });
            //删除按钮
            $("#0"+i).hover(function () {
                    $(this).addClass("ui-state-hover")
                }, function () {
                    $(this).removeClass("ui-state-hover");
                })
                .on("click",function(){
                    $(this).attr("disabled","disabled")
                           .removeClass("ui-state-default ui-state-hover");
                    var  j = parseInt($(this).attr("id"));
                    deleteBtn(j);
                });
            if(workState == "否"){
                $("#0"+i).attr("disabled","disabled")
                    .removeClass("ui-state-default ui-state-hover");
            }
        }
    }

    //给背景色
    function setBakcGroundColor(){
        trLength = $("#ShowTable tr").length;
        for(var q = 0;q<trLength/2;q++){
            for(t = 7; t < 14; t++){
                $("#ShowTable td").eq(t+14*q).css({"background-color":"#BFFFFF"});
            }
        }
    }
    //   删除按键事件
    function deleteBtn(k){
        $("#ShowTable td").eq(4+6*k).text("否");
        $("#0"+k).attr("disabled","disabled")
            .removeClass("ui-state-default");

        var staffType = 0;
        //对岗位类型进行判断
        switch ($("#ShowTable td").eq(2+6*k).text()){
            case "仓库操作员" :
                staffType = 0;
                break;
            case "厂线操作员" :
                staffType = 1;
                break;
            case "IPQC" :
                staffType = 2;
                break;
            case "管理员" :
                staffType = 3;
                break;
        }
        var staffId = $("#ShowTable td").eq(6*k).text();
        var staffName = $("#ShowTable td").eq(6*k+1).text();
        var staffClassType = $("#ShowTable td").eq(6*k+3).text() == "白班" ? 0 : 1 ;
        var staffEnable = false;
        $.ajax({
            url: "user/update",
            type: "post",
            dataType: "json",
            data: {
                id :staffId,
                name:staffName,
                type:staffType,
                classType:staffClassType,
               enabled :staffEnable
            },
            success: function (data){
                if(data.result == "succeed"){
                    alert("删除成功！！");
                }
            },
            error:function(){
                console.log("数据传输失败");
            }
        });
    }

//    修改键事件
    function  modifyBtn(h){
        var targetType = 0;
        //对岗位类型进行判断
        switch ($("#ShowTable td").eq(2+7*h).text()){
            case "仓库操作员" :
                targetType = 0;
                break;
            case "厂线操作员" :
                targetType = 1;
                break;
            case "IPQC" :
                targetType = 2;
                break;
            case "管理员" :
                targetType = 3;
                break;
        }
        var targetId = $("#ShowTable td").eq(7*h).text();
        var targetName = $("#ShowTable td").eq(1+7*h).text();
        var targetClassType =$("#ShowTable td").eq(3+7*h).text() == "白班" ? 0 : 1;
        var targetEnable = $("#ShowTable td").eq(4+7*h).text() == "是" ? true : false;
        $("#modifyId").val(targetId)  ;
        $("#modifyName").val(targetName) ;
        $("#modifyType").find('option[value='+targetType+']').attr("selected",true);
        $("#modifyClassType").find('option[value='+targetClassType+']').attr("selected",true);
        $("#modifyEnable").find('option[value='+targetEnable+']').attr("selected",true);
        $("#staff").css("display","none");
        $("#staff-modify").css("display","block");
    }

//自动补全函数
    function autoComplete(id , array,fn){
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

//    工号查询回调函数
    function IDSearch(a){
        $.ajax({
            url: "user/list",
            type: "post",
            dataType: "json",
            data:{
                id :a,
                name:$("#name").val()==""?null:$("#name").val(),
                type:$("#type").val(),
                classType:$("#classType").val(),
                enabled :$("#enabled").val()
            },
            success: function (data){
                autoCreateTable(data);
                btnBindEvent(data);
            },
            error:function(){
                console.log("数据传输失败!!!");
            }
    })
    }

//    姓名查询回调函数
    function nameSearch(a){
        $.ajax({
            url: "user/list",
            type: "post",
            dataType: "json",
            data: {
                id: $("#workId").val() == "" ? null : $("#workId").val(),
                name: a,
                type: $("#type").val(),
                classType: $("#classType").val(),
                enabled: $("#enabled").val()
            },
            success: function (data) {
                autoCreateTable(data);
                btnBindEvent(data);
            },
            error: function () {
                console.log("数据传输失败!!!");
            }
        });
    }
});

