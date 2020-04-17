<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Course created</title>
    <style>
        <%@include file="css/style.css" %>
    </style>
</head>
<body>
<c:import url="navibar.jsp"/>
<p align="myform">Course with title ${course_title} created</p>
</body>
</html>
