$(function(){
   var screenWidth = $(window).width();     //获取屏幕宽度
    var screenHeight = $(window).height(); //获取屏幕高度
    //根据屏幕设置宽高度
    $(".manageSystem #banner .main").css("width",screenWidth);    //版头的宽度
    $(".main").css("height",screenHeight-81);               //

//    设置导航栏鼠标放置的动作
    $("#nav-manage, #nav-report,#find ,#addBtn").hover(function(){
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

//    查询按钮点击事件
    $("#find").on("click",function(){
        //console.log($("#id").val());
  //var a =  $("#id").val()==""?null:$("#id").val()
  //      console.log(a);
        //console.log($("#name").val());
        //console.log($("#type").val());
        //console.log($("#classType").val());
        //console.log($("#enabled").val());

        $.ajax({
            url: "user/list",
            type: "post",
            dataType: "json",
            data:{
                id :$("#id").val()==""?null:$("#id").val(),
                name:$("#name").val()==""?null:$("#id").val(),
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

    });
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

        //创建按钮
        //修改按钮
        var btn1 = $("<button id='0" + i + "' class='operateBtn ui-state-default ui-corner-all ui-corner-top'>修改</button>");
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
        });
        btn2.hover(function(){
            $(this).addClass("ui-state-hover")
        },function(){
            $(this).removeClass("ui-state-hover");
        });
        $("#mainTable").append(btn1)
            .append(btn2);
    }

}
