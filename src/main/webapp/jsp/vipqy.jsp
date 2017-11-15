<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>

<body class="active-body">
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/${backUrl}">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>${title}
</div>
<div class="vipqy-box">
    ${info}
</div>
<script>
</script>
</body>
</html>