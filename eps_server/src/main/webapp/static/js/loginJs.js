$(function () {
    var screenWidth = $(window).width();
    var screenHeight = $(window).height();
    $(".login-box").css({
        "width": screenWidth,
        "height": screenHeight
    });
//        登录按钮事件
    $("#denglu").hover(function () {
        $(this).addClass("ui-state-hover");
    }, function () {
        $(this).removeClass("ui-state-hover")
    })
        .on("click", function () {
            checkId();
        });
    $(window).keyup(function (e) {
        if (e.which === 13) {
            checkId();
        }
    });

    // 登录验证
    function checkId() {
        $.ajax({
            url: "user/login",
            type: "post",
            dataType: "json",
            data: {
                id: $("#user").val(),
                password: $("#password").val()
            },
            success: function (data) {
                if (data.result == "succeed") {
                    window.location.href = "/eps_server/user/goManage";
                }
                else {
                    alert("账号或密码错误，请重新输入！");
                    $("#password").val("")
                        .focus();
                }
            },
            error: function () {
                console.log("数据传输失败");
            }
        });
    }
});
