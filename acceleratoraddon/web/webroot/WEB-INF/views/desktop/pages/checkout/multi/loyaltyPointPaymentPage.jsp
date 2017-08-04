<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi" %>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/desktop/address" %>
 
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">
 
 	<div id="globalMessages">
    	<common:globalMessages/>
	</div>
 
    <multi-checkout:checkoutProgressBar steps="${checkoutSteps}" progressBarId="${progressBarId}"/>
 
 	<div class="span-14 append-1">
 		<div id="checkoutContentPanel" class="clearfix">
            <div class="headline">Loyalty point payment</div>
			<div class="description"><p>Amount of loyalty point that you want to spend</p></div>
            <form:form method="post" modelAttribute="loyaltyPointAmountForm">	
            	<div class="clearfix">
            		<form:input path="loyaltyPointAmount" />
	 				<form:errors path="loyaltyPointAmount" cssStyle="color: red;" element="div"/>
            	</div>
	 			<div class="form-actions">
					<a class="button" href="<c:url value="${previousStepUrl}"/>"><spring:theme code="checkout.multi.cancel" text="Cancel"/></a>
	            	<button id="loyaltyPointPayment_continue_button" class="show_processing_message">
						<spring:theme code="checkout.multi.loyaltyPointPayment.continue" text="Continue"/>
					</button>
				</div>
	 		</form:form>
        </div>
 		
 	</div>
 
    <multi-checkout:checkoutOrderDetails cartData="${cartData}" showShipDeliveryEntries="true" showPickupDeliveryEntries="true" showTax="false"/>
    <cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
        <cms:component component="${feature}"/>
    </cms:pageSlot>
 
</template:page> 