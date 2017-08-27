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
</body>
</html>