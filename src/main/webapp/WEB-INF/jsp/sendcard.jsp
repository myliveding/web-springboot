<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top"><a href="index.html"><img src="images/icon_left.png" alt="" class="nav-toback"></a>卡券赠送</div>
<div class="sendcard-container">
    <div class="sendcard-item">
        <img src="images/sendcard_green.png" alt="">
        <div class="flex-box sendcard-txt">
            <img src="images/icon_logo_white.png" alt="">
            <p>全场消费满399元<span>即可使用</span></p>
            <div class="sendcard-number sendcard-txt-green">6.6<span>折</span></div>
        </div>
        <a href="" class="sendcard-status-green">赠送</a>
    </div>
    <div class="sendcard-item">
        <img src="images/sendcard_yellow.png" alt="">
        <div class="flex-box sendcard-txt">
            <img src="images/icon_logo_white.png" alt="">
            <p>全场消费满399元<span>即可使用</span></p>
            <div class="sendcard-number sendcard-txt-yellow">6.6<span>折</span></div>
        </div>
        <a href="" class="sendcard-status-yellow">赠送</a>
    </div>
    <div class="sendcard-item">
        <img src="images/sendcard_purple.png" alt="">
        <div class="flex-box sendcard-txt">
            <img src="images/icon_logo_white.png" alt="">
            <p>全场消费满399元<span>即可使用</span></p>
            <div class="sendcard-number sendcard-txt-purple">6.6<span>折</span></div>
        </div>
        <a href="" class="sendcard-status-purple">赠送</a>
    </div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>