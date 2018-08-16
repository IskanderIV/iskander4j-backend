<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="device" value="${requestScope.device}" scope="request"/>
<c:set var="index" value="${requestScope.index}"/>
<c:set var="deviceId" value="${device.id}"/>
<c:set var="isDeviceAdj" value="${device.adj}"/>
<c:set var="isDeviceRotate" value="${device.rotate}"/>
<c:set var="isDeviceSignaling" value="${device.signaling}"/>
<c:set var="deviceAck" value="${device.ack}"/>
<c:set var="deviceDiscrete" value="${device.discrete}"/>
<c:set var="deviceCtrlVal" value="${device.ctrlVal}"/>
<c:set var="hasDeviceRadioError" value="${device.radioErr}"/>
<c:set var="hasDeviceDataError" value="${device.dataError}"/>
<c:set var="hasDeviceControlError" value="${device.controlError}"/>
<c:set var="error" value="${hasDeviceDataError || (isDeviceAdj && hasDeviceControlError) || hasDeviceRadioError}"/>

<div>
    <h3>Device_${deviceId}</h3>
    <label for="device_${deviceId}_ack">Data</label>
    <form:input type="text" id="device_${deviceId}_ack" path="devices[${index}].ack" value="${error ? '0' : deviceAck}"
                readonly="true"/>

    <c:set var="errorText" value="Device Error"/>

    <c:if test="${error}">
        <c:choose>
            <c:when test="${hasDeviceRadioError}">
                <c:set var="errorText" value="Device Radio Error"/>
            </c:when>

            <c:when test="${hasDeviceDataError}">
                <c:set var="errorText" value="Device Data Error"/>
            </c:when>

            <c:when test="${(isDeviceAdj && hasDeviceControlError)}">
                <c:set var="errorText" value="Device Control Error"/>
            </c:when>
        </c:choose>
        <snap>${errorText}</snap>
    </c:if>
    <br>

</div>