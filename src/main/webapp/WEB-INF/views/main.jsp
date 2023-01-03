<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- head -->
<link href="resources/styleMain.css" type="text/css" rel="stylesheet">

</head>
<body>
<!-- body style -->
<link href="https://fonts.googleapis.com/css?family=DM+Sans:400,500,700&display=swap" rel="stylesheet">
<!-- body style-->	
  <table width="800" height="500" align="center" border="1">
	<tr>
	 <td width="500" height="500">
	  <img src="resources/imgs/여행.png" alt="메인 페이지 이미지" width="100%" height="100%">
     </td>	
	</tr>
	<tr>
	  <td>
	   <input type="button" value="게시판 목록" onClick="location='list.do'">
	   <input type="button" value="로그인" onClick="location='loginForm.do'">
	   	   여행정보 공유 사이트 입니다.
	  </td>
	</tr>
  </table>
</body>
</html>