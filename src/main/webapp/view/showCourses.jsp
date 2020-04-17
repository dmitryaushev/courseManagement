<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Show Courses</title>
    <style>
        <%@include file="css/style.css" %>
    </style>
</head>
<body>
<c:import url="navibar.jsp"/>
<c:if test="${not empty courses}">
    <table class="zui-table myform">
        <thead>
        <tr>
            <th>Title</th>
            <th>Course status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${courses}" var="course">
            <tr>
                <td>
                    <c:out value="${course.title}"/>
                </td>
                <td>
                    <c:out value="${course.courseStatus}"/>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/course/get?id=${course.id}" class="button" role="button"
                       tabindex="0">Detailed</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
</body>
</html>
