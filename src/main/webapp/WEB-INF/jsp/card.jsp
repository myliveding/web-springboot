<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/login/index">
        <img src="images/icon_left.png" alt="" class="nav-toback">
    </a>
    我的卡券
</div>
<div class="sendcard-container">

    <ul>
        <c:forEach var="card" items="${cards}">
            <li>
                <div class="sendcard-item card-item">
                    <c:choose>
                        <c:when test="${card.status eq '1'}">
                            <c:choose>
                                <c:when test="${card.isOver eq '1'}">
                                    <img src="images/card_yellow.png" alt="">
                                </c:when>
                                <c:otherwise>
                                    <img src="images/card_green.png" alt="">
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <img src="images/card_purple.png" alt="">
                        </c:otherwise>
                    </c:choose>
                    <div class="flex-box sendcard-txt">
                        <img src="images/icon_logo_white.png" alt="">
                        <p>全场消费满${card.user_price}元<span>即可使用</span></p>
                        <div class="sendcard-number sendcard-txt-green">
                                ${card.price}
                            <span>折</span>
                        </div>
                    </div>
                    <c:choose>
                        <c:when test="${card.isOver eq '1'}">
                            <p>未使用</p>
                            <div class="card-shadow"></div>
                            <div class="card-status"><img src="images/card_timeout.png" alt=""></div>
                        </c:when>
                        <c:otherwise>
                            <p>有效期至${card.over_time}</p>
                            <div class="card-shadow"></div>
                            <div class="card-status"><img src="images/card_used.png" alt=""></div>
                        </c:otherwise>
                    </c:choose>

                </div>
            </li>
        </c:forEach>
    </ul>
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>