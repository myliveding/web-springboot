<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="javascript:history.back(-1)">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>
    卡券赠送
</div>
<div class="sendcard-container">
    <c:forEach var="card" items="${cards}">
        <div class="sendcard-item" data-id="${card.id}" data-name="${card.discount}折"
             data-method="${card.discount}/100">
            <input type="hidden" class="room_type_id" value="${card.id}"/>
            <img src="${pageContext.request.contextPath}/images/sendcard_green.png" alt="">
            <div class="flex-box sendcard-txt">
                <img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">
                <p>全场消费满${card.user_price}元<span>即可使用</span></p>
                <div class="sendcard-number sendcard-txt-green">${card.discount}<span>折</span></div>
            </div>
            <c:if test="${card.status eq '1'}">
                <a class="sendcard-status-green" onclick="send(${card.id})">赠送</a>
            </c:if>
        </div>
    </c:forEach>
</div>
<div class="flex-box show-loading">
    <img src="${pageContext.request.contextPath}/images/loading.gif" alt="">
    <p>加载更多</p>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    $('.sendcard-item').click(function () {
        var json = {}
        json.card_id = $(this).attr('data-id');
        json.card_name = $(this).attr('data-name');
        json.card_method = $(this).attr('data-method');
        var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
        selectedinfo.integral = false
        selectedinfo.cardinfo = {}
        selectedinfo.cardinfo.card_method = 1
        selectedinfo.cardinfo.card_id = ''
        selectedinfo.cardinfo.card_name = ''
        selectedinfo.cardinfo.card_headline = ''
        selectedinfo.sendinfo = json
        localStorage.selectedinfo = JSON.stringify(selectedinfo)
        window.history.go(-1);
    })
</script>
</body>
</html>