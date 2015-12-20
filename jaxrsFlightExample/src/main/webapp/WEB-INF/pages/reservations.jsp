<%@ include file="/taglibs.jsp"%>
<div class="span3">
	<h2>Reservations</h2>
	View reservations made by all agents. A regular user can view all
	reservations as well. Major BUG. File a Jira ticket
</div>

<div class="span7">
	<display:table name="${it.reservations}" class="table table-condensed"
		requestURI="" id="ticket" export="true" pagesize="10">
		<display:column title="Reservation Numer" property="id" />
		<display:column title="Reservation Name" property="reservationName"
			sortable="true" />
		<display:column title="Number of Seats" property="numberOfSeats" />
		<display:column title="Departure City" property="flight.from.city"
			sortable="true" escapeXml="true" />
		<display:column title="Arrival City" property="flight.to.city"
			sortable="true" escapeXml="true" />
		<display:column title="" sortable="false">
			<a href="/reservations/${ticket.id}.html"> Show details </a>
		</display:column>
	</display:table>
</div>