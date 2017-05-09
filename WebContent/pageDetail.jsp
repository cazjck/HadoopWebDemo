<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<!-- Bootstrap Core CSS -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="bootstrap/css/shop-item.css" rel="stylesheet">
<title>Chi tiết bài báo</title>
</head>

<body>
	<%
		String url;
		JSONObject object;
		String pages="",volume="",publisher="",isbn="",booktitle="",year="",key="",publtype="";
		if (session.getAttribute("json") != null) {
			object = (JSONObject) session.getAttribute("json");
			if(object.has("volume")) volume=object.get("volume").toString();
			if(object.has("pages")) pages=object.get("pages").toString();
			if(object.has("publisher")) publisher=object.get("publisher").toString();
			if(object.has("isbn")) isbn=object.get("isbn").toString();
			if(object.has("booktitle")) booktitle=object.get("booktitle").toString();
			if(object.has("year")) year=object.get("year").toString();
			if(object.has("key")) key=object.get("key").toString();
			if(object.has("publtype")) publtype=object.get("publtype").toString();
	%>
	<%!public String getAuthor(String author) {
		String str;
		if (author.split(",").length > 1) {
			str = author.substring(author.indexOf("[") + 1, author.lastIndexOf("]")).replaceAll("\"", " ");
			//String[] arr=str.split(",");
		} else {
			str = author;
		}
		return str;

	}%>



	<!-- Page Content -->
	<div class="container">
		<div class="jumbotron" style="text-align: center;">
			<h2><%=object.get("title")%></h2>
		</div>
		<div class="row">
			<div class="col-md-12"">
				<div style="width: 70%;margin: auto;">
				
				<%
					if (object.has("author")) {
							String author = object.get("author").toString();
				%>
				<label>Authors: </label> <span><%=getAuthor(author)%></span><br>
				<%
					} else if (object.has("editor")) {
							String editor = object.get("editor").toString();
				%>
				<label>Editors: </label> <span><%=getAuthor(editor)%></span><br>
				<%
					}
				%>



				<%
					if (object.has("journal")) {
							String journal = object.getString("journal");
						%>
							<label>Journal: </label> <span><%=journal%> <label> <%=volume%></label> (<%=object.get("year") %>), <%=pages%></span><br>
							
						 <% 
						}
				%>

				<label>Type: </label> <span><%=object.get("type")%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				
				<%
					if(pages!=""){
				%>	
				<label>     Pages: </label> <span><%=pages%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				<% 		
					}
				%>
				
				<%
					if(year!=""){
				%>	
				<label>    Year: </label> <span><%=year%></span><br>
				<% 		
					}
				%>
				
				
				<%
					if(booktitle!=""){
				%>	
				<label>Booktitle: </label> <span><%=booktitle%></span><br>
				<% 		
					}
				%>
				
				
				<%
					if(publisher!=""){
				%>	
				<label>Publisher: </label> <span><%=publisher%></span><br>
				<% 		
					}
				%>
				
				
				<%
					if(isbn!=""){
				%>	
				<label>ISBN: </label> <span><%=isbn%></span><br>
				<% 		
					}
				%>
				
				
				<%
					if(publtype!=""){
				%>	
				<label>Publtype: </label> <span><%=publtype%></span>
				<% 		
					}
				%>
				
				
				<%
					if(key!=""){
				%>	
				<label>Key: </label> <span><%=key%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><br>
				<% 		
					}
				%>
				
				
				<%
					if (object.has("ee")) {
							String ee = object.get("ee").toString();
							if (ee.split(",").length > 1) {
								String[] arr = ee.split(",");
								url = arr[0].substring(1, arr[0].length());
				%>
				<label>URL: </label> <span><%=getAuthor(ee)%></span><br>
				<%
					} else {
								url = ee.toString();
				%>
				<label>URL: </label> <span><%=ee%></span><br>
				<%
					}
						} else {
							url = "error.jsp";
						}
				%>
				</div>
				<hr>
				<iframe width="100%" height="500px" id="content" src="<%=url%>"></iframe>
			</div>

			<%
				}
			%>


			<!-- /.container -->

			<div class="container">
				<!-- Footer -->
				<div style="margin: auto;">Copyright &copy; Hadoop Cluster
					Demo</div>


			</div>

			<!-- /.container -->

			<!-- jQuery -->
			<script src="bootstrap/js/jquery.js"></script>

			<!-- Bootstrap Core JavaScript -->
			<script src="bootstrap/js/bootstrap.min.js"></script>
			<script src="bootstrap/js/main.js"></script>
</body>

</html>