<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top"><a href="${pageContext.request.contextPath}/login/index"><img src="images/icon_left.png" alt=""
                                                                                   class="nav-toback"></a>卡券赠送
</div>
<div class="sendcard-container">
    <ul>
        <c:forEach var="card" items="${cards}">
            <li>

                <div class="sendcard-item">
                    <c:choose>
                        <c:when test="${card.status eq '2'}">
                            <img src="images/sendcard_green.png" alt="">
                        </c:when>
                        <c:when test="${card.status eq '1'}">
                            <img src="images/sendcard_yellow.png" alt="">
                        </c:when>
                        <c:otherwise>
                            <img src="images/sendcard_purple.png" alt="">
                        </c:otherwise>
                    </c:choose>
                    <div class="flex-box sendcard-txt">
                        <img src="images/icon_logo_white.png" alt="">
                        <p>全场消费满${card.user_price}元<span>即可使用</span></p>
                        <div class="sendcard-number sendcard-txt-green">${card.discount}<span>折</span></div>
                    </div>
                    <c:if test="${card.status eq '2'}">
                        <a onclick="send()" class="sendcard-status-green">赠送</a>
                    </c:if>
                </div>
            </li>
        </c:forEach>
    </ul>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    function send() {

    }
</script>
</body>
</html>