<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Locale" %>
<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%
    java.util.Date nowdate = new java.util.Date();
    String myString = "2008-09-08";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    Date d = sdf.parse(myString);
    boolean flag = d.before(nowdate);
    if (flag)
        System.out.print("早于今天");
    else
        System.out.print("晚于今天");
%>
<!DOCTYPE html>
<html lang="en">
<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate value="<%=new Date() %>" pattern="yyyy-MM-dd"/>
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/login/index">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>
    我的卡券
</div>
<div class="sendcard-container">
    <fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd" var="nowDate"/>
    <c:forEach var="card" items="${cards}">
        <div class="sendcard-item card-item">
            <fmt:formatDate value="${card.expiration_date}" pattern="yyyy-MM-dd" var="expireDate"/>
            <c:if test="${nowDate gt expireDate}" var="rs">
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
                        ${card.price}
                    <span>折</span>
                </div>
            </div>
            <c:if test="${nowDate gt expireDate}" var="rs">
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
                                    html += '<fmt:formatDate value="' + d.data[i].expiration_date + '" pattern="yyyy-MM-dd" var="expireDate"/>’
                                    html += '<c:if test="${nowDate gt expireDate}" var="rs">'
                                    html += '<img src="${pageContext.request.contextPath}/images/card_yellow.png" alt="">'
                                    html += '</c:if>'
                                    html += '<c:if test="${!rs}">'
                                    html += '<c:if test="${' + d.data[i].status + ' eq '1'}">'
                                    html += '<img src="${pageContext.request.contextPath}/images/card_green.png" alt="">'
                                    html += '</c:if>'
                                    html += '<c:if test="${' + d.data[i].status + ' eq '2'}">'
                                    html += '<img src="${pageContext.request.contextPath}/images/card_purple.png" alt="">'
                                    html += '</c:if>'
                                    html += '</c:if>'
                                    html += '<div class="flex-box sendcard-txt">'
                                    html += '<img src="${pageContext.request.contextPath}/images/icon_logo_white.png" alt="">'
                                    html += '<p>全场消费满' + d.data[i].user_price + '元<span>即可使用</span></p>'
                                    html += '<div class="sendcard-number sendcard-txt-green">'
                                    html += d.data[i].price
                                    html += '<span>折</span>'
                                    html += '</div>'
                                    html += '</div>'
                                    html += '<c:if test="${nowDate gt expireDate}" var="rs">'
                                    '
                                    html += '<p>未使用</p>'
                                    html += '<div class="card-shadow"></div>'
                                    html += '<div class="card-status">'
                                    html += '<img src="${pageContext.request.contextPath}/images/card_timeout.png" alt="">'
                                    html += '</div>'
                                    html += '</c:if>'
                                    html += '<c:if test="${!rs}">'
                                    html += '<p>有效期至' + d.data[i].expiration_date + '</p>'
                                    html += '<c:if test="${' + d.data[i].status + ' eq '2'}">'
                                    html += '<div class="card-shadow"></div>'
                                    html += '<div class="card-status">'
                                    html += '<img src="${pageContext.request.contextPath}/images/card_used.png" alt="">'
                                    html += '</div>'
                                    html += '</c:if>'
                                    html += '</c:if>'
                                    html += '</div>'
                                }
                                $(".sendcard-container").append(html);
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