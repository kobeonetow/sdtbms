<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/hr.js"></script>

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
				
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				<ss:secure roles="employee_data_download">
				<a id="btn_download_employees" class="btn ui-state-default ui-corner-all" href="javascript:void;">
					导出员工花名册<input type="hidden" value="${pageContext.request.contextPath}/actionbean/Filedownload.action"/>
				</a>
				</ss:secure>
				
				<ss:secure roles="employee_driver_data_download">
				<a id="btn_download_drivers" class="btn ui-state-default ui-corner-all" href="javascript:void;">
					导出驾驶员资料<input type="hidden" value="${pageContext.request.contextPath}/actionbean/Filedownload.action"/>
				</a>
				</ss:secure>
				
				<ss:secure roles="employee_coor_data_download">
				<a id="btn_download_month_report" class="btn ui-state-default ui-corner-all" href="javascript:void;">
					导出月报表<input type="hidden" value="${pageContext.request.contextPath}/actionbean/Filedownload.action"/>
				</a>
				<div id="btn_download_report_dialog" title="导出报表">
					<stripes:form id="form_get_coordination" beanclass="com.bus.stripes.actionbean.FiledownloadActionBean">
						<table>
							<tr>
								<td>类型:</td>
								<td><stripes:select name="coortype">
									<stripes:option value="resign">离职</stripes:option>
									<stripes:option value="offer">录入</stripes:option>
									<stripes:option value="coor">调动</stripes:option>							
								</stripes:select></td>
							</tr>
							<tr>
								<td>起始日期:</td>
								<td><stripes:text name="startdate"  formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
								<td>截止日期:</td>
								<td><stripes:text name="enddate"  formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
								<stripes:submit name="monthreport" value="提交"/>
							<tr>
						</table>
					</stripes:form>
				</div>
				</ss:secure>
				<ss:secure roles="employee_edit">
				<a id="btn_new_document_link" class="btn ui-state-default ui-corner-all" href="#">
						<span class="ui-icon ui-icon-person"></span>
						新建档案
				</a>
				<div id="btn_new_document_dialog" title="新建档案">
				<!-- The Form to submit new employee data -->
				<stripes:form id="form_new_employee" beanclass="com.bus.stripes.actionbean.EmployeeActionBean" focus="">
					<stripes:errors/>
					<table>
						<tr>
							<td>姓名:</td><td><stripes:text class="required" name="employee.fullname"/></td>
							<td>籍贯:</td><td><stripes:text class="required" name="employee.pob"/></td>
								<td rowspan="8">照片:</td><td rowspan="7">&nbsp;<stripes:hidden name="employee.hrimage.id"/></td>
						</tr>
						<tr>
							<td>编号<a href="javascript:void;" id="getNewWorkerId">(工)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?getnewworkerid="/>:</td><td><stripes:text class="required" name="employee.documentkey"/></td>
							<td>工号:</td><td><stripes:text class="required" name="employee.workerid"/><a href="javascript:void;" id="checkWorkerId">(查)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?checkworkerid="/></td>
						
						</tr>
						<tr>
							<td>性别:</td><td><stripes:radio name="employee.sex" value="男"/>男 &nbsp; &nbsp; <stripes:radio name="employee.sex" value="女"/>女</td>
							<td>身份证:</td><td><stripes:text class="required" name="employee.identitycode"/></td>
						</tr>
						<tr>
							<td>出生年月:</td><td><stripes:text  name="employee.dob"  formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
							<td>民族:</td><td><stripes:select class="required" name="employee.ethnic"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.ethnic}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>婚姻状况:</td><td><stripes:select class="required" name="employee.marriage"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.marriage}" label="label" value="value"/></stripes:select></td>
							<td>政治面貌:</td><td><stripes:select name="employee.politicalstatus"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.politicalStatus}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>联系电话:</td><td><stripes:text name="employee.homenumber"/></td>
							<td>手机号码:</td><td><stripes:text class="required" name="employee.mobilenumber"/></td>
						</tr>
						<tr>
							<td>电子邮箱:</td><td><stripes:text name="employee.email"/></td>
							<td>其它联系方式:</td><td><stripes:text name="employee.othercontact"/></td>
						</tr>
						<tr>
							<td>家庭地址:</td><td><stripes:text class="required" name="employee.homeaddress"/></td>
							<td>邮编:</td><td><stripes:text name="employee.postcode"/></td>
						</tr>
						<tr>
							<td>入党时间:</td><td><stripes:text name="employee.timeofjoinrpc" formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>参加工作时间:</td><td><stripes:text name="employee.firstworktime"  formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
							<td>职级:</td><td><stripes:select class="required" name="employee.joblevel"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.joblevel}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>文化程度:</td><td><stripes:select class="required" name="employee.qualification"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.qualification}" label="label" value="value"/></stripes:select></td>
							<td>部门:</td><td><stripes:select class="required" name="employee.department.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.department}" label="label" value="value"/></stripes:select></td>
							<td>职位:</td><td><stripes:select class="required" name="employee.position.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.position}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>所属镇街:</td><td><stripes:select name="employee.placebelong"><stripes:option value=""></stripes:option><stripes:options-collection collection="${actionBean.placebelongs}" label="label" value="label"/></stripes:select></td>
							<td>户籍类型:</td><td><stripes:select class="required" name="employee.domiciletype"><stripes:option value="">请选择...</stripes:option><stripes:options-collection collection="${actionBean.domiciletypes}" label="label" value="value"/></stripes:select></td>
							<td>特殊身份:</td><td><stripes:select name="employee.army"><stripes:options-collection collection="${actionBean.specialPeople}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>最后毕业学校:</td><td><stripes:text name="employee.colleage"></stripes:text></td>
							<td>专业:</td><td colspan=3><stripes:text name="employee.major"/></td>
						</tr>
						<tr>
							<td>调入时间:</td>
							<td><stripes:text name="employee.transfertime" formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>职称:</td>
							<td colspan=3><stripes:select class="reqired" name="employee.workertype"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.workertype}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>注释:</td>
							<td colspan=5><stripes:textarea name="employee.remark"></stripes:textarea></td>
						</tr>
					</table>
				</stripes:form>
				</div>
				</ss:secure>
				
				<!-- 合同档案新建 -->
				<ss:secure roles="employee_add_contract">
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
				</ss:secure>
				
				</div>
				<hr/>
				
				<!--  File Upload -->
				<div class="clear"></div>
				<div>
				文件上传
				<stripes:form id="file_submit_form" beanclass="com.bus.stripes.actionbean.EmployeeActionBean">
				 <ss:secure roles="employee_datafile_upload">
					<Label>员工资料平台 :</Label><stripes:file name="employeeexcel" id="file_employee" /><stripes:submit name="employeefileupload" value="在职"/><stripes:submit name="employeeresignfileupload" value="辞职"/>
					<br/>
					</ss:secure>
					<ss:secure roles="employee_idcheck_file_upload">
					<Label>检查员工ID:</Label><stripes:file name="checkIds"  id="file_employee_check" /><stripes:submit name="checkids" value="提交"/>
					<br/>
					</ss:secure>
					<ss:secure roles="employee_datafile_upload">
					<Label>驾驶员文件上传:</Label><stripes:file name="drivers" id="file_drivers"/><stripes:submit name="driverlisences" value="提交"/>
					<br/>
					</ss:secure>
					
					<ss:secure roles="employee_coordination_file_upload">
					<Label>调动文件csv:</Label><stripes:file name="coordinatorfile" id="file_coordinator"/><stripes:submit name="coordinatorfileupload" value="提交"/>
					</ss:secure>					
<%-- 					<stripes:submit name="resignemployeecoordination" value="离职"/> --%>
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
					<stripes:form id="employee_selection_form" beanclass="com.bus.stripes.actionbean.EmployeeActionBean" focus="">
						<tr>
							<td colspan=13 style="text-align:left">
								<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/>/<stripes:label name="${actionBean.totalcount}"/>  <stripes:submit name="nextpage" value="下页"/>
							
							显示数量:<stripes:text name="lotsize"/>
							<label>总数:${actionBean.recordsTotal} 行</label>
							</td>
						</tr>
						<tr>
							<td colspan=11 style="text-align:left">
								<Label class='selector'>名称:</Label><stripes:text name="employeeselector.name"/>
								<Label class='selector'>工号:</Label><stripes:text name="employeeselector.workerid"/>
								<Label class='selector'>入职期从:</Label><stripes:text name="employeeselector.workstartdate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/>
								到<stripes:text name="employeeselector.workenddate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
							</td>
							<td><Label class='selector'>部门:</Label></td>
							<td><Label class='selector'>职位:</Label></td>
						</tr>
						<tr>
							<td colspan=11 style="text-align:left">
								<Label class='selector'>性别:</Label><stripes:radio value="" name="employeeselector.sex"/>不限<stripes:radio value="男" name="employeeselector.sex"/>男<stripes:radio value="女" name="employeeselector.sex"/>女
								<br/>
								<Label class='selector'>职称:</Label><stripes:select name="employeeselector.workertype"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.workertype}" label="label" value="value"/></stripes:select>
								<br/>
								<Label class='selector'>籍贯:</Label><stripes:text name="employeeselector.pob"/>
								<Label class='selector'>驾驶员:</Label><stripes:text name="employeeselector.driver"/>
								<Label class='selector'>所属镇街:</Label><stripes:select name="employeeselector.placebelong"><stripes:option value=""></stripes:option><stripes:options-collection collection="${actionBean.placebelongs}" label="label" value="label"/></stripes:select>
<%-- 								年龄:<stripes:text name="employeeselector.age"/> --%>
							</td>
							<td rowspan=3><stripes:select name="employeeselector.ds"  multiple="multiple" style="height:200px;"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.department}" label="label" value="value"/></stripes:select></td>
							<td rowspan=3><stripes:select name="employeeselector.ps" multiple="multiple" style="height:200px;"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.position}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td colspan=11 style="text-align: left">
								<Label class='selector'>出生月份:</Label>
									<stripes:select name="employeeselector.birthmonth">
										<stripes:option value="">不限</stripes:option>
										<stripes:option value="1">1</stripes:option>
										<stripes:option value="2">2</stripes:option>
										<stripes:option value="3">3</stripes:option>
										<stripes:option value="4">4</stripes:option>
										<stripes:option value="5">5</stripes:option>
										<stripes:option value="6">6</stripes:option>
										<stripes:option value="7">7</stripes:option>
										<stripes:option value="8">8</stripes:option>
										<stripes:option value="9">9</stripes:option>
										<stripes:option value="10">10</stripes:option>
										<stripes:option value="11">11</stripes:option>
										<stripes:option value="12">12</stripes:option>
									</stripes:select>
								<Label class='selector'>职级:</Label><stripes:select name="employeeselector.joblevel"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.joblevel}" label="label" value="value"/></stripes:select>
								<Label class='selector'>学历:</Label><stripes:select name="employeeselector.qualification"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.qualification}" label="label" value="value"/></stripes:select>
								<br/>
								<Label class='selector'>民族:</Label><stripes:select name="employeeselector.ethnic"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.ethnic}" label="label" value="value"/></stripes:select>
								<Label class='selector'>政治面貌:</Label><stripes:select name="employeeselector.politicalstatus"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.politicalStatus}" label="label" value="value"/></stripes:select>
								<Label class='selector'>婚姻:</Label><stripes:select name="employeeselector.marriage"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.marriage}" label="label" value="value"/></stripes:select>
								<Label class='selector'>户籍:</Label><stripes:select name="employeeselector.domiciletype"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.domiciletypes}" label="label" value="value"/></stripes:select>
							</td>
						</tr>
						<tr>
							<td colspan=11 style="text-align:left">
								<Label class='selector'>特殊身份(例:军人):</Label><stripes:text name="employeeselector.army"/>
								<br/>
								<Label class='selector'>日期:</Label><stripes:radio name="employeeselector.date" value="0"/>不限 <stripes:radio name="employeeselector.date" value="1"/>入职时间<stripes:radio name="employeeselector.date" value="2"/>按年龄排
								<br/>
								<Label class='selector'>员工状态:</Label><stripes:radio name="employeeselector.status" value="A"/>在职<stripes:radio name="employeeselector.status" value="E"/>辞职<stripes:radio name="employeeselector.status" value="A,E"/>全部
								<br/>
								<stripes:submit name="filter" value="提交"/>
								<ss:secure roles="employee_data_download">
								<stripes:submit name="downloadcurrentform" value="下载当前选项表单"/>
								</ss:secure>
							</td>
						</tr>
					</stripes:form>
						<tr>
						<th>操作</th>
						<th>编号</th>
						<th>姓名</th>
						<th>工号</th>
						<th>工龄</th>
<!-- 						<th>民族</th> -->
<!-- 						<th>婚姻</th> -->
						<th>年龄</th>
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
										<ss:secure roles="employee_rm">
										<stripes:submit name="delete" value="删除"/>
										</ss:secure>
										
										<ss:secure roles="employee_view_detail">
										<stripes:submit name="detail" value="详细"/>
										</ss:secure>
										
										<ss:secure roles="employee_add_contract">
										<stripes:submit name="contract" value="合同 "/>
										</ss:secure>
										
										<ss:secure roles="employee_idcards_view">
										<stripes:submit name="idcards" value="证"/>
										</ss:secure>
										
										<ss:secure roles="employee_resign">
											<c:if test="${emp.status == 'A'}">
												<stripes:submit class="resign" name="getresign" value="辞"/>
											</c:if>
										</ss:secure>
										
										<ss:secure roles="employee_edit">
											<c:if test="${emp.status == 'E'}">
												<stripes:submit class="rejoin" name="getrejoin" value="复"/>
											</c:if>
										</ss:secure>
									</div>
								</stripes:form>
							</td>
							<td>
								${emp.documentkey}
							</td>
							<td>${emp.fullname}</td>
							<td>${emp.workerid}</td>
							<td>${emp.workage}</td>
							<td>${emp.age}</td>
							<td>${emp.dobstr}</td>
							<td>${emp.sex}</td>
							<td>${emp.department.name}</td>
							<td>${emp.position.name}</td>
							<td>${emp.pob}</td>
<%-- 							<td>${emp.homenumber}</td> --%>
							<td>${emp.workertype}</td>
							<td>${emp.createtimestr}</td>
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
					</tbody>
				</table>
				</div>
				</div>
				
				<ss:secure roles="employee_resign">
				<div id="btn_resign_date_dialog" title="辞职日期">
					<stripes:form beanclass="com.bus.stripes.actionbean.EmployeeActionBean">
						辞职日期:<stripes:text name="resigndate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/>
						<input id="resignemployeeId" type="hidden" name="targetId" value=""/>
						<br/>
						<textarea name="remark"></textarea>
						<stripes:submit name="resign" value="辞"/>
					</stripes:form>
				</div>
				</ss:secure>
				<ss:secure roles="employee_edit">
				<div id="btn_rejoin_date_dialog" title="复职日期">
					<stripes:form beanclass="com.bus.stripes.actionbean.EmployeeActionBean">
						复职日期:<stripes:text name="resigndate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/>
						<input id="rejoinemployeeId" type="hidden" name="targetId" value=""/>
						<br/>
						<stripes:submit name="rejoin" value="复职"/>
					</stripes:form>
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