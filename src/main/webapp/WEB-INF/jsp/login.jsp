<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="login-body">
<header class="login-header">
    <a href="mine.html"><img src="images/icon_left.png" alt="" class="nav-toback"></a>
    <img src="images/icon_logo_white.png" alt="" class="login-logo">
</header>
<div class="login-container">
    <ul class="nav nav-tabs login-tab" role="tablist">
        <li role="presentation" class="active" id="tab-regist">
            <a href="#home" aria-controls="home" role="tab" data-toggle="tab">注册</a></li>
        <li role="presentation" id="tab-login">
            <a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">登录</a>
        </li>
    </ul>
    <div class="login-con">
        <div role="tabpanel" class="login-pane active" id="home">
            <div class="flex-box login-item"><img src="images/icon_login_name.png" alt="">
                <input class="r_name" type="text" placeholder="请填写您的真实姓名">
            </div>
            <div class="flex-box login-item"><img src="images/icon_login_birth.png" alt="">
                <input class="r_birth" type="text" placeholder="请填写您的生日(2010-10-10)">
            </div>
            <div class="flex-box login-item"><img src="images/icon_login_phone.png" alt="">
                <input class="r_tel" type="text" placeholder="请输入手机号码">
            </div>
            <div class="flex-box login-item"><img src="images/icon_login_four.png" alt="">
                <input class="r_code" type="text" placeholder="请输入四位验证码">
                <button class="login-yzm" onclick="sendSms()">发送验证码</button>
            </div>
            <div class="flex-box login-item">
                <img src="images/icon_login_pwd.png" alt="">
                <input class="r_pdw" type="text" placeholder="请输入6-20位密码">
            </div>
            <div class="self-btn pwd-btn login-btn">
                <a href="${pageContext.request.contextPath}/login/perfectInfo">下一步</a>
            </div>
            <div class="login-agree">
                <input type="checkbox" name="" id="">同意
                <a href="">《臻品珠宝网络服务协议》</a>
            </div>
        </div>

        <div role="tabpanel" class="login-pane" id="profile">
            <div class="flex-box login-item regist-item">
                <img src="images/icon_login_phone.png" alt="">
                <input class="tel" type="text" placeholder="请输入手机号码"></div>
            <div class="flex-box login-item regist-item-pwd regist-item-active">
                <img src="images/icon_login_pwd.png" alt="">
                <input class="pwd" type="text" placeholder="请输入6-20位密码">
            </div>
            <div class="flex-box login-item regist-item-yzm"><img src="images/icon_login_four.png" alt="">
                <input class="code" type="text" placeholder="请输入四位验证码">
                <button class="login-yzm" onclick="sendSms()">发送验证码</button>
            </div>
            <div class="self-btn pwd-btn login-btn">
                <img src="images/icon_login_pwd.png" class="login-btn-img" alt="">
                <button onclick="login()">登录</button>
            </div>
            <div class="regist-operate">
                <span class="regist-yzm regist-btn-active">验证码登录</span>
                <span class="regist-pwd">密码登录</span>
                <span>忘记密码</span>
            </div>
        </div>
    </div>
</div>
<jsp:include page="foot.jsp" flush="true"/>

<script>
    //发送验证码
    function rSendSms() {
        var tel = $('.tabpanel .r_tel').val();
        sms(tel);
    }
    //发送验证码
    function sendSms() {
        var tel = $('.login-pane .tel').val();
        sms(tel);
    }
    function sms(tel) {
        $.ajax({
            'url': "${pageContext.request.contextPath}/free/sendSms",
            'type': 'post',
            'dataType': 'json',
            'data': {
                mobile: tel,
            },
            success: function success(d) {
                alert(d.errmsg);
            }
        });
    }

    //注册
    function register() {
        var name = $('.login-pane .r_name').val();
        var birth = $('.login-pane .r_birth').val();
        var mobile = $('.login-pane .r_tel').val();
        var password = $('.login-pane .r_pdw').val();
        var code = $('.login-pane .r_code').val();
        $.ajax({
            'url': "${pageContext.request.contextPath}/free/sendSms",
            'type': 'post',
            'dataType': 'json',
            'data': {
                name: name,
                birth: birth,
                mobile: mobile,
                password: password,
                code: code
            },
            success: function success(d) {
                if (d.errcode == 0) {
                    window.location.href = "${pageContext.request.contextPath}/login/perfectInfo";
                } else {
                    alert(d.errmsg);
                }
            }
        });
    }

    //登录接口
    function login() {
        var tel = $('.login-pane .tel').val();
        var pwd = $('.login-pane .pwd').val();
        var code = $('.login-pane .code').val();
        $.ajax({
            'url': "${pageContext.request.contextPath}/free/login",
            'type': 'post',
            'dataType': 'json',
            'data': {
                mobile: tel,
                password: pwd,
                code: code
            },
            success: function success(d) {
                if (d.status == 0) {
                    //进入下一步
                    window.location.href = "${pageContext.request.contextPath}/login/index";
                } else {
                    alert(d.errmsg);
                    return;
                }
            }
        });
    }
</script>
</body>
</html>