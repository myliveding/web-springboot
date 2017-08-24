<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">活动列表</div>
<div class="active-container">
    <ul>
        <c:forEach var="activity" items="${activitys}">
            <li>
                <div class="active-item">
                    <a href="${pageContext.request.contextPath}/login/${activity.id}">
                        <img src="${activity.image_url}" alt="">
                        <p>${activity.title}</p>
                    </a>
                </div>
            </li>
        </c:forEach>
    </ul>
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>