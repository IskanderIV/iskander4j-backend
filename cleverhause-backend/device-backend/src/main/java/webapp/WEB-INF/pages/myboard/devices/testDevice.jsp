<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="device" value="${requestScope.device}" scope="request"/>
<c:set var="index" value="${param.index}"/>

<div>
    <form:input type="hidden" path="devices[${index}].ack" value="${device.ack}"/>
</div>