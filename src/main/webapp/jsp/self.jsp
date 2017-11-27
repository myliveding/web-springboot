<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/login/userCenter">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>个人资料
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
            <div class="flex self-radio">
                <input type="radio" name="sex1" id="sex1" value="1">男
                <input type="radio" name="sex2" id="sex2" value="2">女
            </div>
        </div>
        <div class="flex-box self-item">
            <h4>生日</h4>
            <input class="birth" name="" id="" type="date" placeholder="生日格式:1990-01-05"
                   value="${user.birthday}" class="start-time-day">
        </div>
        <div class="flex-box self-item">
            <h4>收货地址</h4>
            <input class="address" type="text" value="${user.address}">
        </div>
        <div class="self-btn">
            <button onclick="update()">修改资料</button>
        </div>
    </div>
    <img src="${pageContext.request.contextPath}/images/icon_logo.png" alt="" class="flex1 self-bg">
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    var sex = ${user.gender};
    $(document).ready(function () {
        $("#sex" + sex).attr("checked", "checked");
    });

    $(function () {
        $("#sex1").click(function () {
            $("#sex1").attr("checked", "checked");
            $("#sex2").removeAttr("checked");
            sex = $("#sex1").val();
        });
        $("#sex2").click(function () {
            $("#sex2").attr("checked", "checked");
            $("#sex1").removeAttr("checked");
            sex = $("#sex2").val();
        });
    });

    function update() {
        var name = $('.name').val();
        var wechat = $('.wechat').val();
        var birth = $('.birth').val();
        var address = $('.address').val();
        $.ajax({
            'url': "${pageContext.request.contextPath}/rest/savePerfectInfo",
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
                if (d.status == 0) {
                    //window.location.reload();
                    window.location.href = "${pageContext.request.contextPath}/login/userCenter";
                } else {
                    alert(d.errmsg);
                }
            }
        });
    }
</script>
</body>
</html>