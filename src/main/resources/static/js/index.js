$(function () {
	$("#tab-login").click(function () {
		$('body').addClass("login-active");
	});
	$("#tab-regist").click(function () {
		$('body').removeClass("login-active");
	});
	$(".regist-yzm").click(function () {
		$(this).removeClass("regist-btn-active");
		$(".regist-pwd,.login-btn-img").addClass("regist-btn-active");
		$(".regist-item-yzm").addClass("regist-item-active");
		$(".regist-item-pwd").removeClass("regist-item-active");
	});
	$(".regist-pwd").click(function () {
		$(this).removeClass("regist-btn-active");
		$(".login-btn-img").removeClass("regist-btn-active");
		$(".regist-yzm").addClass("regist-btn-active");
		$(".regist-item-pwd").addClass("regist-item-active");
		$(".regist-item-yzm").removeClass("regist-item-active");
	});
	$(".info-vip").click(function () {
		$(".index-vip").show();
	});
	$(".index-vip-top").click(function (e) {
		$(".index-vip").hide();
	})

	$(".bshow-btn").click(function () {
		$(".bshow-box").show();
	})
	$(".bshow-box-shadow").click(function () {
		$(".bshow-box").hide();
	});
	$("#resigt_yzm").click(function () {
		setTime($(this), 60);
	});
	$("#login_yzm").click(function () {
		setTime($(this), 60);
	})

	function setTime(obj, time) {
		setTimeout(function () {
			if (time > 0) {
				time = time - 1;
				obj.text(time + 's后重新发送');
				setTime(obj, time);
			} else {
				obj.text('重新发送');
			}
		}, 1000)
	}

	$("#lg-agree").click(function () {
		if ($(this).is(':checked')) {
			$("#lg-btn a").attr('disabled', false)
		} else {
			$("#lg-btn a").attr('disabled', true)
		}
	})


	$("#lg-btn").click(function () {
		if ($("#lg-name").val() == '') {
			alert("姓名不能为空")
		} else if ($("#lg-birth").val() == '') {
			alert("生日不能为空")
		} else if ($("#lg-telphone").val() == '') {
			alert("手机号码不能为空")
		} else if ($("#lg-yzm").val() == '') {
			alert("验证码不能为空")
		} else if ($("#lg-pwd").val() == '') {
			alert("密码不能为空")
		} else {
			window.location.href = "fillself.html";
		}
	})

	$("#sign-btn").click(function () {
		if ($("#sign-telphone").val() == '') {
			alert("手机号码不能为空！")
		} else if ($("#sign-pwd").val() == '' || $("#sign-yzm").val() == '') {
			alert("密码或验证码不能为空！")
		}
	})


	$("#update-btn").click(function () {
		if ($("#update-pwd1").val() == '') {
			alert("密码不能为空！")
		} else if ($("#update-pwd1").val() !== $("#update-pwd2").val()) {
			alert("两次输入不相同！")
		}
	})
})

      

