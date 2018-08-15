<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="deviceItem" value="${requestScope.device}"/>
<c:set var="isDeviceAdj" value="${deviceItem.adj}"/>
<c:set var="deviceDiscrete" value="${deviceItem.discrete}"/>

<div>
    <c:choose>
        <c:when test="${isDeviceAdj && deviceDiscrete == 1.0}">
            <jsp:include page="includes/AdjustableWithDiscreteOutputDevice.jsp">
                <jsp:param name="deviceItem" value="${deviceItem}"/>
            </jsp:include>
        </c:when>

        <c:when test="${isDeviceAdj && deviceDiscrete < 0.9}">
            <jsp:include page="includes/AdjustableWithAnalogOutputDevice.jsp">
                <jsp:param name="deviceItem" value="${deviceItem}"/>
            </jsp:include>
        </c:when>

        <c:otherwise>
            <jsp:include page="includes/NonAdjustableDevice.jsp">
                <jsp:param name="deviceItem" value="${deviceItem}"/>
            </jsp:include>
        </c:otherwise>
    </c:choose>
</div>