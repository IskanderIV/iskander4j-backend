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

<div>
    <c:choose>
        <c:when test="${isDeviceAdj == true && deviceDiscrete == 1.0}">
            111
        </c:when>

        <c:when test="${isDeviceAdj == true && deviceDiscrete < 0.9}">
            222
        </c:when>

        <c:otherwise>
            333
        </c:otherwise>
    </c:choose>
</div>