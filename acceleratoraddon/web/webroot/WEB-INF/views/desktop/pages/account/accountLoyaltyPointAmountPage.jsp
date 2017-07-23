<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="headline">
	Loyalty Points
</div>
<p>Amount: ${fn:escapeXml(customerData.loyaltyPointAmount)}</p>