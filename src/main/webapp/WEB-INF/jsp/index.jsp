<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body>
<div class="swiper-container banner">
    <div class="swiper-wrapper">
        <c:forEach var="banner" items="${banners}">
            <div class="swiper-slide">
                <a><img src="${banner.image_url}" alt=""></a>
            </div>
        </c:forEach>
    </div>
    <div class="swiper-pagination"></div>
</div>
<div class="index-cb1"></div>
<div class="flex-box index-info">
    <div class="flex-box flex1 info-txt">
        <c:choose>
            <c:when test="${user.head_url eq 'null' || user.head_url eq ''}">
                <img src="${pageContext.request.contextPath}/images/my_cover.jpg" alt="" class="my-cover">
            </c:when>
            <c:otherwise>
                <img src="${user.head_url}" alt="" class="my-cover">
            </c:otherwise>
        </c:choose>
        <div class="flex-box flex1 myinfo">
            <div class="myinfo-t">${user.name}
                <c:choose>
                    <c:when test="${user.gender eq '2'}">
                        <img src="${pageContext.request.contextPath}/images/icon_woman.png" alt="">
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/images/icon_man.png" alt="">
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${user.member_type eq '1'}">
                        <span>普通用户</span>
                    </c:when>
                    <c:otherwise>
                        <span>臻品会员</span>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="flex-box myinfo-b">
                <div class="myinfo-btxt">ID:6618</div>
                <div class="myinfo-btxt">微信：${user.wechat_num}</div>
            </div>
        </div>
    </div>
    <c:if test="${user.member_type eq '1'}">
        <a class="info-vip" href="${pageContext.request.contextPath}/login/gotoVip">升级臻品会员></a>
    </c:if>
</div>
<div class="flex-box index-pay">
    <div class="flex-box pay-item" onclick="scan()">
        <img src="${pageContext.request.contextPath}/images/icon_scancode.png">
        <span>扫码付款</span>
    </div>
</div>
<div class="index-nav">
    <div class="flex-box index-nav-box">
        <div class="flex-box nav-item"><a href="${pageContext.request.contextPath}/login/gotoVip"><img
                src="${pageContext.request.contextPath}/images/icon_nav1.png" alt="">
            <p>会员充值</p></a></div>
        <div class="flex-box nav-item"><a href="${pageContext.request.contextPath}/login/gotoConsumptionRecords"><img
                src="${pageContext.request.contextPath}/images/icon_nav2.png" alt="">
            <p>消费记录</p></a></div>
        <div class="flex-box nav-item"><a href="${pageContext.request.contextPath}/login/gotoRecharge"><img
                src="${pageContext.request.contextPath}/images/icon_nav3.png" alt="">
            <p>积分记录</p></a></div>
    </div>
    <div class="flex-box index-nav-box">
        <div class="flex-box nav-item"><a href="${pageContext.request.contextPath}/login/gotoTeam"><img
                src="${pageContext.request.contextPath}/images/icon_nav4.png" alt="">
            <p>团队</p></a></div>
        <div class="flex-box nav-item"><a href="${pageContext.request.contextPath}/login/productCates"><img
                src="${pageContext.request.contextPath}/images/icon_nav5.png" alt="">
            <p>臻品推广</p><span>(兼职赚钱)</span></a></div>
        <div class="flex-box nav-item">
            <a href="${pageContext.request.contextPath}/login/gotoDiscountCard">
                <img src="${pageContext.request.contextPath}/images/icon_nav6.png" alt="">
                <p>卡券赠送</p>
                <c:if test="${user.card_count > 0}">
                    <i>${user.card_count}</i>
                </c:if>
            </a>
        </div>
    </div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    //保存按钮的事件
    function scan() {
        window.location.href = "${pageContext.request.contextPath}/login/gotoCode";
    };

    $(document).ready(function () {
        $('.home img').attr("src", "${pageContext.request.contextPath}/images/icon_homed.png");
        $('.home p').attr("class", "tabar-hover");
    });
</script>
</body>
</html>