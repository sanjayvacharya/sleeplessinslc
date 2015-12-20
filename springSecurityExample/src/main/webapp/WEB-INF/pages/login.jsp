<%@ include file="/taglibs.jsp"%>
<head>
	<script type="text/javascript">
	 $(document).ready(function() {
		$("#submitBtn").popover();
		$("#j_username").popover();
		$("#j_password").popover();
	 })
	</script>
</head>
<div class="span3">&nbsp;</div>
<div class="span5 well">
	<fieldset>
		<legend>Login</legend>

		<form name='f' action="<c:url value='j_spring_security_check' />"
			method="POST" class="form-horizontal">


			<c:if test="${not empty error}">
				<div class="alert alert-block alert-error fade in">Your login attempt was not successful with the
					username/password provided.
				</div>
			</c:if>


			<fieldset class="control-group">
				<label class="required control-label"> User Name <span
					class="required">*</span>
				</label>

				<div class="controls">
					<input type="text" name="j_username" id="j_username" tabindex="1"
				 title="User Name" data-content="Enter your user name." />
				</div>
			</fieldset>

			<fieldset class="control-group">
				<label for="j_password" class="required control-label">
					Password <span class="required">*</span>
				</label>

				<div class="controls">
					<input type="password" name="j_password" id="j_password" title="User Password" data-content="Enter your password."
						tabindex="2" />
				</div>
			</fieldset>

			<fieldset class="form-actions">
				<input type="submit" id="submitBtn" class="btn btn-primary" name="submit"
					value="Login" tabindex="4" rel="popover" title="Login" data-content="Login to the Flight Application." />
			</fieldset>
		</form>
	</fieldset>
</div>
