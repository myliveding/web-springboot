var myApp = new Framework7({
//Tell Framework7 to compile templates on app init
    precompileTemplates: true,
    modalTitle: '',
    modalButtonOk: '知道了'
});

var mainView = myApp.addView('.view-main', {
    dynamicNavbar: true,
    domCache: true
});


getData(data);

function getData(data) {
    myApp.hideIndicator();
    var coupons = "${}";
    var cards = "${}";

    if (refundData.insuranceRefundMonth) {
        if (refundData.insureList.length > 1) {
            $$('.five-part a').attr('href', 'select.jsp?type=1');
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
            $$('.fund-part a').attr('href', 'select.jsp?type=2');
            $$('.fund-part').addClass('item-link');
        }
        $$('.fund-part').attr('data-time', refundData.housingFundRefundMonth);
        $$('.fund-part .item-after a').text(new Date(refundData.housingFundRefundMonth * 1000).format('YY-MM'));
        $$('.fund-part').show()
    } else {
        $$('.fund-part').hide()
    }

    mainView.router.back({
        'pageName': 'code'
    });
}

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
});