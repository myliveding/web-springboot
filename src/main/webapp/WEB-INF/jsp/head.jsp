<%@ page language="java" pageEncoding="utf-8" %>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/swiper.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
<title>臻品珠宝</title>

<script type="text/javascript">
    function font(w) {
        var w = w || 640,
                docEl = document.documentElement,
                resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
                recalc = function () {
                    var clientWidth = docEl.clientWidth;
                    if (!clientWidth) return;
                    docEl.style.fontSize = 100 * (clientWidth / w) + 'px';
                };
        if (!document.addEventListener) return;
        window.addEventListener(resizeEvt, recalc, false);
        document.addEventListener('DOMContentLoaded', recalc, false);
    }
    font();

    //隐藏微信菜单
    //    wx.hideMenuItems({
    //        menuList: [
    //            "menuItem:share:appMessage",
    //            "menuItem:share:timeline",
    //            "menuItem:share:qq",
    //            "menuItem:share:weiboApp",
    //            "menuItem:favorite",
    //            "menuItem:share:facebook",
    //            "menuItem:share:QZone"
    //        ] // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
    //    });
    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
        WeixinJSBridge.call('hideToolbar'); //隐藏下菜单
        WeixinJSBridge.call('hideOptionMenu'); //隐藏右上角菜单
    });
</script>