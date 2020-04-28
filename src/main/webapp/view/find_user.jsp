<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Find user</title>
    <style>
        <%@include file="css/style.css" %>
    </style>
</head>
<body>
<c:import url="navibar.jsp"/>
<form:form id="form" name="form" method="post" action="findUser" modelAttribute="user">
    <table>
        <tbody>
        <tr>
            <td>
                <p>Email</p>
            </td>
            <td>
                <form:input type="text" path="email" id="email"/>
                <form:errors path="email" cssClass="errror"/>
            </td>
        </tr>
        </tbody>
    </table>
    <button type="submit" class="button">Find</button>
</form:form>
<c:if test="${not empty error}">
    <p style="color: red">${error}</p>
</c:if>
</body>
</html>