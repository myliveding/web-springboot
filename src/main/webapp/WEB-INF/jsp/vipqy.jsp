<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<div class="nav-top">
    <a href="${backUrl}">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
        <br>
    </a>
</div>
<body class="active-body">
<div class="nav-top">${info.title}</div>
<div class="vipqy-box">
    ${info.service_agreement}
</div>
</body>
</html>