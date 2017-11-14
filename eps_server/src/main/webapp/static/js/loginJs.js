$(function(){
    var screenWidth = $(window).width();
    var screenHeight = $(window).height();
    $(".login-box").css({
        "width":screenWidth,
        "height":screenHeight
    });
//        登录按钮事件
    $("#denglu").hover(function(){$(this).addClass("ui-state-hover")},
        function(){$(this).removeClass("ui-state-hover")
            .on("click",function(){
                $.ajax({
                    url :"user/login",
                    type : "post",
                    dataType : "json",
                    data : {
                        id : $("#user").val(),
                        password : $("#password").val()
                    },
                    success : function(data){
                       if(data.result == "succeed"){
                           window.location.href = "user/goManage";
                       }
                    },
                    error : function(){
                        console.log("数据传输失败");
                    }
                });
                //
            });
        });

//        确定按钮事件
    $("#errorShowBox-btn").on("click",function(){

        $("#showError").css("display","none");
        $("#password").val("")
            .focus();
    });
});
