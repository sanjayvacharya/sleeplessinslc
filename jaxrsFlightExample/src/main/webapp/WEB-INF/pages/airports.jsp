<%@ include file="/taglibs.jsp"%>

<head>
<title>Airports</title>
</head>

<div class="span3">
	<h2>Airports</h2>
	All Airports in the world. If you are an admin, you can create an
	airport.
</div>

<div class="span7">

	<display:table name="${it.airports}" class="table table-condensed"
		requestURI="" id="a" export="true" pagesize="10">
		<display:column title="Code" property="code" sortable="true" />
		<display:column title="Name" property="name" sortable="true" />
		<display:column title="City" property="city" sortable="true" />
	</display:table>

	<sec:authorize ifAllGranted="ROLE_ADMIN">
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
			<form action="/airports.html" method="POST">
				<div class="span7">

					<div class="well form-horizontal">
						<fieldset>
							<legend>Create Airport</legend>
							<div class="control-group">
								<label class="control-label">Airport Code:</label>
								<div class="controls">
									<input type="text" id="code" name="code"
										value="${it.airport.code}" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">Airport Name:</label>
								<div class="controls">
									<input type="text" id="name" name="name"
										value="${it.airport.name}" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">Airport City:</label>
								<div class="controls">
									<input type="text" id="city" name="city"
										value="${it.airport.city}" />
								</div>
							</div>
							<fieldset class="form-actions">
								<input type="submit" class="btn btn-primary"
									value="Create Airport" />
							</fieldset>
						</fieldset>
					</div>

				</div>
			</form>
		</div>
	</sec:authorize>
</div>
