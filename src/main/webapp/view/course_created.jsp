<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Course created</title>
    <style>
        <%@include file="style.css" %>
    </style>
</head>
<body>
<c:import url="navibar.jsp"/>
<p>Course with title ${courseTitle} created</p>
</body>
</html>
