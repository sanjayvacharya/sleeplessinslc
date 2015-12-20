<%@ include file="/taglibs.jsp"%>
<head>
<title>Reservations</title>

<script type="text/javascript">
    var selReservation = null;
    
	function cancelReservation(reservationId) {
		document.getElementById('reservationId').innerHTML = reservationId;
		selReservation = reservationId;
		$('#myModal').modal('show');
	}
	
	function execDelete() {
	  document.location.href="<c:url value='/reservations/cancel/'/>" + selReservation;		
	}
</script>
 </head>

<div class="span3">
	<h2>Reservations</h2>
	View reservations made by all agents. A regular user can view all
	reservations as well. Major BUG. File a Jira ticket
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
		<a onclick="execDelete()" class="btn btn-primary">Cancel Reservation</a>
 	 	<a href="#" class="btn" data-dismiss="modal">Close</a>	
 	</div>
</div>

<div class="span7">
	<display:table name="${reservations}" class="table table-striped table-bordered table-condensed"
		requestURI="" id="ticket" export="true" pagesize="10">
		<display:column title="Reservation Number">
			<a href="/reservations/${ticket.id}.html">${ticket.id}</a>
		</display:column>
		<display:column title="Reservation Number" property="id" />
		<display:column title="Reservation Name" property="reservationName"
			sortable="true" />
		<display:column title="Number of Seats" property="numberOfSeats" />
		<display:column title="Departure City" property="flight.from.city"
			sortable="true" escapeXml="true" />
		<display:column title="Arrival City" property="flight.to.city"
			sortable="true" escapeXml="true" />
		<sec:authorize ifAllGranted="ROLE_AGENT">
			<display:column title="Cancel Reservation">
				<a onclick="cancelReservation('<c:out value='${ticket.id}'/>')">
				Cancel</a>
			</display:column>
		</sec:authorize>
	</display:table>
</div>