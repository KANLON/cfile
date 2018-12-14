let lengthReg =  new RegExp("^.{0,20}$");

/**
 * 初始化函数
 */
function init(){
	getSubmitStudentIdList();
}
init();


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
	    	   //遍历所有名单
	    	   for(let i=0;i<json.data.length;i++){
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
