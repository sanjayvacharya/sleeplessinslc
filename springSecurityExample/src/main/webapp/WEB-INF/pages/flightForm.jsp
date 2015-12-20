<%@ include file="/taglibs.jsp"%>

<head>
<title>Flight Details</title>
</head>

<div class="span3">
	<h2>Flight Details</h2>
</div>
<div class="span7">
	
	<form:form commandName="flight" action='/flightForm.html'
		method="post">
		<form:hidden path="id"/>
		<div class="well form-horizontal">
			<spring:bind path="number">
			 <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			</spring:bind>	
				<label class="control-label">Flight Number:</label>
				<div class="controls">
					<form:input path="number" />
					<form:errors path="number" cssClass="help-inline" />
				</div>
			</fieldSet>
			<spring:bind path="fromAirportCode">
			 <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			</spring:bind>	
				<label class="control-label">From Airport Code:</label>
				<div class="controls">
					<select name="fromAirportCode" STYLE="width: 250px">
						<c:forEach items="${airports}" var="airport">
							<option
								<c:if test="${flight.fromAirportCode eq airport.code}">
                        		 selected="selected"
                        		 </c:if>
								value='<c:out value="${airport.code}"/>'>
								<c:out value='${airport.name}' />
							</option>
						</c:forEach>
					</select>
				</div>
			</fieldset>
			
			<spring:bind path="toAirportCode">
			 <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			</spring:bind>	
				<label class="control-label">To:</label>
				<div class="controls">
					<select name="toAirportCode" STYLE="width: 250px">
						<c:forEach items="${airports}" var="airport">
							<option
								<c:if test="${flight.toAirportCode eq airport.code}">
                         			selected="selected"
                         		</c:if>
								value='<c:out value="${airport.code}"/>'>
								<c:out value='${airport.name}' />
							</option>
						</c:forEach>
					</select>
				</div>
			</fieldset>
			
			<spring:bind path="departureDate">
			 <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			</spring:bind>	
				<label class="control-label">Departure Date:</label>
				<div class="controls">
					<div class="clearfix">
           			 <div class="input">
             			 <div class="inline-inputs">
                			<input data-datepicker="datepicker" class="small" type="text"  name="departureDate" 
                			 value="<fmt:formatDate value="${flight.departureDate}" type="both" pattern="MM-dd-yyyy" />" />
             			 </div>
            		 </div>
         			</div>
				</div>
			</fieldset>
			
			<spring:bind path="departureTime">
			 <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			</spring:bind>	
				<label class="control-label">Departure Time (HH:mm):</label>
				<div class="controls">
					<form:input path="departureTime" />
					<form:errors path="departureTime" cssClass="help-inline" />
				</div>
			</fieldset>

			<spring:bind path="arrivalDate">
			 <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			</spring:bind>	
				<label class="control-label">Arrival Date:</label>
				<div class="controls">
					<input name="arrivalDate" data-datepicker="datepicker" class="small" type="text" value="<fmt:formatDate value="${flight.arrivalDate}" type="both" pattern="MM-dd-yyyy" />" />
				</div>
			</fieldset>
			<spring:bind path="arrivalTime">
			 <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			</spring:bind>	
				<label class="control-label">Arrival Time (HH:mm):</label>
				<div class="controls">
					<form:input path="arrivalTime" />
					<form:errors path="arrivalTime" cssClass="help-inline" />
				</div>
			</fieldset>
			<spring:bind path="numberOfSeats">
			 <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			</spring:bind>	
				<label class="control-label">Number Of Seats:</label>
				<div class="controls">
					<form:input path="numberOfSeats" />
					<form:errors path="numberOfSeats" cssClass="help-inline" />
				</div>
			</fieldset>		
			<spring:bind path="miles">
			 <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			</spring:bind>	
				<label class="control-label">Miles:</label>
				<div class="controls">
					<form:input path="miles" />
					<form:errors path="miles" cssClass="help-inline" />
				</div>
			</fieldset>		
	</form:form>
			<fieldset class="form-actions">
				<input type="submit" class="btn btn-primary" name="save"
					value="Save" />
				<input type="submit" class="btn btn-primary" name="cancel"
					value="Cancel" />	
			</fieldset>
</div>
