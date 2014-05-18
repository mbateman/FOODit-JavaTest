<%@ attribute name="id" required="false"%>
<%@ taglib prefix="t" uri="http://threewks.com/thundr/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" lang="en-us" />
<t:style src="bootstrap.min.css" />
<t:style src="font-awesome.min.css" />
<t:script src="jquery-1.9.0.min.js" />
<jsp:doBody />
</head>
<body id="${id}">
	<header class="container">
		<nav class="navbar">
			<div class="navbar-inner">
				<a class="brand" href="<t:route name="list"/>">My tasks</a>
			<ul class="nav pull-right">
  				<li>
    				<a href="/_ah/admin">Admin</a>
  				</li>
			</ul>
			</div>
		</nav>
	</header>
	<section class="container">