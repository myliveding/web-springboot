<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="javascript:history.back(-1)">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>
    卡券赠送
</div>
<div class="sendcard-container">
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    var cards = JSON.parse('<%=session.getAttribute("cards") %>');
    if (cards.length > 0) {
        var html = ''
        for (var i = 0; i < cards.length - 1; i++) {
            html += '<div class="sendcard-item card-item" data-id="'
                + cards[i].id + '" data-name="全场消费满'
                + cards[i].user_price + '元即可使用' + cards[i].discount + '折" data-method="'
                + cards[i].discount + '">'
            html += '<img src="${pageContext.request.contextPath}/images/sendcard_green.png" alt="">'
            html += '<div class="flex-box sendcard-txt">'
            html += '<img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">'
            html += '<p>全场消费满' + cards[i].user_price + '元<span>即可使用</span></p>'
            html += '<div class="sendcard-number sendcard-txt-green">' + cards[i].discount + '<span>折</span>'
            html += '</div>'
            html += '</div>'
            html += '</div>'
        }
        $(".sendcard-container").append(html);
    }

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
        window.history.go(-1);
    })
</script>
</body>
</html>