<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>User list</title>
    <style>
        .listTable{
            border-collapse: collapse;
        }
        .listTable td,
        .listTable th{
            border: 1px solid gray;
            padding: 5px;
            text-align: center;
        }
        .red{
            color: red;
        }
        .green{
            color: green;
        }
    </style>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<h2>User list</h2>

<c:choose>
    <c:when test="${empty mealsList}">
        <p>User haven't meals</p>
    </c:when>
    <c:otherwise>
        <table class="listTable" c>
            <tr>
                <th>Date</th>
                <th>Description</th>
                <th>Calories</th>
            </tr>
            <c:forEach items="${mealsList}" var="meal">
                <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealWithExceed"/>
                <tr class="${meal.exceed ? 'red' : 'green'}">
                    <td>
                        <%= TimeUtil.formatDate(meal.getDateTime())%>
                    </td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>
