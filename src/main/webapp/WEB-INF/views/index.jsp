<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
hello

	<script>
		var name = 'access_token';
		var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
		var cookie = value? value[2] : null;
		console.log("///", cookie)
	</script>
</body>
</html>