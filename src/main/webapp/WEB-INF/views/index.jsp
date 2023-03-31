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
	
	<h4>사용자 수정</h4>
	<button type="button" onclick="put()">put</button>
	<input type="file" name="file">
	
	<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
	
	<script>
	var conf = {
		rootPath: "http://localhost/iwms/v1"
	}
	
	localStorage.setItem("accessToken", "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJmN0pKd2g3aEZDTEt2OVhEaE5NOEFNTlZpRkRZTGZxVmdqUGpXMUN1bEhZIn0.eyJleHAiOjE2ODAyMzYyNjIsImlhdCI6MTY4MDIzNDQ2MiwianRpIjoiOTViNTFlMDEtYmM3NS00MTljLTg4N2ItZWI5MTZhZWU1MjdhIiwiaXNzIjoiaHR0cDovL3JlZ2lzdHJ5Lml3aS5jby5rcjo4MTgwL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJpd21zIiwic3ViIjoiNjE2OTJjZGYtODAwNy00MGIxLWI2YjEtOThjMjNlNzRmZTUyIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiaXdtcyIsInNlc3Npb25fc3RhdGUiOiJjNDRjMWRhZi0wZTVlLTQ3NTEtODI4MC0yMmI0MzExZTFhYzEiLCJhY3IiOiIxIiwic2NvcGUiOiJpd21zIiwic2lkIjoiYzQ0YzFkYWYtMGU1ZS00NzUxLTgyODAtMjJiNDMxMWUxYWMxIiwidXNlcl9uYW1lIjoiaXdpMSIsImF1dGhvcml0aWVzIjpbIlJPTEVfSVdNU19QTSJdfQ.RJ3l5dbZNHqv8m013J_LHFTGf8yW4L5hmmwVLRp_c_XqNNlM1Mn2xhHDPXLxbX3pHMl8Im6Gb7t2VvrUDCF_2D3e6NfOef3EpMz3gT2sdunh20V0fqTOgPA_Wl4ixR_ggR5AWnviwngXagS6aQ3zPvwNSBJF_n0_96qFzaZ1p_Py5TkwCEOX8R1AapIkI1oNHZesOkVfeZebLzjV1eefdH87avImilbs1IqdeeJe8k0gwdz2vHvghKjBtvZIjvwhMPPReu34jZ7KCObz1-33ZZ2Mes69hp5FZtYRO-UsrTbbEgCb4udY0XflkzgQ2sGck_75K2eE_fUTEFphJ6yGhw");
	
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
	
	
	function put() {
		var formData = new FormData();
		formData.append("userNm", "22사용자 이름 수정");
		formData.append("userEmail", "test@nvaer.ocm");
		formData.append("authCd", "ROLE_IWMS_ADMIN");
		formData.append("compSeq", 1);
		formData.append("deptSeq", 2);
		formData.append("userGbCd", "01");
		formData.append("busiRollCd", "DV");
		formData.append("VERIFY_YN", "N");
		formData.append("useYn", "Y");
		
		
		var files = document.getElementsByName("file")[0].files;
		
		for(var i = 0; i < files.length; i++) {
			formData.append("file", files[i]);	
		}
		
		callAjax("/users/24", "put", formData).then(success => {
			console.log("put success, ", success);
		}, err => {
			console.error("put fail, ", err);
		});
	}
	
	function del() {
		callAjax("/notices/11", "delete").then(success => {
			console.log("delete success, ", success);
		}, err => {
			console.error("delete fail, ", err);
		});
	}
	
	var download = (url, name) => {
		fetch(url)
	    .then(response => response.blob())
		.then(blob => {
			var fileObjectUrl = window.URL.createObjectURL(blob);
			var a = document.createElement("a");
			a.href = fileObjectUrl;
			a.download = name;
			a.click();
			window.URL.revokeObjectURL(fileObjectUrl);
		})
		.catch(error => console.error('Failed to file download, ', error));  
	}
	
	</script>
</body>
</html>