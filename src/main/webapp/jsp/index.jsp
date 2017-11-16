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
                <a><img src="${banner.image_url}" alt="" width=100% height=150></a>
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
                <img src="${pageContext.request.contextPath}/images/default.jpg" alt="" class="my-cover">
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
                <div class="myinfo-btxt">ID：${user.mobile}</div>
                <c:if test="${user.wechat_num ne ''}">
                    <div class="myinfo-btxt">微信：${user.wechat_num}</div>
                </c:if>
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
        <div class="flex-box nav-item"><a href="javascript:;" onclick="showmask()"><img
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
<div style="text-align:center">
    <img src="${pageContext.request.contextPath}/images/icon_logo.png" alt="" class="flex1 self-bg">
</div>
<jsp:include page="foot.jsp" flush="true"/>
<div class="mask"
     style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.7);z-index:99999;display:none">
    <img src="${pageContext.request.contextPath}/images/pointer.png" alt="" style="position:absolute;top:0;right:0">
    <p style="color:#f4f4f4;font-size:14px;padding-top:120px;width:80%;margin:0 auto">
        请点击右上角的“...”进行分享<br>
        请点击右上角的“...”进行分享
    </p>

</div>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    localStorage.moneyinfo = ''
    //微信菜单功能
    wx.config({
        debug: false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${appid}",// 必填，公众号的唯一标识
        timestamp: "${timestamp}", // 必填，生成签名的时间戳
        nonceStr: "${noncestr}", // 必填，生成签名的随机串
        signature: "${signature}",// 必填，签名
        jsApiList: [
            'checkJsApi',
            'onMenuShareTimeline',
            'onMenuShareAppMessage'
        ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });
    //https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzI5OTEwODM2Ng==&scene=110#wechat_redirect
    var shareUrl = "${shareUrl}";
    wx.ready(function () {
        var wxData = {
            title: '臻品vip折扣卡',
            desc: '臻品vip折扣卡等你来拿',
            link: shareUrl + "/login/productCates",
            imgUrl: shareUrl + '/images/logo.png', // 分享图标
        };

        //监听分享给朋友
        wx.onMenuShareAppMessage(wxData);
        //监听分享到朋友圈”
        wx.onMenuShareTimeline(wxData);
    });

    //保存按钮的事件
    function scan() {
        window.location.href = "${pageContext.request.contextPath}/login/gotoCode";
    };
    //遮罩
    showmask = function () {
        $('.mask').show()
    };
    $('.mask').click(function () {
        $(this).hide()
    })

    $(document).ready(function () {
        $('.home img').attr("src", "${pageContext.request.contextPath}/images/icon_homed.png");
        $('.home p').attr("class", "tabar-hover");
    });
</script>
</body>
</html>