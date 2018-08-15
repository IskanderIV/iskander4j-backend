<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="devices" value="${requestScope.devices}"/>

<div>
    <form:form class="form-signin" method="post" modelAttribute="deviceList" role="form">
        <div class="form-group">
            <c:forEach var="device" items="${devices}">
                <c:set var="device" value="${device}" scope="request"/>
                <c:import url="devices/device.jsp"/>
            </c:forEach>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <button class="btn btn-lg btn-primary btn-block" type="submit">Update</button>

        </div>
    </form:form>
</div>