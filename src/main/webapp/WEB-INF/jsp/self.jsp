<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top"><a href="mine.jsp"><img src="images/icon_left.png" alt="" class="nav-toback"></a>个人资料</div>
<div class="flex-box self-container">
    <div class="self-con">
        <div class="flex-box self-item"><h4>姓名</h4><input type="text" value="用户名"></div>
        <div class="flex-box self-item"><h4>手机号码</h4><input type="text" value="1868868888"></div>
        <div class="flex-box self-item"><h4>微信</h4><input type="text" value="1868868888"></div>
        <div class="flex-box self-item"><h4>性别</h4><input type="text" value="用户名"></div>
        <div class="flex-box self-item"><h4>生日</h4></div>
        <div class="flex-box self-item"><h4>收货地址</h4></div>
        <div class="self-btn">
            <button>修改资料</button>
        </div>
    </div>
    <img src="images/icon_logo.png" alt="" class="flex1 self-bg">
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>