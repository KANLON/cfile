let lengthReg =  new RegExp("^.{0,20}$");

/**
 * 初始化函数
 */
function init(){
	showTaskInfo();
	getSubmitStudentIdList();
}
init();

/**
 * 检查输入的名字是否符合要求
 */
function checkName(name){
	$("#name_error").hide();
	if(isNullOrWhiteSpace(name)){
		$("#name_error").html("姓名为空或全是空格"); 
		$("#name_error").show();
		return false;
	}else if(!lengthReg.test(name)){
		$("#name_error").html("姓名长度大于20个字符"); 
		$("#name_error").show();
		return false;
	}
	return true;
}

/**
 * 检查输入的学号是否符合要求
 */
function checkStudentId(studentId){
	$("#student_id_error").hide();
	if(isNullOrWhiteSpace(studentId)){
		$("#student_id_error").html("学号为空或全是空格"); 
		$("#student_id_error").show();
		return false;
	}else if(studentId.length!=9){
		$("#student_id_error").html("学号长度不是9位，请填写正确的学号！"); 
		$("#student_id_error").show();
		return false;
	}
	return true;
}

/**
 * 检查是否有提交文件
 */
function checkFile(file){
	$('#file_error').hide();
	//var fileInput = $('#reportXML').get(0).files[0];
	//不为null
	if(file){
		return true;
	}else{
		//为null
		$('#file_error').show();
		return false;
	}
}

/**
 * 得到已经提交了任务文件的学号
 */
function getSubmitStudentIdList(){
	let tid = getUrlParam(window.location.search)['tid'];
	let uid = getUrlParam(window.location.search)['uid'];
      $.ajax({  
	       url: 'student/task/list/'+uid+"/"+tid,  
	       type: 'GET',  
	       dataType:'json',
	       cache: false,  
	       processData: true,  
	   }).done(function(json){  
		   console.log(json);
	       if(json.code==0){
	    	   //递增序号
	    	   let cnt = 0;
	    	   //表格的html结果
	    	   let s='';
	    	   //最多遍历100个名单
	    	   let minNum =json.data.length>100?100:json.data.length;
	    	   //遍历所有名单
	    	   for(let i=0;i<minNum;i++){
	    		   cnt++;
	    		   s+='<tr><td>'+cnt+'</td><td>'+ + json.data[i] + '</td></tr>';
	    	   }
	    	   $('#submit_list_table').empty();
	    	   $('#submit_list_table').append('<thead> <tr class="CaseRow"> <td>序号</td> <td>学号</td> </tr> </thead>');
	    	   $('#submit_list_table').append(s);
	    	   
	       }else if(json.code==1){  
	           alert('打开页面失敗,请求错误！'+json.msg);  
	       }else{
	    	   alert("内部服务器错误！请重试或联系管理者：zhangcanlong"+json.msg);
	       }  
	   }); 
	
}

/**
 * 展示任务信息
 */
function showTaskInfo(){
	let tid = getUrlParam(location.search)['tid'];
	let uid = getUrlParam(location.search)['uid'];
	$.ajax({  
	       url: 'student/task/'+tid,  
	       type: 'GET',  
	       dataType:'json',
	       cache: false,  
	       processData: true,  
	   }).done(function(json){  
		   console.log(json);
	       if(json.code==0){  
	    	   $("#task_name").html(json.data.taskName); 
	    	   $("#publisher").html("发布人："+json.data.publisher); 
	    	   $("#dendline").html(json.data.dendlineStr); 
	    	   $("#file_type").html(getChineseTypeByFileType(json.data.fileType));
	    	   let authenStr =json.data.authentication=='1'?'是':'否(请勿提交隐私信息)';
	    	   $("#authentication").html(authenStr); 
	    		//$("#submitNum").html(submitNum); 
	    	   $("#remark").html(json.data.remark); 
	    	   $('#task_list').attr("href","student_submit_list.html?uid="+uid+"&tid="+tid);
	       }else if(json.code==1){  
	           alert('打开页面失敗,请求错误！');  
	       }else{
	    	   alert("内部服务器错误！请重试或联系管理者：zhangcanlong");
	       }  
	   }); 
	
}
/**
 * 根据英文类型得到中文类型
 */
function getChineseTypeByFileType(fileType){
	if(fileType=='all'){
		return "全部类型";
	}else if(fileType=="word"){
		return "word文档"
	}else if(fileType=="excel"){
		return "excel文件"
	}else if(fileType=="powerpoint"){
		return "PowerPoint文件，PPT文件"
	}else if(fileType=="image"){
		return "图片"
	}else if(fileType=="pdf"){
		return "PDF文件"
	}else if(fileType=="zip"){
		return "压缩包"
	}
}


/**
 * 提交任务信息和文件
 */
function submitFile(taskInfoFromData,uid,tid){
   console.log("发送的fromdata"+convertFormDataToJson(taskInfoFromData));
   $.ajax({  
       url: 'student/submit/'+uid+'/'+tid,  
       type: 'POST',  
       data: taskInfoFromData,  
       dataType:'json',
       cache: false,  
       processData: false,  
       contentType: false  
   }).done(function(json){  
	   console.log(json);
       if(json.code==0){  
           alert("提交成功");  
           //getSubmitStudentIdList();
           window.location.reload();
       }else if(json.code==1){  
           alert('提交失敗,请求错误！');  
       }else{
    	   alert("内部服务器错误！请重试或联系管理者：zhangcanlong");
       }  
   });  
}

/*提交点击按钮的事件*/
$("#student_submit").click(function() {
	let flagVaild = checkName($('#name').val()) && checkName($('#student_id').val()) &&checkFile($('#file').get(0).files[0]);
	if(!flagVaild){
		return;
	}
	let taskInfoFromData = new FormData($('#student_task')[0]);
	let uid = getUrlParam(location.search)['uid'];
	let tid = getUrlParam(location.search)['tid'];
	if(isNull(uid) || isNull(tid)){
		window.alert("请确保url正确！，当前你的url缺少uid或tid参数");
	}else{
	    submitFile(taskInfoFromData,uid,tid);
	}
});

/*输入姓名改变事件*/
$('#name').change(function(){
	checkName($('#name').val());
});
/*输入学号改变事件*/
$('#student_id').change(function(){
	checkStudentId($('#student_id').val());
});
/*提交文件改变事件*/
$('#file').change(function(){
	checkFile($('#file').get(0).files[0]);
});





