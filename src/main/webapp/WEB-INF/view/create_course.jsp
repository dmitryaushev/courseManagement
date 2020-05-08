<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Create Course</title>
    <link href="${contextPath}/resources/css/style.css" rel="stylesheet"/>
</head>
<body>
<c:import url="${contextPath}/WEB-INF/view/navibar.jsp"/>
<div id="stylized" class="myform">
    <form:form id="form" name="form" method="post" action="createCourse" modelAttribute="course">
        <h1>Create project form</h1>
        <label>Course title
            <span class="small">Course title</span>
        </label>
        <form:input type="text" path="title" id="title"/>
        <form:errors path="title" cssClass="error"/>
        <label>Course Status
            <span class="small">Select status</span>
        </label>
        <form:select path="courseStatus" items="${courseStatuses}"/>
        <form:errors path="courseStatus" cssClass="error"/>
        <button type="submit" class="button">Create</button>
        <div class="spacer"></div>
    </form:form>
    <c:if test="${not empty error}">
        <p style="color: red">${error}</p>
    </c:if>
</div>
</body>
</html>