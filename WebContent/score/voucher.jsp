<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>

<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
   	<script type="text/javascript" src="${pageContext.request.contextPath}/js/score.js"></script>
    <link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />
		<div id="sub-nav"><div class="page-title">
			<h1>奖券管理</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
		<!-- Side bar ****************************************************************** -->
		
				<!-- Start of main content -->
				<div id="rightcontent">
				
					<ss:secure roles="score_voucher_management">
					<div>
					给奖券:
					<hr/>
					<stripes:form beanclass="com.bus.stripes.actionbean.score.VoucherActionBean">
						<div>
							名称:<input type="text" readOnly="readonly" id="employeenamefromid1"/>工号:<stripes:text name="workerid"/>
								<a href="javascript:void;" id="getNameById3">获取</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?getnamebyid="/>
							<br/>
							分:<stripes:text name="totalscore"/>&nbsp;数量:<stripes:text name="quantity"/>
							<br/>
							<stripes:submit name="giveVouchers" value="录入"/>
						</div>
					</stripes:form>
					<hr/>
					</div>
					</ss:secure>
					
					<div>
						<stripes:form beanclass="com.bus.stripes.actionbean.score.VoucherActionBean">
						名称：<stripes:text name="filtername"/>
						工号：<stripes:text name="filterworkerid"/>
						<stripes:submit name="getVouchers" value="查找"/>
						</stripes:form>
						<br/>
						<hr/>
						<div>
							<table class="hastable">
								<thead>
									<tr>
										<th>&nbsp;</th>
										<th>ID</th>
										<th>员工姓名</th>
										<th>员工工号</th>
										<th>获取数量</th>
										<th>使用分</th>
										<th>日期</th>
										<th>已用分数</th>
									</tr>
								</thead>
								<tbody>
									<c:set var="voucherurl" value="${pageContext.request.contextPath}/actionbean/Voucher.action" scope="page"/>
									<c:forEach items="${actionBean.voucherlist}" var="voucher" varStatus="loop">
										<tr>
											<td>
												<ss:secure roles="score_voucher_management">
												<a href="javascript:deleteVoucher('${voucherurl}?deleteVoucher=&targetId=${voucher.id}');">删除</a>
												</ss:secure>
											</td>
											<td>${voucher.id}</td>
											<td>${voucher.scoremember.employee.fullname}</td>
											<td>${voucher.scoremember.employee.workerid}</td>
											<td>${voucher.quantity}</td>
											<td>${voucher.score}</td>
											<td>${voucher.datestr}</td>
											<td>${voucher.scoremember.voucherscore}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>