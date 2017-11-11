<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/login/index">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>
    卡券赠送
</div>
<div class="sendcard-container">
    <c:forEach var="card" items="${cards}">
        <div class="sendcard-item" data-id="${card.id}" data-name="${card.discount}折"
             data-method="${card.discount}/100">
            <input type="hidden" class="room_type_id" value="${card.id}"/>
            <img src="${pageContext.request.contextPath}/images/sendcard_green.png" alt="">
            <div class="flex-box sendcard-txt">
                <img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">
                <p>全场消费满${card.user_price}元<span>即可使用</span></p>
                <div class="sendcard-number sendcard-txt-green">${card.discount}<span>折</span></div>
            </div>
            <c:if test="${card.status eq '1'}">
                <a class="sendcard-status-green" onclick="send(${card.id})">赠送</a>
            </c:if>
        </div>
    </c:forEach>
</div>
<div class="flex-box show-loading">
    <img src="${pageContext.request.contextPath}/images/loading.gif" alt="">
    <p>加载更多</p>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    var page = 2;
    $(document).ready(function () {
        $(window).scroll(function () {
            if ($(document).scrollTop() <= 0) {
                //alert("滚动条已经到达顶部");
            }
            if ($(document).scrollTop() >= $(document).height() - $(window).height()) {
                $(".show-loading").show();
                $.ajax({
                    'url': "${pageContext.request.contextPath}/rest/discountPaging",
                    'type': 'post',
                    'dataType': 'json',
                    'data': {
                        page: page,
                    },
                    success: function success(d) {
                        if (d.status == 0) {
                            if (d.data.length > 0) {
                                page++;
                                var html = ''
                                for (var i = 0; i < d.data.length - 1; i++) {
                                    html += '<div class="sendcard-item" data-id="' + d.data[i].id + '" data-name="' +
                                        d.data[i].discount + '折" data-method="' + d.data[i].discount / 100 + '">'
                                    html += '<img src="${pageContext.request.contextPath}/images/sendcard_green.png" alt="">'
                                    html += '<div class="flex-box sendcard-txt">'
                                    html += '<img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">'
                                    html += '<p>全场消费满' + d.data[i].id + '元<span>即可使用</span></p>'
                                    html += '<div class="sendcard-number sendcard-txt-green">' + d.data[i].discount + '<span>折</span>'
                                    html += '</div>'
                                    if (d.data[i].status = 1) {
                                        html += '<a class="sendcard-status-green" onclick="send(d.data[i].id)">赠送</a>'
                                    }
                                    html += '</div>'
                                }
                                $(".sendcard-container").append(html);
                            } else {
                                //alert("没有更多了");
                            }
                        } else {
                            alert(d.errmsg);
                        }
                        $(".show-loading").hide();
                    }
                });
            }
        });
    });

    $('.sendcard-item').click(function () {
        var json = {}
        json.card_id = $(this).attr('data-id');
        json.card_name = $(this).attr('data-name');
        json.card_method = $(this).attr('data-method');
        var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
        selectedinfo.integral = false
        selectedinfo.cardinfo = {}
        selectedinfo.cardinfo.card_method = 1
        selectedinfo.cardinfo.card_id = ''
        selectedinfo.cardinfo.card_name = ''
        selectedinfo.cardinfo.card_headline = ''
        selectedinfo.sendinfo = json
        localStorage.selectedinfo = JSON.stringify(selectedinfo)
        window.location.href = "${pageContext.request.contextPath}/login/gotoCode"
    })

    //微信菜单功能
    wx.config({
        debug: false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${appid}",// 必填，公众号的唯一标识
        timestamp: "${timestamp}", // 必填，生成签名的时间戳
        nonceStr: "${noncestr}", // 必填，生成签名的随机串
        signature: "${signature}",// 必填，签名
        jsApiList: [
            'checkJsApi',
            'onMenuShareTimeline',
            'onMenuShareAppMessage'
        ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    //开启分享功能
    //    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    //        WeixinJSBridge.call('showToolbar'); //隐藏下菜单
    //        WeixinJSBridge.call('showOptionMenu'); //隐藏右上角菜单
    //    });
    //    wx.showAllNonBaseMenuItem();

    //需要动态重组分享得地址
    var id = $('.sendcard-container div:eq(0)').find('.room_type_id').val();

    function send(cardId) {
        bindListen({link: shareUrl.replace("ID", cardId.toString())});
    }


    var shareUrl = "${shareUrl}";
    var wxData = {
        title: '臻品vip折扣卡',
        desc: '臻品vip折扣卡等你来拿',
        link: shareUrl.replace("ID", id.toString()),
        imgUrl: "${domain}" + '/images/logo.png', // 分享图标
    };

    /**
     * 绑定事件
     * @type {string}
     */
    function bindListen(obj) {
        //监听分享给朋友
        wx.onMenuShareAppMessage(obj ? Object.assign(wxData, obj) : wxData);
        //监听分享到朋友圈”
        wx.onMenuShareTimeline(obj ? Object.assign(wxData, obj) : wxData);
    }

    wx.ready(function () {
        bindListen();
    });
</script>
</body>
</html>