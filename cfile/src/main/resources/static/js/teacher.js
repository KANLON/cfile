
/**
 * 初始化函数
 */
function init(){
	 getAllTask();
	//画未交和已交的饼图
	 drawingPie(unSubmitAndSubmitingPieData,'unsubmit_submiting_num');
	 //画任务总数的饼图
	 drawingTaskNum(taskNumPieData,"task_num",0);
}

init();
/**
 * 得到所有已经发布的任务信息
 */
function getAllTask(){
      $.ajax({  
	       url: '/teacher/all/tasks',  
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
	    		   //提交的人数大于0才能下载文件
	    		   let fileLinkStr="暂无人交";
	    		   //未提交的人数
	    		   let unSubmitNum = json.data[i].submitNum-json.data[i].submitingNum;
	    		   unSubmitNum = unSubmitNum<=0?0:unSubmitNum;
	    		   if(json.data[i].submitingNum>0){
	    		       fileLinkStr = "<a href='teacher/files/"+json.data[i].tid +"'>点击下载</a>";
	    		   }
	    		   s+="<tr id='task_"+json.data[i].tid+"'><td>"+cnt+'</td><td>' + json.data[i].taskName
	    		   +"</td><td id='submiting_num_"+ json.data[i].tid +"'>" + json.data[i].submitingNum+"</td><td id='submit_num_"+ json.data[i].tid +"'>" 
	    		   + json.data[i].submitNum+'</td><td>' + json.data[i].dendlineStr+'</td><td>' + json.data[i].authencation 
	    		   +'</td><td>' + fileLinkStr+ '</td></tr>';
	    		   //默认加载第一个任务的名单时
	    		   if(cnt==1){
	    			   setOverView(json.data[i].tid);
	    			   getOneTaskSubmitList(json.data[i].tid);
	    			   //画未交和已交的饼图
	    			   unSubmitAndSubmitingPieData=[ {name : '已交',y : json.data[i].submitingNum,color : '#89A54E'},{name : '未交',y : unSubmitNum,color : '#737fdb'} ];
	    			   drawingPie(unSubmitAndSubmitingPieData,'unsubmit_submiting_num');
	    			   //画任务总数的饼图
	    			   taskNumPieData= [ {name : '任务总数',y : json.data.length,color : '#4572a7'} ];
	    			   drawingTaskNum(taskNumPieData,"task_num",json.data.length);
	    		   }
	    	   }
	    	   $('#all_task_info').empty();
	    	   $('#all_task_info').append('<thead> <tr class="CaseRow"> <th>序号</th> <th>任务名</th> <th>已提交数</th><th>预提交数</th><th>截止时间</th><th>是否验证</th><th>文件</th></tr> </thead>');
	    	   $('#all_task_info').append(s);
	    	   rollbackTableBlackColor();
	    	   
	    	   //给表格每行添加点击事件和鼠标经过事件
	    	   for(let i=0;i<json.data.length;i++){
	    		   let rowId = '#task_'+json.data[i].tid;
	    	     $(rowId).click(function(){
	    	    	  rowTaskClick(rowId);
	    	    	  rollbackTableBlackColor();
	    	     });
	    	     //鼠标经过每行事件
	    	     $(rowId).mouseover(function(){
	    	    	  rollbackTableBlackColor();
	    	    	  $(rowId).css("background-color", "floralwhite");
	    	     });
	    	   }
	    	   
	       }else if(json.code==1){  
	           alert('打开页面失敗,请求错误！'+json.msg);  
	       }else{
	    	   alert("内部服务器错误！请重试或联系管理者：zhangcanlong"+json.msg);
	       }  
	   }); 
}


/**
 * 得到某个任务的提交名单
 */
function getOneTaskSubmitList(tid){
      $.ajax({  
	       url: '/teacher/task/list/'+tid,  
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
	    		   let studentId = json.data[i].substr(0, 9);
	    		   let name = json.data[i].substr(9, json.data[i].indexOf(".")-9);
	    		   cnt++;
	    		   s+='<tr><td>'+cnt+'</td><td>' + studentId+'</td><td>' + name+'</td><td>'  + "<a href='teacher/file/"+tid + "?filename="+json.data[i]+"'>点击下载</a>" + '</td></tr>';
	    	   }
	    	   $('#task_submit_list').empty();
	    	   $('#task_submit_list').append('<thead> <tr class="CaseRow"> <th>序号</th> <th>学号</th> <th>姓名</th><th>文件</th></tr> </thead>');
	    	   $('#task_submit_list').append(s);
	    	   
	       }else if(json.code==1){  
	           alert('打开页面失敗,请求错误！'+json.msg);  
	       }else{
	    	   alert("内部服务器错误！请重试或联系管理者：zhangcanlong"+json.msg);
	       }  
	   }); 
}

/**
 * 根据任务id，设置该任务的总体情况展示
 */
function setOverView(tid){
	$.ajax({  
	       url: 'teacher/task/'+tid,  
	       type: 'GET',  
	       dataType:'json',
	       cache: false,  
	       processData: true,  
	   }).done(function(json){  
		   console.log(json);
	       if(json.code==0){  
	    	   $('#task_name_overview').html(json.data.taskName+"概况");
	    	   
	       }else if(json.code==1){  
	           alert('打开页面失敗,请求错误！');  
	       }else{
	    	   alert("内部服务器错误！请重试或联系管理者：zhangcanlong");
	       }  
	   }); 
}

/**
 * 任务信息每行点击之后的事件
 */
function rowTaskClick(rowId){
	$(rowId).css("background-color", "floralwhite");
	let tid = rowId.substr(rowId.indexOf("_")+1);
	let submitingNum = $('#submiting_num_'+tid).html();
	let unSubmitNum = $('#submit_num_'+tid).html()-$('#submiting_num_'+tid).html();
	unSubmitNum = unSubmitNum<=0?0:unSubmitNum;
	setOverView(tid);
	//画未交和已交的饼图
	unSubmitAndSubmitingPieData=[ {name : '已交',y : parseInt(submitingNum),color : '#89A54E'},{name : '未交',y : parseInt(unSubmitNum),color : '#737fdb'} ];
	$('#unsubmit_submiting_num').html("");
	drawingPie(unSubmitAndSubmitingPieData,'unsubmit_submiting_num');
	getOneTaskSubmitList(tid);
}


/**
 * 还原表格的颜色
 */
function rollbackTableBlackColor(){
	$('tr').css("background-color", "#fff");
}







