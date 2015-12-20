<%@ include file="/taglibs.jsp"%>
<head>
<title>Confirmation</title>
</head>
<div class="span3">
	<h2>Reservation Confirmation</h2>
	Your Reservation confirmation and details are available here. Please
	print a copy for your records.
</div>
<div class="span7">
	<div class="well form-horizontal">
		<div class="control-group">
			<label for="confirmationId" class="control-label">Confirmation
				Number:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out value="${it.ticket.id}" /></span>
			</div>
		</div>
		<div class="control-group">
            <label class="control-label">Reservation Name:</label>
            <div class="controls">
                <span class="uneditable-input"><c:out value="${it.ticket.reservationName}" /></span>
            </div>
        </div>
		<div class="control-group">
			<label class="control-label">Ticket Issue date:</label>
			<div class="controls">
				<span class="uneditable-input">'<c:out
						value="${it.ticket.issueDate}" />'
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Flight Number:</label>
			<div class="controls">
				<span class="uneditable-input">'<c:out
						value="${it.ticket.flight.number}" />'
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Number of Seats:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${it.ticket.numberOfSeats}" /> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">From:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${it.ticket.flight.from.city}" />|<c:out
						value="${it.ticket.flight.from.code}" /> </span>
			</div>

		</div>
		<div class="control-group">

			<label class="control-label">Departure Time:</label>
			<div class="controls">
				<span class="uneditable-input"><joda:format
						value="${it.ticket.flight.departureTime}" style="MM" /></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">To:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${it.ticket.flight.to.city}" />|<c:out
						value="${it.ticket.flight.to.code}" /> </span>
			</div>
		</div>
		<div class="control-group">

			<label class="control-label">Arrival Time:</label>
			<div class="controls">
				<span class="uneditable-input"><joda:format
						value="${it.ticket.flight.arrivalTime}" style="MM" /> </span>
			</div>
		</div>
	</div>
</div>