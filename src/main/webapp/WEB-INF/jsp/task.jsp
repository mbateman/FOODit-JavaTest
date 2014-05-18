<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" uri="http://threewks.com/thundr/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:header id="tasks">
	
	<script>
		$(document).ready(function(){
			$("#change").click(function(){
				$("#view").hide();
				$("#edit").show();
			});
		});
	</script>
	
</tags:header>

<div class="hero-unit">
	
	<h1>${task.title}</h1>
	
	<div id="view" class="media">
		<div class="span6">
		
			<div class="media-object pull-left span2" style="font-size: 4em; color: #CCC;">
				<t:if condition="${task.status == 'Done'}">
			  		<i class="media-object icon icon-thumbs-up" style="color: #BDB"></i>
			  	</t:if>
			  	<t:elseif condition="${task.status == 'Underway'}">
			  		<i class="media-object icon icon-hand-right" style="color: #A0C0EE"></i>
			  	</t:elseif>
			  	<t:else>
			  	 	<i class="media-object icon icon-thumbs-down" style="color: #CCC"></i>
			  	</t:else>
		  	</div>
		  	
			<p>${task.description}</p>
			
			<p>
				<a id="change" class="btn btn-primary btn-large">Change</a>
			</p>
		</div>
		
		<div>
			<t:if condition="${task.status == 'Done'}">
				<a href="<t:route name="stop-task" task="${task.id}"/>" class="btn">Reset</a>
		  		<a href="<t:route name="archive-task" task="${task.id}"/>" class="btn btn-primary">Archive</a>
		  	</t:if>
		  	<t:elseif condition="${task.status == 'Underway'}">
		  		<a href="<t:route name="stop-task" task="${task.id}"/>" class="btn">Reset</a>
		  		<a href="<t:route name="finished-task" task="${task.id}"/>" class="btn btn-success">Done</a>
		  	</t:elseif>
		  	<t:else>
		  	 	<a href="<t:route name="start-task" task="${task.id}"/>" class="btn">Start</a>
		  	 	<a href="<t:route name="archive-task" task="${task.id}"/>" class="btn">Archive</a>
		  	</t:else>
		</div>
	</div>
	<div id="edit" style="display: none;">
		
		<form action="<t:route name="update-task" task="${task.id}"/>" method="post" class="form-horizontal">
			<input type="hidden" name="task.id" value="${task.id}" />
			<input type="hidden" name="task.archived" value="${task.archived}" />
			
			<div class="control-group">
				<label class="control-label" for="title">Title</label>
				<div class="controls">
					<input id="title" type="text" name="task.title" value="${task.title}" class="span3" />
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="description">Description</label>
				<div class="controls">
					<textarea id="description" name="task.description" class="span4" rows="4">${task.description}</textarea>
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
					<input type="submit" value="Update" class="btn" />
				</div>
			</div>
		</form>
		
	</div>
</div>

<tags:footer/>