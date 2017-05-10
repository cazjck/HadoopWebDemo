
<%@page import="java.util.ArrayList"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Hadoop MapReduce trên DBLP(Digital Bibliography & Library Project)</title>

<!-- Bootstrap Core CSS -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="bootstrap/css/shop-item.css" rel="stylesheet">
<style>
	.loading{
		display: none; 	
		position: absolute;
		width:100%;
		height:100%;
		
		z-index: 1000; 
	}
	.loading-icon{
		position: absolute;
		
	}
	.loading-icon img{
		margin-left:60px;
		width: 150px;
		height: 150px;
	}
	.jumbotron h1 {
   		 font-size: 55px;
   	}
	.jumbotron h1 img{
		width: 230px;
		height: 150px;
	}
</style>
<script type="text/javascript">
    
    function showAlert(message) {
    	var widthD=$('.messsage-dialog').width();
    	var pageWith=$(window).width();
    	var x=(pageWith / 2 ) - ( widthD / 2);
    	$('message-dialog').css({left: x +"px"});
    	$('message-dialog').css({height:200+"px"});
    	
    }
    function loadingHadoop(){
    	var x=$(window).width() / 2 - 150;
    	var y=$(window).height() / 2 - 200;
    	$('.loading-icon').css({"left":x, "top":y});
    	$(document).not("loading-icon").click(function(){ return; });
    	if (validateForm()==true) { // Kiểm tra dữ liệu
    		$('.loading').show();
    		$("#formHadoopID").submit(); // Bắt đầu submit form
		}
    
    }
    
    
    function validateForm(){
    	if($('#search').val()=="") { 
    		alert("Từ khóa không để trống"); 
    		$('#search').focus(); // Bắt đầu submit form
    		return false; 
    	}
    	else  {
    		return true;
		}
    	
    }
    
    </script>

</head>

<body>
	<div class="loading">
		<div class="loading-icon">
			<img src="bootstrap/image/loading1.gif"/>
			<h4>Đang chạy Hadoop MapReduce....</h4>
		</div>
					
	</div>
	<!-- Page Content -->
	<div class="container">

		<div class="jumbotron">
			<h1>Hadoop MapReduce trên DBLP <img src="bootstrap/image/HadoopMapReduce.png"/></h1>
			
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-inline" role="form">
					<form role="form" id="formHadoopID" name="formHadoop" action="index" method="POST">
						<div class="col-md-3">
							<label>Từ khóa:</label> <input class="inputForm" type="text"
								name="search" id="search" placeholder="Nhập tên tác giả"">
						</div>
						<div class="col-md-4">
							<label>Tiêu chí tìm kiếm:</label>
							<div class="form-control">
								<label class="radio"> <input type="radio"
									name="radTieuchitimkiem" id="rad1" value="0" checked="checked" >
									Tương đối
								</label> <label class="radio"> <input type="radio" 
									name="radTieuchitimkiem" id="rad2" value="1"> Tuyệt đối
								</label>
							</div>
						</div>
						<div class="col-md-3">
							<label>Loại bài báo:</label> <select class="form-control"
								name="loaibaibao">
								<option>All</option>
								<option>article</option>
								<option>inproceedings</option>
								<option>proceedings</option>
								<option>book</option>
								<option>incollection</option>
								<option>phdthesis</option>
								<option>mastersthesis</option>
								<option>person</option>
								<option>data</option>
							</select>
						</div>
						<div class="col-md-2">
							<button name="search" type="button" onclick="loadingHadoop()" class="btn btn-primary">Tìm kiếm</button>
						</div>
					</form>
			</div>
		</div>
	</div>

	</div>
	<div style="margin: auto; width: 70%">
		<hr>
		 <%
			Object object = session.getAttribute("empty_list");
			 String message;
			if (object != null) {
				 message = object.toString();
			}
			else{
				message="";	
			}
		%>
		<%-- <c:redirect url="pageDetail.jsp" > PageDetail</c:redirect> --%>
		<display:table name="sessionScope.listPage" pagesize="25"
			export="false" sort="list" class="table table-striped"
			uid="tableDBLP" requestURI="">

			<%-- <display:column  title="Id"  >
				<c:out   value="${tableDBLP_rowNum - 1}" />

			</display:column> --%>

			<display:column property="title" title="Tiêu đề"
				href="${pageContext.request.contextPath}/pageDetail"  paramId="id"
				paramProperty="jsonObject" sortable="true" headerClass="sortable">
				<%-- <stripes:hidden name="index"  value="${tableDBLP_rowNum -1}" /> --%>
			</display:column>
			<display:column property="author" title="Tác giả" sortable="true"
				headerClass="sortable" />
			<display:setProperty name="basic.msg.empty_list" value='<%=message %>' />
		</display:table>
		<%-- <%
			}
		%> --%>
	</div>



	<!-- /.container -->

	<div class="container">
		<!-- Footer -->
		 <hr>
      <footer>
        <p>Copyright &copy; Hadoop - MapReduce Demo</p>
      </footer>
	</div>

	<!-- /.container -->

	<!-- jQuery -->
	<script src="bootstrap/js/jquery.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="bootstrap/js/bootstrap.min.js"></script>
	<script src="bootstrap/js/main.js"></script>

</body>

</html>