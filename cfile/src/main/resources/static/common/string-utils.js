/**
 * 判断字符串是否为null或者都是空格，如：str=null，str="  "
 */
function isNullOrWhiteSpace(str){
	if(str==null||str==undefined||str=="" || str.replace(/\s+/g,"")==""){
		return true;
	}
	return false;
}

/**
 * 判断是否为字符串是否为null，如：str="",str=null
 */
function isNull(str){
	if(str==null||str==undefined||str==""){
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
};


/**
 * 获取根据url获取链接后面的参数，即获取？后面的参数，一般传入：location.search（表示获取？后面的字符，包含？），window.location.href获取的是url完整的字符串
 * 调用例子：
 * var Param= new Object();
 * Param= getUrlParam();
 * 参数1 = Param['参数1'];
 * 参数2 = Param['参数2'];
 */
function getUrlParam(url) { 
    var url = location.search; //获取url中"?"符后的字符串包括‘？’ ，window.location.href获取的是url完整的字符串
    var theParam = new Object(); 
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
	var f_x = Math.round(x * 100) / 100;
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


/*输入函数模块*/
/*export default;*/