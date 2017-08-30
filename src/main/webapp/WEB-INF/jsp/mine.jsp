<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">用户中心</div>
<div class="mine-container">
    <div class="mine-vip">
        <img src="${pageContext.request.contextPath}/images/mine_vip_bg.jpg" alt="" class="mine-vip-bg">
        <div class="mine-vip-bottom">
            <%--<div class="fl mine-vip-number">NO.6868668</div>--%>
            <div class="fr mine-vip-level"><img src="${pageContext.request.contextPath}/images/icon_vip.png"
                                                alt=""><span></span></div>
        </div>
    </div>
    <div class="mine-txt">
        <h4><a href="login.jsp">会员权益 ></a></h4>
        <div class="mine-txt-item">
            <div class="fl mine-txt-integral"><p>${user.integral}</p><span>当前积分</span></div>
            <div class="fr mine-txt-balance"><p>￥${user.balance}</p><span>当前余额</span></div>
            <div class="mine-txt-border"></div>
        </div>
        <div class="mine-self-item">
            <div class="mine-item-sub flex-box mine-item-sub-h4">
                <div class="flex-box fl mine-item-sub-h4txt">
                    <h4>${user.name}</h4>
                    <img src="${pageContext.request.contextPath}/images/icon_mine_vip.png">
                    <c:choose>
                        <c:when test="${user.member_type eq '1'}">
                            <span>普通用户</span>
                        </c:when>
                        <c:otherwise>
                            <span>臻品会员</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="fr">
                    <a href="">积分规则 ></a>
                </div>
            </div>
            <div class="mine-item-sub">
                <a href="${pageContext.request.contextPath}/login/gotoUserInfo" class="flex-box">
                    <div class="fl">
                        <img src="${pageContext.request.contextPath}/images/icon_mine_person.png" alt=""
                             class="mine-item-subimg">
                        <span>个人资料</span>
                    </div>
                    <div class="fr mine-item-operate">
                        <img src="${pageContext.request.contextPath}/images/mine_right.png" alt="">
                    </div>
                </a>
            </div>
            <div class="mine-item-sub">
                <a href="${pageContext.request.contextPath}/login/gotoPwd" class="flex-box">
                    <div class="fl">
                        <img src="${pageContext.request.contextPath}/images/icon_mine_pwd.png" alt=""
                             class="mine-item-subimg">
                        <span>修改登陆密码</span>
                    </div>
                    <div class="fr mine-item-operate">
                        <img src="${pageContext.request.contextPath}/images/mine_right.png" alt="">
                    </div>
                </a>
            </div>
            <div class="mine-item-sub">
                <a href="${pageContext.request.contextPath}/login/gotoCouponsCard" class="flex-box">
                    <div class="fl">
                        <img src="${pageContext.request.contextPath}/images/icon_mine_ticket.png" alt=""
                             class="mine-item-subimg">
                        <span>我的优惠券</span>
                    </div>
                    <div class="fr mine-item-operate">
                        <img src="${pageContext.request.contextPath}/images/mine_right.png" alt="">
                    </div>
                </a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>