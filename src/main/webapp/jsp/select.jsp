<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/login/index">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>扫码付款
</div>
<div class="navbar">
    <div class="navbar-inner">
        <div class="left sliding">
            <a href="" class="link icon-only back">
                <i class="icon icon-back"></i>
            </a>
        </div>
        <div class="center sliding">选择退款原因</div>
        <div class="right"></div>
    </div>
</div>

<div class="page navbar-fixed" data-page="refundreason">
    <div class="page-content">
        <div class="list-block refundreason">
            <ul>
            </ul>
        </div>
    </div>
</div>