<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="deviceListForm" value="${requestScope.deviceListForm}"/>

<div>
    <form:form class="form-signin" method="post" modelAttribute="deviceListForm" role="form">
        <div class="form-group">
            <c:forEach var="device" items="${deviceListForm.devices}" varStatus="dStatus">
                <c:set var="device" value="${device}" scope="request"/>
                <c:set var="index" value="${dStatus.index}" scope="request"/>
                <form:input type="hidden" path="devices[${dStatus.index}].id" value="${device.id}"/>
                <form:input type="hidden" path="devices[${dStatus.index}].adj" value="${device.adj}"/>
                <form:input type="hidden" path="devices[${dStatus.index}].rotate" value="${device.rotate}"/>
                <form:input type="hidden" path="devices[${dStatus.index}].signaling"
                            value="${device.signaling}"/>
                <form:input type="hidden" path="devices[${dStatus.index}].discrete"
                            value="${device.discrete}"/>
                <form:input type="hidden" path="devices[${dStatus.index}].radioErr"
                            value="${device.radioErr}"/>
                <form:input type="hidden" path="devices[${dStatus.index}].dataError"
                            value="${device.dataError}"/>
                <form:input type="hidden" path="devices[${dStatus.index}].controlError"
                            value="${device.controlError}"/>
                <c:import url="devices/device.jsp"/>
            </c:forEach>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <button class="btn btn-lg btn-primary btn-block" type="submit">Update</button>

        </div>
    </form:form>
</div>