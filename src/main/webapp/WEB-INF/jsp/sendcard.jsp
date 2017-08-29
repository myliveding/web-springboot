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
        <div class="sendcard-item">
            <c:choose>
                <c:when test="${card.status eq '2'}">
                    <img src="${pageContext.request.contextPath}/images/sendcard_green.png" alt="">
                </c:when>
                <c:when test="${card.status eq '1'}">
                    <img src="${pageContext.request.contextPath}/images/sendcard_yellow.png" alt="">
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/images/sendcard_purple.png" alt="">
                </c:otherwise>
            </c:choose>
            <div class="flex-box sendcard-txt">
                <img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">
                <p>全场消费满${card.user_price}元<span>即可使用</span></p>
                <div class="sendcard-number sendcard-txt-green">${card.discount}<span>折</span></div>
            </div>
            <c:if test="${card.status eq '2'}">
                <a href="" class="sendcard-status-green">赠送</a>
            </c:if>
        </div>
    </c:forEach>
</div>
<%--<div class="flex-box show-loading">--%>
<%--<img src="${pageContext.request.contextPath}/images/loading.gif" alt="">--%>
<%--<p>加载更多</p>--%>
<%--</div>--%>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    var page = 2;
    $(document).ready(function () {
        $(window).scroll(function () {
            if ($(document).scrollTop() <= 0) {
                alert("滚动条已经到达顶部");
            }
            if ($(document).scrollTop() >= $(document).height() - $(window).height()) {
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
                                for (var i = 0; i < d.data.length; i++) {
                                    html += '<div class="sendcard-item">'
                                    html += '<img src="${pageContext.request.contextPath}/images/sendcard_green.png" alt="">'
                                    html += '<div class="flex-box sendcard-txt">'
                                    html += '<img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">'
                                    html += '<p>全场消费满' + d.data[i] + '元<span>即可使用</span></p>'
                                    html += '<div class="sendcard-number sendcard-txt-green">' + d.data[i].discount + '<span>折</span>'
                                    html += '</div>'
                                    html += '<a href="" class="sendcard-status-green">赠送</a>'
                                    html += '</div>'
                                }
                                $(".flex-box .team-con").append(html);
                            } else {
                                alert("没有更多了");
                            }
                        } else {
                            alert(d.errmsg);
                        }
                    }
                });
            }
        });
    });
</script>
</body>
</html>