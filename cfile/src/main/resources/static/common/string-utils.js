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
function isNull(){
	if(str==null||str==undefined||str==""){
		return true;
	}
	return false;
}

/*输入函数模块*/
/*export default;*/