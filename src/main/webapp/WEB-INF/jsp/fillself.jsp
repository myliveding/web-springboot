<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="fillself-body">
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/login/userCenter">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>填写个人资料
</div>
<div class="fillself-container">
    <div class="flex-box fillself-item">
        <span>姓名</span>
        <input class="name" type="text" placeholder="请填写您的真实姓名">
    </div>
    <div class="flex-box fillself-item">
        <span>性别</span>
        <input class="sex" type="text" placeholder="请填写您的性别">
    </div>
    <div class="flex-box fillself-item">
        <span>生日</span>
        <input class="birth" type="text" placeholder="请填写您的生日(2010-10-10)">
    </div>
    <div class="flex-box fillself-item">
        <span>微信号</span>
        <input class="wechat" type="text" placeholder="请填写您的微信号">
    </div>
    <div class="flex-box fillself-item">
        <span>收货地址</span>
        <input class="adress" type="text" placeholder="请填写您的收货地址">
    </div>
    <div class="fillself-btn">
        <button onclick="save()">确定</button>
    </div>
    <div class="fillself-skip"><a href="${pageContext.request.contextPath}/login/index">跳过此步骤</a></div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    function save() {
        var name = $('.name').val();
        var sex = $('.sex').val();
        var birth = $('.birth').val();
        var wechat = $('.wechat').val();
        var adress = $('.adress').val();
        $.ajax({
            'url': "${pageContext.request.contextPath}/login/savePerfectInfo",
            'type': 'post',
            'dataType': 'json',
            'data': {
                name: name,
                sex: sex,
                birth: birth,
                wechat: wechat,
                adress: adress
            },
            success: function success(d) {
                if (d.status == 0) {
                    window.location.href = "${pageContext.request.contextPath}/login/index";
                } else {
                    alert(d.errmsg);
                }
            }
        });
    }
</script>
</body>
</html>