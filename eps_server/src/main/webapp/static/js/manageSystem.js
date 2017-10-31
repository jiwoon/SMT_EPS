$(function(){
   var screenWidth = $(window).width();     //获取屏幕宽度
    var screenHeight = $(window).height(); //获取屏幕高度
    //根据屏幕设置宽高度
    $(".manageSystem #banner .main").css("width",screenWidth);    //版头的宽度
    $(".main").css("height",screenHeight-81);               //

//    设置导航栏鼠标放置的动作
    $("#nav-manage, #nav-report,#find ,#addBtn,#new-position-save,#new-position-back").hover(function(){
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
        $("#staff").css("display","block");
        $("#guard").css("display","none");
    });

//    查询按钮点击事件
    $("#find").on("click",function(){
        $.ajax({
            url: "user/list",
            type: "post",
            dataType: "json",
            data:{
                id :$("#id").val()==""?null:$("#id").val(),
                name:$("#name").val()==""?null:$("#name").val(),
                type:$("#type").val(),
                classType:$("#classType").val(),
                enable :$("#enabled").val()
            },
            success: function (data){
                autoCreateTable(data);
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
    $("#new-position-save").on("click",function(){
        var btnNum = $(".modify").length;   //获取原先按键的个数
        btnMoveRatio++;
        var createTime = new Date();    //存储保存时间
        var year = createTime.getFullYear();
        var month = createTime.getMonth()+1;
        var day = createTime.getDate();
        var hour = createTime.getHours();
        var min = createTime.getMinutes();
        var sec = createTime.getSeconds();
        hour < 10 ? hour = "0" + hour : hour;
        min < 10 ? min = "0" + min : min;
        sec < 10 ? sec = "0" + sec : sec;
        var time  = year + "-" + month + "-" + day + " " + hour+":"+min+":"+sec;
        var html = "";
        html += "<tr>"
        html +=      "<td>" + $("#newId").val() + "</td>"
        html +=      "<td>" + $("#newName").val() + "</td>"
        html +=      "<td>" + $("#newType option:selected").text() + "</td>"
        html +=      "<td>" + $("#newClassType option:selected").text() + "</td>"
        html +=      "<td>" + "是" +"</td>"
        html +=      "<td>" + time + "</td>"
        html += "</tr>";
        $("#ShowTable tr").eq(btnNum-1).after(html);
        var btn3 = $("<button id='0" + btnNum + "' class='operateBtn ui-state-default ui-corner-all ui-corner-top modify'>修改</button>");
        //删除按钮
        var btn4 = $("<button id='"+btnNum+"' class='operateBtn ui-state-default ui-corner-all ui-corner-top delete'>删除</button>");
        btn3.css("top",65+btnNum*55);
        btn4.css("top",65+btnNum*55);
        btn3.hover(function(){
            $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");
        });
        btn3.on("click",function(){
            console.log(33);
        });
        btn4.on("click",function(){           //删除键点击事件
            var  j = parseInt($(this).attr("id"));
            $("#ShowTable td").eq(4+6*j).text("否");
            $("#0"+j).attr("disabled","disabled")
                .removeClass("ui-state-default");
            $(this).attr("disabled","disabled")
                .removeClass("ui-state-default ui-state-hover");

            var deleteStaff = 6*j;     //行数对应的系数
            var staffId = $("#ShowTable td").eq(deleteStaff).text();
            var staffName = $("#ShowTable td").eq(deleteStaff+1).text();
            var staffType = $("#ShowTable td").eq(deleteStaff+2).text();
            var staffClassType = $("#ShowTable td").eq(deleteStaff+3).text();
            var staffEnable = $("#ShowTable td").eq(deleteStaff+4).text();
            $.ajax({
                url: "user/update",
                type: "post",
                dataType: "json",
                data: {
                    id :staffId,
                    name:staffName,
                    type:staffType,
                    classType:staffClassType,
                    enable :staffEnable
                },
                success: function (data){
                    if(data){
                        alert("删除成功！！");
                    }
                },
                error:function(){
                    console.log("数据传输失败");
                }
            });
        });
        btn4.hover(function(){
            $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");
        });
        $("#mainTable").append(btn3)
            .append(btn4);

        //$.ajax({
        //    url: "user/list",
        //    type: "post",
        //    dataType: "json",
        //    data:{
        //        id :$("#newId").val(),
        //        name:$("#newName").val(),
        //        type:$("#newType").val(),
        //        classType:$("#classType").val(),
        //    },
        //    success: function (data){
        //        console.log(data);
        //        if(data == "succeed"){
        //            alert("添加成功");
        //            var html = "";
        //            html += "<tr>"
        //            html +=      "<td>" + $("#newId").val() + "</td>"
        //            html +=      "<td>" + $("#newName").val() + "</td>"
        //            html +=      "<td>" + $("#newType").val() + "</td>"
        //            html +=      "<td>" + $("#classType").val() + "</td>"
        //              html +=      "<td>" + "是" +"</td>"
        //            html +=      "<td>" + time + "</td>"
        //            html += "</tr>";
        //            $("#ShowTable tr:eq(0)").before(html);
        //        }
        //        else if(data == "failed_id_exist"){
        //            alert("工号重复，请重新输入！");
        //        }
        //    },
        //    error:function(){
        //        console.log("数据传输失败");
        //    }
        //});
    });

    $("#new-position-back").on("click",function(){
        $("#staff").css("display","block");
        $("#staff-operation").css("display","none");
    });

    //页面一加载就获得的数据
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
});

//    动态生成表格
    function autoCreateTable(data){
        $("#ShowTable").empty();
        var staffNum = data.length;    //获取员工总人数
        var html = "";     //用于动态创建表格
        for(var i = 0;i<staffNum;i++) {
            //创建表格
            html += "<tr>";
            html += "<td>" + data[i].id + "</td>"
            html += "<td>" + data[i].name + "</td>"
            html += "<td>" + data[i].typeName + "</td>"
            html += "<td>" + data[i].classType + "</td>"
            html += "<td>" + data[i].enabled + "</td>"
            html += "<td>" + data[i].createTimeString + "</td>"
            html += "</tr>";
            $("#ShowTable").html(html);

            //if((("#ShowTable tr").length)%2 == 0){
            //    co$nsole.log(33);
            //}
            //创建按钮
            //修改按钮
            var btn1 = $("<button id='0" + i + "' class='operateBtn ui-state-default ui-corner-all ui-corner-top modify'>修改</button>");
            //删除按钮
            var btn2 = $("<button id='"+i+"' class='operateBtn ui-state-default ui-corner-all ui-corner-top delete'>删除</button>");
            btn1.css("top",65+i*55);
            btn2.css("top",65+i*55);
            btn1.hover(function(){
                $(this).addClass("ui-state-hover")
            },function(){
                $(this).removeClass("ui-state-hover");
            });
            btn1.on("click",function(){
                console.log(33);
            });
            btn2.on("click",function(){           //删除键点击事件
                var  j = parseInt($(this).attr("id"));
                $("#ShowTable td").eq(4+6*j).text("否");
                $("#0"+j).attr("disabled","disabled")
                         .removeClass("ui-state-default");
                $(this).attr("disabled","disabled")
                       .removeClass("ui-state-default ui-state-hover");

                var deleteStaff = 6*j;     //行数对应的系数
                var staffId = $("#ShowTable td").eq(deleteStaff).text();
                var staffName = $("#ShowTable td").eq(deleteStaff+1).text();
                var staffType = $("#ShowTable td").eq(deleteStaff+2).text();
                var staffClassType = $("#ShowTable td").eq(deleteStaff+3).text();
                var staffEnable = $("#ShowTable td").eq(deleteStaff+4).text();
                $.ajax({
                    url: "user/update",
                    type: "post",
                    dataType: "json",
                    data: {
                        id :staffId,
                        name:staffName,
                        type:staffType,
                        classType:staffClassType,
                        enable :staffEnable
                    },
                    success: function (data){
                        if(data){
                            alert("删除成功！！");
                        }
                    },
                    error:function(){
                        console.log("数据传输失败");
                    }
                });
            });
            btn2.hover(function(){
                $(this).addClass("ui-state-hover")
            },function(){
                $(this).removeClass("ui-state-hover");
            });
            $("#mainTable").append(btn1)
                .append(btn2);
        }
        var trLength = $("#ShowTable tr").length/2
        for(var q = 0;q<trLength;q++){
            for(t = 6; t < 12; t++){
                $("#ShowTable td").eq(t+12*q).css({"background-color":"#BFFFFF"});
            }
        }
}

