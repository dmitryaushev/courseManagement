<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<form method="post" action="findUser">
    <table>
        <tbody>
        <tr>
            <td>
                <p>Email</p>
            </td>
            <td><input type="text" name="email"></td>
        </tr>
        </tbody>
    </table>
    <button type="submit" class="button">Find</button>
</form>
<c:if test="${not empty errors}">
    <c:forEach items="${errors}" var="error">
        <p style="color: red">${error.field} ${error.error}</p>
    </c:forEach>
</c:if>
</body>
</html>