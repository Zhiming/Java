<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%
     	String path = request.getContextPath();
     	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
<html>
<head>
<base href="<%=basePath%>"></base>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="resources/css/layout.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="resources/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript">
			window.onload = function(){
				$("form input[name='btnSearch']").click(function(){
					var keyword = $("form input[name='keyword']").val();
					if(keyword == null){
						alert("Please enter a keyword");
						return;
					}else{
						$("form").submit();
					}
				});
			}
		</script>
<title>Search</title>
</head>
<body>
	<div>
		<form action="search" method="post">
			<label for="keyword">Keyword:</label> 
			<input type="text" name="keyword" />
			<input type="button" value="Search" name="btnSearch"/>
		</form>
	</div><br/>
	<div>
		<ul>
			<c:forEach var="entry" items="${results}">
	    		<li>
		    		<a href="${entry.key}">${entry.key}</a><br/>
		    		${entry.value}<br/><br/>
	    		</li>
			</c:forEach>
		</ul>
	</div>
</body>
</html>