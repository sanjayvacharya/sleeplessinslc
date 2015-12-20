<%@ include file="/taglibs.jsp"%>
<head>
<title>Confirmation</title>
</head>
<div class="span3">
	<h2>Reservation Confirmation</h2>
	Your Reservation confirmation and details are available here. Please
	print a copy for your records.
</div>

<div class="modal hide fade" id="myModal">
	<div class="modal-header">
		<a class="close" data-dismiss="modal" >&times;</a>
		<h3>Cancel Reservation</h3>	
	</div>
	<div class="modal-body">
		<p>Are you sure you want to cancel reservation <b id="reservationId"></b>?</p>
	</div>
	<div class="modal-footer">
		<a onclick="cancelReservation()" class="btn btn-primary">Cancel Reservation</a>
 	 	<a href="#" class="btn" data-dismiss="modal">Close</a>	
 	</div>
</div>

<div class="span7">
	<div class="well form-horizontal">
		<div class="control-group">
			<label for="confirmationId" class="control-label">Confirmation
				Number:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out value="${ticket.id}" /></span>
			</div>
		</div>
		<div class="control-group">
            <label class="control-label">Reservation Name:</label>
            <div class="controls">
                <span class="uneditable-input"><c:out value="${ticket.reservationName}" /></span>
            </div>
        </div>
		<div class="control-group">
			<label class="control-label">Ticket Issue date:</label>
			<div class="controls">
				<span class="uneditable-input">'<c:out
						value="${ticket.issueDate}" />'
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Flight Number:</label>
			<div class="controls">
				<span class="uneditable-input">'<c:out
						value="${ticket.flight.number}" />'
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Number of Seats:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${ticket.numberOfSeats}" /> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">From:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${ticket.flight.from.city}" />|<c:out
						value="${ticket.flight.from.code}" /> </span>
			</div>

		</div>
		<div class="control-group">

			<label class="control-label">Departure Time:</label>
			<div class="controls">
				<span class="uneditable-input"><joda:format
						value="${ticket.flight.departureTime}" style="MM" /></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">To:</label>
			<div class="controls">
				<span class="uneditable-input"><c:out
						value="${ticket.flight.to.city}" />|<c:out
						value="${ticket.flight.to.code}" /> </span>
			</div>
		</div>
		<div class="control-group">

			<label class="control-label">Arrival Time:</label>
			<div class="controls">
				<span class="uneditable-input"><joda:format
						value="${ticket.flight.arrivalTime}" style="MM" /> </span>
			</div>
		</div>
	</div>
</div>