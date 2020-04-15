<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Details</title>
    <style>
        <%@include file="style.css" %>
    </style>
</head>
<body>
<c:import url="navibar.jsp"/>
<table class="zui-table">
    <thead>
    <tr>
        <th>First Name</th>
        <th>Last Name</th>
        <th>User Role</th>
        <th>User Status</th>
        <th>Course</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>
            <c:out value="${user.firstName}"/>
        </td>
        <td>
            <c:out value="${user.lastName}"/>
        </td>
        <td>
            <c:out value="${user.userRole}"/>
        </td>
        <td>
            <c:out value="${user.status}"/>
        </td>
        <td>
            <a href="${pageContext.request.contextPath}/course/get?id=${user.course.id}" class="button"
               role="button"
               tabindex="0">${user.course.title}</a><br>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>