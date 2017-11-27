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
                <p style="color:#FF0000;">${t.remark}</p>
                <input type="hidden" value="${t.id}">
                <div class="vip-item-opreate">
                    <input type="radio" name="pay" id="middling" onclick="updateDate(${t.id}, ${t.price})">
                </div>
            </div>
        </c:forEach>
    </ul>
</div>
<div class="self-btn pwd-btn">
    <button onclick="pay()">确认支付</button>
</div>
<img src="${pageContext.request.contextPath}/images/icon_logo.png" alt="" class="self-bg code-bg">
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    var id = 0;
    var price = 0;

    function updateDate(idww, priceww) {
        id = idww;
        price = priceww;
    }

    function isWeixnOpen() {
        var ua = navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == "micromessenger") {
            return true;
        } else {
            return false;
        }
    }

    //保存按钮的事件
    function pay() {
        if (id == 0 && price == 0) {
            return false;
        }

        if (!isWeixnOpen()) {
            alert('<div><div><img src="${pageContext.request.contextPath}/images/10.png"></div><div style="color:#2cca6f;">微信不支持在浏览器端的支付！</div><div style="color:#333;">可通过以下方式完成支付</div><div style="color:#999; text-align:left;">方式一：打开微信，关注“盛世欢唱”公众号，进入“我的>我的订单”中完成交易</div><div style="color:#999; text-align:left;">方式二：选择其他支付方式完成交易</div></div>');
            return false;
        } else {
            setTimeout(function () {
                window.location.href = "${pageContext.request.contextPath}/wechatPay/index?productId="
                    + id + "&price=" + price + "&type=1";
            }, 1000);
        }

        $.ajax({
            'url': "${pageContext.request.contextPath}/rest/couponsPaging",
            'type': 'post',
            'dataType': 'json',
            'data': {
                page: page,
            },
            success: function success(d) {
                if (d.status == 0) {
                } else {
                    alert(d.errmsg);
                }
            }
        });
    };

</script>
</body>
</html>