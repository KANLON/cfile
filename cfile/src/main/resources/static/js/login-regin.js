/*import {isNullOrWhiteSpace,isNull} from '../common/string-utils.js';*/
let lengthReg =  new RegExp("^.{0,20}$");
/************ 登陆的******************/
/**
 * 检查登陆用户名
 */
function checkLoginUsername(username){
	$("#username_error_login").hide();
	if(isNullOrWhiteSpace(username)){
		$("#username_error_login").html("用户名为空或全是空格"); 
		$("#username_error_login").show();
		return false;
	}else if(!lengthReg.test(username)){
		$("#username_error_login").html("用户名长度大于20个字符"); 
		$("#username_error_login").show();
		return false;
	}
	return true;
}

/**
 * 检查登陆密码
 */
function checkLoginPassword(password){
	$("#password_error_login").hide();
	if(isNullOrWhiteSpace(password)){
		$("#password_error_login").html("密码为空或全是空格"); 
		$("#password_error_login").show();
		return false;
	}else if(!lengthReg.test(password)){
		$("#password_error_login").html("密码长度大于20个字符"); 
		$("#password_error_login").show();
		return false;
	}
	return true;
	
}

/**
 * 检查登陆验证码
 */
function checkLoginCaptcha(captcha){
	$("#captcha_error_login").hide();
	if(isNullOrWhiteSpace(captcha) || captcha.length!==5){
		$("#captcha_error_login").html("验证码不正确！"); 
		$("#captcha_error_login").show();
		return false;
	}	
	return true;
}


/**
 * 登陆发送json的函数
 */
function login(username,password,loginCaptcha) {
	//如果基础信息验证不通过，直接返回false
	let validFlag = checkLoginUsername(username) && checkLoginPassword(password) && checkLoginCaptcha(loginCaptcha);
	if(validFlag===false){
		return;
	}
	let loginDataJson={
			"username":username,
			"password":password,
			"captcha":loginCaptcha
	}
	let loginDataJsonStr = JSON.stringify(loginDataJson);
	console.log(loginDataJsonStr);
	
	$.ajax({
        url: 'login/teacher',
        type: 'post',
        headers: {
          'Content-type': 'application/json'
        },
        dataType: 'json',
        data: loginDataJsonStr,
      })
	.done(function(json){
		console.log(json);
		if(json.code===0){
			window.alert("登陆成功");
			//设置首页的 登录/注册按钮 // TODO(还要考虑退出，设置全局变量)
			USER.status=1;
			USER.username=username;
			setCookie("user",JSON.stringify(USER),1/48);
			window.location.href = '/teacher.html';
		//登陆失败
		}else if(json.code===1){
			window.alert("登陆失败！"+json.msg);
			$('#captcha_login_img').attr('src','login/login/captcha?tm='+Math.random());
		//其他情况，内部服务器错误等
		}else{
			window.alert("登录失败！后台服务器有问题！请稍后重试！"+json.msg);
			$('#captcha_login_img').attr('src','login/login/captcha?tm='+Math.random());
		}
		
	}
	)
	
}


/*登陆按钮的事件*/
$("#login").click(function() {
	let username = $('#username_login').val();
	let password = $('#password_login').val();
	let loginCaptcha = $('#captcha_login').val();
	login(username,password,loginCaptcha);
});


/************ 注册的******************/

/**
 * 检查用户名
 */
function checkUsername(username){
	$("#username_error_reg").hide();
	if(isNullOrWhiteSpace(username)){
		$("#username_error_reg").html("用户名为空或全是空格"); 
		$("#username_error_reg").show();
		return false;
	}else if(!lengthReg.test(username)){
		$("#username_error_reg").html("用户名长度大于20个字符"); 
		$("#username_error_reg").show();
		return false;
	}
	return true;
}

/**
 * 检查昵称（可以为null，非必填）
 */
function checkNickname(nickname){
	$("#nickname_error_reg").hide();
    if(!lengthReg.test(nickname)){
		$("#nickname_error_reg").html("昵称长度大于20个字符"); 
		$("#nickname_error_reg").show();
		return false;
	}
	return true;
}

/**
 * 检查密码
 */
function checkPassword(password){
	$("#password_error_reg").hide();
	if(isNullOrWhiteSpace(password)){
		$("#password_error_reg").html("密码为空或全是空格"); 
		$("#password_error_reg").show();
		return false;
	}else if(!lengthReg.test(password)){
		$("#password_error_reg").html("密码长度大于20个字符"); 
		$("#password_error_reg").show();
		return false;
	}
	return true;
}

/**
 * 检查二次密码
 */
function checkCertainPassword(password,certainPassword){
	$("#certain_password_error_reg").hide();
	if(password!=certainPassword){
		$("#certain_password_error_reg").html("两次密码不一致！");
		$("#certain_password_error_reg").show();
		return false;
	}
	return true;
}

/**
 * 检查验证码
 */
function checkRegCaptcha(regCaptcha){
	$("#captcha_error_reg").hide();
	if(isNullOrWhiteSpace(regCaptcha) || regCaptcha.length!==5){
		$("#captcha_error_reg").html("验证码不正确！"); 
		$("#captcha_error_reg").show();
		return false;
	}	
	return true;
}

/**
 * 注册验证是否填写正确信息的函数,如果有错误，则返回false；如果都正确了，则返回true；
 */
function reginValid(username,nickname,password,certainPassword,regCaptcha){
	return (checkUsername(username)&&
	checkNickname(nickname)&&
	checkPassword(password)&&
	checkCertainPassword(password,certainPassword)&&
	checkRegCaptcha(regCaptcha));
}


/**
 * 注册发送json的函数
 */
function register(username,nickname,password,certainPassword,regCaptcha) {
	//如果基础信息验证不通过，直接返回false
	let validFlag = reginValid(username,nickname,password,certainPassword,regCaptcha);
	if(validFlag===false){
		return;
	}
	let reginDataJson={
			"username":username,
			"password":password,
			"email":"",
			"imgCaptcha":regCaptcha
	}
	let reginDataJsonStr = JSON.stringify(reginDataJson);
	console.log(reginDataJsonStr);
	
	$.ajax({
        url: 'login/register',
        type: 'post',
        headers: {
          'Content-type': 'application/json'
        },
        dataType: 'json',
        data: reginDataJsonStr,
      })
	.done(function(json){
		console.log(json);
		if(json.code===0){
			window.alert("注册成功");
			window.location.href = '/login.html';
		//注册失败
		}else if(json.code===1){
			window.alert("注册失败！"+json.msg);
			$('#captcha_reg_img').attr('src','login/register/captcha?tm='+Math.random());
		//其他情况，内部服务器错误等
		}else{
			window.alert("注册失败！后台服务器有问题！请稍后重试！"+json.msg);
			$('#captcha_reg_img').attr('src','login/register/captcha?tm='+Math.random());
		}
		
	}
	)
	
}

/*注册按钮的事件*/
$("#register").click(function() {
	let username = $('#username_reg').val();
	let nickname = $('#nickname').val();
	let password = $('#password_reg').val();
	let certainPassword = $('#certain_password_reg').val();
	let regCaptcha = $('#captcha_reg').val();
	register(username,nickname,password,certainPassword,regCaptcha);
});

/*再次输入密码按钮事件*/
$('#certain_password_reg').change(function(){
	let password = $('#password_reg').val();
	let certainPassword = $('#certain_password_reg').val();
	checkCertainPassword(password,certainPassword);
});
		


function addHandler(id, action, func) {
    'use strict';
    var domID = document.querySelector(`#${id}`);
    domID.addEventListener(action, function(event) {
      event.preventDefault();
      func(domID.value);
    });
  }

 function init() {
    'use strict';
    addHandler('username_reg','keyup',checkUsername);
    addHandler('nickname','keyup',checkNickname);
    addHandler('password_reg','change',checkPassword);
    addHandler('captcha_reg','change',checkRegCaptcha);
}
init();
 

