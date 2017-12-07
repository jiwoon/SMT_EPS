$(function(){
    var lineChoice = "";
    var $timeId1 = 0 ;    //时间设置值的id
    var idNum = 0 ;
    var sendArray =[];   //存放要传输的数据
    var flag = 0 ;    //用于判断是否执行
//存储同一线号的数据
    var $301 = [];
    var $302 = [];
    var $303 = [];
    var $304 = [];
    var $305 = [];
    var $306 = [];
    var $307 = [];
    var $308 = [];
//    查询按钮
    $("#searchBtn").hover(function(){
        $(this).addClass("ui-state-hover");
    },function(){
        $(this).removeClass("ui-state-hover");
    }).on("click",function(){
        for( var i = 0 ; i < $("#lineChoice option").length ; i++){
            $("#lineChoice option").eq(i).removeAttr("selected");
        }
        $("#lineChoice option").eq(0).attr("selected",true);
            $timeId1 = 0;
            $("#main").empty();
            $.ajax({
                url : "config/list" ,
                type : "post",
                dataType : "json",
                data : {},
                success : function(data){
                    $301 = [];
                    $302 = [];
                    $303 = [];
                    $304 = [];
                    $305 = [];
                    $306 = [];
                    $307 = [];
                    $308 = [];
                    var dataLength = data.length;
                    for( var i = 0 ; i < dataLength ; i++){
                        var $value = data[i].line;
                        switch ($value){
                            case "301" : $301.push(data[i]);
                                break;
                            case "302" : $302.push(data[i]);
                                break;
                            case "303" : $303.push(data[i]);
                                break;
                            case "304" : $304.push(data[i]);
                                break;
                            case "305" : $305.push(data[i]);
                                break;
                            case "306" : $306.push(data[i]);
                                break;
                            case "307" : $307.push(data[i]);
                                break;
                            case "308" : $308.push(data[i]);
                                break;
                        }
                    }
                    //301线
                    for( var i = 1 ; i < 9 ; i++){
                        createTable(eval("$30"+i));
                    }
                    //产线下拉框
                    $("#lineChoice").on("change",function(){
                        flag = 0;
                        $("#main").empty();
                        lineChoice = $("#lineChoice option:selected").text();
                        select(lineChoice ,flag);
                    });
                },
                error : function(){
                    console.log("数据传输错误！！");
                }
            });
    });
    //保存按钮
    $("#saveBtn").hover(function(){
        $(this).addClass("ui-state-hover");
    },function(){
                    $(this).removeClass("ui-state-hover");})
        .on("click",function(){
            lineChoice = $("#lineChoice option:selected").text();
            sendArray =[];  //每次清空数组
            idNum = 0;
            flag = 1 ;
            select(lineChoice ,flag);
            //将所有数组放到array中
            console.log($301);
            for( var i =  1; i < 9 ; i++){
                getAndPush(eval("$30"+i))
            }
            var $newJson =  JSON.stringify(sendArray);
                    $.ajax({
                        url : "config/set",
                        type : "post",
                        dataType : "json",
                        data : {
                            configs : $newJson
                        },
                        success : function(data){
                            if(data.result == "succeed"){
                                alert("保存成功");
                            }
                        },
                        error : function(){
                            console.log("数据传输错误！！");
                        }
                    });
        });
    //针对一个线号对应多个数据
    function createMulTable(data,w){
        var html = "" ;
        var checkBox = data[0].enabled == true ? "<input id='0" + $timeId1 + "' type='checkbox' checked="+ false +">" :"<input id='0" + $timeId1 + "' type='checkbox'>";
        html += "<tr>"
        html +=     "<td rowspan='"+ w +"'>"+ data[0].line +"</td>"
        html +=     "<td>" + data[0].alias +"</td>"
        html +=     "<td>" + "<input id='"+ $timeId1+"'  type='text' value='"+ data[0].value+"' style='border: 1px solid black;padding: 5px;'>" + "</td>"
        html +=     "<td>" + checkBox + "</td>"
        html +=     "<td>" + data[0].description + "</td>"
        html += "</tr>";
        $("#main").append(html);
        $timeId1++;
    }
//    生成缩进一格的一行table
    function createOneTable(data , k){
        var checkBox = data[k].enabled == true ? "<input id='0" + $timeId1 + "' type='checkbox' checked="+ false +">" :"<input id='0" + $timeId1 + "' type='checkbox'>";
        var html = "" ;
        html += "<tr>"
        html +=     "<td>" + data[k].alias +"</td>"
        html +=     "<td>" + "<input id='"+ $timeId1 +"'  type='text' value='"+ data[k].value+"' style='border: 1px solid black;padding: 5px'>" + "</td>"
        html +=     "<td>" + checkBox + "</td>"
        html +=     "<td>" + data[k].description + "</td>"
        html += "</tr>";
        $("#main").append(html);
        $timeId1++;
    }
    //生成单独一行表格
    function createTwoTable(data , k){
        var checkBox = data[k].enabled == true ? "<input id='0" + $timeId1 + "' type='checkbox' checked="+ false +">" :"<input id='0" + $timeId1 + "' type='checkbox'>";
        var html = "" ;
        html += "<tr>"
        html +=     "<td>" + data[k].line +"</td>"
        html +=     "<td>" + data[k].alias +"</td>"
        html +=     "<td>" + "<input id='" + $timeId1 +"' type='text' value='"+ data[k].value+"' style='border: 1px solid black;padding: 5px'>" + "</td>"
        html +=     "<td>" + checkBox + "</td>"
        html +=     "<td>" + data[k].description + "</td>"
        html += "</tr>";
        $("#main").append(html);
        $timeId1++;
    }
    //生成表格
    function createTable(array){
        if(array.length > 1){
            createMulTable(array,array.length);
            for( var i = 1 ; i < array.length ; i++){
                createOneTable(array , i);
            }
        }else if(array.length == 1){
            createTwoTable(array , 0);
        }
    }
//    获取配置内容
    function getConMain(array ,num){
        var index = 0 ;
        for( var i = num ; i <num + array.length ; i++){
            array[index].value = $("#"+i).val();
            array[index].enabled = $("#0"+i).prop("checked");
            index++;
        }
    }

//    产线筛选
    function select(a,t){
        switch (a){
            case "不限" :
                $timeId1 = 0 ;
                if( t == 0){
                    for( var i = 1 ; i < 9 ; i++){
                        createTable(eval("$30"+i));
                    }
                }else if( t == 1){
                    for( var i = 1 ; i < 9 ; i++){
                            getConMain(eval("$30"+i),idNum);
                            idNum += eval("$30"+i).length;
                    }
                }
                break;
            case "301" :
                $timeId1 = 0 ;   //保证ID一直一致
                if( t == 0){
                    createTable($301);
                }else if( t == 1){
                    getConMain($301,$timeId1);

                }
                break;
            case "302" :
                $timeId1 = $301.length ;
                if(t == 0){
                    createTable($302);
                }else if( t == 1){
                    getConMain($302,$timeId1);

                }
                break;
            case "303" :
                $timeId1 = $301.length + $302.length ;
                if( t == 0){
                    createTable($303);
                }else if(t == 1){
                    getConMain($303,$timeId1);

                }
                break;
            case "304" :
                $timeId1 = $301.length + $302.length + $303.length;
                if( t == 0){
                    createTable($304);
                }else if( t == 1){
                    getConMain($304,$timeId1);
                }
                break;
            case "305" :
                $timeId1 = $301.length + $302.length + $303.length + $304.length ;
                if( t == 0){
                    createTable($305);
                }
                else if( t == 1){
                    getConMain($305 ,$timeId1);
                }
                break;
            case "306" :
                $timeId1 = $301.length + $302.length + $303.length + $304.length + $305.length;
                if (t == 0){
                    createTable($306);
                }else if( t == 1){
                    getConMain($306 ,$timeId1);
                }
                break;
            case "307" :
                $timeId1 = $301.length + $302.length + $303.length + $304.length + $305.length + $306.length;
                if( t == 0) {
                    createTable($307);
                }else if( t == 1){
                    getConMain($307 ,$timeId1);
                }
                break;
            case "308" :
                $timeId1 = $301.length + $302.length + $303.length + $304.length + $305.length + $306.length + $307.length;
                if ( t == 0){
                    createTable($308);
                }else if( t == 1){
                    getConMain($308 ,$timeId1);
                }
                break;
        }
    }
//    将数据从数组中拿出放到array中
    function getAndPush(array){
        for( var i = 0 ; i < array.length ; i++){
            sendArray.push(array[i]);
        }
    }
});