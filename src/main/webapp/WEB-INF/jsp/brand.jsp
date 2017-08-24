<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="brand-body">
<ul class="nav nav-tabs brand-nav" role="tablist">
    <li role="presentation" class="active"><a href="#neck" aria-controls="neck" role="tab" data-toggle="tab">颈饰</a></li>
    <li role="presentation"><a href="#handle" aria-controls="handle" role="tab" data-toggle="tab">手饰</a></li>
    <li role="presentation"><a href="#ear" aria-controls="ear" role="tab" data-toggle="tab">耳饰</a></li>
    <li role="presentation"><a href="#foot" aria-controls="foot" role="tab" data-toggle="tab">脚饰</a></li>
</ul>
<div role="tabpanel" class="flex-box brand-pane active" id="neck">
    <div class="brand-item">
        <a href="bshow.html">
            <img src="images/brand_img.png" alt="">
            <div class="brand-item-txt">
                <p>臻品珠宝 产品名</p>
                <span>询价</span>
            </div>
        </a>
    </div>
    <div class="brand-item">
        <a href="bshow.html">
            <img src="images/brand_img.png" alt="">
            <div class="brand-item-txt">
                <p>臻品珠宝 产品名</p>
                <span>询价</span>
            </div>
        </a>
    </div>
    <div class="brand-item">
        <a href="bshow.html">
            <img src="images/brand_img.png" alt="">
            <div class="brand-item-txt">
                <p>臻品珠宝 产品名</p>
                <span>询价</span>
            </div>
        </a>
    </div>
    <div class="brand-item">
        <a href="bshow.html">
            <img src="images/brand_img.png" alt="">
            <div class="brand-item-txt">
                <p>臻品珠宝 产品名</p>
                <span>询价</span>
            </div>
        </a>
    </div>
    <div class="brand-item">
        <a href="bshow.html">
            <img src="images/brand_img.png" alt="">
            <div class="brand-item-txt">
                <p>臻品珠宝 产品名</p>
                <span>询价</span>
            </div>
        </a>
    </div>
    <div class="brand-item">
        <a href="bshow.html">
            <img src="images/brand_img.png" alt="">
            <div class="brand-item-txt">
                <p>臻品珠宝 产品名</p>
                <span>询价</span>
            </div>
        </a>
    </div>
    <div class="brand-item">
        <a href="bshow.html">
            <img src="images/brand_img.png" alt="">
            <div class="brand-item-txt">
                <p>臻品珠宝 产品名</p>
                <span>询价</span>
            </div>
        </a>
    </div>
</div>
<div role="tabpanel" class="flex-box brand-pane" id="handle">2</div>
<div role="tabpanel" class="flex-box brand-pane" id="ear">3</div>
<div role="tabpanel" class="flex-box brand-pane" id="foot">4</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>