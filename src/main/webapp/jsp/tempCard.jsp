<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" flush="true"/>
<body>
<script src="${pageContext.request.contextPath}/js/jquery-1.12.3.min.js"></script>
<script>

    $(document).ready(function () {
        $.ajax({
            'url': "${pageContext.request.contextPath}/rest/receiveCard",
            'type': 'post',
            'dataType': 'json',
            'data': {
                cardId: "${cardId}"
            },
            success: function success(d) {
                if (d.status == 0) {
                    window.location.href = "${pageContext.request.contextPath}/login/gotoDiscountCard";
                } else {
                    alert(d.errmsg);
                    window.location.href = "${pageContext.request.contextPath}/login/gotoDiscountCard";
                    return;
                }
            }
        });
    });

</script>
</body>
</html>
