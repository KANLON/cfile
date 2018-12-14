//登录的信息的全局变量，存储登录用户的信息()
USER={
	"username":"",
	"nickname":"",
	//表示登录的状态，0表示未登录，1表示已登录
	"status":0 
};
/**
 * 设置cookie
 */
function setCookie(cname,cvalue,exdays){
    var d = new Date();
    d.setTime(d.getTime()+(exdays*24*60*60*1000));
    var expires = "expires="+d.toGMTString();
    document.cookie = cname+"="+cvalue+"; "+expires;
}
/**
 * 得到cookie中的值
 */
function getCookie(cname){
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name)===0) { return c.substring(name.length,c.length); }
    }
    return "";
}


/**
 * 判断字符串是否为null或者都是空格，如：str=null，str="  "
 */
function isNullOrWhiteSpace(str){
	if(str===null||str===undefined||str==="" || str.replace(/\s+/g,"")===""){
		return true;
	}
	return false;
}

/**
 * 判断是否为字符串是否为null，如：str="",str=null
 */
function isNull(str){
	if(str===null||str===undefined||str===""){
		return true;
	}
	return false;
}

/**
 * 将fromdata转化为json数据
 */
function convertFormDataToJson(formData) {
 let objData = {};
 for (let entry of formData.entries()){
     objData[entry[0]] = entry[1];
 }
 return JSON.stringify(objData);
}


/**
 * 获取根据url获取链接后面的参数，即获取？后面的参数，一般传入：location.search（表示获取？后面的字符，包含？），window.location.href获取的是url完整的字符串
 * 调用例子：
 * var Param= new Object();
 * Param= getUrlParam();
 * 参数1 = Param['参数1'];
 * 参数2 = Param['参数2'];
 */
function getUrlParam(url) { 
    //var url = location.search; //获取url中"?"符后的字符串包括‘？’ ，window.location.href获取的是url完整的字符串
    var theParam = {}; 
    if (url.indexOf("?") != -1) { //确保‘？’不是最后一个字符串，即携带的参数不为空
        var str = url.substr(url.indexOf("?")+1); //substr是字符串的用法之一，抽取指定字符的数目，这里抽取？后的所有字符
        strs = str.split("&"); //将获取到的字符串从&分割，输出参数数组，即输出[参数1=xx,参数2=xx,参数3=xx,...]的数组形式
        for(var i = 0; i < strs.length; i ++) { //遍历参数数组
            theParam[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]); //这里意思是抽取每个参数等号后面的值，unescape是解码的意思
        } 
    } 
    return theParam; //返回参数值
}

/**
 * 得到字符串中的所有的数字，返回数组
 */
function getNumsInStr(str){
	let nums=[];
	let numStr = str.replace(/[^0-9]/ig,",");
	nums = numStr.split(/,+/);
	return nums;
}

/**
 * 保留2位小数
 */
function twoDecimal(x) {
	var f_x = parseFloat(x);
	if (isNaN(f_x)) {
		alert('错误的参数');
		return false;
	}
	f_x = Math.round(x * 100) / 100;
	var s_x = f_x.toString();
	var pos_decimal = s_x.indexOf('.');
	if (pos_decimal < 0) {
		pos_decimal = s_x.length;
		s_x += '.';
	}
	while (s_x.length <= pos_decimal + 2) {
		s_x += '0';
	}
	return s_x;
}

/**
 * 根据英文类型得到中文类型，和设置上传文件的格式
 */
function getChineseTypeByFileType(fileType){
	if(fileType=='all'){
		return "全部类型";
	}else if(fileType=="word"){
		$('#file').attr("accept","application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.wordprocessingml.template");
		return "Word文档";
	}else if(fileType=="excel"){
		$('#file').attr("accept","application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.openxmlformats-officedocument.spreadsheetml.template,application/vnd.ms-excel.addin.macroEnabled.12");
		return "Excel文件";
	}else if(fileType=="powerpoint"){
		$('#file').attr("accept","application/vnd.ms-powerpoint,application/vnd.openxmlformats-officedocument.presentationml.presentation");
		return "PPT文件";
	}else if(fileType=="image"){
		$('#file').attr("accept","image/*");
		return "图片文件";
	}else if(fileType=="pdf"){
		$('#file').attr("accept","application/pdf");
		return "PDF文件";
	}else if(fileType=="zip"){
		//不能判断是不是rar压缩文件，所以暂时放弃了
		//$('#file').attr("accept","");
		return "压缩包";
	}else{
		return "全部类型";
	}
	
}


/**
 * 根据英文类型得到中文类型，和设置上传文件的格式
 */
function getEnglishFileTypeByChineseFileType(fileType){
	if(fileType=="全部类型"){
		return 'all';
	}else if(fileType=="Word文档"){
		$('#file').attr("accept","application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.wordprocessingml.template");
		return "word";
	}else if(fileType=="Excel文件"){
		$('#file').attr("accept","application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.openxmlformats-officedocument.spreadsheetml.template,application/vnd.ms-excel.addin.macroEnabled.12");
		return "excel";
	}else if(fileType=="PPT文件"){
		$('#file').attr("accept","application/vnd.ms-powerpoint,application/vnd.openxmlformats-officedocument.presentationml.presentation");
		return "powerpoint";
	}else if(fileType=="图片文件"){
		$('#file').attr("accept","image/*");
		return "image";
	}else if(fileType=="PDF文件"){
		$('#file').attr("accept","application/pdf");
		return "pdf";
	}else if(fileType=="压缩包"){
		//不能判断是不是rar压缩文件，所以暂时放弃了
		//$('#file').attr("accept","");
		return "zip";
	}else{
		return "全部类型";
	}
	
}

/*输入函数模块*/
/*export default;*/