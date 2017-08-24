<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top"><a href="index.html"><img src="images/icon_left.png" alt="" class="nav-toback"></a>会员充值</div>
<div class="vip-header">
    <p>当前余额(元)</p>
    <h4>688.00</h4>
    <span>积分：0</span>
</div>
<div class="code-con vip-con">
    <div class="flex-box vip-item"><img src="images/icon_vip_logo.png" alt=""><span>399元</span>
        <p>中级VIP享特权说明</p>
        <div class="vip-item-opreate"><input type="radio" name="pay" id="middling"></div>
    </div>
    <div class="flex-box vip-item"><img src="images/icon_vip_logo.png" alt=""><span>699元</span>
        <p>高级VIP享特权说明</p>
        <div class="vip-item-opreate"><input type="radio" name="pay" id="high"></div>
    </div>
</div>
<div class="self-btn pwd-btn">
    <button>确认支付</button>
</div>
<img src="images/icon_logo.png" alt="" class="self-bg code-bg">
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>