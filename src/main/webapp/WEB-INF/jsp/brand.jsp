<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="brand-body">
<div class="meal">
    <ul class="nav nav-tabs brand-nav" role="tablist">
        <c:forEach var="cate" items="${cates}">
            <li role="presentation" class="active">
                <input type="hidden" class="cateId" value="${cate.id}"/>
                <a href="javascript:void(0);" aria-controls="neck" role="tab" data-toggle="tab">
                        ${cate.name}
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
<div role="tabpanel" class="flex-box brand-pane active" id="neck">
    <ul>
    </ul>
</div>
<jsp:include page="foot.jsp" flush="true"/>
<script>
    var cateId;
    $(document).ready(function () {
        $('.meal li:eq(0)').addClass('act');
        cateId = $('.meal li:eq(0)').find('.cateId').val();
        getProductList(cateId);
    });

    $('.meal li').click(function () {
        $('.meal li').removeClass('act');
        $(this).addClass('act');
        cateId = $(this).find('.cateId').val();
        getProductList(cateId);
    })

    function getProductList(cateId) {
        $.ajax({
            'url': "${pageContext.request.contextPath}/login/products",
            'type': 'post',
            'dataType': 'json',
            'data': {
                cateId: cateId,
                perPage: 10,
                page: 1
            },
            success: function success(d) {
                if (d.status == 0) {
                    $(".flex-box .brand-pane .active ul").html('');
                    var str = '';
                    if (typeof(d.data) != "undefined") {
                        for (var i = 0; i < d.data.length; i++) {
                            str = str + '<li><div class="brand-item">'
                                    + '<a href="${pageContext.request.contextPath}/login/productDetail?id=' + d.data[i].id + '">'
                                    + '<img src="' + d.data[i].image_url + '" alt="">'
                                    + '<div class="brand-item-txt">'
                                    + '<p>' + d.data[i].title + '臻品珠宝 产品名</p>'
                                    + '</div>'
                                    + '</a>'
                                    + '</div></li>';
                        }
                        $(".flex-box .brand-pane .active ul").html(str);
                    }
                } else {
                    alert(d.msg);
                }
            }
        });
    }


</script>
</body>
</html>