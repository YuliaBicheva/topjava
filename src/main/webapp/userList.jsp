<%@ page import="ru.javawebinar.topjava.AuthorizedUser" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User list</title>
    <style>
        .selected{
            background-color: lightgrey;
        }
    </style>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<h2>User list</h2>
<table border="0">
    <tr>
        <th>#</th>
        <th>Name</th>
        <th>Email</th>
        <th>Password</th>
        <th>Calories per day</th>
        <th>Actions</th>
    </tr>
<c:forEach items="${userList}" var="user" varStatus="loop">
    <jsp:useBean id="user" scope="page" type="ru.javawebinar.topjava.model.User"/>
    <tr class="<%= AuthorizedUser.id() == user.getId() ? "selected" : ""%>">
        <td>${loop.count}</td>
        <td>${user.name}</td>
        <td>${user.email}</td>
        <td>${user.password}</td>
        <td>${user.caloriesPerDay}</td>
        <td><a href="users?action=select&id=${user.id}">Select current user</a></td>
    </tr>
</c:forEach>
</table>
</body>
</html>
