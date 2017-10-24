$(function(){
    //设置初始样式
    var screenWidth  = $(window).width();
    $(".box").css({       //设置盒子的宽度跟随屏幕改变
        "width":screenWidth
    });
    var timeWidth = 0.15*screenWidth;///时间栏的长度设置
    $("#top-left td").eq(0).css({"width":timeWidth});
    $("#top-left div").css({"border-left":timeWidth+"px #BDBABD solid"});
    $(".out em").css({"right":0.5*timeWidth});
    var spanWidth = $(".current span").width()/2;
    $(".current span").css({"margin-left":-spanWidth});
    var operatorWidth = 0.55*screenWidth;   // 设置操作员一栏的长度
    $("#top-left td").eq(1).css({"width":operatorWidth});

    var QCWidth    =0.3*screenWidth;    //QC栏长度设置
    $("#top-left td").eq(2).css({"width":QCWidth});

    var changDu = $("#kong").width()+3;
    console.log(changDu);
    $(".first div").css({"border-left":changDu+"px #BDBABD solid"});
    $(".common em").css({"right":0.5*$("#kong").width()});

    //设置字体颜色

    for(var i = 0;i<$(".second td").length/5;i++){
        $(".second:eq("+i+") td").addClass("suc");
        $(".third:eq("+i+") td").addClass("fail");
        $(".four:eq("+i+") td").addClass("total");
    }
    //
    //console.log($(".second td").length/5);
    getDataAndShow();     //先执行一次，显示初始内容

    $.ajax({
        url:"abc.php",
        type:"get",
        dataType:"json",
        success:function(data){
            console.log(data.feed[0].qwe);
        },
        error:function(e){
            console.log(e);
        }
    });
    var timer=null;
    timer = setInterval(getDataAndShow,3000);

    //获取数据以及展示
    function getDataAndShow(){
        $.ajax({
            url:"abc.php",
            type:"get",
            dataType:"json",
            success:function(data){
               console.log(data.feed[0].qwe);
                var feeds  = data.feed;    //获得上料数组
                var changes = data.changes;    //获得换料数组
                var some   = data.somes;     //获得抽检数组
                var alls   = data.alls;        //获得全检数组
                var times  = feeds[0].time;
                console.log(times);
                $("#span").text("("+ times+")");
                for( var i = 0 ; i <$(".first").length;i++){
                    //$(".time").eq(i).text(feeds[i+1].time);
                    $(".second:eq("+i+") td").eq(1).text(feeds[i].suc).end() //将上料数组的第i个元素即第i时间的成功数填入表格
                                            .eq(2).text(changes[i].suc).end()  //将换料数组的第i个元素即第i时间的成功数填入表格
                                            .eq(3).text(some[i].suc).end()   //将抽检数组的第i个元素即第i时间的成功数填入表格
                                            .eq(4).text(alls[i].suc);  //将全检数组的第i个元素即第i时间的成功数填入表格

                    $(".third:eq("+ i+") td").eq(1).text(feeds[i].fail).end() //将上料数组的第i个元素即第i时间的失败数填入表格
                                             .eq(2).text(changes[i].fail).end()  //将抽料数组的第i个元素即第i时间的失败数填入表格
                                             .eq(3).text(some[i].fail).end()  //将抽检数组的第i个元素即第i时间的失败数填入表格
                                             .eq(4).text(alls[i].fail);  //将全检数组的第i个元素即第i时间的失败数填入表格

                    $(".four:eq("+ i+") td").eq(1).text(feeds[i].total).end()  //将上料数组的第i个元素即第i时间的总数填入表格
                                            .eq(2).text(changes[i].total).end()//将抽料数组的第i个元素即第i时间的总数填入表格
                                            .eq(3).text(some[i].total).end()  //将抽检数组的第i个元素即第i时间的总数填入表格
                                            .eq(4).text(alls[i].total).end();//将全检数组的第i个元素即第i时间的总数填入表格
                }

            },
            error:function(){}
        });
    }

});
