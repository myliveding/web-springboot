<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="bshow-back"><a href="brand.html"><img src="images/bshow_left.png" alt=""></a></div>
<div class="swiper-container banner">
    <div class="swiper-wrapper">
        <ul>
            <c:forEach var="banner" items="${banners}">
                <li>
                    <div class="swiper-slide"><a href="${banner.id}"><img src="${banner.image_url}"
                                                                          alt="">${banner.title}</a></div>
                </li>
            </c:forEach>
        </ul>
    </div>
    <div class="swiper-pagination"></div>
</div>
<div class="bshow-header">
    <h3>臻品珠宝 产品</h3>
    <h4>臻品珠宝 产品广告语 说明</h4>
</div>
<div class="bshow-con">
    <img src="images/bshow_img1.jpg" alt="" class="bshow-btn">
</div>
<div class="bshow-box">
    <div class="bshow-box-shadow"></div>
    <div class="bshow-box-con">
        <img src="images/brand_banner1.jpg" alt="">
        <div class="bshow-box-txt">
            <h4>臻品珠宝 产品</h4>
            <p>臻品珠宝 产品广告语说明</p>
            <span>推</span>
        </div>
    </div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>