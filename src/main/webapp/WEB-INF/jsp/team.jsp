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
    团队
</div>
<div class="team-container">
    <h4 class="team-h4">臻品团队</h4>
    <h5 class="team-h5">
        <img src="${pageContext.request.contextPath}/images/icon_team_logo.png" alt="">
    </h5>
    <div class="team-triangle">
        <img src="${pageContext.request.contextPath}/images/icon_team_triangle.png" alt="">
    </div>
    <div class="flex-box team-con">
        <c:forEach var="team" items="${teams}">
            <div class="team-item">
                <img src="${team.head_url}" alt="">
                <p>${team.name}</p>
                <span>微信号</span><span>${team.wechat_num}</span>
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
    var page = 2;
    $(document).ready(function () {
        $(window).scroll(function () {
            if ($(document).scrollTop() <= 0) {
                //alert("滚动条已经到达顶部");
            }
            if ($(document).scrollTop() >= $(document).height() - $(window).height()) {
                $(".show-loading").show();
                $.ajax({
                    'url': "${pageContext.request.contextPath}/rest/teamPaging",
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
                                    html += '<div class="team-item">'
                                    html += '<img src="' + d.datap[i].head_url + '" alt="">'
                                    html += d.data[i].name
                                    html += '<span>微信号</span>'
                                    html += '<span>' + d.data[i].wechat_num + '</span>'
                                    html += '</div>'
                                }
                                $(".flex-box .team-con").append(html);
                            } else {
                                alert("没有更多了");
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