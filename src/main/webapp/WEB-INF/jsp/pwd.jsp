<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/login/userCenter">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>
    修改密码
</div>
<div class="pwd-container">
    <div class="pwd-h5">
        重新设置您的登录密码
    </div>
    <div class="pwd-item pwd-bottom">
        <img src="${pageContext.request.contextPath}/images/icon_pwd.png" alt="">
        <input class="first" type="password" placeholder="新的登录密码">
    </div>
    <div class="pwd-item">
        <img src="${pageContext.request.contextPath}/images/icon_pwd.png" alt="">
        <input class="secondary" type="password" placeholder="确认新的登录密码">
    </div>
    <div class="self-btn pwd-btn">
        <button onclick="resetPwd()">确定</button>
    </div>
    <img src="${pageContext.request.contextPath}/images/icon_logo.png" alt="" class="pwd-bg">
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    function resetPwd() {
        var first = $('.first').val();
        var secondary = $('.secondary').val();
        $.ajax({
            'url': "${pageContext.request.contextPath}/rest/resetPassword",
            'type': 'post',
            'dataType': 'json',
            'data': {
                first: first,
                secondary: secondary
            },
            success: function success(d) {
                if (d.status == 0) {
                    window.location.reload();
                } else {
                    alert(d.errmsg);
                }
            }
        });
    }
</script>
</body>
</html>