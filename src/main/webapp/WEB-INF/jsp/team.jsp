<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top"><a href="${pageContext.request.contextPath}/login/index"><img src="images/icon_left.png" alt=""
                                                                                   class="nav-toback"></a>团队
</div>
<div class="team-container">
    <h4 class="team-h4">臻品团队</h4>
    <h5 class="team-h5"><img src="images/icon_team_logo.png" alt=""></h5>
    <div class="team-triangle"><img src="images/icon_team_triangle.png" alt=""></div>
    <div class="flex-box team-con">
        <c:forEach var="team" items="${teams}">
            <div class="team-item">
                <img src="${team.head_url}" alt="">
                <p>用户名</p>
                <p>${team.name}</p>
                <span>微信号</span><span>${team.wechat_num}</span>
            </div>
        </c:forEach>
    </div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>