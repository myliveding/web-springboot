<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top"><a href="/"><img src="images/icon_left.png" alt="" class="nav-toback"></a>我的卡券</div>
<div class="sendcard-container">
    <div class="sendcard-item card-item">
        <img src="images/card_green.png" alt="">
        <div class="flex-box sendcard-txt">
            <img src="images/icon_logo_white.png" alt="">
            <p>全场消费满399元<span>即可使用</span></p>
            <div class="sendcard-number sendcard-txt-green">6.6<span>折</span></div>
        </div>
        <p>有效期至2017-08-30</p>
    </div>
    <div class="sendcard-item card-item">
        <img src="images/card_yellow.png" alt="">
        <div class="flex-box sendcard-txt">
            <img src="images/icon_logo_white.png" alt="">
            <p>全场消费满399元<span>即可使用</span></p>
            <div class="sendcard-number sendcard-txt-yellow">6.6<span>折</span></div>
        </div>
        <p>未使用</p>
        <div class="card-shadow"></div>
        <div class="card-status"><img src="images/card_timeout.png" alt=""></div>
    </div>
    <div class="sendcard-item card-item">
        <img src="images/card_purple.png" alt="">
        <div class="flex-box sendcard-txt">
            <img src="images/icon_logo_white.png" alt="">
            <p>全场消费满399元<span>即可使用</span></p>
            <div class="sendcard-number sendcard-txt-purple">6.6<span>折</span></div>
        </div>
        <p>有效期至2017-08-30</p>
        <div class="card-shadow"></div>
        <div class="card-status"><img src="images/card_used.png" alt=""></div>
    </div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>