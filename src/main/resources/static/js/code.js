var myApp = new Framework7({
    precompileTemplates: true,
    modalTitle: '',
    modalButtonOk: '知道了'
});

var mainView = myApp.addView('.view-main', {
    dynamicNavbar: true,
    domCache: true
});


function orderPrompt(text) {
    return '<div class="order-prompt">' + text + '</div>';
}


var coupons = "${info.coupons}";
var cards = "${info.discount_cards}";

//退保原因
myApp.onPageBeforeInit('refundreason', function (e) {

    var reason = $$('.refund-reason-select span').text().trim();
    var refundReasonHtml = Template7.templates.refundreason(cards);
    $$(".refundreason ul").html(refundReasonHtml);
    $$.each(cards, function (i, list) {
        if (reason == cards.user_price) {
            $$('.refundreason li').eq(i).find('input').attr("checked", true);
            return false;
        }
    })

});

//选择退款原因
$$('body').on('click', '.refundreason li', function () {
    var id = $$(this).find('.select-reason-radio').val();

    $$('.refund-reason-select span').text($$(this).find('.item-title').text());
    mainView.router.back({
        'pageName': 'code'
    });
});