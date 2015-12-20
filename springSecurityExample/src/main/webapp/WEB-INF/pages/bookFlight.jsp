<%@ include file="/taglibs.jsp"%>

<head>
<title>Book Seats</title>
</head>

<div class="span3">
	<h2>Book Seats</h2>
	Book Seats on Flight:
	<c:out value="${flight.number}" />
</div>
<div class="span7">
	<div class="well form-horizontal">
		<div class="control-group">
			<label class="control-label">Flight Number:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${flight.number}" /></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">From:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${flight.from.city}" />|<c:out value="${flight.from.code}" /></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">To:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${flight.to.city}" />|<c:out value="${flight.to.code}" /></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Departure Time:</label>
			<div class="controls">
				<span class="uneditable-input"><joda:format
						value="${flight.departureTime}" style="MM" /></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Arrival Time:</label>
			<div class="controls">
				<span class="uneditable-input"><joda:format
						value="${flight.arrivalTime}" style="MM" /></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">Seats Available:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${flight.seatsAvailable}" /></span>
			</div>
		</div>
		<div class="row-fluid">
			<c:if test="${not empty errors}">
				<div class="alert alert-block alert-error fade in">
					<c:forEach var="errorItem" items="${errors}">
						<div>
							<c:out value="${errorItem.propertyPath}" />
							:
							<c:out value="${errorItem.message}" />
						</div>
					</c:forEach>
				</div>
			</c:if>
			<form:form commandName="reservation" method="post"
				action="/bookFlight.html">
				<input type="hidden" name="flightId" value="${flight.id}" />

				<input type="hidden" name="flightId" value="${flight.id}" />
				<div class="span7">
					<div class="well form-horizontal">
						<fieldset>
							<legend>Create Reservation</legend>
							<spring:bind path="reservationName">
							<fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
							</spring:bind>	
								<label class="control-label">Reservation Name:</label>
								<div class="controls">
									<form:input path="reservationName" />
									<form:errors path="reservationName" cssClass="help-inline" />
								</div>
							</fieldset>
							
							<spring:bind path="quantity">
							<fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
							</spring:bind>	
								<label class="control-label">Number of Seats:</label>
								<div class="controls">
									<form:input path="quantity" />
									<form:errors path="quantity" cssClass="help-inline" />
								</div>
							</fieldset>
							<fieldset class="form-actions">
								<input type="submit" class="btn btn-primary" value="Submit" />
							</fieldset>
						</fieldset>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>
