<%@ include file="/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="icon" href="<c:url value="/images/favicon.ico"/>" />
	<title><decorator:title /> | Flight Application</title>
	<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}

.sidebar-nav {
	padding: 9px 0;
}
</style>
<link rel="stylesheet" type="text/css" media="all"
	href="<c:url value='/styles/lib/bootstrap.css'/>" />

<link rel="stylesheet" type="text/css" media="all"
	href="<c:url value='/styles/lib/bootstrap-responsive.css'/>" />

<link rel="stylesheet" type="text/css" media="all"
		href="<c:url value='/styles/style.css'/>" />

<script type="text/javascript" src="<c:url value='/scripts/jquery.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/bootstrap-transition.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/bootstrap-alert.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/bootstrap-tooltips.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/bootstrap-popover.js'/>"></script>

<decorator:head />
</head>
<body>

	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="brand" href="<c:url value='/'/>">Flight Application</a>
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<%@ include file="/messages.jsp"%>
		<div class="row-fluid">
			<decorator:body />
		</div>
	</div>

	<div id="footer">Flight Application using Spring Security and Powered by Twitter
		Bootstrap</div>
</body>

</html>


