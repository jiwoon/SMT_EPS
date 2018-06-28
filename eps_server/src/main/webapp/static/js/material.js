$(function(){
	getAllMaterialInfo();
	
    //查询按钮点击事件
    $("#find").on("click",function(){
    	if(isNumber($('#perifdOfValidity'))||$('#perifdOfValidity').val()==""){
        $.ajax({
            url: "material/list",
            type: "post",
            dataType: "json",
            data:{
            	materialNo:$('#materialNo').val(),
            	perifdOfValidity:$('#perifdOfValidity').val()
            },
            success: function (data){
            	autoCreateTable(data);
            	btnBindEvent(data);
            },
            error:function(){
                console.log("数据传输失败");
            }
        });
    	}else{
    		alert("保质期格式错误");
    	}
    });
    //根据屏幕设置宽高度
    var trLength = 0;   //存储显示表格tr的个数
    var array0 = [];   //用于存储id
    var array1 = [];   //用于存储物料编号
    var array2 = [];   //用于存储保质期
    
	//设置查询和修改按钮样式
    $("#addBtn").hover(function () {
        $(this).addClass("ui-state-hover")
    }, function () {
        $(this).removeClass("ui-state-hover");
    });
    $("#find").hover(function () {
        $(this).addClass("ui-state-hover")
    }, function () {
        $(this).removeClass("ui-state-hover");
    });
    
    //设置获得焦点事件
    $("#materialNo").on("focus",function(){
        $("#search").css("z-index","999");
    }).on("blur",function(){
            $("#search").css("z-index","1");
    });
    $("#perifdOfValidity").on("focus",function(){
        $("#search").css("z-index","999");
    }).on("blur",function(){
        $("#search").css("z-index","1");
    });
    
	//点击增加按钮的点击事件
    $("#addNew").on("click",function(){
        $("#position-table").css("display","none");
        $("#material-operation").css("display","block");
    });
    
	//点击增加界面的返回按钮
    $("#new-position-back").on("click",function(){
        $("#position-table").css("display","block");
        $("#material-operation").css("display","none");
    	$("#addMaterialNo").val("");
    	$("#addPerifdOfValidity").val("");
    	window.location.reload();
    });
    
    //点击新增页面的保存按钮触发的事件
    $("#new-position-save").on("click",function(){
    	if($("#addMaterialNo").val()!=""&&$("#addPerifdOfValidity").val()!=""){
        	if(isNumber($("#addPerifdOfValidity")))
            { 
        		$.ajax({
        			url: "material/add",
        			type: "post",
        			dataType: "json",
        			data:{
        				materialNo :$("#addMaterialNo").val(),
        				perifdOfValidity:$("#addPerifdOfValidity").val(),
        			},
        			success: function (data){
        				if(data.result == "succeed"){
        					alert("保存成功！");
        				}
        				else if(data.result == "failed_materialNo_exist"){
        					alert("物料编号重复，请重新输入！");
        				}
        			},
        			error:function(){
        				console.log("数据传输失败");
        			}
        		});
            }else{
            	alert("保质期格式错误");
            }
    	}
    });
    
    //给背景色
    function setBakcGroundColor(){
        trLength = $("#ShowTable tr").length;
        for(var q = 0;q<trLength/2;q++){
            for(t = 3; t < 6; t++){
                $("#ShowTable td").eq(t+6*q).css({"background-color":"#BFFFFF"});
            }
        }
    }
    
    //动态生成表格
    function autoCreateTable(data){
    	array0 = [];
        array1 = [];
        array2 = [];
        $("#ShowTable").empty();
        $("#btnControl").empty();
        var materialNum = data.length;    //获取物料保质期信息总数
        var html = "";     //用于动态创建表格
        for(var i = 0;i<materialNum;i++) {
            var $json0 = {};
            $json0.label = data[i].id;
            array0.push($json0);
            
            var $json = {};
            $json.label = data[i].materialNo;
            array1.push($json);
            
            var $json1 = {};
            $json1.label = ""+data[i].perifdOfValidity;
            array2.push($json1);
            
            //创建表格
            html += "<tr>"
            html += "<td>" + data[i].materialNo + "</td>"
            html += "<td>" + data[i].perifdOfValidity + "</td>"
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
            //修改按钮
            $("#" + i).hover(
            		function () {
            			$(this).addClass("ui-state-hover")}, 
            		function () {
                        $(this).removeClass("ui-state-hover");
                })
                .on("click",function(){
                    var d = parseInt($(this).attr("id"));     //第几行
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
        }
    }
    
    //修改按钮事件
    function modifyBtn(h){
    	var id = array0[h].label;
    	var materialNo = $("#ShowTable td").eq(3*h).text();
    	var perifdOfValidity = $("#ShowTable td").eq(3*h+1).text();
    	$('#modifyId').val(id);
    	$('#modifyMaterialNo').val(materialNo);
    	$('#modifyPerifdOfValidity').val(perifdOfValidity);
        $("#position-table").css("display","none");
        $("#material-modify").css("display","block");
    }
    
	//点击修改界面的返回按钮
    $("#modify-position-back").on("click",function(){
        $("#position-table").css("display","block");
        $("#material-modify").css("display","none");
    	$("#modifyMaterialNo").val("");
    	$("#modifyPerifdOfValidity").val("");
    	window.location.reload();
    });
    
    //删除按钮事件
    function deleteBtn(j){
    	var id = array0[j].label;
        $.ajax({
            url: "material/delete",
            type: "post",
            dataType: "json",
            data:{
            	id:id
            },
            success: function (data){
                if(data.result == "succeed"){
                    alert("删除成功！");
                    window.location.reload();
                }
                else if(data.result == "failed_not_found"){
                    alert("id不存在！");
                }
            },
            error:function(){
                console.log("数据传输失败");
            }
        });
    }
    
    //查询全部数据
    function getAllMaterialInfo(){
        $.ajax({
            url: "material/list",
            type: "post",
            dataType: "json",
            success: function (data){
            	autoCreateTable(data);
            	btnBindEvent(data);
            	autoComplete("materialNo",array1,materialNoSearch);
            	autoComplete("perifdOfValidity",array2,perifdOfValiditySearch);
            },
            error:function(){
                console.log("数据传输失败");
            }
        });
    }
    
    //点击修改界面的保存按钮触发的事件
    $("#modify-position-save").on("click",function(){
    	if(isNumber($("#modifyPerifdOfValidity"))){
        $.ajax({
            url: "material/update",
            type: "post",
            dataType: "json",
            data:{
            	id:$('#modifyId').val(),
            	materialNo :$("#modifyMaterialNo").val(),
            	perifdOfValidity:$("#modifyPerifdOfValidity").val(),
            },
            success: function (data){
                if(data.result == "succeed"){
                    alert("修改成功！");
                    getAllMaterialInfo();
                }
                else if(data.result == "failed_not_found"){
                    alert("id不存在！");
                }else if(data.result == "failed_materialNo_exist"){
                	alert("物料编号重复,请重新输入!");
                }
            },
            error:function(){
                console.log("数据传输失败");
            }
        });
    	}else{
    		alert("保质期格式错误");
    	}
    });
    
    //判断是否为正整数
    function isNumber(num){
    	var val = num.val();
    	var regu = /^[1-9]\d*$/;
    	if(val!=""){
        	if(regu.test(val)){
        	    return true;
        	} else{
        	    return false;
        	}
    	}
    }

    //物料编号查询回调函数
    function materialNoSearch(a){
            $.ajax({
                url: "material/list",
                type: "post",
                dataType: "json",
                data:{
                	materialNo:a,
                	perifdOfValidity:$('#perifdOfValidity').val()
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

    //保质期查询回调函数
    function perifdOfValiditySearch(a){
            $.ajax({
                url: "material/list",
                type: "post",
                dataType: "json",
                data:{
                	materialNo:$('#materialNo').val(),
                	perifdOfValidity:a
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
    
    //自动补全函数
    function autoComplete(id , array,fn){
        $("#"+id).autocompleter({
            highlightMatches : true,
            source:array,
            template : '{{ label }}',
            empty:false,
            limit:5 ,
            callback:function( index ,value,selected){
                fn(selected.label);
            }
        });
    }
});