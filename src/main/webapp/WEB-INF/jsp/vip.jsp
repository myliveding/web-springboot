<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/login/index">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>会员充值
</div>
<div class="vip-header">
    <p>当前余额(元)</p>
    <h4>${member.balance}</h4>
    <span>积分：${member.integral}</span>
</div>
<div class="code-con vip-con">
    <ul>
        <c:forEach var="t" items="${list}">
            <div class="flex-box vip-item">
                <img src="${pageContext.request.contextPath}/images/icon_vip_logo.png" alt="">
                <span>${t.price}元</span>
                <p>${t.remark}</p>
                <input type="hidden" value="${t.id}">
                <div class="vip-item-opreate">
                    <input type="radio" name="pay" id="middling">
                </div>
            </div>
        </c:forEach>
    </ul>
</div>
<div class="self-btn pwd-btn">
    <button>确认支付</button>
</div>
<img src="${pageContext.request.contextPath}/images/icon_logo.png" alt="" class="self-bg code-bg">
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    //微信菜单功能
    wx.config({
        debug: true,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${appid}",// 必填，公众号的唯一标识
        timestamp: "${timestamp}", // 必填，生成签名的时间戳
        nonceStr: "${noncestr}", // 必填，生成签名的随机串
        signature: "${signature}",// 必填，签名
        jsApiList: [
            'checkJsApi',
            'chooseWXPay'
        ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    //保存按钮的事件
    $$('.self-btn .pwd-btn').on('click', function () {
        wx.chooseWXPay({
            timestamp: 0, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
            nonceStr: '', // 支付签名随机串，不长于 32 位
            package: '', // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
            signType: '', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
            paySign: '', // 支付签名
            success: function (res) {
                // 支付成功后的回调函数
            }
        });
    });

    wx.ready();
</script>
</body>
</html>