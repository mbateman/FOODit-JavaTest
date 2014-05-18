<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" uri="http://threewks.com/thundr/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:header id="tasks"/>

	<div class="container">
		<c:forEach items="${tasks}" var="task">
			<tags:task task="${task}"/>
		</c:forEach>
	</div>
	
	<br/>
	<br/>
	
	<form action="<t:route name="create-task"/>" method="post" class="form-inline well">
		<input type="text" name="task.title" class="span3" placeholder="Title"/>
		<textarea name="task.description" rows="1" class="span7" placeholder="Description"></textarea>
		<input type="submit" value="Create" class="btn span2"/>
	</form>
	
<tags:footer/>