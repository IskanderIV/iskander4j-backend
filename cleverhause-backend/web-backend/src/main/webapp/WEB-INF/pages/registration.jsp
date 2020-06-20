<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>admin page</title>
    <!-- Bootstrap -->
    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/styles/jumbotron-narrow.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="header">
        <ul class="nav nav-pills pull-right">
            <li><a href="${contextPath}/home">Home</a></li>
            <li><a href="${contextPath}/myboard/myboard">Boards</a></li>
            <li><a href="#">About</a></li>
            <li><a href="${contextPath}/contacts">Contact</a></li>
        </ul>
        <h3 class="text-muted">Hello, ${userName}!</h3> <a href="${contextPath}/login?logout"><h3>logout</h3></a>
    </div>
    <div class="jumbotron">
        <div>
            <form:form method="post" modelAttribute="userForm" class="form-signin">
                <h2 class="form-signin-heading">Create your account</h2>
                <spring:bind path="username">
                    <div class="form-group ${status.error ? 'has-error' : ''}">
                        <form:input type="text" path="username" class="form-ctrlVal" placeholder="Username"
                                    autofocus="true"/>
                        <form:deviceErrors path="username"/>
                    </div>
                </spring:bind>
                <spring:bind path="password">
                    <div class="form-group ${status.error ? 'has-error' : ''}">
                        <form:input type="password" path="password" class="form-ctrlVal"
                                    placeholder="Password"/>
                        <form:deviceErrors path="password"/>
                    </div>
                </spring:bind>
                <spring:bind path="confirmPassword">
                    <div class="form-group ${status.error ? 'has-error' : ''}">
                        <form:input type="password" path="confirmPassword" class="form-ctrlVal"
                                    placeholder="Confirm password"/>
                        <form:deviceErrors path="confirmPassword"/>
                    </div>
                </spring:bind>

                <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
            </form:form>
        </div>
    </div>
    <div class="footer">
        <p>&copy; Ivanov's 2017</p>
    </div>
</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<!--<script src="/resources/jQuery/jquery-3.2.1.min.js"></script>-->
<script src="${contextPath}/resources/jQuery/jquery-3.2.1.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>