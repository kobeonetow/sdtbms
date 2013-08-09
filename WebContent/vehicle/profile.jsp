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
    		height:600,
    		width:750,
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
    							alert(response);
    							location.reload();
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
							alert(response);
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
			background-color:red;
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
							<td>车牌号码:</td><td><stripes:text  class="required" name="profile.vid"/></td>
							<td>入户日期:</td><td><stripes:text  name="profile.datejoin"  formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
						</tr>
						<tr>
							<td>自编号:</td><td><stripes:text  class="required" name="profile.selfid"/></td>
							<td>登记证号:</td><td><stripes:text  class="required" name="profile.recordid"/></td>
						</tr>
						<tr>
							<td>使用性质:</td><td><stripes:text  class="required" name="profile.servicetype"/></td>
							<td>运行日期:</td><td><stripes:text  name="profile.dateuse"  formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
						</tr>
						<tr>
							<td>车辆来源:</td><td><stripes:text  class="required" name="profile.source"/></td>
							<td>购买日期:</td><td><stripes:text  name="profile.datepurchase"  formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
						</tr>
						<tr>
							<td>车价(元):</td><td><stripes:text  class="required" name="profile.vehicleprice"/></td>
							<td>购置费(元):</td><td><stripes:text  class="required" name="profile.subprice"/></td>
						</tr>
						<tr>
							<td>车属单位:</td><td colspan=3><stripes:text  class="required" name="profile.company"/></td>
						</tr>
						<tr>
							<td>单位地址:</td><td colspan=3><stripes:text  class="required" name="profile.companyaddr"/></td>
						</tr>
						<tr>
							<td>车辆类型:</td><td><stripes:text  class="required" name="profile.vtype"/></td>
							<td>评定等级:</td><td><stripes:text  class="required" name="profile.vlevel"/></td>
						</tr>
						<tr>
							<td>购置凭证税号:</td><td><stripes:text  class="required" name="profile.ptaxnumber"/></td>
							<td>强制报废日期:</td><td><stripes:text  name="profile.dateinvalidate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
						</tr>
						<tr>
							<td>分公司:</td>
							<td>
								<stripes:select name="profile.subcompany" class="required">
									<stripes:option value="公交一">公交一</stripes:option>
									<stripes:option value="公交二">公交二</stripes:option>
									<stripes:option value="长途">长途</stripes:option>
									<stripes:option value="其它">其它</stripes:option>
								</stripes:select>
							</td>
							<td>出厂编号:</td><td><stripes:text name="profile.productioncode"/></td>
						</tr>
						<tr>
							<td class="subtitle" colspan=4>车辆参数配置<hr/></td>
						</tr>
						<tr>
							<td>车辆型号:</td><td><stripes:text name="profile.model"/></td>
							<td>车辆配置:</td><td><stripes:text name="profile.subsides"/></td>
						</tr>
						<tr>
							<td>车辆产地:</td><td><stripes:text name="profile.madein"/></td>
							<td>车辆出厂日期:</td><td><stripes:text name="profile.dateproduction"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
						</tr>
						<tr>
							<td>车辆制造企业名称:</td><td colspan=3><stripes:text name="profile.productionaddr"/></td>
						</tr>
						<tr>
							<td>车身颜色:</td><td><stripes:text name="profile.color"/></td>
							<td>座位(座/总):</td><td><stripes:text name="profile.sits"/></td>
						</tr>
						<tr>
							<td>轮胎数:</td><td><stripes:text name="profile.tyrenum"/></td>
							<td>轮胎型号:</td><td><stripes:text name="profile.tyremodel"/></td>
						</tr>
						<tr>
							<td>燃料:</td><td><stripes:text name="profile.fueltype"/></td>
							<td>外形尺寸:</td><td><stripes:text name="profile.bodysize"/></td>
						</tr>
						<tr>
							<td>最高车速(km/h):</td><td><stripes:text name="profile.vspeed"/></td>
							<td>排量/功率:</td><td><stripes:text name="profile.vpower"/></td>
						</tr>
						<tr>
							<td>总质量(kg):</td><td><stripes:text name="profile.totalweight"/></td>
							<td>整车备质量(kg):</td><td><stripes:text name="profile.subweight"/></td>
						</tr>
						<tr>
							<td>前轮距(mm):</td><td><stripes:text name="profile.frontwheel"/></td>
							<td>后轮距(mm):</td><td><stripes:text name="profile.backwheel"/></td>
						</tr>
						<tr>
							<td>轴距(mm):</td><td><stripes:text name="profile.wheelbase"/></td>
							<td>驱动形式:</td><td><stripes:text name="profile.drivemode"/></td>
						</tr>
						<tr>
							<td>发动机号:</td><td><stripes:text name="profile.enginenum"/></td>
							<td>发动机型号:</td><td><stripes:text name="profile.enginemodel"/></td>
						</tr>
						<tr>
							<td>车架号码:</td><td><stripes:text name="profile.framenum"/></td>
							<td>底盘型号:</td><td><stripes:text name="profile.basednum"/></td>
						</tr>
						<tr>
							<td>转向形式:</td><td><stripes:text name="profile.turntype"/></td>
							<td>转向器式:</td><td><stripes:text name="profile.turnmethod"/></td>
						</tr>
						<tr>
							<td>行车制动形式:</td><td><stripes:text name="profile.movebreak"/></td>
							<td>驻车制动形式:</td><td><stripes:text name="profile.stopbreak"/></td>
						</tr>
						<tr>
							<td>悬挂形式:</td><td colspan=3><stripes:text name="profile.hangmodel"/></td>
						</tr>
						<tr>
							<td>空调:</td><td colspan=3><stripes:text name="profile.aircond"/></td>
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
							车辆保养维修日期导入:<stripes:file name="repairFile"/><stripes:submit name="vehicleRepaireDatesUpload"/>
							<br/>
							车队车队长导入:<stripes:file name="teamFile"/><stripes:submit name="vehicleTeamFileUpload"/>
							<br/>
							新车辆导入:<stripes:file name="newVehicleFile"/><stripes:submit name="vehicleNewFileUpload"/>
							<br/>
							车辆分队以及路线导入:<stripes:file name="teamLaneFile"/><stripes:submit name="vehicleTeamAndLaneUpload"/>
							<br/>
							登记证号:<stripes:file name="recordIdFile"/><stripes:submit name="vehicleRecordIdUpload"/>
							<br/>
							公交一公司车辆:<stripes:file name="subCompanyOneFile"/><stripes:submit name="vehicleSubCompanyOneListUpload"/>
							<br/>
							总公里数上传:<stripes:file name="totlMilesFile"/><stripes:submit name="vehicleMilesUpload"/>
						</stripes:form>
					</ss:secure>
					<div class="inner-page-title">
						车辆档案信息
					</div>
					<div class="hastable">
				
				<table class="vehiclelist">
					<thead>
					<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" focus="">
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
							<td colspan=11 style="text-align:left">
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
										<ss:secure roles="vehicle_mile_view">
											<button class="btnMiles">公里数</button>
										</ss:secure>
										<stripes:submit class="btnVehicleDetail" name="VehicleDetail" value="详细资料"/>
								</stripes:form>
							</td>
							<td>${veh.selfid}</td>
							<td>${veh.vid}</td>
							<td>${veh.recordid}</td>
							<td><a href="${pageContext.request.contextPath}/actionbean/VehicleTeam.action?teamId=${veh.team.team.id}">${veh.team.team.name}</a></td>
							<td>${veh.subcompany}</td>
							<td>${veh.lane.lane.num}</td>
							<td><a href="${pageContext.request.contextPath}/actionbean/VehicleLane.action?targetId=${veh.lane.lane.id}&routeDetail=">${veh.lane.lane.detail}</a></td>
							<td>${veh.throwdateStr}</td>
							<td>${veh.dateinvalidateStr}</td>
							<td>${veh.totalmiles}</td>
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