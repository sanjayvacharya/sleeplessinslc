<%@ include file="/taglibs.jsp"%>

<head>
<title>Book Seats</title>
</head>
<div class="span3">
	<h2>Book Seats</h2>
	Book Seats on Flight:
	<c:out value="${it.flight.number}" />
</div>
<div class="span7">
	<div class="well form-horizontal">
		<div class="control-group">
			<label class="control-label">Flight Number:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${it.flight.number}" /></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">From:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${it.flight.from.city}" />|<c:out
						value="${it.flight.from.code}" /></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">To:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${it.flight.to.city}" />|<c:out
						value="${it.flight.to.code}" /></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Departure Time:</label>
			<div class="controls">
				<span class="uneditable-input"><joda:format
						value="${it.flight.departureTime}" style="MM" /></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Arrival Time:</label>
			<div class="controls">
				<span class="uneditable-input"><joda:format
						value="${it.flight.arrivalTime}" style="MM" /></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">Seats Available:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${it.flight.seatsAvailable}" /></span>
			</div>
		</div>
		<div class="row-fluid">
			<c:if test="${not empty it.errors}">
				<div class="alert alert-block alert-error fade in">
					<c:forEach var="errorItem" items="${it.errors}">
						<div>
							<c:out value="${errorItem.propertyPath}" />
							:
							<c:out value="${errorItem.message}" />
						</div>
					</c:forEach>
				</div>
			</c:if>
			<c:if test="${not empty it.message}">
				<div class="alert alert-block alert-error fade in">
					<div>
						<c:out value="${it.message}" />
					</div>
				</div>
			</c:if>
			<form method="post" action="/bookFlight.html">

				<input type="hidden" name="flightId" value="${it.flight.id}" />
				<div class="span7">
					<div class="well form-horizontal">
						<fieldset>
							<legend>Create Reservation</legend>
							<div class="control-group">
								<label class="control-label">Reservation Name:</label>
								<div class="controls">
									<input type="text" name="reservationName" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Number of Seats:</label>
								<div class="controls">
									<input type="text" name="quantity" />
								</div>
							</div>
							<fieldset class="form-actions">
								<input type="submit" class="btn btn-primary" value="Submit" />
							</fieldset>
						</fieldset>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>