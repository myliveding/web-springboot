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
    <input type="text">
</div>
<div class="code-con">
    <div class="flex-box code-item code-item-balance">
        <img src="${pageContext.request.contextPath}/images/icon_code_balance.png" alt="">
        <div class="code-item-txt">余额：<span>￥1888.00</span></div>
        <div class="code-item-opreate"><input type="radio" name="pay" id="balance">
        </div>
    </div>
    <div class="flex-box code-item code-item-integral">
        <img src="${pageContext.request.contextPath}/images/icon_nav3.png" alt="">
        <div class="code-item-txt">
            积分：<span>18888</span>
        </div>
        <div class="code-item-opreate">
            <input type="radio" name="pay" id="">
        </div>
    </div>
    <div class="flex-box code-item code-item-discount">
        <img src="${pageContext.request.contextPath}/images/icon_code_discount.png" alt="">
        <div class="code-item-txt">打折卡</div>
        <div class="code-item-opreate">
            <img src="${pageContext.request.contextPath}/images/icon_code_right.png" alt="">
        </div>
    </div>
    <div class="flex-box code-item code-item-ticket">
        <img src="${pageContext.request.contextPath}/images/icon_mine_ticket.png" alt="">
        <div class="code-item-txt">优惠券<span></span></div>
        <div class="code-item-opreate">
            <img src="${pageContext.request.contextPath}/images/icon_code_right.png" alt="">
        </div>
    </div>
    <div class="code-number">共支付：<p><span>￥</span>299.00</p></div>
</div>
<div class="self-btn pwd-btn">
    <button>确认支付</button>
</div>
<img src="${pageContext.request.contextPath}/images/icon_logo.png" alt="" class="self-bg code-bg">
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>