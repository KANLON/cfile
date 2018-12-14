let userStr = getCookie("user");
if (!isNull(userStr)) {
	let user = JSON.parse(userStr);
	if (!isNull(user) && user.status === 1) {
		$("#a-login-register").html(user.username);
		$("#a-login-register").attr("href", "teacher.html");
	}
}