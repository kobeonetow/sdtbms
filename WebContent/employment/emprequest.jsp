<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>

<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
   	<script type="text/javascript" src="${pageContext.request.contextPath}/js/employment.js"></script>
   	<script type="text/javascript">
		$(document).ready(function(){
			callAjaxForEdit("approve_fail");
			callAjaxForEdit("approve_success");
			callAjaxForEdit("approve_consider");
			callAjaxForEdit("delete_request");
			
			buildBasicDatePickDialog('date_select_dialog',"200","150");
			callAjaxForEditWithDatePicker("approve_date");
			callAjaxForEditWithDatePicker("send_approve_date");
		});
   	</script>
		<div id="sub-nav"><div class="page-title">
			<h1>增补人员管理</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/employment/empsidebar.jsp" />
			
		<!-- Side bar ****************************************************************** -->
		
				<!-- Start of main content -->
				<div id="rightcontent">
				
				<div id="items_top_button_bar" style="height:40px;">&nbsp;  
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				
				<ss:secure roles="employment_request_create">
				<a id="btn_new_emp_req_link" class="btn ui-state-default ui-corner-all" href="#">
						<span class="ui-icon ui-icon-person"></span>
						新建增补
				</a>
				
				<div id="btn_new_emp_req_dialog" title="新建增补">
					<stripes:form id="form_new_emp_req" beanclass="com.bus.stripes.actionbean.application.EmpRequestActionBean">
						<div class='hastable'>
						<table>
								<tr>
									<td>部门:</td><td><stripes:select name="empRequest.department.id"  class="required"><stripes:options-collection collection="${actionBean.departments}" label="name" value="id"/></stripes:select></td>
								</tr>
								<tr>
									<td>职位:</td><td><stripes:select name="empRequest.position.id"  class="required"><stripes:options-collection collection="${actionBean.positions}" label="name" value="id"/></stripes:select></td>
								</tr>
								<tr>
									<td>额外信息:</td><td><stripes:text name="empRequest.remark"/></td>
								</tr>
								<tr>	
									<td>需要人数:</td><td><stripes:text name="empRequest.requireNumber"  class="required" formatType="number" formatPattern="integer"/></td>
								</tr>
								<tr>	
									<td>已招人数:</td><td><stripes:text name="empRequest.commitNumber"  formatType="number" formatPattern="integer"/></td>
								</tr>
								<tr>	
									<td>收表日期:</td><td><stripes:text name="empRequest.receiveFormDate" formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
								</tr>
								<tr>	
									<td>送审日期:</td><td><stripes:text name="empRequest.sendApproveDate"   formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
								</tr>
						</table>
						</div>
					</stripes:form>
				</div>
				</ss:secure>
				</div>
				<hr/>
				
				
				<div class="hastable">
						<table>
							<thead>
								<stripes:form beanclass="com.bus.stripes.actionbean.application.EmpRequestActionBean">
								<tr>
									<td colspan=10 style="text-align: left !important;">
										<label class="selector">部门:</label><stripes:select name="selector.department"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.departments}" label="name" value="id"/></stripes:select><br/>
										<label class="selector">职位:</label><stripes:select name="selector.position"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.positions}" label="name" value="id"/></stripes:select><br/>
										<br/><label class="selector">额外信息:</label><stripes:text name="selector.extrainfo"/>
										<br/><label class="selector">缺人的增补表:</label><stripes:radio name="selector.numberMatch" value=""/>否<stripes:radio name="selector.numberMatch" value="1"/>是
										<br/><label class="selector">收表日期:</label><stripes:text name="selector.receiveDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
										<br/><label class="selector">送审结果:</label><stripes:radio name="selector.approveResult" value=""/>无<stripes:radio name="selector.approveResult" value="1"/>已批<stripes:radio name="selector.approveResult" value="2"/>考虑<stripes:radio name="selector.approveResult" value="3"/>不批
									</td>
								</tr>
								<tr>
										<td colspan=10 style="text-align:left !important">
											<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/> / ${actionBean.pagecount}<stripes:hidden name="totalcount"/>  <stripes:submit name="nextpage" value="下页"/>
											显示数量:<stripes:text name="lotsize"/>${actionBean.totalcount}行记录
											<stripes:submit name="filter" value="提交"/>
										</td>
								</tr>
								</stripes:form>
								<tr>
								<th></th>
								<th>部门</th>
								<th>职位</th>
								<th>额外信息</th>
								<th>需要人数</th>
								<th>已聘人数</th>
								<th>收表日期</th>
								<th>送审日期</th>
								<th>审批日期</th>
								<th>送审结果</th>
								</tr>
							</thead>
							<tbody>
								<c:set var="color" value="0" scope="page"/>
								<c:forEach items="${actionBean.emprequests}" var="req" varStatus="loop">
								<c:choose>
									<c:when test="${color%2 == 0}">
										<tr>
									</c:when>
									<c:otherwise>
										<tr class="alt">
									</c:otherwise>
								</c:choose>
								<td style="text-align: right;">${req.id}<ss:secure roles="employment_request_edit">
										<a class="delete_request" href="javascript:void;">(删)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpRequest.action?targetId=${req.id}&deleteRequest="/>
									</ss:secure>
								</td>
								<td>${req.department.name}</td>
								<td>${req.position.name}</td>
								<td>${req.remark}</td>
								<td>${req.requireNumber}</td>
								<td class="editbox">${req.commitNumber}
									<ss:secure roles="employment_request_edit">
										<input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpRequest.action?targetId=${req.id}&update=commitNum&updateRequest="/>
									</ss:secure>
									</td>
								<td>${req.receiveFormDateStr}</td>
								<td>${req.sendApproveDateStr}<ss:secure roles="employment_request_edit">
									<a class="send_approve_date" href="javascript:void;">(设)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpRequest.action?targetId=${req.id}&update=sendApproveDate&updateRequest="/>
									</ss:secure>
								</td>
								<td>${req.approveDateStr}<ss:secure roles="employment_request_edit">
									<a class="approve_date" href="javascript:void;">(设)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpRequest.action?targetId=${req.id}&update=approveDate&updateRequest="/>
									</ss:secure>
								</td>
								<td>${req.approveResultStr}<ss:secure roles="employment_request_edit">
									<a class="approve_fail" href="javascript:void;">(N</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpRequest.action?targetId=${req.id}&approveResult=&approveStatus=N"/>/
									<a class="approve_consider" href="javascript:void;">C</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpRequest.action?targetId=${req.id}&approveResult=&approveStatus=C"/>/
									<a class="approve_success" href="javascript:void;">Y)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpRequest.action?targetId=${req.id}&approveResult=&approveStatus=Y"/>
									</ss:secure>
								</td>
							</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
							</tbody>
						</table>
					</div>
				
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
			<div id="date_select_dialog" title="选择日期">
				<label>日期:</label><input type="text" id="date_select_box" class="datepickerClass"/>
				<input type="hidden" id="opener" value=""/>
			</div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>