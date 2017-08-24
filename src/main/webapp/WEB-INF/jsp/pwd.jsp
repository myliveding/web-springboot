<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top"><a href="mine.jsp"><img src="images/icon_left.png" alt="" class="nav-toback"></a>修改密码</div>
<div class="pwd-container">
    <div class="pwd-h5">重新设置您的登录密码</div>
    <div class="pwd-item pwd-bottom"><img src="images/icon_pwd.png" alt=""><input type="text" placeholder="新的登录密码">
    </div>
    <div class="pwd-item"><img src="images/icon_pwd.png" alt=""><input type="text" placeholder="确认新的登录密码"></div>
    <div class="self-btn pwd-btn">
        <button>确定</button>
    </div>
    <img src="images/icon_logo.png" alt="" class="pwd-bg">
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>