<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
，
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/static/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css">
    <link rel="stylesheet" href="<%=basePath%>css/swiper.min.css">
    <link rel="stylesheet" href="<%=basePath%>css/index.css">
    <title>臻品珠宝</title>
</head>
<script>
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
</script>
<body class="active-body">
<div class="bshow-back"><a href="brand.html"><img src="images/bshow_left.png" alt=""></a></div>
<div class="swiper-container banner">
    <div class="swiper-wrapper">
        <ul>
            <c:forEach var="banner" items="${banners}">
                <li>
                    <div class="swiper-slide"><a href="${banner.id}"><img src="${banner.image_url}"
                                                                          alt="">${banner.title}</a></div>
                </li>
            </c:forEach>
        </ul>
    </div>
    <div class="swiper-pagination"></div>
</div>
<div class="bshow-header">
    <h3>臻品珠宝 产品</h3>
    <h4>臻品珠宝 产品广告语 说明</h4>
</div>
<div class="bshow-con">
    <img src="images/bshow_img1.jpg" alt="" class="bshow-btn">
</div>
<div class="bshow-box">
    <div class="bshow-box-shadow"></div>
    <div class="bshow-box-con">
        <img src="images/brand_banner1.jpg" alt="">
        <div class="bshow-box-txt">
            <h4>臻品珠宝 产品</h4>
            <p>臻品珠宝 产品广告语说明</p>
            <span>推</span>
        </div>
    </div>
</div>
<nav class="navbar-fixed-bottom">
    <div class="container-fluid index-tabar">
        <div class="col-xs-4"><a href="index.html"><img src="images/icon_home.png" height="20"></a>
            <p>首页</p></div>
        <div class="col-xs-4"><a href="active.html"><img src="images/icon_active.png" height="20"></a>
            <p>活动</p></div>
        <div class="col-xs-4"><a href="mine.html"><img src="images/icon_mine.png" height="20"></a>
            <p>我的</p></div>
    </div>
</nav>
<script src="js/jquery-1.12.3.min.js"></script>
<script src="js/index.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/swiper.min.js"></script>
<script>
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        // mousewheelControl : true,
    });
</script>
</body>
</html>