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
			callAjaxForEdit('zhuang_fail');
			callAjaxForEdit('zhuang_pass');
			callAjaxForEdit('road_fail');
			callAjaxForEdit('road_pass');
			callAjaxForEdit('delete_driverexam');
			
   		});
   	</script>
   	
		<div id="sub-nav"><div class="page-title">
			<h1>驾驶员考试记录</h1>
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
				
				</div>
				<hr/>
					
					
					<div class="hastable">
						<table>
							<thead>
								<stripes:form beanclass="com.bus.stripes.actionbean.application.EmpDriverExamActionBean">
								<tr>
									<td colspan=8 style="text-align: left !important;">
										<label class="selector">姓名:</label><stripes:text name="selector.name"/>
										<br/><label class="selector">考试日期:</label><stripes:text name="selector.examdate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/>
										<br/><label class="selector">桩考:</label><stripes:radio value="" name="selector.zhuangexam"/>不限<stripes:radio value="1" name="selector.zhuangexam"/>合格<stripes:radio value="2" name="selector.zhuangexam"/>不合格
										<br/><label class="selector">路考:</label><stripes:radio value="" name="selector.roadexam"/>不限<stripes:radio value="1" name="selector.roadexam"/>合格<stripes:radio value="2" name="selector.roadexam"/>不合格
									<td>
								<tr>
								<tr>
										<td colspan=8 style="text-align:left !important">
											<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/> / ${actionBean.pagecount}<stripes:hidden name="totalcount"/>  <stripes:submit name="nextpage" value="下页"/>
											显示数量:<stripes:text name="lotsize"/>${actionBean.totalcount}行记录
											<stripes:submit name="filter" value="提交"/>
										</td>
								</tr>
								</stripes:form>
								<tr>
								<th>报考日期</th>
								<th>姓名</th>
								<th>电话</th>
								<th>次数</th>
								<th>桩考</th>
								<th>路考</th>
								<th>注释</th>
								</tr>
							</thead>
							<tbody>
								<c:set var="color" value="0" scope="page"/>
								<c:forEach items="${actionBean.drivingexams}" var="driver" varStatus="loop">
								<c:choose>
									<c:when test="${color%2 == 0}">
										<tr>
									</c:when>
									<c:otherwise>
										<tr class="alt">
									</c:otherwise>
								</c:choose>
								<td><ss:secure roles="employment_driverexam_edit">
										<a class="delete_driverexam" href="javascript:void">(删)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpDriverExam.action?targetId=${driver.id}&deleteInstance="/>
									</ss:secure>${driver.examdateStr}</td>
								<td>${driver.applicant.name}</td>
								<td>${driver.applicant.mobile}</td>
								<td>${driver.examtimes}</td>
								<td>${driver.zhuangPassStr}<ss:secure roles="employment_driverexam_edit">
										<a class="zhuang_fail" href="javascript:void">(N</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpDriverExam.action?targetId=${driver.id}&update=zhuang&updateRequest=&value=2"/>/
										<a class="zhuang_pass" href="javascript:void">Y)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpDriverExam.action?targetId=${driver.id}&update=zhuang&updateRequest=&value=1"/>
									</ss:secure>
								</td>
								<td>${driver.roadPassStr}<ss:secure roles="employment_driverexam_edit">
										<a class="road_fail" href="javascript:void">(N</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpDriverExam.action?targetId=${driver.id}&update=road&updateRequest=&value=2"/>/
										<a class="road_pass" href="javascript:void">Y)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpDriverExam.action?targetId=${driver.id}&update=road&updateRequest=&value=1"/>
									</ss:secure>
								</td>
								<td class="editbox">${driver.remark}<ss:secure roles="employment_driverexam_edit">
										<input type="hidden" value="${pageContext.request.contextPath}/actionbean/EmpDriverExam.action?targetId=${driver.id}&update=remark&updateRequest="/>
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
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>