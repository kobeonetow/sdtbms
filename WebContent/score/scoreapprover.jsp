<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	 <stripes:layout-component name="contents">
	 <script type="text/javascript">
    			function openSelectEmpWindow(names,workerids,extras,multi){
    				var link="";
        			if(multi == true)
        				link = "${pageContext.request.contextPath}/actionbean/EmployeeSelector.action?eleIdOne="+names+"&eleIdTwo="+workerids+"&extra="+extras+"&multi="+multi;
        			else
        				link = "${pageContext.request.contextPath}/actionbean/EmployeeSelector.action?eleIdOne="+names+"&eleIdTwo="+workerids+"&extra="+extras;
				window.open(link,"_blank","width=400,height=600");
        		}

    			function delApproverSection(sectionid){
    				if(confirm("真的要删除Id:"+sectionid+"?")){
        				var link = "${pageContext.request.contextPath}/actionbean/ScoreApprover.action?targetId="+sectionid+"&delApproverSection=";
						$.ajax({
							url:link,
							success:function(response){
								var JSONobj = $.parseJSON(response);
								if(JSONobj.result == "1"){
									alert("删除成功");
									$('#checkWorkerId1').change();
								}else{
									alert(JSONobj.msg);
								}
							}
						});
            		}
        		}

				function toggleSection(sectionid){
					if(confirm("要更改Id:"+sectionid+"的审核状态?")){
						var link = "${pageContext.request.contextPath}/actionbean/ScoreApprover.action?targetId="+sectionid+"&toggleSection=";
						$.ajax({
							url:link,
							success:function(response){
								var JSONobj = $.parseJSON(response);
								if(JSONobj.result == "1"){
									alert("更改成功");
									$('#checkWorkerId1').change();
								}else{
									alert(JSONobj.msg);
								}
							}
						});
            		}
				}
        		
        		$(document).ready(function(){
					$('#checkWorkerId1').change(function(){
						var workerid = $(this).val();
						var link = "${pageContext.request.contextPath}/actionbean/ScoreApprover.action?workerid=" + workerid + "&getApprovedList=";
						$.ajax({
							url:link,
							type:"GET",
							success:function(response){
								var JSONobj = $.parseJSON(response);
								if(JSONobj.result == "1"){
									var list = JSONobj.list;
									var html = "<table><tr><td>Id</td><td>部门\/车队</td><td>审核人</td><td>操作</td></tr>";
									for(var i=0;i<list.length;i++){
										html += "<tr>";
										html += "<td>" + list[i].id + "</td>";
										html += "<td><input type='hidden' value='" + list[i].id + "'/>"+list[i].name+"</td>";
										html += "<td>"+list[i].isapprover+"</td>";
										html += "<td>"
											+	"<a href='javascript:toggleSection(" + list[i].id + ")'>改状态</a>" +"&nbsp;\/&nbsp;"
											+ 	"<a href='javascript:delApproverSection(" + list[i].id + ")'>删除</a>"
											+	"</td>";
										html += "</tr>";												
									}
									html += "</table>";
									$('#approverTableContainer').html(html);
								}else{
									alert(JSONobj.msg);
								}
							},
							error:function(error){
								alert(error);
							}
						});
					});

					$('.choosable').click(function(){
						var name = $(this).html();
						var workerid = $(this).next().val();
						$('#employeenamefromid1').val(name);
						$('#checkWorkerId1').val(workerid);
						$('#checkWorkerId1').change();
					});
            	});
    </script>
	 <style type="text/css">
				table.addline tr td{ 
					border: 1px solid rgb(0, 0, 0);
					border-collapse:collapse;
					padding:2px;
					height:20px;
					vertical-align:middle;
				}
				.choosable{
					cursor:pointer;
    				color:purple;
    				text-decoration:underline;
				}
	</style>
			
	 <div id="sub-nav"><div class="page-title">
			<h1>审核人管理</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
					<ss:secure roles="scoreapprover_system">
					<stripes:form beanclass="com.bus.stripes.actionbean.score.ScoreApproverActionBean">
					<table class="addline">
						<tr>
							<td colspan=2 style="text-align: center;color:red;">
								审核人审核范围调整
							</td>
						</tr>
						<tr>
							<td colspan=2 style="text-align: center;">
								名称:<stripes:text readonly="true" name="newApprover.approver.fullname" style="background-color:#CCCCBB;"  id="employeenamefromid1"/>工号:<stripes:text name="newApprover.approver.workerid" id="checkWorkerId1"/>
									<input type="hidden" value="" id="extra1"/><a href="javascript:void;" onclick="openSelectEmpWindow('employeenamefromid1','checkWorkerId1','extra1',false)">从列表选择</a>
							</td>
						</tr>
						<tr>
							<td style="text-align: center;color:blue;">
								已经加入的部门
							</td>
							<td style="text-align: center;color:blue;">
								请选择要添加的部门
							</td>
						</tr>
						<tr>
							<td id="approverTableContainer">
								<table>
									<tr>
										<td>部门</td><td>审核资格</td>
									</tr>
								</table>
							</td>
							<td>
								部门:<stripes:select name="newApprover.department.id"><stripes:option value="">无</stripes:option><stripes:options-collection collection="${actionBean.departments}" label="name" value="id"/></stripes:select>
								<br/>
<%-- 								车队:<stripes:select name="newApprover.vehicleteam.id"><stripes:option value="">无</stripes:option><stripes:options-collection collection="${actionBean.vehicleteams}" label="name" value="id"/></stripes:select> --%>
<!-- 								<br/> -->
								审核人:<stripes:select name="newApprover.isapprover"><stripes:option value="N">否</stripes:option><stripes:option value="Y">是</stripes:option></stripes:select>
								<br/>
							</td>
						</tr>
						<tr>
							<td colspan=2 style="text-align: center;">
								<stripes:submit name="addApprover" value="确认添加"/>
							</td>
						</tr>
					</table>
					</stripes:form>
					</ss:secure>
				</div>
				<br/>
				<br/>
				<div>
					<table class="addline">
						<tr>
							<td colspan=3 style="text-align:center;color:red;">
								目前拥有的审核录入列表
							</td>
						</tr>
						<tr style="color:blue;text-align:center;">
							<td>Id</td><td>名字</td><td>审核部门和状态</td>
						</tr>
						<c:forEach items="${actionBean.approvers}" var="app" varStatus="loop">
							<tr>
								<td>${app.id }</td>
								<td><span class="choosable">${app.approver.fullname }</span><input type="hidden" value="${app.approver.workerid }"/></td>
								<c:if test="${app.vehicleteam != null}">
									<td>${app.vehicleteam.name }(${app.isapprover })</td>
								</c:if>
								<c:if test="${app.department != null}">
									<td>${app.department.name }(${app.isapprover })</td>
								</c:if>
							</tr>
						</c:forEach>
					</table>
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>