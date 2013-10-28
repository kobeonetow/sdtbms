<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	 <stripes:layout-component name="contents">
	 <style type="text/css">
		table.data thead tr th{
			border: 1px solid rgb(0, 0, 0);
			border-collapse:collapse;
			background-color:#616161;
			color:black;
			padding:5px;
			font-size:15pt;
			text-align:center;
		}
		table.data tbody tr td{
			border: 1px solid rgb(0, 0, 0);
			border-collapse:collapse;
			padding:5px;
			font-size:12pt;
			height:25px;
			vertical-align:middle;
			text-align:center;
		}
		table.data tr.alt{
			background-color:rgb(200,200,200);
		}
		table.data tbody tr:hover{
			background-color:yellow;
		}
		
	</style>
	<script type="text/javascript">
		$(document).ready(function(){
			$('#selectAll').click(function(){
				if(this.checked){
					$('#dataForm :checkbox').each(function(){
						this.checked = true;
					});
				}else{
					$('#dataForm :checkbox').each(function(){
						this.checked = false;
					});
				}
			});
			
			$('#dataForm').submit(function(){return false;});
			var formAction = $('#dataForm').attr('action');
			$('#delete').click(function(){
				if(!isSelection())
					return false;
				var serialize = $('#dataForm').serialize();
				$.ajax({
					url:formAction+"?deleteAction=",
					data:serialize,
					success:function(response){
						var json = $.parseJSON(response);
						alert(json.msg);
						if(json.result=="1"){
							location.reload();
						}
					}
				});
			});
			
			$('#getback').click(function(){
				if(!isSelection())
					return false;
				var serialize = $('#dataForm').serialize();
				$.ajax({
					url:formAction+"?getbackAction=",
					data:serialize,
					success:function(response){
						var json = $.parseJSON(response);
						alert(json.msg);
						if(json.result=="1"){
							location.reload();
						}
					}
				});
			});
			$('#resubmit').click(function(){
				if(!isSelection())
					return false;
				var serialize = $('#dataForm').serialize();
				$.ajax({
					url:formAction+"?resubmitAction=",
					data:serialize,
					success:function(response){
						var json = $.parseJSON(response);
						alert(json.msg);
						if(json.result=="1"){
							location.reload();
						}
					}
				});
			});
			
			$("#searchDate").click(function(){
				var startdate = $('#startDate').val();
				var enddate  = $('#endDate').val();
				var link = $(this).next().val() + "&startDate="+startdate+"&endDate="+enddate;
				window.location = link;
			});
		});
		function isSelection(){
			var count = $('#dataForm input:checkbox:checked').length;
			if(count <= 0){
				alert("未选上任何项目");
				return false;
			}
			return true;
		}

	</script>
	  <div id="sub-nav"><div class="page-title">
			<h1>审核条例</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
					<ss:secure roles="score_approve_submit_items">
						<div style="height:40px;font-size:17pt;padding:5px;">						
							<c:if test="${actionBean.mode==null }">
								<a style="color:black;font-weight:bold;" href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=">等待审核(${actionBean.countWaitting }):</a>&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=2">拒绝审核/需重新提交(${actionBean.countReject }):</a>&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=3">未提交审核的(${actionBean.countCreated }):</a>&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=-1">已提交并审核的记录</a>
							</c:if>
							<c:if test="${actionBean.mode==2 }">
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=">等待审核(${actionBean.countWaitting }):</a>&nbsp;|&nbsp;
								<a style="color:black;font-weight:bold;" href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=2">拒绝审核/需重新提交(${actionBean.countReject }):</a>&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=3">未提交审核的(${actionBean.countCreated }):</a>&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=-1">已提交并审核的记录</a>
							</c:if>
							<c:if test="${actionBean.mode==3 }">
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=">等待审核(${actionBean.countWaitting }):</a>&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=2">拒绝审核/需重新提交(${actionBean.countReject }):</a>&nbsp;|&nbsp;
								<a style="color:black;font-weight:bold;" href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=3">未提交审核的(${actionBean.countCreated }):</a>&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=-1">已提交并审核的记录</a>
							</c:if>
							<c:if test="${actionBean.mode<0 }">
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=">等待审核(${actionBean.countWaitting }):</a>&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=2">拒绝审核/需重新提交(${actionBean.countReject }):</a>&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=3">未提交审核的(${actionBean.countCreated }):</a>&nbsp;|&nbsp;
								<a style="color:black;font-weight:bold;" href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=-1">已提交并审核的记录</a>
							</c:if>
						</div>
						<c:if test="${actionBean.mode<0 }">
							<div>
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=-1">今天</a>
								&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=-1&selectPeriod=w">今周</a>
								&nbsp;|&nbsp;
								<a href="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=-1&selectPeriod=m">今月</a>
								&nbsp;|&nbsp;
								<br/>
								起始日期:<input type="text" id='startDate' name="startDate" class="datepickerClass"/>&nbsp;&nbsp;
								结束日期:<input type="text" id='endDate' name="endDate" class="datepickerClass"/>&nbsp;&nbsp;
								<button id='searchDate'>按时间段搜索</button><input type="hidden" value="${pageContext.request.contextPath}/actionbean/ScoreApprove.action?preapprove=&mode=-1&selectPeriod=a"/>
							</div>
							<div style="padding:5px;color:red;font-weight:bold;">
								<c:if test="${actionBean.selectPeriod==null }">今天</c:if>
								<c:if test='${actionBean.selectPeriod== "w"}'>今周</c:if>
								<c:if test='${actionBean.selectPeriod== "m"}'>今月</c:if>
								<c:if test='${actionBean.selectPeriod== "a"}'>时间段</c:if>
							</div>
						</c:if>
						<div>
							<stripes:form id="dataForm" beanclass="com.bus.stripes.actionbean.score.ScoreApproveActionBean">
							<table class="data">
								<thead>
									<tr>
										<th>全选<input type="checkbox" id="selectAll"/></th>
										<th>Id</th>
										<th>打分人</th>
										<th>受分员工</th>
										<th>职位</th>
										<th>日期</th>
										<th>分值</th>
										<th>条例编号</th>
										<th>条例详细</th>
									</tr>
								</thead>
								<tbody>
									<c:set var="count" value="0"  scope="page"/>
									<c:forEach items="${actionBean.displayList}" var="list" varStatus="loop">
										<c:if test="${count%2==0}">
											<tr class="alt">
										</c:if>
										<c:if test="${count%2!=0}">
											<tr>
										</c:if>
											<td><stripes:checkbox name="selected" value="${list.id }"/></td>
											<td>${list.id }</td>
											<td>${list.sender.employee.fullname }</td>
											<td>${list.receiver.employee.fullname }</td>
											<td>${list.receiver.employee.position.name }</td>
											<td>${list.scoredatestr }</td>
											<td>${list.score }</td>
											<td>${list.scoretype.reference }</td>
											<td>${list.scoretype.description}</td>
										</tr>
										<c:set var="count" value="${count + 1}" scope="page"/>
									</c:forEach>
								</tbody>
							</table>
							</stripes:form>
							<div>
								<c:if test="${actionBean.mode==null  || actionBean.mode >=0}">
									<button id="delete">删除</button>
								</c:if>
								<c:if test="${actionBean.mode==null}">
									<button id="getback">撤回</button>
								</c:if>
								<c:if test="${actionBean.mode!=null && actionBean.mode != -1}">
									<button id="resubmit">提交</button>
								</c:if>
							</div>
						</div>
					</ss:secure>
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>