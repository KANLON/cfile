//创建/修改 任务 提交状态（用于防止重复提交表单）,默认为未提交（false）
let createOrModifySubmitStatu = false;


/************       全部任务信息的js   ****************/

/**
 * 初始化函数
 */
function init() {
	getAllTask();
	//画未交和已交的饼图
	drawingPie(unSubmitAndSubmitingPieData, 'unsubmit_submiting_num');
	//画任务总数的饼图
	drawingTaskNum(taskNumPieData, "task_num", 0);
	//测试隐藏所有任务功能页面
	//$('#all-task-info-div').hide();
	//隐藏创建（修改）任务的功能页面
	$('#create-task-div').hide();
	//隐藏“个人中心”页面
	$("#person-center-div").hide();
	//隐藏“关于”页面
	$("#about-div").hide();
	//时间控件的设置
	$("#dendline_create").datetimepicker({
		format : 'yyyy-mm-dd hh:ii'
	});
	//隐藏所有错误信息
	$(".help-block").hide();
}

init();
/**
 * 得到所有已经发布的任务信息
 */
function getAllTask() {
	$.ajax({
		url : '/teacher/all/tasks',
		type : 'GET',
		dataType : 'json',
		cache : false,
		processData : true,
	}).done(function(json) {
		console.log(json);
		if (json.code === 0) {
			// 递增序号
			let cnt = 0;
			// 所有任务信息页面表格的row结果
			let s = '';
			// 创建/修改任务页面的表格row结果
			let rowDataStr = "";
			//如果名单为null,直接返回
			if(isNull(json.data)){
				return;
			}
			// 遍历所有名单
			for (let i = 0; i < json.data.length; i++) {
				cnt++;
				//提交的人数大于0才能下载文件
				let fileLinkStr = "暂无人交";
				//未提交的人数
				let unSubmitNum = json.data[i].submitNum - json.data[i].submitingNum;
				unSubmitNum = unSubmitNum <= 0 ? 0 : unSubmitNum;
				//提交任务的链接
				let submitTaskLinkStr = "<a id='a_" + json.data[i].tid + "' href='#'>点击获取 </a>";
				if (json.data[i].submitingNum > 0) {
					fileLinkStr = "<a href='teacher/files/" + json.data[i].tid + "'>点击下载</a>";
				}

				s += "<tr id='task_" + json.data[i].tid + "'><td>" + cnt + '</td><td>' + json.data[i].taskName +
					"</td><td id='submiting_num_" + json.data[i].tid + "'>" + json.data[i].submitingNum + "</td><td id='submit_num_" + json.data[i].tid + "'>" +
					json.data[i].submitNum + '</td><td>' + json.data[i].dendlineStr + '</td><td>' +
					json.data[i].authencation + '</td><td>' + fileLinkStr + '</td><td>' + submitTaskLinkStr + '</td></tr>';

				rowDataStr += "<tr id='create-row-task-" + json.data[i].tid + "'><td>" + cnt + "</td><td id='create-task-name-" + json.data[i].tid + "'>" + json.data[i].taskName +
					"</td><td id='create-submit-num-" + json.data[i].tid + "'>" + json.data[i].submitNum + "</td><td id='create-dendline-str-" + json.data[i].tid + "'>" + json.data[i].dendlineStr + "</td>" +
					"<td id='create-file-type-" + json.data[i].tid + "'>" + getChineseTypeByFileType(json.data[i].fileType) + "</td>" +
					"<td>" + "<a href='#' id='create-row-a-" + json.data[i].tid + "'>点击修改</a>" + "</td>" +
					"<td id='create-remark-" + json.data[i].tid + "'>" + json.data[i].remark + "</td>" +
					"</tr>";

				//默认加载第一个任务的名单时
				if (cnt == 1) {
					setOverView(json.data[i].tid);
					getOneTaskSubmitList(json.data[i].tid);
					//画未交和已交的饼图
					unSubmitAndSubmitingPieData = [ {
						name : '已交',
						y : json.data[i].submitingNum,
						color : '#89A54E'
					}, {
						name : '未交',
						y : unSubmitNum,
						color : '#737fdb'
					} ];
					drawingPie(unSubmitAndSubmitingPieData, 'unsubmit_submiting_num');
					//画任务总数的饼图
					taskNumPieData = [ {
						name : '任务总数',
						y : json.data.length,
						color : '#4572a7'
					} ];
					drawingTaskNum(taskNumPieData, "task_num", json.data.length);
				}
			}
			//设置创建或修改页面的任务信息
			setAllTaskInCreatePage(rowDataStr);
			//设置全部任务信息页面的任务表格
			$('#all_task_info').empty();
			$('#all_task_info').append('<thead> <tr class="CaseRow"> <th>序号</th> <th>任务名</th> <th>已提交数</th><th>预提交数</th><th>截止时间</th><th>是否验证</th><th>文件</th><th>提交链接</th></tr> </thead>');
			$('#all_task_info').append(s);
			rollbackTableBlackColor();

			//给表格每行添加点击事件和鼠标经过事件
			for (let i = 0; i < json.data.length; i++) {
				//添加查看所有任务的提交名单和任务概况
				let rowId = '#task_' + json.data[i].tid;
				let taskLinkId = '#a_' + json.data[i].tid;
				$(rowId).click(function() {
					rowTaskClick(rowId);
				});
				//鼠标经过每行事件
				$(rowId).mouseover(function() {
					//rollbackTableBlackColor();
					//$(rowId).css("background-color", "floralwhite");
				});
				//给获取任务id添加事件
				$(taskLinkId).click(function() {
					getTaskLink(taskLinkId.substr(taskLinkId.indexOf("_") + 1));
				//alert("学生提交该任务文件的链接为(复制以下链接发送给学生，让其提交文件即可)：\n");
				});

				//添加创建修改页面的任务修改的修改链接事件
				let modifyAId = "#create-row-a-" + json.data[i].tid;
				$(modifyAId).click(function() {
					setModifyTask(modifyAId);
				});
			}

		} else if (json.code == 1) {
			alert('打开页面失敗,请求错误！' + json.msg);
		} else {
			alert("内部服务器错误！请重试或联系管理者：zhangcanlong" + json.msg);
		}
	});
}


/**
 * 得到某个任务的提交名单
 */
function getOneTaskSubmitList(tid) {
	$.ajax({
		url : '/teacher/task/list/' + tid,
		type : 'GET',
		dataType : 'json',
		cache : false,
		processData : true,
	}).done(function(json) {
		console.log(json);
		if (json.code === 0) {
			//递增序号
			let cnt = 0;
			//表格的html结果
			let s = '';
			//遍历所有名单
			for (let i = 0; i < json.data.length; i++) {
				let studentId = json.data[i].substr(0, 9);
				let name = json.data[i].substr(9, json.data[i].indexOf(".") - 9);
				cnt++;
				s += '<tr><td>' + cnt + '</td><td>' + studentId + '</td><td>' + name + '</td><td>' + "<a href='teacher/file/" + tid + "?filename=" + json.data[i] + "'>点击下载</a>" + '</td></tr>';
			}
			$('#task_submit_list').empty();
			$('#task_submit_list').append('<thead> <tr class="CaseRow"> <th>序号</th> <th>学号</th> <th>姓名</th><th>文件</th></tr> </thead>');
			$('#task_submit_list').append(s);

		} else if (json.code == 1) {
			alert('打开页面失敗,请求错误！' + json.msg);
		} else {
			alert("内部服务器错误！请重试或联系管理者：zhangcanlong" + json.msg);
		}
	});
}

/**
 * 根据任务id，设置该任务的总体情况展示
 */
function setOverView(tid) {
	$.ajax({
		url : 'teacher/task/' + tid,
		type : 'GET',
		dataType : 'json',
		cache : false,
		processData : true,
	}).done(function(json) {
		console.log(json);
		if (json.code === 0) {
			$('#task_name_overview').html(json.data.taskName + "概况");

		} else if (json.code == 1) {
			alert('打开页面失敗,请求错误！');
		} else {
			alert("内部服务器错误！请重试或联系管理者：zhangcanlong");
		}
	});
}

/**
 * 得到某项目的提交链接
 */
function getTaskLink(tid) {
	$.ajax({
		url : 'teacher/task/link/' + tid,
		type : 'GET',
		dataType : 'json',
		cache : false,
		processData : true,
	}).done(function(json) {
		console.log(json);
		if (json.code === 0) {
			window.location.href=json.data;
		} else if (json.code == 1) {
			alert('获取错误！' + json.msg);
		} else {
			alert("内部服务器错误！请重试或联系管理者：zhangcanlong" + json.msg);
		}
	});

}

/**
 * 任务信息每行点击之后的事件
 */
function rowTaskClick(rowId) {
	rollbackTableBlackColor();
	$(rowId).css("background-color", "floralwhite");
	let tid = rowId.substr(rowId.indexOf("_") + 1);
	let submitingNum = $('#submiting_num_' + tid).html();
	let unSubmitNum = $('#submit_num_' + tid).html() - $('#submiting_num_' + tid).html();
	unSubmitNum = unSubmitNum <= 0 ? 0 : unSubmitNum;
	setOverView(tid);
	//画未交和已交的饼图
	unSubmitAndSubmitingPieData = [ {
		name : '已交',
		y : parseInt(submitingNum),
		color : '#89A54E'
	}, {
		name : '未交',
		y : parseInt(unSubmitNum),
		color : '#737fdb'
	} ];
	$('#unsubmit_submiting_num').html("");
	drawingPie(unSubmitAndSubmitingPieData, 'unsubmit_submiting_num');
	getOneTaskSubmitList(tid);
}


/**
 * 还原表格的颜色
 */
function rollbackTableBlackColor() {
	$('tr').css("background-color", "#fff");
}


/************       创建或修改页面的js   ****************/

/**
 * 在创建或修改任务设置全部任务列表,传入行的数据
 */
function setAllTaskInCreatePage(rowDataStr) {
	let tableTitle = '<thead> <tr class="CaseRow"> <th>序号</th> <th>任务名</th> <th>预提交数</th><th>截止时间</th><th>文件类型</th><th>修改</th><th>备注</th></tr> </thead>';
	$('#create-task-info-table').empty();
	$("#create-task-info-table").append(tableTitle);
	$("#create-task-info-table").append(rowDataStr);
}

/**
 * 创建任务
 */
function createTask(taskInfoStr) {
	//如果是已经发送异步请求，则直接返回
	if(createOrModifySubmitStatu){return;}
	//设置已经发送异步请求
	createOrModifySubmitStatu=true;
	console.log(taskInfoStr);
	var settings = {
		"async" : true,
		"crossDomain" : true,
		"url" : "teacher/task",
		"method" : "POST",
		"headers" : {
			"Content-Type" : "application/json"
		},
		"processData" : false,
		"data" : taskInfoStr
	};
	$.ajax(settings).done(function(json) {
		console.log(json);
		if (json.code === 0) {
			alert("创建任务 成功");
			cleanTaskInfo();
			getAllTask();
			//等异步请求回来后设置提交状态为false
			createOrModifySubmitStatu=false;
		} else if (json.code === 1) {
			alert('创建  失敗,请求错误！' + json.msg);
		} else {
			alert("内部服务器错误！请重试或联系管理者：zhangcanlong" + json.msg);
		}
	});

}

/**
 * 提交更新任务信息(根据提供的taskInfo数据和tid)
 */
function updateTask(taskInfoStr, tid) {
	//如果是已经发送异步请求，则直接返回
	if(createOrModifySubmitStatu){return;}
	//设置已经发送异步请求
	createOrModifySubmitStatu=true;
	console.log(taskInfoStr);
	var settings = {
		"async" : true,
		"crossDomain" : true,
		"url" : "teacher/task/" + tid,
		"method" : "PUT",
		"headers" : {
			"Content-Type" : "application/json"
		},
		"processData" : false,
		"data" : taskInfoStr
	};
	$.ajax(settings).done(function(json) {
		console.log(json);
		if (json.code === 0) {
			alert("修改成功");
			cleanTaskInfo();
			getAllTask();
			//等异步请求回来后设置提交状态为false
			createOrModifySubmitStatu=false;
		} else if (json.code === 1) {
			alert('修改失敗,请求错误！' + json.msg);
		} else {
			alert("内部服务器错误！请重试或联系管理者：zhangcanlong" + json.msg);
		}
	});

}

/**
 * 检查这个任务名是否是能输入的任务名
 */
function canInputTaskName(taskName) {
	$('#help-task-name-create').hide();
	if (isNullOrWhiteSpace(taskName)) {
		$('#help-task-name-create').html("任务名为空");
		$('#help-task-name-create').show();
		return false;
	} else if (taskName.length > 30) {
		$('#help-task-name-create').html("任务名长度大于30个字符");
		$('#help-task-name-create').show();
		return false;
	}
	return true;
}

/**
 * 检查这个输入的截止时间是否正确
 */
function canInputDendline(dendlineStr) {
	$('#help-dendline-create').hide();
	if (!/^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}$/.test(dendlineStr) && !isNull(dendlineStr)) {
		$('#help-dendline-create').html("截止时间不符合格式要求");
		$('#help-dendline-create').show();
		return false;
	}
	return true;
}

/**
 * 检查这个输入的预提交人数
 */
function canInputSubmitNum(submitNum) {
	$('#help-submit-num-create').hide();
	if (!(/^[0-9]{0,5}$/.test(submitNum) && submitNum <= 10000)) {
		$('#help-submit-num-create').html("预提交人数大于10000或不全是数字");
		$('#help-submit-num-create').show();
		return false;
	}
	return true;
}

/**
 * 检查这个输入的备注是否正确
 */
function canInputRemark(remark) {
	$('#help-remark-createe').hide();
	if (remark.length > 500) {
		$('#help-remark-create').html("输入的备注长度大于500!");
		$('#help-remark-create').show();
		return false;
	}
	return true;
}
/**
 * 点击表格中每行的修改链接时，设置修改内容,传入该a标签的id
 */
function setModifyTask(aId) {
	let tid = aId.split("-")[3];
	//得到该行任务的信息
	let taskName = $("#create-task-name-" + tid).html();
	let dendlineStr = $("#create-dendline-str-" + tid).html();
	let submitNum = $("#create-submit-num-" + tid).html();
	let fileType = $("#create-file-type-" + tid).html();
	fileType = getEnglishFileTypeByChineseFileType(fileType);
	let remark = $("#create-remark-" + tid).html();


	//设置隐藏域为修改，2和设置tid
	$('#create-or-modify').val(2);
	$('#input-hidden-tid').val(tid);
	//设置各个输入框的值
	$("#task_name_create").val(taskName);
	$("#dendline_create").val(dendlineStr.substr(0, 16));
	$("#submit_num_create").val(submitNum);
	$("#file_type_create").val(fileType);
	$("#remark_create").val(remark);
}

/**
 * 清除提交from中的任务信息，还原为默认值
 */
function cleanTaskInfo() {
	//设置隐藏域为创建，1
	$('#create-or-modify').val(1);
	$('#input-hidden-tid').val("");
	//设置各个输入框的值
	$("#task_name_create").val("");
	$("#dendline_create").val("");
	$("#submit_num_create").val("");
	$("#file_type_create").val("全部类型");
	$("#remark_create").val("");
}

/**
 * 所有任务按钮点击的事件
 */
function allTaskClick() {
	//激活目录
	$('#catalog-all-task').attr("class", "active");
	$('#catalog-create-or-modify-task').attr("class", "");
	$('#catalog-person-center').attr("class", "");
	$('#catalog-about').attr("class", "");
	//展所有任务功能页面
	$('#all-task-info-div').show();
	//隐藏创建（修改）任务的功能页面
	$('#create-task-div').hide();
	//隐藏“个人中心”页面
	$("#person-center-div").hide();
	//隐藏“关于”页面
	$("#about-div").hide();

}
/**
 * 创建/修改 按钮点击的事件
 */
function createOrModifyClick() {
	//激活目录
	$('#catalog-all-task').attr("class", "");
	$('#catalog-create-or-modify-task').attr("class", "active");
	$('#catalog-person-center').attr("class", "");
	$('#catalog-about').attr("class", "");
	//测试隐藏所有任务功能页面
	$('#all-task-info-div').hide();
	//隐藏创建（修改）任务的功能页面
	$('#create-task-div').show();
	//隐藏“个人中心”页面
	$("#person-center-div").hide();
	//隐藏“关于”页面
	$("#about-div").hide();

}
/**
 * 关于 按钮点击的事件
 */
function aboutClick() {
	//激活目录
	$('#catalog-all-task').attr("class", "");
	$('#catalog-create-or-modify-task').attr("class", "");
	$('#catalog-person-center').attr("class", "");
	$('#catalog-about').attr("class", "active");
	//测试隐藏所有任务功能页面
	$('#all-task-info-div').hide();
	//隐藏创建（修改）任务的功能页面
	$('#create-task-div').hide();
	//隐藏“关于”页面
	$("#about-div").show();
	//隐藏“个人中心”页面
	$("#person-center-div").hide();

}
/**
 * 退出按钮点击之后
 */
function logoutClick(){
	var settings = {
			"async" : true,
			"crossDomain" : true,
			"url" : "/login/logout",
			"method" : "GET",
			"processData" : false
		};
		$.ajax(settings).done(function(json) {
			console.log(json);
			if (json.code === 0) {
				console.log("退出成功");
				//登出设置cookie为null
				setCookie("user",null);
				window.location="/index.html";
			} else if (json.code === 1) {
				alert('修改失敗,请求错误！' + json.msg);
			} else {
				alert("内部服务器错误！请重试或联系管理者：zhangcanlong" + json.msg);
			}
		});
	
}

/**
 * “个人中心” 按钮点击的事件
 */
function personCenterClick() {
	//激活目录
	$('#catalog-all-task').attr("class", "");
	$('#catalog-create-or-modify-task').attr("class", "");
	$('#catalog-person-center').attr("class", "active");
	$('#catalog-about').attr("class", "");
	//测试隐藏所有任务功能页面
	$('#all-task-info-div').hide();
	//隐藏创建（修改）任务的功能页面
	$('#create-task-div').hide();
	//隐藏“个人中心”页面
	$("#person-center-div").show();
	//隐藏“关于”页面
	$("#about-div").hide();
}

/**********            个人中心页面功能函数                     **********/

/**
 * 进入个人中心页面，得到个人用户信息
 */
function getPersonCenterInfo(){
	var settings = {
			"async" : true,
			"crossDomain" : true,
			"url" : "/teacher/center/info",
			"method" : "GET",
			"processData" : false
		};
		$.ajax(settings).done(function(json) {
			console.log(json);
			if (json.code === 0) {
				console.log(json);
				$("#center-username").val(json.data.username);
				$("#center-nickname").val(json.data.nickname);
				$("#center-email").val(json.data.email);
			} else if (json.code === 1) {
				alert('修改失敗,请求错误！' + json.msg);
			} else {
				alert("内部服务器错误！请重试或联系管理者：zhangcanlong" + json.msg);
			}
		});
}

/**
 * 修改个人中心信息（目前这个只能修改昵称）
 */
function setCenterInfo(centerInfoStr){
	var settings = {
			"async" : true,
			"crossDomain" : true,
			"url" : "/teacher/center/info",
			"method" : "PUT",
			"processData" : false,
			"headers" : {
				"Content-Type" : "application/json"
			},
			"data":centerInfoStr
		};
		$.ajax(settings).done(function(json) {
			console.log(json);
			if (json.code === 0) {
				window.alert("修改成功！");
				$("#center-nickname-save").attr('disabled',true);
			} else if (json.code === 1) {
				window.alert('修改失敗,请求错误！' + json.msg);
			} else {
				window.alert("内部服务器错误！请重试或联系管理者：zhangcanlong" + json.msg);
			}
		});	
}


/**********             个人中心功能函数结束                               ***********/

/**
 * 点击昵称修改
 */
$("#center-nickname-save").click(function(){
	/**
	 * 个人信息的对象，主要用来封装修改的个人信息
	 */
	let centerInfo={};
	centerInfo.nickname=$("#center-nickname").val();
	let centerInfoStr = JSON.stringify(centerInfo);
	setCenterInfo(centerInfoStr);
});
//昵称输入框改变事件，设置按钮可以输入
$("#center-nickname").change(function(){
	$("#center-nickname-save").attr("disabled",false);
});


// 给创建/修改按钮添加事件
$("#create_submit").click(function() {
	let modifyOrCreateFlag = $('#create-or-modify').val();
	let tid = $("#input-hidden-tid").val();
	let taskName = $("#task_name_create").val();
	//自己加上时间秒数（因为后台接受需要）
	let dendline = $("#dendline_create").val();
	dendline = isNullOrWhiteSpace(dendline) ? "2099-01-01 00:00:00" : dendline + ":00";
	let submitNum = $("#submit_num_create").val();
	let fileType = $("#file_type_create").val();
	let remark = $("#remark_create").val();
	let taskInfo = {
		"taskName" : taskName,
		"dendlineStr" : dendline,
		"fileType" : fileType,
		"submitNum" : submitNum,
		"remark" : remark
	};
	let taskInfoStr = JSON.stringify(taskInfo);
	//创建任务
	if (modifyOrCreateFlag == 1) {
		//创建之前进行检验，检验不通过则返回
		let flagVaild = false;
		flagVaild = (canInputTaskName($("#task_name_create").val()) && canInputDendline($("#dendline_create").val()) &&
		canInputSubmitNum($("#submit_num_create").val()) && canInputRemark($("#remark_create").val()));
		if (!flagVaild) {
			return;
		}
		createTask(taskInfoStr);
	} else if (modifyOrCreateFlag == 2) {
		//更新之前进行检验，检验不通过则返回
		flagVaild = canInputTaskName($("#task_name_create").val()) && canInputDendline($("#dendline_create").val()) &&
		canInputSubmitNum($("#submit_num_create").val()) && canInputRemark($("#remark_create").val());
		if (!flagVaild) {
			return;
		}
		//修改任务
		updateTask(taskInfoStr, tid);
	}

});
//任务名改变事件
$("#task_name_create").change(function() {
	canInputTaskName($("#task_name_create").val());
});
//截止时间改变事件
$("#dendline_create").change(function() {
	canInputDendline($("#dendline_create").val());
});
//预提交人数改变事件
$("#submit_num_create").change(function() {
	canInputSubmitNum($("#submit_num_create").val());
});
//备注改变事件
$("#remark_create").change(function() {
	canInputRemark($("#remark_create").val());
});
//点击全部任务目录栏或导航栏
$("#catalog-all-task").click(function() {
	allTaskClick();

});
$("#nav-all-task").click(function() {
	allTaskClick();
});
//点击创建/修改任务的目录栏或导航栏
$("#catalog-create-or-modify-task").click(function() {
	createOrModifyClick();
});
$("#nav-create-or-modify-task").click(function() {
	createOrModifyClick();
});
//点击“个人中心”的目录栏或导航栏
$("#catalog-person-center").click(function() {
	personCenterClick();
	getPersonCenterInfo();
});
$("#nav-person-center").click(function() {
	personCenterClick();
	getPersonCenterInfo();
});

//点击关于的目录栏或导航栏
$("#catalog-about").click(function() {
	aboutClick();
});
$("#nav-about").click(function() {
	aboutClick();
});

//点击退出的目录栏或导航栏
$("#catalog-logout").click(function() {
	logoutClick();
});
$("#nav-logout").click(function() {
	logoutClick();
});