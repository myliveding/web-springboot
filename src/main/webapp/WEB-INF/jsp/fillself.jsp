<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="fillself-body">
<div class="nav-top"><a href="mine.html"><img src="images/icon_left.png" alt="" class="nav-toback"></a>填写个人资料</div>
<div class="fillself-container">
    <div class="flex-box fillself-item"><span>性别</span><input type="text" placeholder="请填写您的性别"></div>
    <div class="flex-box fillself-item"><span>生日</span><input type="text" placeholder="请填写您的生日(2010-10-10)"></div>
    <div class="flex-box fillself-item"><span>微信号</span><input type="text" placeholder="请填写您的微信号"></div>
    <div class="flex-box fillself-item"><span>收货地址</span><input type="text" placeholder="请填写您的收货地址"></div>
    <div class="flex-box fillself-item"><span>推广人</span><input type="text" placeholder="请填写您推广人"></div>
    <div class="fillself-btn">
        <button>确定</button>
    </div>
    <div class="fillself-skip"><a href="">跳过此步骤</a></div>
</div>
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>