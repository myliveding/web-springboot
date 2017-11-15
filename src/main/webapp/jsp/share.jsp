<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body>
<script>

    var goUrl = '';
    var name = "${name}"
    var cardId = "${cardId}";
    if (name == 'card') {
        goUrl = shareUrl + 'cardId=' + cardId + '&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect'
    }
    window.location.href = goUrl;
</script>
</body>
</html>
