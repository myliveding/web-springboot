<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">活动列表</div>
<div class="active-container">
    <c:forEach var="activity" items="${activitys}">
        <div class="active-item">
            <a href="${pageContext.request.contextPath}/login/activityDetail?id=${activity.id}">
                <img src="${activity.image_url}" alt="" width=100% height=100>
                <p>${activity.title}</p>
            </a>
        </div>
    </c:forEach>
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
                    'url': "${pageContext.request.contextPath}/free/activitysPaging",
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
                                    html += '<div class="active-item">'
                                    html += '<a href="${pageContext.request.contextPath}/login/activityDetail?id=' + d.data[i].id + '">'
                                    html += '<img src="' + d.data[i].image_url + '" alt="" width=100% height=100>'
                                    html += '<p>' + d.data[i].title + '</p>'
                                    html += '</a></div>'
                                }
                                $(".active-container").append(html);
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

    $(document).ready(function () {
        $('.active img').attr("src", "${pageContext.request.contextPath}/images/icon_actived.png");
        $('.active p').attr("class", "tabar-hover");
    });
</script>
</body>
</html>