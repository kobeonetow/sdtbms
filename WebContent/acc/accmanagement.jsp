<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/acc.js"></script>
		<div id="sub-nav"><div class="page-title">
			<h1>账号管理</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/acc/accsidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				
				
				<div>
				
					<ss:secure roles="account_createaccount">
					<div class="hastable">
					<div>新建用户</div>
				<stripes:form beanclass="com.bus.stripes.actionbean.account.AccountActionBean">
				<table>
					<tr>
						<td>工号</td><td><stripes:text name="empworkerid"/><a href="javascript:void;" id="checkworkerid">查</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?checkworkerid="/></td>
					</tr>
					<tr>
						<td>用户名</td><td><stripes:text name="username" class="required"/></td>
					</tr>
					<tr>
						<td>密码</td><td><stripes:password name="password" class="required"/></td>
					</tr>
					<tr>
						<td colspan=2>
							<stripes:submit name="createaccount" value="新建账号"/>
							<stripes:submit name="createAllAccount" value="创建所有未有账号"/>
						</td>
					</tr>
				</table>
				</stripes:form>
				</div>
				 </ss:secure>
				
				</div>
				
				
				
				<hr/>
				<!-- create account group -->
				<div>
				
					<ss:secure roles="account_creategroup">
					<div class="hastable">
					<div>新建用户组</div>
				<stripes:form beanclass="com.bus.stripes.actionbean.account.AccountActionBean">
				<table>
					<tr>
						<td>用户组名</td><td><stripes:text name="groupname" class="required"/></td>
					</tr>
					<tr>
						<td colspan=2>
							<stripes:submit name="creategroup"/>
						</td>
					</tr>
				</table>
				</stripes:form>
				</div>
				</ss:secure>
				</div>
				
				<hr/>
				<!-- assign users to group -->
				<div class="hastable">
					<div>添加用户到用户组</div>
					<stripes:form beanclass="com.bus.stripes.actionbean.account.AccountActionBean">	
					<table>
						<tr>
							<td>用户</td><td>用户组</td><td>添加/删除</td>
						</tr>
						<tr>
							<td>
								<stripes:select name="userids" multiple="multiple" style="height:200px;width:250px;"><stripes:options-collection collection="${actionBean.users}" label="label" value="value"/></stripes:select>
								
								<ss:secure roles="account_viewaccountgroups">
								  <a href="javascript:void;" id="accountgroups">查看编辑已归入组</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/account/Account.action?accountgroups="/>
								</ss:secure>
								
							</td>
							<td>
								<stripes:select name="groupids" multiple="multiple" style="height:200px;width:250px;"><stripes:options-collection collection="${actionBean.groups}" label="label" value="value"/></stripes:select>
								<ss:secure roles="account_viewgroupactions">
								  <a href="javascript:void;" id="groupactions">查看编辑权限</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/account/Account.action?groupactions="/>
								</ss:secure>
							</td>
							<td>
								<ss:secure roles="account_assigngroup">
								<stripes:submit name="assigngroups" value="入组"/>
								</ss:secure>
								<ss:secure roles="account_removeaccount">
								<stripes:submit name="removeusers" value="删除用户"/>
								</ss:secure>
								<br/>
								<ss:secure roles="account_resignaccount">
								<stripes:submit name="resignusers" value="辞去用户"/>
								</ss:secure>
								<ss:secure roles="account_removegroup">
								<stripes:submit name="removegroups" value="删除组"/>
								</ss:secure>
							</td>
						</tr>
					</table>
					</stripes:form>
				</div>
				
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>