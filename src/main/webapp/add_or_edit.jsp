<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meal add or update</title>
</head>
<body>
<form action="meals" method="post">
    <jsp:useBean id="userMeal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
    <input type="hidden" name="id" value="${userMeal.id}"/>
    <label>Date: </label>
    <input id="datetimepicker"  type="datetime-local" name="dateTime" value="<%= TimeUtil.formatDate(userMeal.getDateTime())%>"/>

    <label>Description: </label>
    <input type="text" name="description" value="${userMeal.description}"/>

    <label>Calories: </label>
    <input type="number" name="calories" value="${userMeal.calories}"/>

    <input type="submit" value="${empty userMeal.id ? 'Save' : 'Update'}"/>
</form>
</body>

<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery.datetimepicker.css'/>" />
<script src="<c:url value='/js/jquery.js'/>"></script>
<script src="<c:url value='/js/jquery.datetimepicker.full.js'/>"></script>
<script>
    jQuery('#datetimepicker').datetimepicker({
            format: 'd.m.Y H:i'
    });
</script>
</html>
