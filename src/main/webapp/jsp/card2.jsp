<%@ page language="java" pageEncoding="utf-8" %>
<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<style>
    .selectedred {
        display: inline-block;
        width: 15px;
        height: 15px;
        border-radius: 50%;
        background: red;
        position: absolute;
        top: 4px;
        right: 4px;
        display: none;
    }
</style>
<body class="active-body">
<div class="nav-top">
    <a href="javascript:history.back(-1)">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>
    我的优惠券
</div>
<div class="sendcard-container">
    <c:forEach var="card" items="${cards}">
        <div class="sendcard-item card-item" data-id="102" data-name="满200减100" data-method="100" data-headline="200">
            <!-- method:减多少元   headline：满多少 -->
            <span style="display:inline-block;width:15px;height:15px;border-radius:50%;background:red;position:absolute;top:4px;right:4px"></span>
            <fmt:parseDate value="${card.expiration_date}" pattern="yyyy-MM-dd" var="expireDate"/>
            <fmt:formatDate value="${expireDate}" pattern="yyyyMMdd" var="expire"/>
            <c:if test="${now gt expire}" var="rs">
                <img src="${pageContext.request.contextPath}/images/card_yellow.png" alt="">
            </c:if>
            <c:if test="${!rs}">
                <c:if test="${card.status eq '1'}">
                    <img src="${pageContext.request.contextPath}/images/card_green.png" alt="">
                </c:if>
                <c:if test="${card.status eq '2'}">
                    <img src="${pageContext.request.contextPath}/images/card_purple.png" alt="">
                </c:if>
            </c:if>
            <div class="flex-box sendcard-txt">
                <img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">
                <p>全场消费满${card.user_price}元<span>即可使用</span></p>
                <div class="sendcard-number sendcard-txt-green">
                        ${card.price} <span>元</span>
                </div>
            </div>
            <c:if test="${now gt expire}" var="rs">
                <p>未使用</p>
                <div class="card-shadow"></div>
                <div class="card-status">
                    <img src="${pageContext.request.contextPath}/images/card_timeout.png" alt="">
                </div>
            </c:if>
            <c:if test="${!rs}">
                <p>有效期至${card.expiration_date}</p>
                <c:if test="${card.status eq '2'}">
                    <div class="card-shadow"></div>
                    <div class="card-status">
                        <img src="${pageContext.request.contextPath}/images/card_used.png" alt="">
                    </div>
                </c:if>
            </c:if>
        </div>
    </c:forEach>
</div>
</div>
<div class="flex-box show-loading">
    <img src="${pageContext.request.contextPath}/images/loading.gif" alt="">
    <p>加载更多</p>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    $('.card-item').click(function () {
        var json = {}
        json.card_id = $(this).attr('data-id');
        json.card_name = $(this).attr('data-name');
        json.card_method = $(this).attr('data-method');
        json.card_headline = $(this).attr('data-headline');
        var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
        selectedinfo.integral = false
        selectedinfo.sendinfo = {}
        selectedinfo.sendinfo.card_method = 1
        selectedinfo.sendinfo.card_id = ''
        selectedinfo.sendinfo.card_name = ''
        selectedinfo.cardinfo = json
        localStorage.selectedinfo = JSON.stringify(selectedinfo)
        window.history.go(-1);
    })

    var page = 2;
    $(document).ready(function () {
        $(window).scroll(function () {
            if ($(document).scrollTop() <= 0) {
                //alert("滚动条已经到达顶部");
            }
            if ($(document).scrollTop() >= $(document).height() - $(window).height()) {
                $(".show-loading").show();
                $.ajax({
                    'url': "${pageContext.request.contextPath}/rest/couponsPaging",
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
                                for (var i = 0; i < d.data.length; i++) {
                                    html += '<div class="sendcard-item card-item">'
                                    var expire = d.data[i].expiration_date;
                                    var oldTime = new Date(expire);
                                    var curTime = oldTime.getFullYear() * 10000 + (oldTime.getMonth() + 1) * 100 + oldTime.getDate();
                                    var now = '${now}';
                                    if (now > curTime) {
                                        html += '<img src="${pageContext.request.contextPath}/images/card_yellow.png" alt="">';
                                    } else {
                                        if (d.data[i].status == '1') {
                                            html += '<img src="${pageContext.request.contextPath}/images/card_green.png" alt="">'
                                        } else if (d.data[i].status == '2') {
                                            html += '<img src="${pageContext.request.contextPath}/images/card_purple.png" alt="">'
                                        }
                                    }
                                    html += '<div class="flex-box sendcard-txt">'
                                    html += '<img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">'
                                    html += '<p>全场消费满' + d.data[i].user_price + '元<span>即可使用</span></p>'
                                    html += '<div class="sendcard-number sendcard-txt-green">'
                                    html += d.data[i].price
                                    html += '<span>元</span>'
                                    html += '</div>'
                                    html += '</div>'
                                    if (now > curTime) {
                                        html += '<p>未使用</p>'
                                        html += '<div class="card-shadow"></div>'
                                        html += '<div class="card-status">'
                                        html += '<img src="${pageContext.request.contextPath}/images/card_timeout.png" alt="">'
                                        html += '</div>'
                                    } else {
                                        html += '<p>有效期至' + d.data[i].expiration_date + '</p>'
                                        if (d.data[i].status == '2') {
                                            html += '<div class="card-shadow"></div>'
                                            html += '<div class="card-status">'
                                            html += '<img src="${pageContext.request.contextPath}/images/card_used.png" alt="">'
                                            html += '</div>'
                                        }
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
</script>
</body>
</html>