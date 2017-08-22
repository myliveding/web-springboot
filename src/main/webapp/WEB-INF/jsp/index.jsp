<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body>
<div class="swiper-container banner">
    <div class="swiper-wrapper">
        <ul>
            <c:forEach var="banner" items="${banners}">
                <li>
                    <div class="swiper-slide">
                        <a href="${banner.id}code.html"><img src="${banner.image_url}" alt=""></a>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
    <div class="swiper-pagination"></div>
</div>
<div class="index-cb1"></div>
<div class="flex-box index-info">
    <div class="flex-box flex1 info-txt">
        <img src="images/my_cover.jpg" alt="" class="my-cover">
        <div class="flex-box flex1 myinfo">
            <div class="myinfo-t">姜哲<img src="images/icon_man.png" alt=""><span>普通用户</span></div>
            <div class="flex-box myinfo-b">
                <div class="myinfo-btxt">ID:6618</div>
                <div class="myinfo-btxt">微信:661868</div>
            </div>
        </div>
    </div>
    <div class="info-vip">升级臻品会员></div>
</div>
<div class="flex-box index-pay">
    <div class="flex-box pay-item"><img src="images/icon_scancode.png"><span>扫码付款</span></div>
    <div class="flex-box pay-item"><img src="images/icon_pay.png" alt=""><span>臻品支付</span></div>
</div>
<div class="index-nav">
    <div class="flex-box index-nav-box">
        <div class="flex-box nav-item"><a href="vip.html"><img src="images/icon_nav1.png" alt="">
            <p>会员充值</p></a></div>
        <div class="flex-box nav-item"><a href="consumption.html"><img src="images/icon_nav2.png" alt="">
            <p>消费记录</p></a></div>
        <div class="flex-box nav-item"><a href="recharge.html"><img src="images/icon_nav3.png" alt="">
            <p>积分记录</p></a></div>
    </div>
    <div class="flex-box index-nav-box">
        <div class="flex-box nav-item"><a href="team.html"><img src="images/icon_nav4.png" alt="">
            <p>团队</p></a></div>
        <div class="flex-box nav-item"><a href="brand.html"><img src="images/icon_nav5.png" alt="">
            <p>臻品推广</p><span>(兼职赚钱)</span></a></div>
        <div class="flex-box nav-item"><a href="sendcard.html"><img src="images/icon_nav6.png" alt="">
            <p>卡券赠送</p><i>16</i></a></div>
    </div>
</div>
<div class="index-vip">
    <div class="index-vip-top"></div>
    <div class="index-vip-box">
        <button>￥299.00升级臻品会员</button>
    </div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>