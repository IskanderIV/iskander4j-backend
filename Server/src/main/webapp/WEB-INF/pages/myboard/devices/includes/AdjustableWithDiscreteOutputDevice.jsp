<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="deviceItem" value="${requestScope.device}"/>
<c:set var="deviceId" value="${deviceItem.id}"/>
<c:set var="isDeviceAdj" value="${deviceItem.adj}"/>
<c:set var="isDeviceRotate" value="${deviceItem.rotate}"/>
<c:set var="isDeviceSignaling" value="${deviceItem.signaling}"/>
<c:set var="deviceAck" value="${deviceItem.ack}"/>
<c:set var="deviceDiscrete" value="${deviceItem.discrete}"/>
<c:set var="deviceCtrlVal" value="${deviceItem.ctrlVal}"/>
<c:set var="hasDeviceRadioError" value="${deviceItem.radioErr}"/>
<c:set var="hasDeviceDataError" value="${deviceItem.dataError}"/>
<c:set var="hasDeviceControlError" value="${deviceItem.controlError}"/>
<c:set var="error" value="${hasDeviceDataError || (isDeviceAdj && hasDeviceControlError) || hasDeviceRadioError}"/>

<div>
    <h3>Device_${deviceId}</h3>
    <label for="device_${deviceId}_ack">Data</label>
    <input type="text" id="device_${deviceId}_ack" name="ack" readonly
           value="${error ? '0' : deviceAck}"/>
    <label for="device_${deviceId}_ctrl">Control</label>
    <form:input type="checkbox" id="device_${deviceId}_ctrl" path="${deviceItem.ctrlVal}" name="ctrlVal"
                checked="checked"
                value="${error ? '0' : deviceCtrlVal}"/>

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