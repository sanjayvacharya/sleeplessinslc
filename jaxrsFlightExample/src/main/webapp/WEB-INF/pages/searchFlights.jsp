<%@ include file="/taglibs.jsp"%>

<head>
<title>Search Flights</title>
</head>

<div class="span3">
	<h2>Search Flights</h2>
	Search for Flights. If you are an agent you can book a flight as well.
</div>

<div class="span7">
	<div class="well form-horizontal">

		<form action="/searchFlights.html" method="POST">
			<div class="control-group">
				<label class="control-label">From:</label>
				<div class="controls">
					<select name="fromAirportCode" STYLE="width: 250px">
						<option value="">--Select One--</option>
						<c:forEach items="${it.airports}" var="airport">
							<option
								<c:if test="${it.from eq airport.code}">
                         selected="selected"
                         </c:if>
								value='<c:out value="${airport.code}"/>'>
								<c:out value='${airport.name}' />
							</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div class="control-group">
				<label class="control-label">To:</label>
				<div class="controls">
					<select name="toAirportCode" STYLE="width: 250px">
						<option value="">--Select One--</option>
						<c:forEach items="${it.airports}" var="airport">
							<option
								<c:if test="${it.to eq airport.code}">
                         selected="selected"
                         </c:if>
								value='<c:out value="${airport.code}"/>'>
								<c:out value='${airport.name}' />
							</option>
						</c:forEach>
					</select>

				</div>
			</div>
			
			<fieldset class="form-actions">
				<input type="submit" class="btn btn-primary" name="submit"
					value="Search" />
			</fieldset>
		</form>
	</div>
	
	<display:table name="${it.flightSearchResult.flights}"
		class="table table-condensed" requestURI="" id="flight" export="true"
		pagesize="10">
		<display:column title="Number" property="number" sortable="true" />

		<display:column title="Departure City" property="from.city"
			sortable="true" escapeXml="true" />
		<display:column title="Departure" sortable="true"
			sortProperty="departureTime" escapeXml="true">
			<joda:format value="${flight.departureTime}" style="SS" />
		</display:column>
		<display:column title="Arrival City" property="to.city"
			sortable="true" escapeXml="true" />
		<display:column title="Arrival" sortable="true"
			sortProperty="arrivalTime" escapeXml="true">
			<joda:format value="${flight.arrivalTime}" style="SS" />
		</display:column>
		<display:column title="Seats Available" property="seatsAvailable" />
		<display:column title="Miles" property="miles" />
		<sec:authorize ifAllGranted="ROLE_AGENT">
            <display:column title="" sortable="false" href="/bookFlight.html"
                paramId="id" paramProperty="id" titleKey="flightId">
                    Book
            </display:column>
         </sec:authorize>
	</display:table>
</div>