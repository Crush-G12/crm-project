<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String basePath = request.getScheme() + "://" + request.getServerName()
+ ":" + request.getServerPort()
+ request.getContextPath() + "/"; %>
<html>
<head>
	<base href="<%= basePath %>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<%--bootStrap日期插件--%>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<!--  PAGINATION 分页插件 -->
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<script type="text/javascript">

	$(function(){
		//给创建按钮绑定单击事件
		$("#createActivity_btn").click(function (){
			//清空表单
			$("#createActivityForm").get(0).reset();
			//弹出模态窗口
			$("#createActivityModal").modal("show");
		});

		//给保存按钮绑定单击事件
		$("#saveActivity_btn").click(function (){
			//获取参数(获取标签的属性值)
			var owner = $("#create-marketActivityOwner").val();
			var name = $.trim($("#create-marketActivityName").val());
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endDate").val();
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-description").val());
			//发送请求之前，进行表单验证
			if(owner == ""){
				alert("所有者不能为空");
				return;
			}
			if(name == ""){
				alert("名称不能为空");
				return;
			}
			if(startDate != "" && endDate != ""){
				//比较字符串的大小
				if(startDate>endDate){
					alert("结束日期不能比开始日期小");
					return;
				}
			}else {
				alert("日期不能为空");
				return;
			}
			//成本是非负整数
			var regExp = /^(([1-9]\d*)|0)$/;
			if(!regExp.test(cost)){
				alert("成本只能是非负整数");
				return;
			}
			//使用Ajax给后台发送异步请求
			$.ajax({
				url:'/crm/workbench/activity/createActivity',
				data:{
					owner:owner,name:name,startDate:startDate,endDate:endDate,
					cost:cost,description:description
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if(data.code == "1"){
						//成功之后，关闭模态窗口
						$("#createActivityModal").modal("hide");
						//刷新市场活动
						var pageSize = $("#pagination").bs_pagination('getOption','rowsPerPage');
						queryActivityByConditionForPage(1,pageSize);
					}else {
						//失败之后，提示信息
						alert(data.message);
						//模态窗口不关闭
						$("#createActivityModal").modal("show");
					}
				}
			});

		});
	});

	<%--入口函数——页面加载完成后执行--%>
	$(function (){
		//使用类的选择器
		$(".addDate").datetimepicker({
			//通过参数控制日历的显示效果
			language:'zh-CN',
			//生成的日期格式
			format:'yyyy-mm-dd',
			//显示到‘日’即可
			minView:'month',
			//初始化日期
			initialDate:new Date(),
			//自动关闭日历
			autoclose:true,
			//选择当天的日期
			todayBtn:true,
			//显示清空按钮
			clearBtn:true
		});


		//查询所有数据的第一页和数据的总条数
		queryActivityByConditionForPage(1,10);

		//给查询按钮绑定单击事件
		$("#query_button").click(function (){
			var pageSize = $("#pagination").bs_pagination('getOption','rowsPerPage');
			queryActivityByConditionForPage(1,pageSize);
		});

		//全选/取消全选
		$("#select_checkbox").click(function (){
			if(this.checked == true){
				$("#tBody input[type = 'checkbox']").prop('checked',true);
			}else{
				$("#tBody input[type = 'checkbox']").prop('checked',false);
			}
		});

        //对所以多选框加上单击事件
        /*$("#tBody input[type = 'checkbox']").click(function (){
            if($("#tBody input[type = 'checkbox']").size() == $("#tBody input[type = 'checkbox']:checked").size()){
                $("#select_checkbox").prop('checked',true);
            }else {
                $("#select_checkbox").prop('checked',false);
            }
        });*/

        //对所以多选框加上单击事件
        $("#tBody").on("click","input[type = 'checkbox']",function (){
            if($("#tBody input[type = 'checkbox']").size() == $("#tBody input[type = 'checkbox']:checked").size()){
                $("#select_checkbox").prop('checked',true);
            }else {
                $("#select_checkbox").prop('checked',false);
            }
        })

		//删除市场活动功能
		//点击删除，提醒用户
		$("#delete_activity_btn").click(function (){
			//先获取活动的id
			var activityIds = $("#tBody input[type='checkbox']:checked");
			if(activityIds.size() == 0){
				alert("请先选择市场活动")
				return;
			}
			if(!window.confirm("您确定要删除吗？")){
				return;
			}
			var ids = "";
			$.each(activityIds,function (){
				ids += "id=" + this.value + "&";
			});
			ids = ids.substr(0,ids.length-1);
			//使用Ajax发送请求
			$.ajax({
				url:"/crm/workbench/activity/deleteActivityByIds",
				data:ids,
				type:'post',
				dataType:'json',
				success:function (data){
					//判断是否成功
					if(data.code == "0"){
						//失败则提示用户
						alert(data.message);
					}else if(data.code == "1"){
						//成功则刷新页面
						alert(data.message);
						var pageSize = $("#pagination").bs_pagination('getOption','rowsPerPage');
						queryActivityByConditionForPage(1,pageSize);
					}
				}
			});
		});

		//修改市场活动(显示模态窗口)
		$("#update_activity_btn").click(function (){
			//获取选中的市场活动（数组）
			var activityId = $("#tBody input[type='checkbox']:checked");
			if(activityId.size() > 1){
				alert("每次只能修改一个市场活动，请重新选择");
				return;
			}
			if(activityId.size() == 0){
				alert("请先选择市场活动");
				return;
			}
			//获取jQuery对象的值
			var id = activityId[0].value;
			//获取id后，查询出市场活动的信息
			$.ajax({
				url:"workbench/activity/queryActivityById",
				data:{id:id},
				type:'post',
				dataType:'json',
				success:function (data){
					//更改模态窗口的信息（此时模态窗口已经存在，只是没有显示出来）
					$("#edit-id").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startTime").val(data.startDate);
					$("#edit-endTime").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-describe").val(data.description);
					//显示模态窗口
					$("#editActivityModal").modal("show");
				}
			});
		});

		//保存修改市场活动
		$("#saveEditActivity_btn").click(function (){
			//表单验证，获取参数
			var id = $("#edit-id").val();
			var owner = $("#edit-marketActivityOwner").val();
			var name = $.trim($("#edit-marketActivityName").val());
			var startDate = $("#edit-startTime").val();
			var endDate = $("#edit-endTime").val();
			var cost = $.trim($("#edit-cost").val());
			var description = $.trim($("#edit-describe").val());
			//发送请求之前，进行表单验证
			if(owner == ""){
				alert("所有者不能为空");
				return;
			}
			if(name == ""){
				alert("名称不能为空");
				return;
			}
			if(startDate != "" && endDate != ""){
				//比较字符串的大小
				if(startDate>endDate){
					alert("结束日期不能比开始日期小");
					return;
				}
			}else {
				alert("日期不能为空");
				return;
			}
			//成本是非负整数
			var regExp = /^(([1-9]\d*)|0)$/;
			if(!regExp.test(cost)){
				alert("成本只能是非负整数");
				return;
			}
			//Ajax给后台发送请求
			$.ajax({
				url:'/crm/workbench/activity/saveEditActivity',
				data:{
					id:id,owner:owner,name:name,startDate:startDate,endDate:endDate,
					cost:cost,description:description
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if(data.code == "1"){
						//成功之后，关闭模态窗口
						$("#editActivityModal").modal("hide");
						//刷新市场活动
						var pageSize = $("#pagination").bs_pagination('getOption','rowsPerPage');
						var pageNo = $("#pagination").bs_pagination('getOption','currentPage');
						queryActivityByConditionForPage(pageNo,pageSize);
					}else {
						//失败之后，提示信息
						alert(data.message);
						//模态窗口不关闭
						$("#editActivityModal").modal("show");
					}
				}
			});
		});

		//批量导出
		$("#exportActivityAllBtn").click(function (){
			window.location.href = "/crm/workbench/activity/exportAllActivities";
		});

		//导入市场活动
		$("#importActivityBtn").click(function (){
			//通过文件名做表单验证
			var fileName = $("#activityFile").val();
			var lastName = fileName.substr(fileName.lastIndexOf("."));
			lastName = lastName.toLowerCase();
			if(lastName != ".xls"){
				//后缀名不正确，拦截
				alert("文件格式只支持.xls")
				return;
			}
			//获取文件
			var activityFile = $("#activityFile")[0].files[0];
			//大小不超过5M
			if(activityFile.size > 5*1024*1024){
				alert("文件大小不能超过5M")
				return;
			}
			//发送请求
			var formData = new FormData;
			formData.append("activityFile",activityFile);
			$.ajax({
				url:"/crm/workbench/activity/importActivity",
				data:formData,
				//是否将参数转化为字符串格式，默认为true
				processData:false,
				//是否将参数统一用urlEncoded编码，默认为true
				contentType:false,
				type:'post',
				dataType:'json',
				success:function (data){
					if(data.code == "1"){
						alert("成功导入" + data.retData + "条记录");
						//关闭模态窗口
						$("#importActivityModal").modal("hide");
						//刷新列表
						var pageSize = $("#pagination").bs_pagination('getOption','rowsPerPage');
						queryActivityByConditionForPage(1,pageSize);
					}else {
						alert(data.message);
						$("#importActivityModal").modal("show");
					}
				}
			});
		});

	});


	//封装成函数

	function queryActivityByConditionForPage(pageNo,pageSize){
		//收集参数
		var name = $("#name_for_query").val();
		var owner = $("#owner_for_query").val();
		var startDate = $("#startDate_for_query").val();
		var endDate = $("#endDate_for_query").val();
		//默认情况的页码
		// var pageNo = 1;
		// var pageSize = 10;
		//发送请求
		$.ajax({
			url:'/crm/workbench/activity/queryActivityByConditionForPage',
			data:{
				name:name,owner:owner,startDate:startDate,
				endDate:endDate,pageNo:pageNo,pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function (data){
				//总条数
				//$("#pageTotalCount_for_query").text(data.totalCount);

				//市场活动列表
				var str = "";
				$.each(data.activityList,function (index,obj){
					str += "<tr class=\"active\">";
					str += "<td><input type=\"checkbox\" value=\"" + obj.id + "\"/></td>";
					str += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/queryActivityForDetail?id="+obj.id+"'\">"+ obj.name +"</a></td>";
					str += "<td>"+ obj.owner +"</td>";
					str += "<td>"+ obj.startDate +"</td>";
					str += "<td>"+ obj.endDate +"</td>";
					str += "</tr>";
				});
				//往tBody中显示数据
				$("#tBody").html(str);
                //每次查询之后取消全选
                $("#select_checkbox").prop("checked",false);
				//分页插件工具函数
				var totalPages = 1;
				if(data.totalCount % pageSize == 0){
					totalPages = data.totalCount/pageSize;
				}else{
					totalPages = parseInt(data.totalCount/pageSize) + 1;
				}
				$("#pagination").bs_pagination({
					currentPage:pageNo,//当前页号,相当于pageNo

					rowsPerPage:pageSize,//每页显示条数,相当于pageSize
					totalRows:data.totalCount,//总条数
					totalPages: totalPages,  //总页数,必填参数.

					visiblePageLinks:5,//最多可以显示的卡片数

					showGoToPage:true,//是否显示"跳转到"部分,默认true--显示
					showRowsPerPage:true,//是否显示"每页显示条数"部分。默认true--显示
					showRowsInfo:true,//是否显示记录的信息，默认true--显示

					//切换页号才会调用本函数（递归条件）
					onChangePage: function(event,pageObj) { // returns page_num and rows_per_page after a link has clicked
						//js代码
						queryActivityByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});
			},
			error:function (r){
				console.log("出错了");
			}
		});
	}


</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control addDate" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control addDate" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveActivity_btn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <c:forEach items="${userList}" var="user">
									  <option value="${user.id}">${user.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control addDate" id="edit-startTime" value="" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control addDate" id="edit-endTime" value="" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditActivity_btn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="name_for_query">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="owner_for_query">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="startDate_for_query" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="endDate_for_query">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="query_button">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivity_btn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-toggle="modal" id="update_activity_btn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="delete_activity_btn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="select_checkbox"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
				<div id="pagination"></div>
			</div>
			
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="pageTotalCount_for_query">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>