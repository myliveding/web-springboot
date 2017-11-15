<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="login-body">
<header class="login-header">
    <a href="mine.jsp"><img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback"></a>
    <img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="" class="login-logo">
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
            <div class="flex-box login-item">
                <img src="${pageContext.request.contextPath}/images/icon_login_phone.png" alt="">
                <input class="r_tel" type="text" placeholder="请输入手机号码">
            </div>
            <div class="flex-box login-item">
                <img src="${pageContext.request.contextPath}/images/icon_login_four.png" alt="">
                <input class="r_code" type="text" placeholder="请输入四位验证码">
                <button class="resigt_yzm">发送验证码</button>
            </div>
            <div class="flex-box login-item">
                <img src="${pageContext.request.contextPath}/images/icon_login_pwd.png" alt="">
                <input class="r_pdw" type="password" placeholder="请输入6-20位密码">
            </div>
            <div class="flex-box login-item">
                <img src="${pageContext.request.contextPath}/images/icon_login_name.png" alt="">
                <input class="r_name" type="text" placeholder="请填写您推广人">
            </div>
            <div class="self-btn pwd-btn login-btn">
                <button onclick="register()">下一步</button>
            </div>
            <div class="login-agree">
                <input type="checkbox" name="" id="service">同意
                <a href="${pageContext.request.contextPath}/login/systemInfo?type=2">《臻品珠宝网络服务协议》</a>
            </div>
        </div>

        <div role="tabpanel" class="login-pane" id="profile">
            <div class="flex-box login-item regist-item">
                <img src="${pageContext.request.contextPath}/images/icon_login_phone.png" alt="">
                <input class="tel" type="text" placeholder="请输入手机号码"></div>
            <div class="flex-box login-item regist-item-pwd regist-item-active">
                <img src="${pageContext.request.contextPath}/images/icon_login_pwd.png" alt="">
                <input class="pwd" type="password" placeholder="请输入6-20位密码">
            </div>
            <div class="flex-box login-item regist-item-yzm">
                <img src="${pageContext.request.contextPath}/images/icon_login_four.png" alt="">
                <input class="code" type="text" placeholder="请输入四位验证码">
                <button class="login-yzm">发送验证码</button>
            </div>
            <div class="self-btn pwd-btn login-btn">
                <img src="${pageContext.request.contextPath}/images/icon_login_pwd.png" class="login-btn-img" alt="">
                <button onclick="login()">登录</button>
            </div>
            <div class="regist-operate">
                <span class="regist-yzm regist-btn-active">验证码登录</span>
                <span class="regist-pwd">密码登录</span>
                <%--<span>忘记密码</span>--%>
            </div>
        </div>
    </div>
</div>
<jsp:include page="foot.jsp" flush="true"/>

<script>
    var rsended = false
    var sended = false
    //发送验证码
    $(".resigt_yzm").click(function () {
        var tel = $('.r_tel').val();
        if (rsended) {
            return;
        } else {
            sms(tel, $(this), 1);
        }
    });
    $(".login-yzm").click(function () {
        var tel = $('.tel').val();
        if (sended) {
            return;
        } else {
            sms(tel, $(this), 2);
        }
    });

    function sms(tel, obj, theindex) {
        if (null == tel || '' == tel) {
            alert("请填写手机号码");
            return false;
        }
        $.ajax({
            'url': "${pageContext.request.contextPath}/free/sendSms",
            'type': 'post',
            'dataType': 'json',
            'data': {
                mobile: tel,
            },
            success: function success(d) {
                if (d.status == 0) {
                    setTime(obj, 60, theindex);
                    alert("验证码发送成功");
                    if (theindex == 1) {
                        rsended = true
                    } else {
                        sended = true
                    }
                } else {
                    alert(d.errmsg);
                    return false;
                }
            }
        });
    }

    function setTime(obj, time, theindex) {
        setTimeout(function () {
            if (time > 0) {
                time = time - 1;
                obj.text(time + 's后重新发送');
                setTime(obj, time);
            } else {
                obj.text('重新发送');
                if (theindex == 1) {
                    rsended = false
                } else {
                    sended = false
                }
            }
        }, 1000)
    }

    var tel = "${tel}";
    if (tel != null && "" != tel) {
        $('.r_name').val(tel);
        $('.r_name').attr("readOnly", true);
    }

    var backUrl = "${backUrl}";

    //注册
    function register() {
        //推广人
        var name = $('.r_name').val();

        var mobile = $('.r_tel').val();
        var password = $('.r_pdw').val();
        if (password.length < 6 || password.length > 20) {
            alert("请输入6到20位密码");
            return false;
        }
        var code = $('.r_code').val();


        if ($('#service').is(':checked')) {
        } else {
            alert("请勾选服务协议");
            return false;
        }

        $.ajax({
            'url': "${pageContext.request.contextPath}/free/register",
            'type': 'post',
            'dataType': 'json',
            'data': {
                name: name,
                mobile: mobile,
                password: password,
                code: code
            },
            success: function success(d) {
                if (d.status == 0) {
                    window.location.href = "${pageContext.request.contextPath}/login/perfectInfo?backUrl=" + backUrl;
                } else {
                    alert(d.errmsg);
                }
            }
        });
    }

    //登录接口
    function login() {
        var tel = $('.tel').val();
        var pwd = $('.pwd').val();
        var code = $('.code').val();
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
                    if (backUrl) {
                        window.location.href = "${pageContext.request.contextPath}" + backUrl;
                    }
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