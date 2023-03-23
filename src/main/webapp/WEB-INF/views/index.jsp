<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Hello test!</title>
</head>
<body>
	<h1>Hello test!</h1>
	<h4>공지사항 목록</h4>
	<button type="button" onclick="get()">get</button>
	<h4>공지사항 등록</h4>
	<button type="button" onclick="post()">post</button>
	<input type="file" name="files" multiple="multiple">
	<h4>공지사항 삭제</h4>
	<button type="button" onclick="del()">delete</button>
	
	<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
	
	<script>
	var conf = {
		rootPath: "/iwms/v1"
	}
	
	$.ajaxPrefilter(function(options, originalOptions, jqXHR ) {
		options.headers = {"Authorization": "Bearer " + localStorage.getItem("accessToken")},
		console.log("options", options);

		tokenReissue = function() {
			var formData = new FormData();
			formData.append("refreshToken", localStorage.getItem("refreshToken"));
			var body = urlencodeFormData(formData);

			$.ajax({
				url : conf.rootPath + "/reissue",	
				method: "post",
				data: body
			})
			.done(function(resp) {
				localStorage.setItem("accessToken", resp.accessToken);
				$.ajax(options);
			})
			.fail(function(xhr, status, errorThrown) {
				logout();
			})
			.always(function(xhr, status) {
				console.log("loader [hide]");	
			});
			
		};
	});
	
	function callAjax(uri, method, body) {
		return new Promise((resolve, reject) => {
	    	console.log("uri: " + uri + ", method: " + method + ", body: ", body)

			var options = {
				url : conf.rootPath + (uri.charAt(0) == "/" ? uri : "/" + uri),
			    beforeSend: function () {
			    	console.log("loader [show]")
			    }
			}
			
			if(method) {
				options.method = method;
				if(body) {
					options.data = body;
					if(body instanceof FormData) {
						options.processData = false;
						options.contentType = false;	
					}
				}
			}
		
			$.ajax(options)
			.done(function(resp) {
				resolve(resp);

			})
			.fail(function(xhr, status, errorThrown) {
				if(xhr.status == 401) {
					if(xhr.responseJSON.code == "023") {
						//토큰 재발급
						tokenReissue();
					} else {
						//인증 실패. 재로그인 필요
						logout();
					}
				} else {
					reject(xhr.responseJSON);
				}
			})
			.always(function(xhr, status) {
				if(!(status == 'error' && xhr.status == 401 && xhr.responseJSON.code == "023")) {
					console.log("loader [hide]");
				}
			});
		});
	}
	
	function urlencodeFormData(fd){
	    var s = "";
	    function encode(s){ return encodeURIComponent(s).replace(/%20/g,'+'); }
	    for(var pair of fd.entries()){
	        if(typeof pair[1] == "string"){
	            s += (s ? "&" : "") + encode(pair[0]) + "=" + encode(pair[1]);
	        }
	    }
	    return s;
	}

	
	function logout() {
		console.log("인증 실패. 다시 로그인");
		//location.href = "/logout";
	}
	
	function get() {
		callAjax("/notices").then(success => {
			console.log("get success, ", success);
		}, err => {
			console.error("get fail, ", err);
		});
	}
	
	function post() {
		var formData = new FormData();
		formData.append("title", "공지사항 제목");
		formData.append("content", "<p>공지사항 내용</p>");
		formData.append("useYn", "Y");
		
		
		var files = document.getElementsByName("files")[0].files;
		
		for(var i = 0; i < files.length; i++) {
			formData.append("files", files[i]);	
		}
		
		callAjax("/notices", "post", formData).then(success => {
			console.log("post success, ", success);
		}, err => {
			console.error("post fail, ", err);
		});
	}
	
	function del() {
		callAjax("/notices/5", "delete").then(success => {
			console.log("delete success, ", success);
		}, err => {
			console.error("delete fail, ", err);
		});
	}
	
	</script>
</body>
</html>