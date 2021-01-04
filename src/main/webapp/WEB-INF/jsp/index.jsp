<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<br>
<section>
    <p><spring:message code="app.description"/></p>
    <p><a class="btn" href="swagger-ui.html" target="_blank">Swagger REST Api Documentation</a></p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>