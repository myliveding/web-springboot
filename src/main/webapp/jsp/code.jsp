<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body class="active-body">
<div class="nav-top">
    <a href="${pageContext.request.contextPath}/login/index">
        <img src="${pageContext.request.contextPath}/images/icon_left.png" alt="" class="nav-toback">
    </a>扫码付款
</div>
<div class="flex-box code-payment">
    <span>金额</span>
    <input type="text" id="moneyinput">
</div>
<div class="code-con">
    <div class="flex-box code-item code-item-balance"><img
            src="${pageContext.request.contextPath}/images/icon_code_balance.png" alt="">
        <div class="code-item-txt">余额：￥<span id="balancenum">${info.balance}</span></div>
        <div class="code-item-opreate">
            <input type="checkbox" name="balance" id="" value="1" disabled>
        </div>
    </div>
    <div class="flex-box code-item code-item-integral">
        <img src="${pageContext.request.contextPath}/images/icon_nav3.png" alt="">
        <div class="code-item-txt">积分：<span id="intergralnum">${info.integral}</span>
        </div>
        <div class="code-item-opreate">
            <input type="checkbox" name="grade" id="" value="2" disabled>
        </div>
    </div>
    <div class="flex-box code-item code-item-discount" id="discount">
        <img src="${pageContext.request.contextPath}/images/icon_code_discount.png" alt="">
        <div class="code-item-txt">打折卡<span style="float:right;display:inline-block;padding-right:15px"></span></div>
        <div class="code-item-opreate">
            <img src="${pageContext.request.contextPath}/images/icon_code_right.png" alt="">
        </div>
    </div>
    <div class="flex-box code-item code-item-ticket" id="ticket">
        <img src="${pageContext.request.contextPath}/images/icon_mine_ticket.png" alt="">
        <div class="code-item-txt">
            优惠券
            <span style="float:right;display:inline-block;padding-right:15px" id="showinfo"></span>
        </div>
        <div class="code-item-opreate">
            <img src="${pageContext.request.contextPath}/images/icon_code_right.png" alt="">
        </div>
    </div>
    <div class="code-info">
        <p>总金额￥<span id="showallmoney">0</span></p>
        <p>-余额￥<span id="minusbalance">0</span></p>
        <p>-积分￥<span id="minusintegral">0</span></p>
        <p>-优惠￥<span id="minusdiscount">0</span></p>
    </div>
    <div class="code-number">共支付：<p>￥<span id="needpay">0.00</span></p></div>
</div>
<div class="self-btn pwd-btn">
    <button>确认支付</button>
</div>
<img src="${pageContext.request.contextPath}/images/icon_logo.png" alt="" class="self-bg code-bg">
<jsp:include page="foot.jsp" flush="true"/>
<script>
    //获取最优支付方案得方法，得在金额输入框有值得情况下，才去调用
    function bestPay(money) {
        $.ajax({
            'url': "${pageContext.request.contextPath}/rest/payChoice",
            'type': 'post',
            'dataType': 'json',
            'data': {
                money: money
            },
            success: function success(d) {
                if (d.error_code == 0) {
                    //type  0优惠券 1打折卡 2积分
                    //19代表优惠券或是打折卡的id
                    //98代表优惠券或是打折卡的优惠金额
                    //如果返回type=2时，key=1只是为了格式一致，无其他作用
                } else {
                    alert(d.errmsg);
                }
            }
        });
    };
</script>
<script>
    $.fn.toggle = function (fn, fn2) {
        var args = arguments, guid = fn.guid || $.guid++, i = 0,
            toggle = function (event) {
                var lastToggle = ( $._data(this, "lastToggle" + fn.guid) || 0 ) % i;
                $._data(this, "lastToggle" + fn.guid, lastToggle + 1);
                event.preventDefault();
                return args[lastToggle].apply(this, arguments) || false;
            };
        toggle.guid = guid;
        while (i < args.length) {
            args[i++].guid = guid;
        }
        return this.click(toggle);
    };
</script>
<script>

    setvalue()
    setselectValue()
    //     $("input[type='checkbox']").click(function(e){
    //     e.stopPropagation();
    // });
    $('.code-item-balance').toggle(
        function () {
            $(this).find("input[type='checkbox']").prop("checked", false);
            var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
            selectedinfo.balance = false
            localStorage.selectedinfo = JSON.stringify(selectedinfo)
            setselectValue()
        },
        function () {
            $(this).find("input[type='checkbox']").prop("checked", "checked");
            var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
            selectedinfo.balance = true
            localStorage.selectedinfo = JSON.stringify(selectedinfo)
            setselectValue()
        });
    $('.code-item-integral').toggle(function () {
            $(this).find("input[type='checkbox']").prop("checked", "checked");
            var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
            selectedinfo.integral = true
            localStorage.selectedinfo = JSON.stringify(selectedinfo)
            clearcard()
            setselectValue()
        },
        function () {
            $(this).find("input[type='checkbox']").prop("checked", false);
            var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
            selectedinfo.integral = false
            localStorage.selectedinfo = JSON.stringify(selectedinfo)
            setselectValue()
        });
    $('#discount').click(function () {
        window.location.href = "${pageContext.request.contextPath}/jsp/sendcard2.jsp";
    });
    $('#ticket').click(function () {
        window.location.href = "${pageContext.request.contextPath}/jsp/card2.jsp";
    });
    $('#moneyinput').bind('input propertychange', function () {
        var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
        selectedinfo.inputmoney = $(this).val()
        localStorage.selectedinfo = JSON.stringify(selectedinfo)
        if ($(this).val()) {
            $('#showallmoney').text($(this).val())
        } else {
            $('#showallmoney').text('0.00')
        }
        setselectValue()
    });

    function setselectValue() {
        var allmoney = $('#moneyinput').val()
        var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
        var method = 1
        var headline
        var headline2
        if (selectedinfo.cardinfo) {
            method = selectedinfo.cardinfo.card_method < selectedinfo.sendinfo.card_method ? selectedinfo.cardinfo.card_method : selectedinfo.sendinfo.card_method
            headline = selectedinfo.cardinfo.card_headline
            headline2 = selectedinfo.sendinfo.card_headline
        }
        var balanceselect = $("input[name='balance']").prop('checked')
        var integralselect = $("input[name='grade']").prop('checked')
        var discount
        if (headline) {
            method = selectedinfo.cardinfo.card_method
            if ((allmoney * 1) >= (headline * 1)) {
                discount = method
            } else {
                discount = 0
            }
        } else {
            if ((allmoney * 1) >= (headline2 * 1)) {
                discount = (allmoney * (1 - method)).toFixed(2)
            }
            else {
                discount = 0
            }
        }
        $('#minusdiscount').text(discount)
        allmoney -= discount
        if (integralselect) {
            if ($('#intergralnum').text() > allmoney) {
                $('#minusintegral').text(allmoney)
                allmoney = 0
            } else {
                $('#minusintegral').text($('#intergralnum').text())
                allmoney -= ($('#intergralnum').text() * 1)
            }
        } else {
            $('#minusintegral').text('0')
        }

        if (balanceselect) {
            if ($('#balancenum').text() > allmoney) {
                $('#minusbalance').text(allmoney)
                allmoney = 0
            } else {
                $('#minusbalance').text($('#balancenum').text())
                allmoney -= ($('#balancenum').text() * 1)
            }
        } else {
            $('#minusbalance').text('0')
        }
        var w = allmoney.toString().indexOf(".");
        if (w != -1) {
            allmoney = allmoney.toString().substring(0, (parseInt(w) + 3));
        }
        $('#needpay').text(allmoney)
    }

    function setvalue() {
        var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
        if (selectedinfo.cardinfo) {
            $('#moneyinput').val(selectedinfo.inputmoney)
            $('#ticket span').text(selectedinfo.cardinfo.card_name)
            $('#discount span').text(selectedinfo.sendinfo.card_name)
            if (selectedinfo.inputmoney) {
                $('#showallmoney').text(selectedinfo.inputmoney)
            } else {
                $('#showallmoney').text('0.00')
            }
        }
        if (selectedinfo.integral) {
            $('.code-item-integral').find("input[type='checkbox']").prop("checked", "checked");
        } else {
            $('.code-item-integral').find("input[type='checkbox']").prop("checked", false);
        }
        if (typeof(selectedinfo.balance) == 'undefined') {
            $('.code-item-balance').find("input[type='checkbox']").prop("checked", "checked");
        } else if (selectedinfo.balance) {
            $('.code-item-balance').find("input[type='checkbox']").prop("checked", "checked");
        } else {
            $('.code-item-balance').find("input[type='checkbox']").prop("checked", false);
        }
    }

    function clearcard() {
        var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {}
        selectedinfo.cardinfo = {}
        selectedinfo.cardinfo.card_method = 1
        selectedinfo.cardinfo.card_id = ''
        selectedinfo.cardinfo.card_name = ''
        selectedinfo.sendinfo = {}
        selectedinfo.sendinfo.card_method = 1
        selectedinfo.sendinfo.card_id = ''
        selectedinfo.sendinfo.card_name = ''
        localStorage.selectedinfo = JSON.stringify(selectedinfo)
        setvalue()
    }
</script>


<script>
    function isWeixnOpen() {
        var ua = navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == "micromessenger") {
            return true;
        } else {
            return false;
        }
    }

    //保存按钮的事件
    $('.self-btn button').on('click', function () {
        var selectedinfo = localStorage.selectedinfo ? JSON.parse(localStorage.selectedinfo) : {};
        var discountCardId = ''; //打折卡ID
        var couponId = ''; //优惠券ID
        var inputMoney = selectedinfo.inputmoney;
        if (selectedinfo.cardinfo) {
            discountCardId = selectedinfo.sendinfo.card_id
            couponId = selectedinfo.cardinfo.card_id
        }

        var payAmt = $('#needpay').text();
        var money = $('#showallmoney').text();
        var balance = $('#minusbalance').text();
        var integral = $('#minusintegral').text();
        var couponDesc = $('#minusdiscount').text();
        if (inputMoney == 0) {
            alert("请先输入购买金额");
            return;
        }
        if (payAmt > 0) {
            //表示需要用户进行支付操作，需要调用微信的预支付接口
            if (!isWeixnOpen()) {
                alert('<div><div><img src="${pageContext.request.contextPath}/images/10.png"></div><div style="color:#2cca6f;">微信不支持在浏览器端的支付！</div><div style="color:#333;">可通过以下方式完成支付</div><div style="color:#999; text-align:left;">方式一：打开微信，关注“盛世欢唱”公众号，进入“我的>我的订单”中完成交易</div><div style="color:#999; text-align:left;">方式二：选择其他支付方式完成交易</div></div>');
                return false;
            } else {
                setTimeout(function () {
                    window.location.href = "${pageContext.request.contextPath}/wechatPay/index?productId=0&type=2"
                        + "&price=" + payAmt + "&money=" + money + "&balance=" + balance
                        + "&integral=" + integral + "&discountCardId=" + discountCardId
                        + "&couponId=" + couponId + "&couponDesc=" + couponDesc;
                }, 1000);
            }
        } else {
            //用户不需要付款，直接调用后台支付接口
            $.ajax({
                'url': "${pageContext.request.contextPath}/rest/balancePay",
                'type': 'post',
                'dataType': 'json',
                'data': {
                    money: money,
                    balance: balance,
                    integral: integral,
                    discountCardId: discountCardId,
                    couponId: couponId
                },
                success: function success(d) {
                    if (d.status == 0) {
                        window.location.href = "${pageContext.request.contextPath}/login/gotoConsumptionRecords";
                    } else {
                        alert(d.errmsg);
                    }
                }
            });
        }
    });

</script>
</body>
</html>