<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="brand-body">
<div class="meal">
    <ul class="nav nav-tabs brand-nav" role="tablist">
        <c:forEach var="cate" items="${cates}">
            <li role="presentation">
                <input type="hidden" class="cateId" value="${cate.id}"/>
                <a href="javascript:void(0);" aria-controls="neck" role="tab" data-toggle="tab">
                        ${cate.name}
                </a>
            </li>
        </c:forEach>
    </ul>
</div>

<div role="tabpanel" class="flex-box brand-pane active nextPanel" id="neck">
</div>
<div class="flex-box show-loading">
    <img src="${pageContext.request.contextPath}/images/loading.gif" alt="">
    <p>加载更多</p>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    var isChange = false;
    var cateId;
    var page = 1;
    $(document).ready(function () {
        $('.meal li:eq(0)').addClass('active');
        cateId = $('.meal li:eq(0)').find('.cateId').val();
        getProductList(cateId);
    });

    $(document).ready(function () {
        $(window).scroll(function () {
            if ($(document).scrollTop() <= 0) {
                //alert("滚动条已经到达顶部");
            }
            if ($(document).scrollTop() >= $(document).height() - $(window).height()) {
                getProductList(cateId);
            }
        });
    });

    //换一个类型之后
    $('.meal li').click(function () {
        $('.meal li').removeClass('active');
        $(this).addClass('active');
        cateId = $(this).find('.cateId').val();
        page = 1;
        isChange = true;
        getProductList(cateId);
    });

    function getProductList(cateId) {
        $(".show-loading").show();
        $.ajax({
            'url': "${pageContext.request.contextPath}/free/products",
            'type': 'post',
            'dataType': 'json',
            'data': {
                cateId: cateId,
                page: page,
                prePage: 10,
            },
            success: function success(d) {
                if (d.status == 0) {
                    if (d.data.length > 0) {
                        var str = '';
                        page++;
                        for (var i = 0; i < d.data.length; i++) {
                            str += '<div class="brand-item">'
                            str += '<a href="${pageContext.request.contextPath}/login/productDetail?id=' + d.data[i].id + '">'
                            str += '<img src="' + d.data[i].image_url + '" alt="">'
                            str += '<div class="brand-item-txt">'
                            str += '<p>' + d.data[i].title + '</p>'
                            str += '<span>询价</span>'
                            str += '</div>'
                            str += '</a>'
                            str += '</div>';
                        }
                        if (isChange == true) {
                            isChange = false;
                            $(".nextPanel").html(str);
                        } else {
                            $(".nextPanel").append(str);
                        }
                    } else {
                        alert("该类型没有更多了");
                    }
                } else {
                    alert(d.errmsg);
                }
                $(".show-loading").hide();
            }
        });
    }
</script>
</body>
</html>