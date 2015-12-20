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
 
	<display:table name="airports" class="table table-striped table-bordered table-condensed" requestURI="" id="airport"
		export="true" pagesize="10">
		<display:column title="Code" property="code" />
		<display:column title="Name" property="name" sortable="true" />
		<display:column title="City" property="city" />
	</display:table>

	<sec:authorize ifAllGranted="ROLE_ADMIN">
		<div class="row-fluid">
			<form:form commandName="airport" method="post"
				action="/airports.html">
				<div class="span8">
					<div class="well form-horizontal">
						<fieldset>
							<legend>Create Airport</legend>
							<spring:bind path="airport.code">
							<fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
							</spring:bind>
								<label class="control-label">Airport Code:</label>

								<div class="controls">
									<form:input path="code" />
									<form:errors path="code" cssClass="help-inline" />
								</div>
							</fieldset>

							<spring:bind path="airport.name">
							<fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
							</spring:bind>
								<label class="control-label">Airport Name:</label>
								<div class="controls">
									<form:input path="name" />
									<form:errors path="name" cssClass="help-inline" />
								</div>
							</fieldset>

							<spring:bind path="airport.city">
							<fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
							</spring:bind>
								<label class="control-label">Airport City:</label>
								<div class="controls">
									<form:input path="city" />
									<form:errors path="city" cssClass="help-inline" />
								</div>
							</fieldset>
							<fieldset class="form-actions">
								<input type="submit" class="btn btn-primary"
									value="Create Airport" />
							</fieldset>
						</fieldset>
					</div>
				</div>
			</form:form>
		</div>
	</sec:authorize>
</div>
