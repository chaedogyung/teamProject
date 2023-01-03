<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--noticeDeletePro.jsp --%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>noticeDeletePro.jsp</title>
		
		<link href="styleMain.css" rel="stylesheet" type="text/css">
	</head>
	
	
	<body>
		<c:if test="${check == 1}">
			<meta http-equiv="Refresh" content="0;url=${ctxpath}/notice/noticeList.do?pageNum=${pageNum}">
		</c:if>
		
		<c:if test="${check == 0}">
			암호가 틀립니다 <br>
			<a href="javascript:history.go(-1)">
				[글삭제 폼으로 되돌아가기]
			</a>
		</c:if>
	
	</body>
</html>