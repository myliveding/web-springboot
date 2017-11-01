<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/login/index">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>扫码付款
</div>
<div class="flex-box code-payment">
    <span>金额</span>
    <input type="text" id="allmoney">
</div>
<div class="code-con">
    <div class="flex-box code-item code-item-balance">
        <img src="${pageContext.request.contextPath}/images/icon_code_balance.png" alt="">
        <div class="code-item-txt">余额：<span>￥${info.balance}</span></div>
        <div class="code-item-opreate"><input type="radio" name="pay" id="balance" value="1">
        </div>
    </div>
    <div class="flex-box code-item code-item-integral">
        <img src="${pageContext.request.contextPath}/images/icon_nav3.png" alt="">
        <div class="code-item-txt">
            积分：<span>${info.integral}</span>
        </div>
        <div class="code-item-opreate">
            <input type="radio" name="pay" id="integral" value="2">
        </div>
    </div>
    <div class="flex-box code-item code-item-ticket">
        <img src="${pageContext.request.contextPath}/images/icon_mine_ticket.png" alt="">
        <div class="code-item-txt">优惠券<span style="float:right;display:inline-block;padding-right:15px"
                                            id="showinfo"></span></div>
        <div class="code-item-opreate" id="benifit" onclick="tocard()">
            <img src="${pageContext.request.contextPath}/images/icon_code_right.png" alt="">
        </div>
    </div>
    <div class="code-info">
        <p>总金额￥1000</p>
        <p>-余额￥100</p>
        <p>-积分100</p>
        <p>-优惠￥100</p>
    </div>
    <div class="code-number">共支付：<p><span>￥</span>299.00</p></div>
    <div class="code-number">共支付：<p><span>￥</span>299.00</p></div>
</div>
<div class="self-btn pwd-btn">
    <button>确认支付</button>
</div>
<img src="${pageContext.request.contextPath}/images/icon_logo.png" alt="" class="self-bg code-bg">
<jsp:include page="foot.jsp" flush="true"/>
<script>
    function tocard() {
        var checkindex = $("input[name='pay']:checked").val()
        var allmoney = $('#allmoney').val()
        var moneyinfo
        if (localStorage.moneyinfo) {
            moneyinfo = localStorage.moneyinfo
        } else {
            moneyinfo = {}
        }
        moneyinfo.checkindex = checkindex
        moneyinfo.allmoney = allmoney
        localStorage.moneyinfo = moneyinfo
        window.location.href = "${pageContext.request.contextPath}/login/gotoCouponsCard"
    }

    function getStorage() {
        if (localStorage.moneyinfo) {
            var moneyinfo = localStorage.moneyinfo
            $('#allmoney').val(moneyinfo.allmoney)
            if (checkindex == 1) {
                $('#balance').checked
            } else {
                $('#integral').checked
            }
            $('#showinfo').text(moneyinfo.benifitinfo)
        }
    }

    getStorage();

    var payAmt = 0;
    function isWeixnOpen() {
        var ua = navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == "micromessenger") {
            return true;
        } else {
            return false;
        }
    }

    //保存按钮的事件
    $('.self-btn .pwd-btn').on('click', function () {
        if (payAmt > 0) {
            if (!isWeixnOpen()) {
                alert('<div><div><img src="${pageContext.request.contextPath}/images/10.png"></div><div style="color:#2cca6f;">微信不支持在浏览器端的支付！</div><div style="color:#333;">可通过以下方式完成支付</div><div style="color:#999; text-align:left;">方式一：打开微信，关注“盛世欢唱”公众号，进入“我的>我的订单”中完成交易</div><div style="color:#999; text-align:left;">方式二：选择其他支付方式完成交易</div></div>');
                return false;
            } else {
                setTimeout(function () {
                    window.location.href = "${pageContext.request.contextPath}/wechatPay/index?productId=0"
                        + "&price=" + payAmt + "&type=2";
                }, 1000);
            }
        } else {
            //用户不需要付款

        }
    });

</script>
</body>
</html>