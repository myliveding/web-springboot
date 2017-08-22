$(function () {
    $("#tab-login").click(function () {
        $('body').addClass("login-active");
    });
    $("#tab-regist").click(function () {
        $('body').removeClass("login-active");
    });
    $(".regist-yzm").click(function () {
        $(this).removeClass("regist-btn-active");
        $(".regist-pwd,.login-btn-img").addClass("regist-btn-active");
        $(".regist-item-yzm").addClass("regist-item-active");
        $(".regist-item-pwd").removeClass("regist-item-active");
    });
    $(".regist-pwd").click(function () {
        $(this).removeClass("regist-btn-active");
        $(".login-btn-img").removeClass("regist-btn-active");
        $(".regist-yzm").addClass("regist-btn-active");
        $(".regist-item-pwd").addClass("regist-item-active");
        $(".regist-item-yzm").removeClass("regist-item-active");
    });
    $(".info-vip").click(function () {
        $(".index-vip").show();
    });
    $(".index-vip-top").click(function (e) {
        $(".index-vip").hide();
    })

    $(".bshow-btn").click(function () {
        $(".bshow-box").show();
    })
    $(".bshow-box-shadow").click(function () {
        $(".bshow-box").hide();
    })

})