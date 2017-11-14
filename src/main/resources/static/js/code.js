var myApp = new Framework7({
//Tell Framework7 to compile templates on app init
    precompileTemplates: true,
    modalTitle: '',
    modalButtonOk: '知道了'
});

//添加菜单功能
jyApp.showMenu();

var mainView = myApp.addView('.view-main', {
    dynamicNavbar: true,
    domCache: true
});


//立即申请
$$(document).on('click', '.js-refund-application', function () {
    var fiveRadio = $$('.five-radio:checked').val(),
        fundRadio = $$('.fund-radio:checked').val(),
        refundType = $$('.refund-way-select').attr("data-cardid"),
        reason = $$('.refund-reason-select span').text().trim(),
        remarks = $$('.refund-marks textarea').val().trim(),
        applyData = {};
    if ($$(this).attr('disabled')) {
        return false;
    }
    if (!fiveRadio && !fundRadio) {
        myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>请选择退款项目</div>`)
        //myApp.alert("请选择退款项目");
        return false;
    }

    if (refundType === undefined) {
        myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>请选择退款方式</div>`)
        //myApp.alert("请选择退款方式");
        return false;
    }

    if (reason === "请选择") {
        myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>请选择退款原因</div>`)
        //myApp.alert("请选择退款原因");
        return false;
    }
    applyData = {
        insurerId: id,
        cityId: $$('.refund-city').data('cityid'),
        cityName: $$('.refund-city .item-after').text().trim(),
        isBank: refundType,
        bankId: refundType == '0' ? '' : $$('.refund-way-select').data('cardid'),
        reason: reason,
        remark: remarks,
        insuranceMonth: ($$('.five-radio:checked').val() ? $$('.five-part').attr('data-time') : '') || '',
        housingFundMonth: ($$('.fund-radio:checked').val() ? $$('.fund-part').attr('data-time') : '') || ''
    };
    $$(this).prop('disabled', true);
    $$.ajax({});
});

myApp.showIndicator();

var id = util.getUrlParam('id'),
    refundData = {},
    data = {};


data = {
    insurerId: id,
    cityId: '',
    isEdit: 0
}
getData(data);

function getData(data) {
    $$.ajax({
        url: packageJson.JAVA_DOMAIN + util.ajaxUrl.refundInit,
        type: "POST",
        dataType: 'json',
        data: data,
        success: function (d) {
            myApp.hideIndicator();
            if (d.status === 0) {
                refundData = d.data;
                if (refundData.forceFlag === 1) {
                    $$('.page-content').prepend(orderPrompt('当前城市，社保和公积金强制缴纳，不可单停'));
                } else if (refundData.forceFlag === 2) {
                    $$('.page-content').prepend(orderPrompt('当前城市，社保强制缴纳，不可单停'));
                }


                $$('.refund-name-part span').text(refundData.insurerName);
                if (refundData.cityList.length > 1) {
                    $$('.refund-city a').attr('href', 'refundcity.jsp');
                    $$('.refund-city a').addClass('item-link');
                }
                $$('.refund-city').attr('data-cityid', refundData.cityId);
                $$('.refund-city .item-after').text(refundData.cityName);

                if (refundData.insuranceRefundMonth) {
                    if (refundData.insureList.length > 1) {
                        $$('.five-part a').attr('href', 'refundmonth.jsp?type=1');
                        $$('.five-part').addClass('item-link');
                    }
                    $$('.five-part').attr('data-time', refundData.insuranceRefundMonth);
                    $$('.five-part .item-after a').text(new Date(refundData.insuranceRefundMonth * 1000).format('YY-MM'));
                    $$('.five-part').show()
                } else {
                    $$('.five-part').hide()
                }

                if (refundData.housingFundRefundMonth) {
                    if (refundData.houseList.length > 1) {
                        $$('.fund-part a').attr('href', 'refundmonth.jsp?type=2');
                        $$('.fund-part').addClass('item-link');
                    }
                    $$('.fund-part').attr('data-time', refundData.housingFundRefundMonth);
                    $$('.fund-part .item-after a').text(new Date(refundData.housingFundRefundMonth * 1000).format('YY-MM'));
                    $$('.fund-part').show()
                } else {
                    $$('.fund-part').hide()
                }

                $$('.refund-fee').html('')
                if (refundData.housingFundRefundAmt) {
                    $$('.refund-fee').append('<div class="fund-fee">公积金：<span class="prompt-green ">&yen;' + refundData.housingFundRefundAmt + '</span></div>');
                }

                if (refundData.insuranceRefundAmt) {
                    $$('.refund-fee').prepend('<div class="five-fee">社保：<span class="prompt-green ">&yen; ' + refundData.insuranceRefundAmt + '</span></div>');
                }
                $$('.refund-reason-select span').text(refundData.reason || '请选择');
            }
        },
        error: function () {
        }
    })
}


function orderPrompt(text) {
    return '<div class="order-prompt">' + text + '</div>';
}


$$('body').on('change', '.five-radio', function () {
    //恢复按钮状态
    if ($$('.five-radio:checked').val()) {
        $$('.five-radio').prop('checked', false)
    } else {
        $$('.five-radio').prop('checked', true)
    }
    //refundData.forceFlag === 1禁用状态不允许点击
    if (refundData.forceFlag === 1 && !$$('.five-radio').prop('checked')) {
        return
    }
    let val = ''
    if ($$('.five-radio').prop('checked')) {
        val = ''
    } else {
        val = $$('.five-part').attr('data-time')
    }
    const data = {
        insurerId: id,
        cityId: $$('.refund-city').data("cityid"),
        insuranceMonth: val,
        housingFundMonth: ($$('.fund-radio:checked').val() ? $$('.fund-part').attr('data-time') : '') || '',
        type: 0,
        isEdit: 0,
        primary: 1
    }
    myApp.showIndicator()
    $$.ajax({
        url: packageJson.JAVA_DOMAIN + util.ajaxUrl.refundInit,
        type: "POST",
        dataType: 'json',
        data: data,
        success: function (d) {
            myApp.hideIndicator()
            if (d.status === 0) {
                if ($$('.five-radio').prop('checked')) {
                    $$('.five-radio').prop('checked', false)
                    $$('.five-fee').hide();
                    $$('.five-part .item-after').hide();
                    $$('.five-part .item-inner').addClass('nomonth');
                } else {
                    $$('.five-radio').prop('checked', true)
                    $$('.five-fee').show()
                    $$('.five-part .item-after').show()
                    $$('.five-part .item-inner').removeClass('nomonth')
                }
            } else {
                myApp.hideIndicator();
                myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>${d.msg}</div>`)
                //myApp.alert(d.msg);
            }
        },
        error: function () {
            myApp.hideIndicator();
            myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>服务器异常，获取数据列表失败</div>`)
            //myApp.alert('服务器异常，获取数据列表失败');
        }
    })


    // if(refundData.forceFlag === 4){
    //     $$('.refund-month input').prop('checked', true);
    //     setTimeout(function(){
    //         myApp.closeModal();
    //
    //     },3000);
    //
    //     myApp.modal({
    //         title:  '',
    //         text: '<div><img src="'+packageJson.JAVA_STATICURL+'/images/icon/10.png"></div><div>'+$$('.order-prompt').text()+'</div>'
    //     })
    // } else if(refundData.forceFlag === 2){
    //     if(!$$(this).prop('checked')){
    //         $$('.five-fee').hide();
    //
    //         $$('.five-part .item-after').hide();
    //         $$('.five-part .item-inner').addClass('nomonth');
    //     } else {
    //         var fundtime = $$('.fund-part').attr('data-time'),
    //             fivetime = $$('.five-part').attr('data-time');
    //         if(fivetime < fundtime) {
    //             $$('.five-part').attr('data-time',fundtime);
    //             $$('.five-month').html($$('.fund-month').html())
    //         }
    //         $$('.refund-month input').prop('checked', true);
    //         $$('.five-fee').show();
    //         $$('.five-part .item-after').show();
    //         $$('.five-part .item-inner').removeClass('nomonth');
    //
    //     }
    // } else if(refundData.forceFlag === 3){
    //     if(!$$(this).prop('checked')){
    //         $$(this).prop('checked', true);
    //     }
    //     setTimeout(function(){
    //         myApp.closeModal();
    //
    //     },3000);
    //
    //     myApp.modal({
    //         title:  '',
    //         text: '<div><img src="'+packageJson.JAVA_STATICURL+'/images/icon/10.png"></div><div>'+$$('.order-prompt').text()+'</div>'
    //     })
    // } else {
    //     if(!$$(this).prop('checked')){
    //         $$('.five-fee').hide();
    //         $$('.five-part .item-after').hide();
    //         $$('.five-part .item-inner').addClass('nomonth');
    //     } else {
    //         $$('.five-fee').show();
    //         $$('.five-part .item-after').show();
    //         $$('.five-part .item-inner').removeClass('nomonth');
    //     }
    // }
});

$$('body').on('change', '.fund-radio', function () {
    //恢复按钮状态
    if ($$('.fund-radio:checked').val()) {
        $$('.fund-radio').prop('checked', false)
    } else {
        $$('.fund-radio').prop('checked', true)
    }

    //refundData.forceFlag === 1禁用状态不允许点击
    if (refundData.forceFlag === 1 && !$$('.fund-radio').prop('checked')) {
        return
    }
    let val = ''
    if ($$('.fund-radio').prop('checked')) {
        val = ''
    } else {
        val = $$('.fund-part').attr('data-time')
    }
    const data = {
        insurerId: id,
        cityId: $$('.refund-city').data("cityid"),
        insuranceMonth: ($$('.five-radio:checked').val() ? $$('.five-part').attr('data-time') : '') || '',
        housingFundMonth: val,
        type: 0,
        isEdit: 0,
        primary: 2,
    }
    myApp.showIndicator()
    $$.ajax({
        url: packageJson.JAVA_DOMAIN + util.ajaxUrl.refundInit,
        type: "POST",
        dataType: 'json',
        data: data,
        success: function (d) {
            myApp.hideIndicator()
            if (d.status === 0) {
                if ($$('.fund-radio').prop('checked')) {
                    $$('.fund-radio').prop('checked', false)
                    $$('.fund-fee').hide()
                    $$('.fund-part .item-after').hide()
                    $$('.fund-part .item-inner').addClass('nomonth')
                } else {
                    $$('.fund-radio').prop('checked', true)
                    $$('.fund-fee').show()
                    $$('.fund-part .item-after').show()
                    $$('.fund-part .item-inner').removeClass('nomonth')
                }
            } else {
                myApp.hideIndicator()
                myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>${d.msg}</div>`)
                //myApp.alert(d.msg)
            }
        },
        error: function () {
            myApp.hideIndicator()
            myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>服务器异常，获取数据列表失败</div>`)
            //myApp.alert('服务器异常，获取数据列表失败')
        }
    })

    // if(refundData.forceFlag === 4){
    //     $$('.refund-month input').prop('checked', true);
    //     setTimeout(function(){
    //         myApp.closeModal();
    //
    //     },3000);
    //
    //     myApp.modal({
    //         title:  '',
    //         text: '<div><img src="'+packageJson.JAVA_STATICURL+'/images/icon/10.png"></div><div>'+$$('.order-prompt').text()+'</div>'
    //     })
    // } else if(refundData.forceFlag === 3){
    //     if(!$$(this).prop('checked')){
    //         $$('.fund-fee').hide();
    //
    //         $$('.fund-part .item-after').hide();
    //         $$('.fund-part .item-inner').addClass('nomonth');
    //
    //     } else {
    //         var fundtime = $$('.fund-part').attr('data-time'),
    //             fivetime = $$('.five-part').attr('data-time');
    //         if(fundtime < fivetime) {
    //             $$('.fund-part').attr('data-time',fivetime);
    //             $$('.fund-month').html($$('.five-month').html())
    //         }
    //         $$('.refund-month input').prop('checked', true);
    //         $$('.fund-fee').show();
    //
    //         $$('.fund-part .item-after').show();
    //         $$('.fund-part .item-inner').removeClass('nomonth');
    //     }
    // } else if(refundData.forceFlag === 2){
    //     if(!$$(this).prop('checked')){
    //         $$(this).prop('checked', true);
    //     }
    //     setTimeout(function(){
    //         myApp.closeModal();
    //
    //     },3000);
    //
    //     myApp.modal({
    //         title:  '',
    //         text: '<div><img src="'+packageJson.JAVA_STATICURL+'/images/icon/10.png"></div><div>'+$$('.order-prompt').text()+'</div>'
    //     })
    // } else {
    //     if(!$$(this).prop('checked')){
    //         $$('.fund-fee').hide();
    //         $$('.fund-part .item-after').hide();
    //         $$('.fund-part .item-inner').addClass('nomonth');
    //     } else {
    //         $$('.fund-fee').show();
    //         $$('.fund-part .item-after').show();
    //         $$('.fund-part .item-inner').removeClass('nomonth');
    //     }
    // }
});

//退保城市
//加载选择退保城市时的选中项
myApp.onPageBeforeInit('refundcity', function () {
    var cityId = $$('.refund-city').data('cityid');


    var refundCityHtml = Template7.templates.refundcity(refundData.cityList);
    $$(".refundcity ul").html(refundCityHtml);

    $$.each(refundData.cityList, function (i, list) {
        if (cityId == refundData.cityList[i].cityId) {
            $$('.refundcity li').eq(i).find('input').attr("checked", true);
            return false;
        }
    })

})
//选择退保城市按钮
$$(document).on('click', '.js-refundcity-confirm', function () {
    // $$('.refund-city').attr('data-cityid', $$('.refundcity input:checked').val());
    data = {
        insurerId: id,
        cityId: $$('.refundcity input:checked').val(),
        isEdit: 0,
        type: 3
    }
    getData(data);
    // $$('.refund-city .item-after').text($$('.refundcity input:checked').parents('.item-inner').find('.item-title').text());
    mainView.router.back({
        'pageName': 'orderrefund'
    });
});


//退保月份
myApp.onPageBeforeInit('refundmonth', function (e) {
    var _this,
        time,
        dataList,
        type = e.query.type;
    if (type == 1) {
        _this = $$('.five-part');
        dataList = refundData.insureList;
        $$('.refundmonth').attr('data-type', '1')
    } else {
        _this = $$('.fund-part');
        dataList = refundData.houseList;
        $$('.refundmonth').attr('data-type', '2')
    }

    time = _this.attr('data-time');
    $$.each(dataList, function (i, list) {
        dataList[i].checked = 0;
        dataList[i].month = new Date(dataList[i].insuranceMonth * 1000).format('YY-MM');
        if (time == dataList[i].insuranceMonth) {
            dataList[i].checked = 1;
            // return false;
        }
    })
    var refundMonthHtml = Template7.templates.refundmonth(dataList);
    $$(".refundmonth ul").html(refundMonthHtml);

});


$$(document).on('change', '.refundmonth label', function (e) {
    var type = $$('.refundmonth').data('type'),
        val = $$(this).find('.refundmonth-radio').val();
    let fundText = ''
    let fiveText = ''
    if (type === '1') {
        data = {
            insurerId: id,
            cityId: $$('.refund-city').data("cityid"),
            insuranceMonth: val,
            housingFundMonth: ($$('.fund-radio:checked').val() ? $$('.fund-part').attr('data-time') : '') || '',
            type: 0,
            isEdit: 0,
            primary: 1
        };
    } else {
        data = {
            insurerId: id,
            cityId: $$('.refund-city').data("cityid"),
            housingFundMonth: val,
            insuranceMonth: ($$('.five-radio:checked').val() ? $$('.five-part').attr('data-time') : '') || '',
            type: 0,
            isEdit: 0,
            primary: 2
        };
    }


    myApp.showIndicator();
    $$.ajax({
        url: packageJson.JAVA_DOMAIN + util.ajaxUrl.refundInit,
        type: "POST",
        dataType: 'json',
        data: data,
        success: function (d) {
            myApp.hideIndicator();
            console.log(d)
            if (d.status === 0) {
                var refundmonth = $$('.refundmonth-radio:checked'),
                    val = refundmonth.val(),
                    time = refundmonth.parents('.item-inner').find('.label').text();
                if (d.data.insuranceRefundMonth !== 0) {
                    fiveText = new Date(d.data.insuranceRefundMonth * 1000).format('YY-MM')
                }

                if (d.data.housingFundRefundMonth !== 0) {
                    fundText = new Date(d.data.housingFundRefundMonth * 1000).format('YY-MM')
                }


                $$('.refund-fee').html('')
                if (d.data.housingFundRefundAmt) {
                    $$('.refund-fee').append('<div class="fund-fee">公积金：<span class="prompt-green ">&yen;' + d.data.housingFundRefundAmt + '</span></div>');
                }

                if (d.data.insuranceRefundAmt) {
                    $$('.refund-fee').prepend('<div class="five-fee">社保：<span class="prompt-green ">&yen; ' + d.data.insuranceRefundAmt + '</span></div>');
                }
                if (type === '1') {
                    $$('.five-month').text(time)
                    $$('.five-part').attr('data-time', val);
                    //修改公积金
                    if (d.data.housingFundRefundMonth !== 0) {
                        $$('.fund-month').text(fundText)
                        $$('.fund-part').attr('data-time', d.data.housingFundRefundMonth);
                    }
                    if (!$$('.fund-radio:checked').val() && d.data.housingFundRefundMonth) {//灰度禁用取消
                        $$('.fund-radio').prop('checked', true)
                        //显示
                        $$('.fund-fee').show();
                        $$('.fund-part .item-after').show();
                        $$('.fund-part .item-inner').removeClass('nomonth');
                    }
                    if (d.data.housingFundRefundMonth == 0) {//灰度禁用
                        $$('.fund-radio').prop('checked', false)
                        $$('.fund-fee').hide();
                        $$('.fund-part .item-after').hide();
                        $$('.fund-part .item-inner').addClass('nomonth');
                    }
                    if (d.data.approveStatus === 1) {
                        myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>当前城市，社保和公积金强制缴纳，起退时间须一致</div>`)
                        //myApp.alert('当前城市，社保和公积金强制缴纳，起退时间须一致')
                    } else if (d.data.approveStatus === 2) {
                        myApp.alert(`<div><i class="iconfont icon-tanhao-copy"></i><br>当前城市，社保和公积金强制缴纳，公积金无法正常办理须强制全月退款</div>`)
                        //myApp.alert('当前城市，社保和公积金强制缴纳，公积金无法正常办理须强制全月退款')
                    }
                } else {
                    $$('.fund-month').text(time)
                    $$('.fund-part').attr('data-time', val)
                    //修改社保
                    if (d.data.insuranceRefundMonth !== 0) {
                        $$('.five-month').text(fiveText)
                        $$('.five-part').attr('data-time', d.data.insuranceRefundMonth)
                    }
                    if (!$$('.five-radio:checked').val() && d.data.insuranceRefundMonth) {//灰度禁用取消
                        $$('.five-radio').prop('checked', true)
                        //显示
                        $$('.five-fee').show()
                        $$('.five-part .item-after').show()
                        $$('.five-part .item-inner').removeClass('nomonth')
                    }
                    if (d.data.insuranceRefundMonth == 0) {//灰度禁用
                        $$('.five-radio').prop('checked', false)
                        //隐藏
                        $$('.five-fee').hide()
                        $$('.five-part .item-after').hide()
                        $$('.five-part .item-inner').addClass('nomonth')
                    }
                    if (d.data.approveStatus === 1) {
                        myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>当前城市，社保和公积金强制缴纳，起退时间须一致</div>`)
                        //myApp.alert('当前城市，社保和公积金强制缴纳，起退时间须一致')
                    } else if (d.data.approveStatus === 2) {
                        myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>当前城市，社保和公积金强制缴纳，社保无法正常办理须强制全月退款</div>`)
                        //myApp.alert('当前城市，社保和公积金强制缴纳，社保无法正常办理须强制全月退款')
                    }
                }
            } else {
                myApp.hideIndicator();
                myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>${d.msg}</div>`)
                // myApp.alert(d.msg)
            }
            mainView.router.back({
                'pageName': 'orderrefund'
            });
        },
        error: function () {
            myApp.hideIndicator();
            myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>服务器异常，获取数据列表失败</div>`)
            //myApp.alert('服务器异常，获取数据列表失败11');
        }
    });


});


//退保原因
myApp.onPageBeforeInit('refundreason', function (e) {

    var reason = $$('.refund-reason-select span').text().trim();
    var refundReasonHtml = Template7.templates.refundreason(refundData.refundReasonList);
    $$(".refundreason ul").html(refundReasonHtml);
    $$.each(refundData.refundReasonList, function (i, list) {
        if (reason == refundData.refundReasonList[i].paramValue) {
            $$('.refundreason li').eq(i).find('input').attr("checked", true);
            return false;
        }
    })

})
//选择退款原因
$$('body').on('click', '.refundreason li', function () {
    var id = $$(this).find('.select-reason-radio').val();

    $$('.refund-reason-select span').text($$(this).find('.item-title').text());
    mainView.router.back({
        'pageName': 'orderrefund'
    });
});

//选择银行卡
myApp.onPageBeforeInit('selectbank', function (e) {
    $$.ajax({
        url: packageJson.JAVA_DOMAIN + util.ajaxUrl.bankList,
        type: 'POST',
        dataType: 'json',
        data: {},
        success: function (d) {
            if (d.status === 0) {
                var id = $$('.card-bank').data("bankid");
                if (id) {
                    $$.each(d.data, function (i, list) {
                        list.checked = 0;
                        if (id == list.paramCode) {
                            list.checked = 1;
                        }
                    })
                }

                var selectBankHtml = Template7.templates.selectbank(d.data);
                $$(".select-bank ul").html(selectBankHtml);
            } else {
                myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>${d.msg}</div>`)
                //myApp.alert(d.msg)
            }
        },
        error: function () {
            myApp.hideIndicator();
            myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>加载失败，请刷新重试！</div>`)
            //myApp.alert('加载失败，请刷新重试！');
        }
    });
});


//获取银行卡列表
myApp.onPageBeforeInit('refundcard', function (e) {
    getRefundCard();
});

function getRefundCard() {
    $$.ajax({
        url: packageJson.JAVA_DOMAIN + util.ajaxUrl.bankCardList,
        type: 'POST',
        dataType: 'json',
        data: {},
        success: function (d) {
            if (d.status === 0) {
                if (d.data.length > 0) {
                    $$(".refundcard").show();
                    $$('.content-block-title').show();

                    $$.each(d.data, function (i, list) {
                        list.bankAccount = list.bankAccount.substring(0, 3) + ' **** **** ' + list.bankAccount.substring(list.bankAccount.length - 4, list.bankAccount.length);
                        list.bankHolder = list.bankHolder.substring(0, 3);
                    });
                    var refundCardHtml = Template7.templates.refundcard(d.data);
                    $$(".refundcard ul").html(refundCardHtml);
                } else {
                    $$(".refundcard").hide();
                    $$('.content-block-title').hide();
                }

            } else {
                myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>${d.msg}</div>`)
                //myApp.alert(d.msg)
            }
        },
        error: function () {
            myApp.hideIndicator();
            myApp.alert(`<div><i class="iconfont icon-tanhao-copy tanhao"></i><br>加载失败，请刷新重试！</div>`)
            //myApp.alert('加载失败，请刷新重试！');
        }
    });
}

$$('body').on('click', '.amt-prompt', function () {
    myApp.modal({
        title: '',
        text: '<div><div><img src="' + packageJson.JAVA_STATICURL + '/images/icon/10.png"></div><div class="prompt-red" style="text-align:left">注意，订单提交后即会产生服务成本，因此退款均不退服务费！</div><div style="text-align:left">退款金额=可退保费-支付成本-转账成本</div><div style="text-align:left; color:#999">可退保费：退款月份的实收保费之和；<br>支付成本：退款月份实收保费之和*0.6%的手续费；<br>转账成本：固定金额10元；</div><div class="prompt-red" style="text-align:left">最终退款金额以社保局的账单为准。</div></div>',
        buttons: [
            {
                text: '知道了',
                onClick: function () {
                }
            }
        ]
    });
});