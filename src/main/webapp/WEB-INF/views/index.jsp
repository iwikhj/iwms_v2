<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>hello test!</title>
</head>
<body>
hello test!

	<script>
		var name = 'refresh_token';
		var cookie = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
		console.log("cookie: ", cookie);
		
		var value = cookie ? cookie[2] : null;
		console.log("cookie value: ", value);
	</script>
</body>
</html>