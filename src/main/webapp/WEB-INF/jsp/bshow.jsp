<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="bshow-back">
    <a href="${pageContext.request.contextPath}/${url}">
        <img src="${pageContext.request.contextPath}/images/bshow_left.png" alt="">
    </a>
</div>
<div class="swiper-container banner">
    <a href="">
        <img src="${info.image_url}" alt="">
    </a>
</div>
<div class="bshow-header">
    ${info.title}
</div>
<div class="bshow-con">
    ${info.content}
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>