<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${actionBean.profile.vid }档案</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script> 

<link href="${pageContext.request.contextPath}/css/themes/black_rose/ui.css" rel="stylesheet" title="style" media="all" />
<link href="${pageContext.request.contextPath}/css/ui/ui.base.css" rel="stylesheet" media="all" />
<link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />
<script type="text/javascript">
$(document).ready(function(){
	$('#fileTabs').tabs();
	
	$('#vehicleTechForm').submit(function(){return false;});

	$(".datepickerClass").datepicker({
    	changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd' 
    });
	
	$('.delVC').click(function(){
		var id = $(this).closest("tr").children().first().children().first().next().val();
		var del = confirm("真的要删除ID:"+id+"?");
		var formurl =  "${pageContext.request.contextPath}/actionbean/VehicleProfile.action?checkId="+id+"&deleteVechileCheck=";
		if(!del)
			return;
		$.ajax({
			url:formurl,
			type:"post",
			dataType:'text',
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
	});
	
	$('#btnEdit').click(function(){
		var formaction = $('#vehicleTechForm').attr('action');
		var params = $('#vehicleTechForm').serialize() + "&saveVehicleBasicInfo=";
		$.ajax({
			url:formaction,
			type:"post",
			dataType:'text',
			data:params,
			success:function(response){
				var json = $.parseJSON(response);
				alert(json.msg);
			},
			error:function(response){
				alert("errors");
			}
		});
	});

	$('#btnClose').click(function(){
		window.close();
	});
});
</script>

<style type="text/css">
body {
 	margin-left: 0px; 
 	margin-top: 0px; 
 	margin-right: 0px; 
 	margin-bottom: 0px; 
 }
 #vehicleTechForm{ 
 	border:0px none; 
 	color:#666; 
 	padding:0; 
 	margin:0; 
 	outline:0px none; 
	vertical-align:middle;
 	width:auto; 
 	height:auto; 
 	} 
 #whole{ 
 	border:solid 1px; 
 	color:#666; 
 	padding-top:5px; 
 	height:auto; 
 	width:1190px;
	overflow:hidden;
 	}

.bar{
	height:50px;
	text-align:center !important;
	}
 .bar span{
 	text-align:center;
 	line-height:50px;
 	height:50px;
 	font-size:50px;
    display:block;
	border:solid 3px; 
     }
a.hover{
	background:#FF0;
	display:block;
	}
 .bat{
	border:solid 1px;
	float:left;
	font-size:16px;
	vertical-align: middle;
	margin:0 auto;
	}
.bat td{
	margin:0 auto;
	vertical-align: middle;
	text-align:center;
	border:solid 2px;
	height:50px;
	line-height:50px;
	}
table>tr>td{
	text-align:center !important;
	vertical-align:middle !important;
} 
/* .kind{ */
/* 	border:solid 1px; */
/* 	} */
.kind td{
	padding:3px;
	border:solid 1px;
/* 	height:30px; */
	font-size: 16px;
/* 	line-height:30px; */
	} 
	
	
/* 下面的是文件上传的 */
table.normal tr td{
	border: 1px solid black;
	border-collapse:collapse;
	padding:5px;
	font-size:12pt;
 	vertical-align: middle; 
}
label.tabSubTitle{
	color:#851A1A;
	font-size:12pt;
}
form li{
	clear:none !important;
}
.button{
	font: bold 15pt Arial;
    text-decoration: none;
    background-color: #EEEEEE;
    color: #333333;
    padding: 2px 6px 2px 6px;
    border-top: 1px solid #CCCCCC;
    border-right: 1px solid #333333;
    border-bottom: 1px solid #333333;
    border-left: 1px solid #CCCCCC;
}
</style>
</head>

<body>
<div id="whole">
<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" id="vehicleTechForm">
	
    <table width="100%" border="1" class="bar">
    	<tr>
    		<td align="center">
    			<span>
    				车牌号：
	    			${actionBean.profile.vid }
	    			<input type="hidden" name="targetId" value="${actionBean.profile.id }"/>
	    			<input type="hidden" name="vBasic.vid.id" value="${actionBean.profile.id }"/>
	    			<input type="hidden" name="vBasic.id" value="${actionBean.vBasic.id }"/>
	    		</span>
    		</td>
    	</tr>
    </table>

	<table width="16%" border="1"  class="bat">	
      <tr>
       <td  style="background-color:#FF0; color:#F00; font-weight:bolder;"><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleDetail=">车辆基本情况</a></td>
      </tr>
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleTechDetail=">车辆技术参数</a></td>
      </tr>
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleRepairRecord=">车辆维修记录</a></td>
      </tr>
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehiclePartsRepair=">车辆主要总成部件更换</a></td>
      </tr>
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleLevel=">车辆等级评定</a></td>
      </tr>
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleChange=">车辆变更</a></td>
      </tr>
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleUseRecord=">车辆使用记录</a></td>

      </tr>
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleAccident=">车辆事故记录</a></td>

      </tr>
    </table>
	
  <table class="kind">
    	<tr>
    		<td colspan=4 style="text-align:center;">
    			<img src="${actionBean.context.hrhostidfile}vehicleprofilepic/${actionBean.profile.image.name}" height="220px" width="300px" alt="读取失败"/>	
    		</td>
    	</tr>
    	<tr>
    		<td>车牌号码:</td>
    		<td><stripes:text name="vBasic.vid.vid" maxlength="10"/></td>
    		<td>自编号:</td>
    		<td><stripes:text name="vBasic.vid.selfid" maxlength="10"/></td>
    	</tr>
    	<tr>
    		<td>车主名称:</td>
    		<td><stripes:text name="vBasic.master" maxlength="64"/></td>
    		<td>分公司:</td>
    		<td>
    			<stripes:select name="vBasic.vid.subcompany">
									<stripes:option value=""></stripes:option>
									<stripes:option value="公交一">公交一</stripes:option>
									<stripes:option value="公交二">公交二</stripes:option>
									<stripes:option value="长途">长途</stripes:option>
									<stripes:option value="其它">其它</stripes:option>
				</stripes:select>
			</td>	
    	</tr>
    	<tr>
    		<td>车辆登记证书号:</td>
    		<td><stripes:text name="vBasic.registernumber" maxlength="10"/></td>
    		<td>经营许可证号:</td>
    		<td><stripes:text name="vBasic.operatenumber" maxlength="20"/></td>
    	</tr>
    	<tr>
    		<td>二级维护周期:</td>
    		<td colspan=3><stripes:text name="vBasic.maintenancemile" maxlength="16"/></td>
    	</tr>
    	<tr>
    		<td>经济类型:</td>
    		<td><stripes:text name="vBasic.companytype" maxlength="20"/></td>
    		<td>颜色:</td>
    		<td><stripes:text name="vBasic.vid.vcolor"/></td>
    	</tr>
    	<tr>
    		<td>经营组织方式:</td>
    		<td><stripes:text name="vBasic.operatemode"/></td>
    		<td>注册日期:</td>
    		<td><stripes:text name="vBasic.vid.registerDate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
    	
    	</tr>
    	<tr>
    		<td>资质等级:</td>
    		<td><stripes:text name="vBasic.operatelevel" maxlength="8"/></td>
    		<td>入户日期:</td>
    		<td><stripes:text name="vBasic.vid.joinDate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
    	
    	</tr>
    	<tr>
    		<td>经营范围:</td>
    		<td colspan=3><stripes:text name="vBasic.operaterange" maxlength="10"/></td>
<!--     		<td>购买日期:</td> -->
<%--     		<td><stripes:text name="vBasic.vid.purchaseDate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td> --%>
    	
    	</tr>
    	<tr>
    		<td>道路运输证号:<br/>/公共汽车登记证号</td>
    		<td colspan=3><stripes:text name="vBasic.transportnumber" maxlength="20"/></td>
<!--     		<td>购买收据号:</td> -->
<%--     		<td><stripes:text name="vBasic.vid.purchaseCode"/></td> --%>
    	</tr>
    	<tr>
    		<td>经营线路:</td>
    		<td><stripes:text name="vBasic.operateroute" maxlength="64"/></td>
    		<td>报废日期:</td>
    		<td><stripes:text name="vBasic.vid.throwDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
    	
    	</tr>
    	<tr>
    		<td>运力来源:</td>
    		<td><stripes:text name="vBasic.source" maxlength="8"/></td>
    		<td>强制报废日期:</td>
    		<td><stripes:text name="vBasic.vid.forcethrowdate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
    	</tr>
      <tr>
      <td colspan=4 >
      	<stripes:submit id="btnEdit" name="saveVehicleBasicInfo" value="保存修改"/> 
      	<button id="btnClose">关闭</button>
      </td>
      </tr>
  </table>
  	</stripes:form>
  	
<ss:secure roles="vehicle_profile_edit">
<div id="profilePicDiv">
	<stripes:form beanclass="com.bus.stripes.actionbean.common.ImageUploadActionBean">
		<c:set var="returnLink" value="/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id}&vehicleDetail=" scope="page"/>
		<input type="hidden" name="returnLink" value="${returnLink}"/>
		<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
		档案头像上传:<stripes:file name="vehicleProfilePicFile" /><stripes:submit name="addProfileVehiclePicture" value="提交"/>
	</stripes:form>
</div>		
</ss:secure>
  	
<div id="fileUploadDiv">
 	<c:set var="returnLink" value="/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id}&vehicleDetail=" scope="page"/>
	<hr/>
		
	<br/>
	<br/>
	
<!-- 	use for status comparison -->
	<div id="fileTabs">
		<ul>
				<li><a href="#fileTabs-1">一保&二保</a></li>
				<li><a href="#fileTabs-2">维修记录</a></li>
				<li><a href="#fileTabs-3">综合检测</a></li>
				<li><a href="#fileTabs-4">年审</a></li>
				<li><a href="#fileTabs-5">附加证件</a></li>
		</ul>
		<div id="fileTabs-1">
			<label class="tabSubTitle">一保&二保</label><br/>
			<br/>
			<div>
				<ss:secure roles="vehicle_file_maintenance">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
				<input type="hidden" name="returnLink" value="${returnLink}"/>
				<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
					<table class="normalAdd">
						<tr>
							<td>类型</td>
							<td>间隔公里数</td>
							<td>日期</td>
							<td>注释</td>
							<td>文件</td>
							<td></td>
						</tr>
						<tr>
							<td><stripes:select name="check.ctype"><stripes:option value="一保">一保</stripes:option><stripes:option value="二保">二保</stripes:option></stripes:select></td>
							<td><stripes:text name="check.miles"/></td>
							<td><stripes:text name="check.cdate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td><stripes:text name="check.remark"/></td>
							<td><stripes:file name="checkFile" /></td>
							<td><stripes:submit name="addVehicleCheck" value="添加"/></td>
						</tr>
					</table>
				</stripes:form>
				</ss:secure>
			</div>
			<br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>间隔公里数</td>
					<td>日期</td>
					<td>文件</td>
					<td>注释</td>
					<td></td>
				</tr>
				<c:forEach items="${actionBean.maintenances}" var="maint" varStatus="loop">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<tr>
						<td>${maint.id}
							<input type="hidden" name="returnLink" value="${returnLink}"/>
							<input type="hidden" name="checkId" value="${maint.id}"/>
						</td>
						<td>${maint.ctype}</td>
						<td>${maint.miles}</td>
						<td>${maint.cdateStr}</td>
						<td>
							<a target="_blank" href="${actionBean.context.hrhostidfile}车辆/${maint.ctype}/${maint.image.filename}">查看</a>
							<stripes:file name="checkFile"/>
						</td>
						<td>${maint.remark}</td>
						<td>
							<ss:secure roles="vehicle_file_maintenance">
								<stripes:submit name="updateVehicleCheck" value="更新"/>
								<a class="delVC" href="javascript:void;">删除</a>
							</ss:secure>
						</td>
					</tr>
				</stripes:form>
				</c:forEach>
			</table>
		</div>
		
		<div id="fileTabs-2">
			<label class="tabSubTitle">维修记录</label><br/>
			<br/>
			<div>
				<ss:secure roles="vehicle_file_repair">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
				<input type="hidden" name="returnLink" value="${returnLink}"/>
				<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
					<table class="normalAdd">
						<tr>
							<td>类型</td>
							<td>间隔公里数</td>
							<td>日期</td>
							<td>注释</td>
							<td>文件</td>
							<td></td>
						</tr>
						<tr>
							<td><stripes:select name="check.ctype"><stripes:option value="小修">小修</stripes:option><stripes:option value="大修">大修</stripes:option><stripes:option value="中修">中修</stripes:option></stripes:select></td>
							<td><stripes:text name="check.miles"/></td>
							<td><stripes:text name="check.cdate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td><stripes:text name="check.remark"/></td>
							<td><stripes:file name="checkFile" /></td>
							<td><stripes:submit name="addVehicleCheck" value="添加"/></td>
						</tr>
					</table>
				</stripes:form>
				</ss:secure>
			</div>
			<br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>间隔公里数</td>
					<td>日期</td>
					<td>文件</td>
					<td>注释</td>
					<td></td>
				</tr>
				<c:forEach items="${actionBean.repairs}" var="rep" varStatus="loop">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<tr>
						<td>${rep.id}
							<input type="hidden" name="returnLink" value="${returnLink}"/>
							<input type="hidden" name="checkId" value="${rep.id}"/>
						</td>
						<td>${rep.ctype}</td>
						<td>${rep.miles}</td>
						<td>${rep.cdateStr}</td>
						<td>
							<a target="_blank" href="${actionBean.context.hrhostidfile}车辆/${rep.ctype}/${rep.image.filename}">查看</a>
							<stripes:file name="checkFile"/>
						</td>
						<td>${rep.remark}</td>
						<td>
							<ss:secure roles="vehicle_file_repair">
								<stripes:submit name="updateVehicleCheck" value="更新"/>
								<a class="delVC" href="javascript:void;">删除</a>
							</ss:secure>
						</td>
					</tr>
				</stripes:form>
				</c:forEach>
			</table>
		</div>
		
		<div id="fileTabs-3">
			<label class="tabSubTitle">综合检测</label><br/>
			<br/>
			<div>
				<ss:secure roles="vehicle_file_fullcheck">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
				<input type="hidden" name="returnLink" value="${returnLink}"/>
				<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
					<table class="normalAdd">
						<tr>
							<td>类型</td>
							<td>间隔公里数</td>
							<td>日期</td>
							<td>注释</td>
							<td>文件</td>
							<td></td>
						</tr>
						<tr>
							<td><stripes:text name="check.ctype" value="综合" readonly="readonly"/></td>
							<td><stripes:text name="check.miles"/></td>
							<td><stripes:text name="check.cdate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td><stripes:text name="check.remark"/></td>
							<td><stripes:file name="checkFile" /></td>
							<td><stripes:submit name="addVehicleCheck" value="添加"/></td>
						</tr>
					</table>
				</stripes:form>
				</ss:secure>
			</div>
			<br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>间隔公里数</td>
					<td>日期</td>
					<td>文件</td>
					<td>注释</td>
					<td></td>
				</tr>
				<c:forEach items="${actionBean.fullchecks}" var="rep" varStatus="loop">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<tr>
						<td>${rep.id}
							<input type="hidden" name="returnLink" value="${returnLink}"/>
							<input type="hidden" name="checkId" value="${rep.id}"/>
						</td>
						<td>${rep.ctype}</td>
						<td>${rep.miles}</td>
						<td>${rep.cdateStr}</td>
						<td>
							<a target="_blank" href="${actionBean.context.hrhostidfile}车辆/${rep.ctype}/${rep.image.filename}">查看</a>
							<stripes:file name="checkFile"/>
						</td>
						<td>${rep.remark}</td>
						<td>
							<ss:secure roles="vehicle_file_fullcheck">
								<stripes:submit name="updateVehicleCheck" value="更新"/>
								<a class="delVC" href="javascript:void;">删除</a>
							</ss:secure>
						</td>
					</tr>
				</stripes:form>
				</c:forEach>
			</table>
		</div>
		
		<div id="fileTabs-4">
			<label class="tabSubTitle">年审</label><br/>
			<br/>
			<div>
				<ss:secure roles="vehicle_file_annul">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
				<input type="hidden" name="returnLink" value="${returnLink}"/>
				<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
					<table class="normalAdd">
						<tr>
							<td>类型</td>
							<td>间隔公里数</td>
							<td>日期</td>
							<td>注释</td>
							<td>文件</td>
							<td></td>
						</tr>
						<tr>
							<td><stripes:text name="check.ctype" value="年审" readonly="readonly"/></td>
							<td><stripes:text name="check.miles"/></td>
							<td><stripes:text name="check.cdate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td><stripes:text name="check.remark"/></td>
							<td><stripes:file name="checkFile" /></td>
							<td><stripes:submit name="addVehicleCheck" value="添加"/></td>
						</tr>
					</table>
				</stripes:form>
				</ss:secure>
			</div>
			<br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>间隔公里数</td>
					<td>日期</td>
					<td>文件</td>
					<td>注释</td>
					<td></td>
				</tr>
				<c:forEach items="${actionBean.annul}" var="rep" varStatus="loop">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<tr>
						<td>${rep.id}
							<input type="hidden" name="returnLink" value="${returnLink}"/>
							<input type="hidden" name="checkId" value="${rep.id}"/>
						</td>
						<td>${rep.ctype}</td>
						<td>${rep.miles}</td>
						<td>${rep.cdateStr}</td>
						<td>
							<a target="_blank" href="${actionBean.context.hrhostidfile}车辆/${rep.ctype}/${rep.image.filename}">查看</a>
							<stripes:file name="checkFile"/>
						</td>
						<td>${rep.remark}</td>
						<td>
							<ss:secure roles="vehicle_file_annul">
								<stripes:submit name="updateVehicleCheck" value="更新"/>
								<a class="delVC" href="javascript:void;">删除</a>
							</ss:secure>
						</td>
					</tr>
				</stripes:form>
				</c:forEach>
			</table>
		</div>
		
		<div id="fileTabs-5">
			<label class="tabSubTitle">附加证件</label><br/>
			<br/>
			<div>
				<ss:secure roles="vehicle_file_extras">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
				<input type="hidden" name="returnLink" value="${returnLink}"/>
				<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
					<table class="normalAdd">
						<tr>
							<td>类型</td>
							<td>间隔公里数</td>
							<td>日期</td>
							<td>注释</td>
							<td>文件</td>
							<td></td>
						</tr>
						<tr>
							<td><stripes:text name="check.ctype" value="附件" readonly="readonly"/></td>
							<td><stripes:text name="check.miles"/></td>
							<td><stripes:text name="check.cdate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td><stripes:text name="check.remark"/></td>
							<td><stripes:file name="checkFile" /></td>
							<td><stripes:submit name="addVehicleCheck" value="添加"/></td>
						</tr>
					</table>
				</stripes:form>
				</ss:secure>
			</div>
			<br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>间隔公里数</td>
					<td>日期</td>
					<td>文件</td>
					<td>注释</td>
					<td></td>
				</tr>
				<c:forEach items="${actionBean.extras}" var="rep" varStatus="loop">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<tr>
						<td>${rep.id}
							<input type="hidden" name="returnLink" value="${returnLink}"/>
							<input type="hidden" class="check_id" name="checkId" value="${rep.id}"/>
						</td>
						<td>${rep.ctype}</td>
						<td>${rep.miles}</td>
						<td>${rep.cdateStr}</td>
						<td>
							<a target="_blank" href="${actionBean.context.hrhostidfile}车辆/${rep.ctype}/${rep.image.filename}">查看</a>
							<stripes:file name="checkFile"/>
						</td>
						<td>${rep.remark}</td>
						<td>
							<ss:secure roles="vehicle_file_extras">
								<stripes:submit name="updateVehicleCheck" value="更新"/>
								&nbsp;&nbsp;
								<a class="delVC" href="javascript:void;">删除</a>
							</ss:secure>
						</td>
					</tr>
				</stripes:form>
				</c:forEach>
			</table>
		</div>
	</div> 		
</div>
</div>
</body>
</html>
