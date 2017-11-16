<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<style>
    .selectedred {
        display: inline-block;
        width: 15px;
        height: 15px;
        border-radius: 50%;
        background: red;
        position: absolute;
        top: 4px;
        right: 4px;
        display: none;
    }
</style>
<body class="active-body">
<div class="nav-top">
    <a href="javascript:history.back(-1)">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>
    我的优惠券
</div>
<div class="sendcard-container">
    <c:forEach var="card" items="${coupons}">
        <div class="sendcard-item card-item" data-id="${card.id}" data-name="满200减100" data-method="${card.price}"
             data-headline="${card.user_price}">
            <!-- method:减多少元   headline：满多少 -->
            <span style="display:inline-block;width:15px;height:15px;border-radius:50%;background:red;position:absolute;top:4px;right:4px"></span>
            <fmt:parseDate value="${card.expiration_date}" pattern="yyyy-MM-dd" var="expireDate"/>
            <fmt:formatDate value="${expireDate}" pattern="yyyyMMdd" var="expire"/>
            <c:if test="${now gt expire}" var="rs">
                <img src="${pageContext.request.contextPath}/images/card_yellow.png" alt="">
            </c:if>
            <c:if test="${!rs}">
                <c:if test="${card.status eq '1'}">
                    <img src="${pageContext.request.contextPath}/images/card_green.png" alt="">
                </c:if>
                <c:if test="${card.status eq '2'}">
                    <img src="${pageContext.request.contextPath}/images/card_purple.png" alt="">
                </c:if>
            </c:if>
            <div class="flex-box sendcard-txt">
                <img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">
                <p>全场消费满${card.user_price}元<span>即可使用</span></p>
                <div class="sendcard-number sendcard-txt-green">
                        ${card.price} <span>元</span>
                </div>
            </div>
            <c:if test="${now gt expire}" var="rs">
                <p>未使用</p>
                <div class="card-shadow"></div>
                <div class="card-status">
                    <img src="${pageContext.request.contextPath}/images/card_timeout.png" alt="">
                </div>
            </c:if>
            <c:if test="${!rs}">
                <p>有效期至${card.expiration_date}</p>
                <c:if test="${card.status eq '2'}">
                    <div class="card-shadow"></div>
                    <div class="card-status">
                        <img src="${pageContext.request.contextPath}/images/card_used.png" alt="">
                    </div>
                </c:if>
            </c:if>
        </div>
    </c:forEach>
</div>
</div>
<div class="flex-box show-loading">
    <img src="${pageContext.request.contextPath}/images/loading.gif" alt="">
    <p>加载更多</p>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    $('.card-item').click(function () {
        var json = {}
        json.card_id = $(this).attr('data-id');
        json.card_name = $(this).attr('data-name');
        json.card_method = $(this).attr('data-method');
        json.card_headline = $(this).attr('data-headline');
        var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
        selectedinfo.integral = false
        selectedinfo.sendinfo = {}
        selectedinfo.sendinfo.card_method = 1
        selectedinfo.sendinfo.card_id = ''
        selectedinfo.sendinfo.card_name = ''
        selectedinfo.cardinfo = json
        localStorage.selectedinfo = JSON.stringify(selectedinfo)
        window.history.go(-1);
    })
</script>
</body>
</html>