<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/hr.js"></script>

		<div id="sub-nav"><div class="page-title">
			<h1>人事管理</h1>
			<span><a href="#" title="Layout Options">人事</a> > <a href="#" title="Two column layout">调动管理</a> > 查看</span>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/hr/hrsidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				
				<!-- 新建档案  Dialog-->
				<div id="hr_top_button_bar" style="height:40px;">&nbsp;  
				<a id="btn_new_coordinate_link" class="btn ui-state-default ui-corner-all" href="#">
						<span class="ui-icon ui-icon-person"></span>
						新调动
				</a>
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				<div id="btn_new_coordinate_dialog" title="新调动">
<!-- 				The Form to submit new coordinate data -->
				<stripes:form id="form_new_coordinate" beanclass="com.bus.stripes.actionbean.HRCoordinatorActionBean" focus="">
					<stripes:errors/>
					<table>
						<tr>
							<td>类型:</td>
							<td colspan=3>
								<stripes:select name="coordinate.type"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.types}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>姓名:</td><td><stripes:text name="coordinate.employee.fullname" class="required employeenamefromid"/></td>
							<td>工号:</td><td><stripes:text name="coordinate.employee.workerid" class="required"/><a href="javascript:void;" id="getNameById">获取</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?getnamebyid="/></td>
						</tr>
						<tr>
							<td>原部门:</td><td><stripes:select name="coordinate.predepartment.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.departments}" label="label" value="value"/></stripes:select></td>
							<td>原岗位:</td><td><stripes:select name="coordinate.preposition.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.positions}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>新部门:</td><td><stripes:select name="coordinate.curdepartment.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.departments}" label="label" value="value"/></stripes:select></td>
							<td>新岗位:</td><td><stripes:select name="coordinate.curposition.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.positions}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>工资停发日:</td><td><stripes:text name="coordinate.movedate"  formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
							<td>新工资发放日:</td><td><stripes:text name="coordinate.activedate"  formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
						</tr>
						<tr>
							<td>注释:</td><td colspan=3><stripes:textarea name="coordinate.remark"></stripes:textarea></td>
						</tr>
					</table>
				</stripes:form>
				</div>
				
				</div>
				<hr/>
				
				<!--  File Upload -->
				<div class="clear"></div>
				<div>
				文件上传
				<stripes:form id="file_submit_form" beanclass="com.bus.stripes.actionbean.HRCoordinatorActionBean">
					<Label>调动csv :</Label><stripes:file name="coordinatebean" id="file_coordinate" /><stripes:submit name="coordinate" value="提交"/>
					<br/>
					<script type="text/javascript">
					$("#file_submit_form").submit(function(){
						var coor_csv =$('#file_coordinate').val().trim();
						if(coor_csv == ""){
							alert("请选择文件");
							return false;
						}
						if(coor_csv != ""){
							var ext = coor_csv.split('.').pop().toLowerCase();
							if($.inArray(ext, ['csv']) == -1) {
						    	alert('驾驶员资料不是合法的csv文件');
						    	return false;
							}
						}
						return true;
					});
					</script>
				</stripes:form>
				<br/>
				<br/>
				<hr/>
				</div>
				
				<!--  Display Employee   -->
				<div>
					<div class="inner-page-title">
						员工调动详细信息
					</div>
					<div class="hastable">
				
				<table>
					<thead>
					<stripes:form beanclass="com.bus.stripes.actionbean.HRCoordinatorActionBean" focus="">
						<tr>
							<td colspan=13 style="text-align:left">
								<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/>/<stripes:label name="${actionBean.totalcount}"/>  <stripes:submit name="nextpage" value="下页"/>
							
							显示数量:<stripes:text name="lotsize"/>
							<stripes:submit name="filter" value="提交"/>
							</td>
						</tr>
						<tr>
							<td colspan=13 style="text-align:left">
								名称:<stripes:text name="selector.name"/>
								工号:<stripes:text name="selector.workerid"/>
								类型:<stripes:select name="selector.type"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.types}" label="label" value="value"/></stripes:select>
								开始日:<stripes:text name="selector.startdate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/>
								结束日:<stripes:text name="selector.enddate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/>
							</td>
						</tr>
						<tr>
							<td colspan=13 style="text-align:left">
								&nbsp;
							</td>
						</tr>
					</stripes:form>
						<tr>
						<th></th>
						<th>Id</th>
						<th>姓名</th>
						<th>工号</th>
						<th>类型</th>
						<th>原部门</th>
						<th>原岗位</th>
						<th>新部门</th>
						<th>新岗位</th>
						<th>工资停发日期</th>
						<th>新工资发放计算日期</th>
						<th>注释</th>
						<th>创建日期</th>
						</tr>
					</thead>
					<tbody>
					
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.coordinates}" var="coor" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<stripes:form beanclass="com.bus.stripes.actionbean.HRCoordinatorActionBean">
									<div>
										<input type="hidden" name="targetId" value="${coor.id}"/>
										<stripes:submit name="delete" value="删除"/>
										<stripes:submit name="edit" value="修改"/>
									</div>
								</stripes:form>
							</td>
							<td>
								${coor.id}
							</td>
							<td>${coor.employee.fullname}</td>
							<td>${coor.employee.workerid}</td>
							<td>${coor.type}</td>
							<td>${coor.predepartment.name}</td>
							<td>${coor.preposition.name}</td>
							<td>${coor.curdepartment.name}</td>
							<td>${coor.curposition.name}</td>
							<td>${coor.movedatestr}</td>
							<td>${coor.activedatestr}</td>
							<td>${coor.remark}</td>
							<td>${coor.createdatestr}</td>
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
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