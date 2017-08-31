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
                <a href="${pageContext.request.contextPath}/login/gotoCode?id=${banner.id}">
                    <img src="${banner.image_url}" alt="">
                </a>
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
                        <img src="${pageContext.request.contextPath}/images/icon_woman.jpg" alt="" class="my-cover">
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
                <div class="myinfo-btxt">微信:${user.wechat_num}</div>
            </div>
        </div>
    </div>
    <c:if test="${user.member_type} eq '1'">
        <div class="info-vip">升级臻品会员></div>
    </c:if>
</div>
<div class="flex-box index-pay">
    <div class="flex-box pay-item">
        <img src="${pageContext.request.contextPath}/images/icon_scancode.png">
        <span>扫码付款</span>
    </div>
    <div class="flex-box pay-item">
        <img src="${pageContext.request.contextPath}/images/icon_pay.png" alt="">
        <span>臻品支付</span>
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
<div class="index-vip">
    <div class="index-vip-top"></div>
    <div class="index-vip-box">
        <button>￥299.00升级臻品会员</button>
    </div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    //微信菜单功能
    wx.config({
        debug: false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${appid}",// 必填，公众号的唯一标识
        timestamp: "${timestamp}", // 必填，生成签名的时间戳
        nonceStr: "${noncestr}", // 必填，生成签名的随机串
        signature: "${signature}",// 必填，签名
        jsApiList: [
            'checkJsApi',
            'scanQRCode'
        ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    wx.ready(function () {
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
        wx.scanQRCode({
            needResult: 0, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
            scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
            success: function (res) {
                var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
            }
        });
    });
</script>
</body>
</html>