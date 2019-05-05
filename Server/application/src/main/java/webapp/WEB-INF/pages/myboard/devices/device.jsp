<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="device" value="${requestScope.device}" scope="request"/>
<c:set var="index" value="${requestScope.index}"/>
<c:set var="isDeviceAdj" value="${device.adj}"/>
<c:set var="deviceDiscrete" value="${device.discrete}"/>

<div>
    <c:choose>
        <c:when test="${isDeviceAdj && deviceDiscrete == 1.0}">
            <c:import url="devices/includes/adjustableWithDiscreteOutputDevice.jsp"/>
        </c:when>

        <c:when test="${isDeviceAdj && deviceDiscrete < 0.9}">
            <c:import url="devices/includes/adjustableWithAnalogOutputDevice.jsp"/>
        </c:when>

        <c:otherwise>
            <c:import url="devices/includes/nonAdjustableDevice.jsp"/>
        </c:otherwise>
    </c:choose>
</div>