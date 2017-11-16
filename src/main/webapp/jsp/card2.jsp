<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String coupons = request.getParameter("coupons");
%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="javascript:history.back(-1)">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>
    我的优惠券
</div>
<div class="sendcard-container">
</div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    var coupons = JSON.parse('<%=session.getAttribute("coupons") %>');
    if (coupons.length > 0) {
        var html = ''
        for (var i = 0; i < coupons.length; i++) {
            html += '<div class="sendcard-item card-item" data-id="' + coupons.id
                + '"data-name="满' + coupons.user_price + '减' + coupons.price
                + '"data-method="' + coupons.price
                + '"data-headline="' + coupons.user_price + '">'
            var expire = coupons[i].expiration_date;
            var oldTime = new Date(expire);
            var curTime = oldTime.getFullYear() * 10000 + (oldTime.getMonth() + 1) * 100 + oldTime.getDate();
            var now = '${now}';
            if (now > curTime) {
                html += '<img src="${pageContext.request.contextPath}/images/card_yellow.png" alt="">';
            } else {
                if (coupons[i].status == '1') {
                    html += '<img src="${pageContext.request.contextPath}/images/card_green.png" alt="">'
                } else if (coupons[i].status == '2') {
                    html += '<img src="${pageContext.request.contextPath}/images/card_purple.png" alt="">'
                }
            }
            html += '<div class="flex-box sendcard-txt">'
            html += '<img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">'
            html += '<p>全场消费满' + coupons[i].user_price + '元<span>即可使用</span></p>'
            html += '<div class="sendcard-number sendcard-txt-green">'
            html += coupons[i].price
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
                html += '<p>有效期至' + coupons[i].expiration_date + '</p>'
                if (coupons[i].status == '2') {
                    html += '<div class="card-shadow"></div>'
                    html += '<div class="card-status">'
                    html += '<img src="${pageContext.request.contextPath}/images/card_used.png" alt="">'
                    html += '</div>'
                }
            }
            html += '</div>'
        }
        $(".sendcard-container").append(html);
    }


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
</script>
</body>
</html>