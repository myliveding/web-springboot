<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="consumption-body">
<div class="nav-top nav-recharge-top">
    <a href="${pageContext.request.contextPath}/login/index">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>
    积分记录
</div>
<div class="consumption-header recharge-header">
    <h4>${integral}</h4>
    <p>当前积分</p>
</div>
<c:forEach var="it" items="${list}">
<c:choose>
<c:when test="${it.time eq '本月'}">
<div class="flex-box consumption-top recharge-top">
    </c:when>
    <c:otherwise>
    <div class="flex-box consumption-top consumption-center">
        </c:otherwise>
        </c:choose>
        <span>${it.time}</span>
        <div class="consumption-top-txt">
            <span>减少</span>${it.reduce}
            <span>增加</span>${it.increase}
    </div>
    </div>
    <c:forEach var="t" items="${it.record}">
    <div class="flex-box consumption-item recharge-item">
        <p>${t.create_time}</p>
        <c:choose>
            <c:when test="${t.type eq '返'}">
                <span class="consumption-status-Recharge">${t.type}</span>
            </c:when>
            <c:otherwise>
                <span>${t.type}</span>
            </c:otherwise>
        </c:choose>
        <div class="consumption-item-right">
                ${t.integral}
        </div>
    </div>
    </c:forEach>
    </c:forEach>
<div class="cb20"></div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>