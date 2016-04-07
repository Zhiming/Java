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
		<script type="text/javascript" src="resources/js/validate.js"></script>
		<script type="text/javascript">
			window.onload = function(){
				$("#btnCrawl").click(function() {
					var url = $(".Data-Items").find("input[name='url']").val();
					var depth = $(".Data-Items").find("input[name='strDepth']").val();
					if(CrawlValidate.validate(url, depth)){
						$("form").submit();
						$("#notice").text("Please have a cup of tea, crawling...");
					}else{
						alert("invalid input, please check them");
					}
				});
			}
		</script>
		<title>Web Crawler</title>
	</head>
	<body>
		<div>
			<form action="crawl" method="post">
				<div class="Data-Title">
					<div class="AlignRight">
						<label for="uri">URI:</label><br/>
						<label for="depth">Crawl depth:</label>
					</div>
				</div>
				
				<div class="Data-Items">
					<input type="text" name="url"/><span id="notice">(Starts with http(s))</span><br/>
					<input type="text" name="strDepth"/>
					<input id="btnCrawl" type="button" value="Crawl"/>
				</div>
			</form>
		</div>
		<div>
			${message}
		</div>
	</body>
</html>