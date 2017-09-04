<%@ page language="java" pageEncoding="utf-8" %>
<nav class="navbar-fixed-bottom">
    <div class="container-fluid index-tabar">
        <div class="col-xs-4">
            <a href="${pageContext.request.contextPath}/login/index">
                <img src="${pageContext.request.contextPath}/images/icon_homed.png" height="20">
            </a>
            <p class="tabar-hover">首页</p>
        </div>
        <div class="col-xs-4">
            <a href="${pageContext.request.contextPath}/login/activitys">
                <img src="${pageContext.request.contextPath}/images/icon_active.png" height="20">
            </a>
            <p>活动</p>
        </div>
        <div class="col-xs-4">
            <a href="${pageContext.request.contextPath}/login/userCenter">
                <img src="${pageContext.request.contextPath}/images/icon_mine.png" height="20">
            </a>
            <p>我的</p>
        </div>
    </div>
</nav>
<script src="${pageContext.request.contextPath}/js/jquery-1.12.3.min.js"></script>
<script src="${pageContext.request.contextPath}/js/calendar.js"></script>
<script src="${pageContext.request.contextPath}/js/index.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/swiper.min.js"></script>
<script>
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        // mousewheelControl : true,
    });
</script>