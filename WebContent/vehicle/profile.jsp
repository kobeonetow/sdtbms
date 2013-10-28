<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    
    <script type="text/javascript">
    $(document).ready(function(){
    	buildBasicDatePickDialog("date_select_dialog",200,150);
    	callAjaxForEditWithDatePicker("btnThrow");
        
    	$("#btn_new_vehicle_link").click(function() {
    		$('#btn_new_vehicle_dialog').dialog('open');
			return true;
    	});

    	$('#btn_new_vehicle_dialog').dialog({
    		autoOpen: false,
    		bgiframe: true,
    		resizable: true,
    		height:300,
    		width:600,
    		modal: true,
    		overlay: {
    			backgroundColor: '#000',
    			opacity: 0.5
    		},
    		buttons: {
    			'新建':function(){
    					if(!formValidation('form_new_vehicle')){
    						return;
    					}
    					var formaction = $('#form_new_vehicle').attr('action');
    					var params = $('#form_new_vehicle').serialize() + "&addVehicle=";
    					$.ajax({
    						url:formaction,
    						type:"post",
    						dataType:'text',
    						data:params,
    						success:function(response){
    							$('#btn_new_vehicle_dialog').dialog('close');
    							var json = $.parseJSON(response);
    							alert(json.msg);
    							if(json.result == "1"){
    								location.reload();
            					}
    						},
    						error:function(response){
    							alert("errors");
    						}
    					});
    			},
    			'取消': function() {
    				$(this).dialog('close');
    			}
    		}
    	});

    	$('.vehicleListFunctionForm').submit(function(){
			return false;
        });

        $('.btnDeleteVehicle').click(function(){
        		var deleteV = confirm("确定删除车辆资料？");
        		if(deleteV){
            		var link = $(this).parent().attr("action");
            		var params = $(this).parent().serialize() + "&deleteVehicle=";
        			$.ajax({
						url:link,
						type:"post",
						dataType:'text',
						data:params,
						success:function(response){
							var json = $.parseJSON(response);
							alert(json.msg);
							if(json.result == "1")
								location.reload();	
						},
						error:function(response){
							alert("errors");
						}
					});
        		}
        });

        $('.btnVehicleDetail').click(function(){
			var targetId = $(this).parent().children().first().val();
			var link = "${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId="+targetId+"&vehicleDetail=";
			window.open(link);
        });

        $('.btnMiles').click(function(){
        	var targetId = $(this).parent().children().first().val();
        	var link = "${pageContext.request.contextPath}/actionbean/VehicleMiles.action?vid="+targetId;
        	window.location = link;			
       	});
    });
    </script>
	<style type="text/css">
		table.vehicleDetail tr td{ 
			border: 1px solid rgb(24, 59, 240);
			border-collapse:collapse;
			padding:3px;
			height:25px;
			vertical-align:middle;
		}
		table.vehicleDetail input{ 
			width:90%;
		}
		table td.subtitle{
			text-align:center;
			font-size:12pt;
			padding-top:10px !important;
			color:red;
		}
		table.vehiclelist tbody td{
			text-align:center;
		}
		tr.expired{
			background-color:pink;
		}
	</style>
		<div id="sub-nav"><div class="page-title">
			<h1>车辆档案</h1>
			<span><a href="#" title="Layout Options">车辆</a> > <a href="#" title="Two column layout">档案管理</a> > 查看</span>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/vehicle/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				
				<!-- 新建档案  Dialog-->
				<div id="hr_top_button_bar" style="height:40px;">&nbsp;  
				
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				
				<ss:secure roles="vehicle_profile_edit">
				<a id="btn_new_vehicle_link" class="btn ui-state-default ui-corner-all" href="#">
						<span class="ui-icon ui-icon-person"></span>
						新建档案
				</a>
				<div id="btn_new_vehicle_dialog" title="新建档案">
				
				<!-- The Form to submit new employee data -->
				<stripes:form id="form_new_vehicle" beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" focus="">
					<stripes:errors/>
					<table class="vehicleDetail">
						<tr>
							<td class="subtitle" colspan=4>车辆基本资料<hr/></td>
						</tr>
						<tr>
							<td>车牌号码:</td><td><stripes:text  class="required" name="profile.vid" maxlength="10"/></td>
							<td>自编号:</td><td><stripes:text  class="required" name="profile.selfid" maxlength="10"/></td>
						</tr>
						<tr>
							<td>购买日期:</td><td><stripes:text  name="profile.purchaseDate"  formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td>购买收据号:</td><td><stripes:text name="profile.purchaseCode" maxlength="64"/></td>
						</tr>
						<tr>
							<td>入户日期:</td><td><stripes:text  name="profile.joinDate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>强制报废日期:</td><td><stripes:text  name="profile.forcethrowdate" formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
						</tr>
						<tr>
							<td>注册日期:</td><td><stripes:text  name="profile.registerDate"  formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td>分公司:</td>
							<td>
								<stripes:select name="profile.subcompany" class="required">
									<stripes:option value=""></stripes:option>
									<stripes:option value="公交一">公交一</stripes:option>
									<stripes:option value="公交二">公交二</stripes:option>
									<stripes:option value="长途">长途</stripes:option>
									<stripes:option value="其它">其它</stripes:option>
								</stripes:select>
							</td>
						</tr>
						<tr>
							<td>颜色:</td><td colspan=3><stripes:text  name="profile.vcolor"  class="required"  maxlength="10px"/></td>
						</tr>
					</table>
				</stripes:form>
				</div>
				</ss:secure>
				</div>
					
				
				<!--  Display Vehicle List   -->
				<div>
					<ss:secure roles="administrator_system">
						<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
							车辆档案详细资料导入:<stripes:file name="detailFile"/><stripes:submit name="vehicleDetailFileUpload"/>
							<br/>
<%-- 							车辆保养维修日期导入:<stripes:file name="repairFile"/><stripes:submit name="vehicleRepaireDatesUpload"/> --%>
<!-- 							<br/> -->
<%-- 							车队车队长导入:<stripes:file name="teamFile"/><stripes:submit name="vehicleTeamFileUpload"/> --%>
<!-- 							<br/> -->
							新车辆导入:<stripes:file name="newVehicleFile"/><stripes:submit name="vehicleNewFileUpload"/>
							<br/>
							车辆分队以及路线导入:<stripes:file name="teamLaneFile"/><stripes:submit name="vehicleTeamAndLaneUpload"/>
							<br/>
							登记证号:<stripes:file name="recordIdFile"/><stripes:submit name="vehicleRecordIdUpload"/>
							<br/>
<%-- 							公交一公司车辆:<stripes:file name="subCompanyOneFile"/><stripes:submit name="vehicleSubCompanyOneListUpload"/> --%>
<!-- 							<br/> -->
<%-- 							总公里数上传:<stripes:file name="totlMilesFile"/><stripes:submit name="vehicleMilesUpload"/> --%>
						</stripes:form>
					</ss:secure>
					<div class="inner-page-title">
						车辆档案信息
					</div>
					<div class="hastable">
				
				<table class="vehiclelist">
					<thead>
					<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
						<tr>
							<td colspan=13 style="text-align:left">
								<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/>/<stripes:label name="${actionBean.totalcount}"/>  <stripes:submit name="nextpage" value="下页"/>
							显示数量:<stripes:text name="lotsize"/>
							<label>总数:${actionBean.recordsTotal} 行</label>
							</td>
						</tr>
						<tr>
							<td colspan=13 style="text-align:left;">
								车牌号:<stripes:text name="selector.vid"/> 自编号:<stripes:text name="selector.selfid"/>
								<br/>
								车队:<stripes:select name="selector.teamId"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.teams}" label="name" value="id"/></stripes:select>
								<br/>
								线路:<stripes:select name="selector.laneId">
											<stripes:option value="">不限</stripes:option>
											<c:forEach items="${actionBean.lanes}" var="lane" varStatus="loop">
												<stripes:option value="${lane.id}">${lane.num}&nbsp;&nbsp;${lane.detail}</stripes:option>
											</c:forEach>
									</stripes:select>
								<br/>
								分公司:
								<stripes:select name="selector.subcompany">
									<stripes:option value="">不限</stripes:option>
									<stripes:option value="公交一">公交一</stripes:option>
									<stripes:option value="公交二">公交二</stripes:option>
									<stripes:option value="长途">长途</stripes:option>
									<stripes:option value="其它">其它</stripes:option>
								</stripes:select>
								<br/>
								排序:<stripes:radio value="" name="selector.order"/>不限
									<stripes:radio value="2" name="selector.order"/>入户日期
									<stripes:radio value="3" name="selector.order"/>报废日期
								<br/>
								显示报废:<stripes:radio name="selector.throwed" value="1"/>是<stripes:radio name="selector.throwed" value="0"/>否
								<br/>
								报废日期:(从)<stripes:text name="selector.expire1"  formatPattern="yyyy-MM-dd" class="datepickerClass"/>---(到)<stripes:text name="selector.expire2"  formatPattern="yyyy-MM-dd" class="datepickerClass"/>
							</td>
						</tr>
						<tr>
							<td colspan=13 style="text-align:left">
								<stripes:submit name="filter" value="提交"/>
							</td>
						</tr>
					</stripes:form>
						<tr>
						<th>操作</th>
						<th>自编号</th>
						<th>车牌号</th>
						<th>登记证号</th>
						<th>车队</th>
						<th>分公司</th>
						<th>号码</th>
						<th>路线</th>
						<th>报废日期</th>
						<th>强制报废</th>
						<th>总行驶里程</th>
						</tr>
					</thead>
					<tbody>
					
					<c:set var="color" value="0" scope="page"/>
					<c:set var="statusE" value="E" scope="page"/>
					<c:forEach items="${actionBean.profiles}" var="veh" varStatus="loop">
						<c:choose>
							<c:when test="${veh.status == statusE}">
								<tr class="expired">
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<stripes:form class="vehicleListFunctionForm" beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
										<input type="hidden" name="targetId" value="${veh.id}"/>
										<ss:secure roles="vehicle_profile_edit">
											<stripes:submit class="btnDeleteVehicle" name="deleteVehicle" value="删除"/>
											<c:if test="${veh.status != statusE}">
												<button class="btnThrow">报废</button><input type="hidden" value="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?vid=${veh.id}&throwVehicle="/>
											</c:if>
										</ss:secure>
<%-- 										<ss:secure roles="vehicle_mile_view"> --%>
<!-- 											<button class="btnMiles">公里数</button> -->
<%-- 										</ss:secure> --%>
										<stripes:submit class="btnVehicleDetail" name="VehicleDetail" value="详细资料"/>
								</stripes:form>
							</td>
							<td>${veh.selfid}</td>
							<td>${veh.vid}</td>
							<td>
								<c:if test="${veh.info != null }">
									${veh.info.registernumber}
								</c:if>
							</td>
							<td>
								<c:if test="${veh.vehicleteam != null }">
									<a href="${pageContext.request.contextPath}/actionbean/VehicleTeam.action?teamId=${veh.vehicleteam.team.id}">${veh.vehicleteam.team.name}</a>
								</c:if>
							</td>
							<td>${veh.subcompany }</td>
							<td>
								<c:if test="${veh.vehiclelane != null }">
									${veh.vehiclelane.lane.num}
								</c:if>
							</td>
							<td>
								<c:if test="${veh.vehiclelane != null }">
									<a href="${pageContext.request.contextPath}/actionbean/VehicleLane.action?targetId=${veh.vehiclelane.lane.id}&routeDetail=">${veh.vehiclelane.lane.detail}</a>
								</c:if>
							</td>
							<td>${veh.throwDateStr }</td>
							<td>${veh.forcethrowdateStr}</td>
							<td></td>
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
			<div id="date_select_dialog" title="选择日期">
				<label>日期:</label><input type="text" id="date_select_box" class="datepickerClass"/>
				<input type="hidden" id="opener" value=""/>
			</div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>