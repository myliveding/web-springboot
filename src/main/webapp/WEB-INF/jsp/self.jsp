<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top"><a href="${pageContext.request.contextPath}/login/userCenter"><img src="images/icon_left.png"
                                                                                        alt="" class="nav-toback"></a>个人资料
</div>
<div class="flex-box self-container">
    <div class="self-con">
        <div class="flex-box self-item">
            <h4>姓名</h4>
            <input class="name" type="text" value="${user.name}"></div>
        <div class="flex-box self-item">
            <h4>微信</h4>
            <input class="wechat" type="text" value="${user.wechat_num}">
        </div>
        <div class="flex-box self-item">
            <h4>性别</h4>
            <input class="sex" type="text"
            <c:choose>
            <c:when test="${user.gender eq '2'}">
                   value="女"
            </c:when>
            <c:otherwise>
                   value="男"
            </c:otherwise>
            </c:choose>
            >
        </div>
        <div class="flex-box self-item">
            <h4>生日</h4>
            <input class="birth" type="text" value="${user.birthday}">
        </div>
        <div class="flex-box self-item">
            <h4>收货地址</h4>
            <input class="address" type="text" value="${user.address}">
        </div>
        <div class="self-btn">
            <button onclick="update()">修改资料</button>
        </div>
    </div>
    <img src="images/icon_logo.png" alt="" class="flex1 self-bg">
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    function update() {
        var name = $('.name').val();
        var wechat = $('.wechat').val();
        var sex = $('.sex').val();
        var birth = $('.birth').val();
        var address = $('.address').val();
        $.ajax({
            'url': "${pageContext.request.contextPath}/login/savePerfectInfo",
            'type': 'post',
            'dataType': 'json',
            'data': {
                name: name,
                wechat: wechat,
                sex: sex,
                birth: birth,
                address: address
            },
            success: function success(d) {
                if (d.errcode == 0) {
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