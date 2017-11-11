<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="code-con">
    <div class="code-info">
        <p>总金额￥${sumAmt}</p>
        <c:if test="${type ne 1}">
            <p>-余额￥${balanceAmt}</p>
            <p>-积分${integral}</p>
            <p>-优惠￥${discount}</p>
        </c:if>
    </div>
    <div class="code-number">共支付：<p><span>￥</span>${sumAmt}</p></div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
    function onBridgeReady() {
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
                "appId": "${appid}",     //公众号名称，由商户传入
                "timeStamp": "${timeStamp}", //时间戳，自1970年以来的秒数
                "nonceStr": "${nonceStr}", //随机串
                "package": "${_package}",
                "signType": "MD5",         //微信签名方式:
                "paySign": "${paySign}" //微信签名
            },

            function (res) {
                //alert(res.err_msg);
                //alert(JSON.stringify(res));
                var type = "${type}";
                if (res.err_msg == "get_brand_wcpay_request:ok") {
                    //alert("付款成功，正在跳转到消费记录页面");
                    window.location.href = "${pageContext.request.contextPath}/login/gotoConsumptionRecords";
                    //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                } else {
//                   if(res.err_msg == "get_brand_wcpay_request:cancel"){
//                       alert("取消支付，正在跳转回操作页面");
//                   }else{
//                       alert("支付失败，正在跳转回操作页面");
//                   }
                    if (type == 1) {
                        window.location.href = "${pageContext.request.contextPath}/login/gotoVip";
                    } else {
                        window.location.href = "${pageContext.request.contextPath}/login/gotoCode";
                    }
                }
            }
        );
    }

    if (typeof WeixinJSBridge == "undefined") {
        if (document.addEventListener) {
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        } else if (document.attachEvent) {
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    } else {
        onBridgeReady();
    }
</script>
<img src="${pageContext.request.contextPath}/images/icon_logo.png" alt="" class="self-bg code-bg">
<jsp:include page="foot.jsp" flush="true"/>
</body>
</html>