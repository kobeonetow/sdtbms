<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<stripes:layout-render name="default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/hr.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script> 
    <link href="${pageContext.request.contextPath}/css/hr.css" rel="stylesheet" media="all" /> 

		<div id="sub-nav"><div class="page-title">
			<h1>人事管理</h1>
			<span><a href="#" title="Layout Options">人事</a> > <a href="#" title="Two column layout">档案管理</a> > 查看</span>
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
				<a id="btn_new_document_link" class="btn ui-state-default ui-corner-all" href="#">
						<span class="ui-icon ui-icon-person"></span>
						新建档案
				</a>
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				<div id="btn_new_document_dialog" title="新建档案">
				<!-- The Form to submit new employee data -->
				<stripes:form id="form_new_employee" beanclass="com.bus.stripes.actionbean.EmployeeActionBean" focus="">
					<stripes:errors/>
					<table>
						<tr>
							<td>姓名:</td><td><stripes:text name="employee.fullname"/></td>
							<td>籍贯:</td><td><stripes:text name="employee.pob"/></td>
								<td rowspan="7">照片:</td><td rowspan="7">&nbsp;<stripes:hidden name="employee.hrimage.id"/></td>
						</tr>
						<tr>
							<td>编号:</td><td><stripes:text name="employee.documentkey"/></td>
							<td>工号:</td><td><stripes:text name="employee.workerid"/></td>
						
						</tr>
						<tr>
							<td>性别:</td><td><stripes:radio name="employee.sex" value="男"/>男 &nbsp; &nbsp; <stripes:radio name="employee.sex" value="女"/>女</td>
							<td>身份证:</td><td><stripes:text name="employee.identitycode"/></td>
						</tr>
						<tr>
							<td>出生年月:</td><td><stripes:text name="employee.dob"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>民族:</td><td><stripes:select name="employee.ethnic"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.ethnic}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>婚姻状况:</td><td><stripes:select name="employee.marriage"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.marriage}" label="label" value="value"/></stripes:select></td>
							<td>政治面貌:</td><td><stripes:select name="employee.politicalstatus"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.politicalStatus}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>联系电话:</td><td><stripes:text name="employee.homenumber"/></td>
							<td>手机号码:</td><td><stripes:text name="employee.mobilenumber"/></td>
						</tr>
						<tr>
							<td>电子邮箱:</td><td><stripes:text name="employee.email"/></td>
							<td>其它联系方式:</td><td><stripes:text name="employee.othercontact"/></td>
						</tr>
						<tr>
							<td>家庭地址:</td><td><stripes:text name="employee.homeaddress"/></td>
							<td>邮编:</td><td><stripes:text name="employee.postcode"/></td>
							<td>工种:</td><td><stripes:select name="employee.workertype"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.workertype}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>入党时间:</td><td><stripes:text name="employee.timeofjoinrpc" formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>参加工作时间:</td><td><stripes:text name="employee.firstworktime"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>职级:</td><td><stripes:select name="employee.joblevel"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.joblevel}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>文化程度:</td><td><stripes:select name="employee.qualification"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.qualification}" label="label" value="value"/></stripes:select></td>
							<td>部门:</td><td><stripes:select name="employee.department.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.department}" label="label" value="value"/></stripes:select></td>
							<td>职位:</td><td><stripes:select name="employee.position.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.position}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>所属镇街:</td><td><stripes:text name="employee.placebelong"/></td>
							<td>户籍类型:</td><td><stripes:select name="employee.domiciletype"><stripes:option value="">请选择...</stripes:option><stripes:options-collection collection="${actionBean.domiciletypes}" label="label" value="value"/></stripes:select></td>
							<td>军人:</td><td><stripes:radio name="employee.army" value="是"/>是<stripes:radio name="employee.army" value="否"/>否</td>
						</tr>
						<tr>
							<td>注释:</td>
							<td colspan=5><stripes:textarea name="employee.remark"></stripes:textarea></td>
						</tr>
					</table>
				</stripes:form>
				</div>
				
				<!-- 合同档案新建 -->
				<div  id="btn_new_contract_dialog" title="新建档案">
					<stripes:form id="form_new_contract" beanclass="com.bus.stripes.actionbean.EmployeeActionBean" focus="">
					<stripes:errors/>
					<table>
						<tr>
							<td colspan=4 style="text-align:center;"><label id="contract_label_name"></label></td>
						</tr>
						<tr>
							<td>合同类型:</td><td><stripes:select name="contract.type"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.contracttype}" label="label" value="value"/></stripes:select></td></td>
							<td>生效日期:</td><td><stripes:text name="contract.activedate"   formatPattern="yyyy-MM-dd" class="datepickerClass"/><input id="contract_target_id" type="hidden" name="targetId" value=""/></td>
						</tr>
						<tr>
							<td>开始日期:</td><td><stripes:text name="contract.startdate"   formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>结束日期:</td><td><stripes:text name="contract.enddate"   formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
						
						</tr>
						<tr>
							<td>附录:</td><td colspan=3><stripes:textarea name="contract.remark"/></td>
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
				<stripes:form id="file_submit_form" beanclass="com.bus.stripes.actionbean.EmployeeActionBean">
					<Label>员工资料平台 :</Label><stripes:file name="employeeexcel" id="file_employee" /><stripes:submit name="employeefileupload" value="在职"/><stripes:submit name="employeeresignfileupload" value="辞职"/>
					<br/>
					<Label>检查员工ID:</Label><stripes:file name="checkIds"  id="file_employee_check" /><stripes:submit name="checkids" value="提交"/>
					<br/>
					<Label>驾驶员文件上传:</Label><stripes:file name="drivers" id="file_drivers"/><stripes:submit name="driverlisences" value="提交"/>
					<br/><Label>调动文件csv:</Label><stripes:file name="coordinatorfile" id="file_coordinator"/><stripes:submit name="coordinatorfile" value="提交"/>
					<script type="text/javascript">
					$("#file_submit_form").submit(function(){
						var employee_csv =$('#file_employee').val().trim();
						var employee_check = $('#file_employee_check').val().trim();
						var drivers  = $('#file_drivers').val().trim();
						var coordinator = $('#file_coordinator').val().trim();
						if(employee_csv != ""){
							var ext = employee_csv.split('.').pop().toLowerCase();
							if($.inArray(ext, ['csv']) == -1) {
						    	alert('员工资料不是合法的csv文件');
						    	return false;
							}
						}else if(employee_check != ""){
							var ext = employee_check.split('.').pop().toLowerCase();
							if($.inArray(ext, ['txt']) == -1) {
						    	alert('员工资料检查不是合法的txt文件');
						    	return false;
							}
						}else if(drivers != ""){
							var ext = drivers.split('.').pop().toLowerCase();
							if($.inArray(ext, ['csv']) == -1) {
						    	alert('驾驶员资料不是合法的csv文件');
						    	return false;
							}
						}else if(coordinator != ""){
							var ext = coordinator.split('.').pop().toLowerCase();
							if($.inArray(ext,['csv'])==-1){ 
								alert('调动文件不是合法的csv文件');
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
						员工档案详细信息
					</div>
					<div class="hastable">
				
				<table>
					<thead>
					<stripes:form beanclass="com.bus.stripes.actionbean.EmployeeActionBean" focus="">
						<tr>
							<td colspan=14 style="text-align:left">
								<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/>/<stripes:label name="${actionBean.totalcount}"/>  <stripes:submit name="nextpage" value="下页"/>
							
							显示数量:<stripes:text name="lotsize"/>
							<stripes:submit name="filter" value="提交"/>
<!-- 							<td colspan=10> -->
							</td>
						</tr>
						<tr>
							<td colspan=14 style="text-align:left">
								名称:<stripes:text name="employeeselector.name"/>
								籍贯:<stripes:text name="employeeselector.pob"/>
								工种:<stripes:select name="employeeselector.workertype"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.workertype}" label="label" value="value"/></stripes:select>
								职位:<stripes:select name="employeeselector.position"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.position}" label="label" value="value"/></stripes:select>
								部门:<stripes:select name="employeeselector.department"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.department}" label="label" value="value"/></stripes:select>
								学历:<stripes:select name="employeeselector.qualification"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.qualification}" label="label" value="value"/></stripes:select>
<%-- 								年龄:<stripes:text name="employeeselector.age"/> --%>
							</td>
						</tr>
					</stripes:form>
						<tr>
						<th>操作</th>
						<th>编号</th>
						<th>姓名</th>
						<th>工号</th>
						<th>入职日期</th>
						<th>身份证号码</th>
<!-- 						<th>民族</th> -->
<!-- 						<th>婚姻</th> -->
						<th>出生年月</th>
						<th>性别</th>
<!-- 						<th>年龄</th> -->
<!-- 						<th>在职工龄</th> -->
						<th>部门</th>
						<th>职位</th>
<!-- 						<th>所属镇街</th> -->
						<th>籍贯</th>
<!-- 						<th>地址</th> -->
<!-- 						<th>联系电话</th> -->
						<th>手机</th>
<!-- 						<th>户籍类型</th> -->
<!-- 						<th>学历</th> -->
<!-- 						<th>毕业校园</th> -->
<!-- 						<th>专业</th> -->
<!-- 						<th>政治面貌</th> -->
<!-- 						<th>入党时间</th> -->
						<th>职称</th>
<!-- 						<th>职级</th> -->
						<th>创建日期</th>
						</tr>
					</thead>
					<tbody>
					
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.employees}" var="emp" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<stripes:form beanclass="com.bus.stripes.actionbean.EmployeeActionBean" class="form_function_list">
									<div>
										<input type="hidden" name="targetId" value="${emp.id}"/>
										<stripes:submit name="delete" value="删除"/>
										<stripes:submit name="detail" value="详细"/>
										<stripes:submit name="contract" value="合同 "/>
										<stripes:submit name="idcards" value="证"/>
									</div>
								</stripes:form>
							</td>
							<td>
								${emp.documentkey}
							</td>
							<td>${emp.fullname}</td>
							<td>${emp.workerid}</td>
							<td>${emp.firstworktime}</td>
							<td>${emp.identitycode}</td>
							<td>${emp.dob}</td>
							<td>${emp.sex}</td>
							<td>${emp.department.name}</td>
							<td>${emp.position.name}</td>
							<td>${emp.pob}</td>
<%-- 							<td>${emp.homenumber}</td> --%>
							<td>${emp.mobilenumber}</td>
							<td>${emp.workertype}</td>
							<td>${emp.createtime}</td>
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