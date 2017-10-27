$(function(){
    var timer1 = null; //控制播放的定时器
    var timer2 = null;  //控制获取数据的定时器
    var timer3 = null;   //获取时间的定时器
//    根据屏幕的宽度设置页面宽度
    var screenWidth  =  $(window).width();    //获取屏幕宽度
    var screenHeight = $(window).height();    //获取屏幕高度
    $(".nav .mainText").width(screenWidth);    //设置导航栏和内容宽度
    $(".mainText").height(screenHeight-100);   //

//    获取文字显示栏 的宽度
    var mainWidth = $("#main").width();
    var divs = $("#main div");  //获取div
    for(var i = 1;i<divs.length;i++){    //将第一个后面的div和section全部放到后面去
        $("#main div").eq(i).css("left",mainWidth);
        $(".mainText section").eq(i).css("left",screenWidth);
    }

//    设置两个按钮鼠标悬浮事件的事件
    $("#click-left em").hover(function(){
        $("#click-left em").css({"border-color":"transparent #000000  transparent transparent",});
        $("#click-left span").css("background-color","#000");
    },function(){
        $("#click-left em").css({"border-color":"transparent #B9B9C8  transparent transparent"});
        $("#click-left span").css("background-color","#B9B9C8");
    });
    $("#click-right em").hover(function(){
        $("#click-right em").css({"border-color":"transparent transparent transparent #000000",});
        $("#click-right span").css("background-color","#000");
    },function(){
        $("#click-right em").css({"border-color":"transparent transparent transparent #B9B9C8"});
        $("#click-right span").css("background-color","#B9B9C8");
    });
//    设置暂停停止按钮事件
$("#nav-right").on("click",function(){
    if($("#nav-right div").hasClass("start"))
    {
            $("#nav-right div").removeClass("start").addClass("stop");
            clearInterval(timer1);
    }
    else {
        $("#nav-right div").removeClass("stop").addClass("start");
        timer1 = setInterval(autoPlay,3000);
    }
});
    var key = 0;
//左右按键点击事件
//    左键
    $("#click-left").on("click",function(){
        $("#main div").eq(key).animate({"left":mainWidth},500);
        $(".mainText div").eq(key).animate({"left":screenWidth},500);
        --key<0?key=3:key;
        $("#main div").eq(key).css("left",-mainWidth);
        $(".mainText div").eq(key).css("left",-screenWidth);
        $("#main div").eq(key).animate({"left":0},500);
        $(".mainText div").eq(key).animate({"left":0},500);
    });
    //console.log($(".mainText div:eq(3)").attr("class"));
//    右键
    $("#click-right").on("click",function(){
        autoPlay();
    });


//   自动播放

    timer1 = setInterval(autoPlay,10000);
    
    function autoPlay(){
        $("#main div").eq(key).animate({"left":-mainWidth},500);
        $(".mainText section").eq(key).animate({"left":-screenWidth},500);
        ++key>3?key=0:key;
        $("#main div").eq(key).css("left",mainWidth);
        $(".mainText section").eq(key).css("left",screenWidth);
        $("#main div").eq(key).animate({"left":0},500);
        $(".mainText section").eq(key).animate({"left":0},500);
    }


    //获取当前时间
   timer3 =  setInterval(gq,1000);
    function gq(){
        var now = new Date();
        //剩余时数
        var hour = parseInt(now.getHours());
        //剩余分钟数
        var min = parseInt(now.getMinutes());
        //剩余秒数
        var se = parseInt(now.getSeconds());
        se<10?se="0"+se:se;
        min<10?min="0"+min:min;
        hour<10?hour="0"+hour:hour;
    $(".clock").text(hour+":"+min+":"+se);
    }

//往画布填充内容
    getAndShow();
  timer2 =setInterval(getAndShow,1000);
    function getAndShow() {
        $.ajax({
            url: "operation/listDisplayReport",
            type: "post",
            dataType: "json",
            data: {line:"308"},
            success: function (data) {
                var nameArray = ["feed", "changes", "somes","alls"];
                //    一、创建舞台
                for(var u = 0;u<4;u++) {
                    var stage = new Konva.Stage({
                        container: 'sec' +(u+1),
                        width: screenWidth,
                        height: screenHeight - 100
                    });
                    //    创建层
                    var layer = new Konva.Layer();
                    stage.add(layer);
                    //    中心坐标
                    var cenx = stage.width() / 2;
                    var ceny = stage.height() / 2;
                    //
                    var dd =nameArray[0].toString();
                    var num = data[nameArray[u]].length;     //数组的个数
                    //console.log(num);
                    var x01 = 1 / 16 * stage.width();   //设定的原坐标
                    var y01 = 2 / 5 * stage.height();    //第一条底线的纵坐标
                    var y02 = 9 / 10 * stage.height();    //第二条底线的纵坐标
                    var width1 = 7 / 8 * stage.width();  //设定的柱状图的总宽度
                    var xWidth = 1 / 24 * width1;        //设定每个点之间的间距
                    var eachWidth = 1 / 6 * width1;    //每个小时的宽度为总宽度的1/6

                    var bsline1 = new Konva.Line({       //画柱状图的底线
                        points: [x01, y01, x01 + width1, y01],
                        strokeWidth: 3,
                        stroke: "black"
                    });

                    var bsline2 = new Konva.Line({
                        points: [x01, y02, x01 + width1, y02],
                        strokeWidth: 3,
                        stroke: "black"
                    });
                    layer.add(bsline1);
                    layer.add(bsline2);

                    //说明
                    var describeTotal = new Konva.Text({
                        x : stage.width()-175,
                        y : 20,
                        fontSize : 25,
                        fill : "black",
                        fontStyle : "bold",
                        text : "黑色：总数 ",
                        width  :150,
                        height : 25
                    });
                    var describeSuc = new Konva.Text({
                        x : stage.width()-175,
                        y : 45,
                        fontSize : 25,
                        fontStyle : "bold",
                        fill : "green",
                        text : "绿色：成功数",
                        width  :150,
                        height : 25
                    });
                    var describeFail = new Konva.Text({
                        x : stage.width()-175,
                        y : 70,
                        fontSize : 25,
                        fontStyle : "bold",
                        fill : "red",
                        text : "红色：失败数",
                        width  :150,
                        height : 25
                    });

                    layer.add(describeTotal);
                    layer.add(describeSuc);
                    layer.add(describeFail);
                    for (var k = 0; k < 6; k++) {
                        var rect = new Konva.Rect({
                            x: x01 + (1 / 6 + k) * eachWidth,
                            y: y01 - data[nameArray[u]][k].total,
                            width: 7 / 27 * eachWidth,
                            height: data[nameArray[u]][k].total,
                            fill: "black"
                        });
                        var text = new Konva.Text({
                            x: x01 + (1 / 6 + k) * eachWidth,
                            y: y01 - data[nameArray[u]][k].total - 20,
                            fontSize: 20,
                            text: data[nameArray[u]][k].total,
                            fill :"black",
                            width: 7 / 27 * eachWidth,
                            Align: "center"
                        });
                        var textBottom = new Konva.Text({
                            x: x01 + (1 / 2 + k) * eachWidth - 10,
                            y: y01 + 10,
                            fontSize: 20,
                            text: data[nameArray[u]][k].time,
                            width: 10 / 27 * eachWidth,
                            Align: "center"
                        });
                        var rect1 = new Konva.Rect({
                            x: x01 + 7 / 27 * eachWidth + (1 / 6 + k) * eachWidth,
                            y: y01 - data[nameArray[u]][k].suc,
                            width: 7 / 27 * eachWidth,
                            height: data[nameArray[u]][k].suc,
                            fill: "green"
                        });
                        var text1 = new Konva.Text({
                            x: x01 + 7 / 27 * eachWidth + (1 / 6 + k) * eachWidth,
                            y: y01 - data[nameArray[u]][k].suc - 20,
                            fontSize: 20,
                            fill : "green",
                            text: data[nameArray[u]][k].suc,
                            width: 7 / 27 * eachWidth,
                            Align: "center"
                        });
                        var rect2 = new Konva.Rect({
                            x: x01 + 14 / 27 * eachWidth + (1 / 6 + k) * eachWidth,
                            y: y01 - data[nameArray[u]][k].fail,
                            width: 7 / 27 * eachWidth,
                            height: data[nameArray[u]][k].fail,
                            fill: "red"
                        });
                        var text2 = new Konva.Text({
                            x: x01 + 14 / 27 * eachWidth + (1 / 6 + k) * eachWidth,
                            y: y01 - data[nameArray[u]][k].fail - 20,
                            fontSize: 20,
                            fill : "red",
                            text: data[nameArray[u]][k].fail,
                            width: 7 / 27 * eachWidth,
                            Align: "center"
                        });
                        layer.add(rect);
                        layer.add(text);
                        layer.add(textBottom);
                        layer.add(rect1);
                        layer.add(text1);
                        layer.add(rect2);
                        layer.add(text2);
                    }
                    //折线
                    var zs = 0;    //总数系数
                    var cgs   = 0;   //成功数系数
                    var sbs  = 0;    //失败数系数
                    switch (nameArray[u]){
                        case "feed":
                            zs = 5;
                            cgs = 4;
                            sbs = 2;
                            break;
                        case "changes":
                            zs = 7;
                            cgs = 5;
                            sbs = 2;
                            break;
                        case "somes" :
                            zs = 7;
                            cgs = 5;
                            sbs = 2;
                            break;
                        case "alls"  :
                            zs = 2.5;
                            cgs = 1;
                            sbs = 1;
                            break;
                    }

                    for (var q = 0; q < num - 1; q++) {
                        var totalLine = new Konva.Line({       //总数折线
<<<<<<< HEAD
                            points: [x01 + q * xWidth, y02 - (parseInt(data[nameArray[u]][q].total))*zs, x01 + [q + 1] * xWidth, y02 - (parseInt(data[nameArray[u]][(q + 1)].total))*zs],
=======
                            points: [x01 + q * xWidth, y02 - (parseInt(data[nameArray[u]][q].total))*5, x01 + [q + 1] * xWidth, y02 - (parseInt(data[nameArray[u]][(q + 1)].total))*5],
>>>>>>> 8401cd161eff00c6e8032c4ab00165c262af4a21
                            strokeWidth: 3,
                            stroke: "black"
                        });
                        var sucLine = new Konva.Line({       //成功数折线
<<<<<<< HEAD
                            points: [x01 + q * xWidth, y02 - (parseInt(data[nameArray[u]][q].suc))*cgs, x01 + [q + 1] * xWidth, y02 - (parseInt(data[nameArray[u]][(q + 1)].suc))*cgs],
=======
                            points: [x01 + q * xWidth, y02 - (parseInt(data[nameArray[u]][q].suc))*5, x01 + [q + 1] * xWidth, y02 - (parseInt(data[nameArray[u]][(q + 1)].suc))*5],
>>>>>>> 8401cd161eff00c6e8032c4ab00165c262af4a21
                            strokeWidth: 3,
                            stroke: "green"
                        });
                        var failLine = new Konva.Line({       //失败数折线
                            points: [x01 + q * xWidth, y02 - (parseInt(data[nameArray[u]][q].fail))*sbs, x01 + [q + 1] * xWidth, y02 - (parseInt(data[nameArray[u]][(q + 1)].fail))*sbs],
                            strokeWidth: 3,
                            stroke: "red"
                        });
                        layer.add(totalLine);
                        layer.add(sucLine);
                        layer.add(failLine);
                    }
                    //折线数据部分
                    for (var w = 0; w < num; w++) {
                        var toatlNum = new Konva.Text({
                            x: x01 + w * xWidth,
<<<<<<< HEAD
                            y: y02 - (parseInt(data[nameArray[u]][w].total)*zs) - 18,
=======
                            y: y02 - parseInt(data[nameArray[u]][q].total)*5 - 20,
>>>>>>> 8401cd161eff00c6e8032c4ab00165c262af4a21
                            fontSize: 18,
                            fill :"black",
                            text: data[nameArray[u]][w].total,
                        });
                        var sucNum = new Konva.Text({
                            x: x01 + w * xWidth,
<<<<<<< HEAD
                            y: y02 - (parseInt(data[nameArray[u]][w].suc)*cgs) - 18,
=======
                            y: y02 - parseInt(data[nameArray[u]][q].suc)*5 + 5,
>>>>>>> 8401cd161eff00c6e8032c4ab00165c262af4a21
                            fontSize: 18,
                            fill : "green",
                            text: data[nameArray[u]][w].suc,
                        });
                        var failNum = new Konva.Text({
                            x: x01 + w * xWidth,
<<<<<<< HEAD
                            y: y02 - (parseInt(data[nameArray[u]][w].fail))*sbs -18,
=======
                            y: y02 - parseInt(data[nameArray[u]][q].fail)*5 - 15,
>>>>>>> 8401cd161eff00c6e8032c4ab00165c262af4a21
                            fontSize: 18,
                            fill : "red",
                            text: data[nameArray[u]][w].fail,
                        });

                        var showTime = new Konva.Text({
                            x: x01 + w * xWidth - 10,
                            y: y02 + 5,
                            fontSize: 15,
                            text: data[nameArray[u]][w].time,
                        });
                        var warm = new Konva.Rect({
                            x: x01 + w * xWidth,
                            y: y02 - 5,
                            width: 2,
                            height: 5,
                            fill: "black"
                        });

                        layer.add(toatlNum);
                        layer.add(sucNum);
                        layer.add(failNum);
                        layer.add(showTime);
                        layer.add(warm);
                    }

                    layer.draw();

                }
            },
            error: function () {
                console.log(33);
            }
        });
    }
});
