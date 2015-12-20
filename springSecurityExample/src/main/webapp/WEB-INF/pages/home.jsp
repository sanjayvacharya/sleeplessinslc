<%@ include file="/taglibs.jsp"%>
<div class="hero-unit">
	<p>Welcome to the flight application. Search Flights and book
		reservations. All reservations require a name and a quantity which
		serve to demonstrate JSR 303 validation.</p>
	<p>Spring Security is enabled on the flight application. An
		embedded LDAP server is used for demonstration. Database or any other
		authentication source can be used as well. This application provides
		Cookie based handling of the user session and stays away from the HTTP
		Session. The following are the three roles defined:</p>
	<ul>
		<li>User: Can view Airports, search flights and view all
			reservations (yeah I know)</li>
		<li>Agent: An agent can do all that a user can with the
			additional ability to create reservations</li>
		<li>Admin: An admin can do all the things a user can but cannot
			reserve flights. However they are the only group that can add new
			airports and flights to the system.</li>
	</ul>
	<p>There are 3 users defined for each type of role:</p>
	<ul>
		<li>Sanjay User - Login/Pass:suser/pass</li>
		<li>Sanjay Agent - Login/Pass:sagent/pass</li>
		<li>Sanjay Admin - Login/Pass:sadmin/pass</li>
	</ul>
	<p>Login as each of them to see the controls available.</p>
</div>