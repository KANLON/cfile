/**
 * 未交和已交的人数数据
 */
let unSubmitAndSubmitingPieData = [ {name : '已交',y : 0,color : '#89A54E'},{name : '未交',y : 0,color : '#737fdb'} ];
/**
 * 任务总数的数据
 */
let taskNumPieData = [ {name : '任务总数',y : 0,color : '#4572a7'} ];

/**
 * 画饼形图 
 */
function drawingPie(pieData,containId) {
	let chart = new Highcharts.Chart({
		chart : {
			renderTo : containId //关联页面元素div#id
		},
		title : { //图表标题
			text : ''
		},
		tooltip : {
			formatter : function() { //格式化鼠标滑向图表数据点时显示的提示框
				var s;
				if (this.point.name) { // 饼状图
					s = '<b>' + this.point.name + '</b>: <br>' + this.y + '人(' + twoDecimal(this.percentage) + '%)';
				} 
				return s;
			}
		},
		exporting : {
			enabled : true //设置导出按钮可用
		},
		credits : {
			enabled:false,
			text : '#',
			href : '#'
		},
		series : [
			{
				type : 'pie', //饼状图
				name : '已交人数/未交人数',
				data : pieData,
				//center : [ 60, 70 ], //饼状图坐标
				size : 150, //饼状图直径大小
				dataLabels : {
					enabled : false //不显示饼状图数据标签
				}
			}

		]
	});
};

/**
 * 画任务总数的饼图
 */
function drawingTaskNum(taskNumData,containId,taskNum){
var chart2 = Highcharts.chart('task_num', {
    chart: {
        spacing : [40, 0 , 40, 0],
    },
    title: {
        floating:true,
        text: taskNum+'个'
    },
    tooltip: {
        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
    },
	credits : {
		enabled:false,
	},
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: true,
                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                style: {
                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                }
            },
            point: {
                events: {
                    mouseOver: function(e) {  // 鼠标滑过时动态更新标题
                        // 标题更新函数，API 地址：https://api.hcharts.cn/highcharts#Chart.setTitle
                        chart2.setTitle({
                            text: e.target.name+ '\t'+ e.target.y + ' %'
                        });
                    }
                }
            },
        }
    },
    series: [{
        type: 'pie',
        innerSize: '80%',
        name: '任务总数',
		//center : [ 60, 70 ], //饼状图坐标
		size : 150, //饼状图直径大小
        data: taskNumData
    }]
}, function(c) { // 图表初始化完毕后的会掉函数
    // 环形图圆心
    var centerY = c.series[0].center[1], titleHeight = parseInt(c.title.styles.fontSize);
    // 动态设置标题位置
    c.setTitle({ y:centerY + titleHeight/2 });
});
}



