<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${actionBean.vtech.vid.vid }档案</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>
<link href="${pageContext.request.contextPath}/css/themes/black_rose/ui.css" rel="stylesheet" title="style" media="all" />
<link href="${pageContext.request.contextPath}/css/ui/ui.base.css" rel="stylesheet" media="all" />
<link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />

<script type="text/javascript">
$(document).ready(function(){
	$('#vehicleTechForm').submit(function(){return false;});

	$(".datepickerClass").datepicker({
    	changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd' 
    });
    
	$('#btnEdit').click(function(){
		var formaction = $('#vehicleTechForm').attr('action');
		var params = $('#vehicleTechForm').serialize() + "&saveVehicleTechDetail=";
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
.kind{
	border:solid 1px;
	float:right;
	}
.kind td{
	border:solid 1px;
	height:30px;
	font-size: 16px;
	line-height:30px;
	} 
</style>
</head>

<body>
<div id="whole">
<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" id="vehicleTechForm">

	<table border="1" cellpadding="0" cellspacing="0" class="bar" width="100%">
      <tr>
        <td colspan=15>
        	  <span>
    				车牌号:
	    			${actionBean.vtech.vid.vid }
	    			<input type="hidden" name="targetId" value="${actionBean.vtech.vid.id }"/>
	    			<input type="hidden" name="vtech.vid.id" value="${actionBean.vtech.vid.id }"/>
	    			<input type="hidden" name="vtech.id" value="${actionBean.vtech.id }"/>
	    	  </span>
    		</td>
       </tr>
    </table>
    
    <table width="16%" border="1" cellpadding="0" cellspacing="0" class="bat">	
      <tr>
          <td ><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleDetail=">车辆基本情况</a></td>
      </tr>
	  <tr>
	    <td style="background-color:#FF0; color:#F00; font-weight:bolder;"><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleTechDetail=">车辆技术参数</a></td>
      </tr>
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleRepairRecord=">车辆维修记录</a></td>
      </tr>
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehiclePartsRepair=">车辆主要总成部件更换</a></td>
      </tr>
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehiclePartsRepair=">车辆等级评定</a></td>
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

  <table width="84%" border="1" cellpadding="0" cellspacing="0" class="kind">
    <tr>
      <td>车辆类型:</td>
      <td><stripes:text name="vtech.vtype" maxlength="8"/></td>
      <td>厂牌型号:</td>
      <td><stripes:text name="vtech.factorycode" maxlength="20"/></td>
      <td>出厂日期:<br/>产地:</td>
      <td>
      		<stripes:text name="vtech.factorydate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/>
      		<br/>
      		<stripes:select name="vtech.madein">
      			<stripes:option value="">请选择</stripes:option>
      			<stripes:option value="国产">国产</stripes:option>
      			<stripes:option value="进口">进口</stripes:option>
      		</stripes:select>
      </td>
    </tr>
    <tr>
      <td>VIN（或车架）号:</td>
      <td><stripes:text name="vtech.vincode" maxlength="32"/></td>
      <td>底盘厂牌型号:</td>
      <td><stripes:text name="vtech.basecode" maxlength="32"/></td>
      <td>客车类型等级:</td>
      <td><stripes:text name="vtech.vlevel" maxlength="8"/></td>
    </tr>
    <tr>
      <td>车辆外廓尺寸:</td>
      <td style=" border-right:0 none;">长:<stripes:text name="vtech.vlength" style="width:48px;" maxlength="8"/>
         宽:<stripes:text name="vtech.vwidth" style="width:48px;" maxlength="8"/>
         高:<stripes:text name="vtech.vheight" style="width:48px;" maxlength="8"/></td>
      <td>总质量:</td>
      <td><stripes:text name="vtech.weight" maxlength="8"/></td>
      <td>座/铺位排列:</td>
      <td><stripes:text name="vtech.sitarrange" maxlength="32"/></td>
    </tr>
    <tr>
      <td>核定载选购量/乘员数:</td>
      <td><stripes:text name="vtech.sits" maxlength="32"/></td>
      <td>核定牵引总质量:</td>
      <td><stripes:text name="vtech.dragweight" maxlength="8"/></td>
      <td>车轴数/驱动轴数:</td>
      <td><stripes:text name="vtech.vaxis" maxlength="8"/></td>
    </tr>
    <tr>
      <td>发动机厂牌型号:</td>
      <td><stripes:text name="vtech.enginemodel" maxlength="32"/></td>
      <td>发动机号码:</td>
      <td><stripes:text name="vtech.enginecode" maxlength="32"/></td>
      <td>燃料种类:</td>
      <td><stripes:text name="vtech.fueltype" maxlength="8"/></td>
    </tr>
    <tr>
      <td>发动机功率:</td>
      <td><stripes:text name="vtech.enginepower" maxlength="16"/></td>
      <td>发动机排量:</td>
      <td><stripes:text name="vtech.enginehorse" maxlength="16"/></td>
      <td>排放标准:</td>
      <td><stripes:select name="vtech.releasestandard">
        <stripes:option  value="">请选择...</stripes:option>
        <stripes:option  value="国Ⅱ">国Ⅱ</stripes:option>
        <stripes:option value="国Ⅲ">国Ⅲ</stripes:option>
        <stripes:option value="国Ⅳ">国Ⅳ</stripes:option>
        <stripes:option value="国Ⅴ">国Ⅴ</stripes:option>
        </stripes:select></td>
    </tr>
    <tr>
      <td>驱动形式:</td>
      <td><stripes:text name="vtech.drivermode" maxlength="8"/></td>
      <td>轮胎数/规格:</td>
      <td><stripes:text name="vtech.tyre" maxlength="16"/></td>
      <td>前照灯制式:</td>
      <td><stripes:text name="vtech.frontlight" maxlength="16"/></td>
    </tr>
    <tr>
      <td>变速器形式:</td>
      <td><stripes:select name="vtech.variatormode">
        <stripes:option value="">请选择...</stripes:option>
        <stripes:option value="自动">自动</stripes:option>
        <stripes:option value="手动">手动</stripes:option>
        <stripes:option value="手自动一体化">手自动一体化</stripes:option>
        </stripes:select></td>
      <td>缓速器:</td>
      <td><stripes:select name="vtech.retarder">
        <stripes:option value="">请选择...</stripes:option>
        <stripes:option value="电磁式">电磁式</stripes:option>
        <stripes:option value="液力式">液力式</stripes:option>
        </stripes:select></td>
      <td>转向器:</td>
      <td><stripes:select name="vtech.turner">
        <stripes:option value="">请选择...</stripes:option>
        <stripes:option value="动力转向">动力转向</stripes:option>
        <stripes:option value="非动力转向">非动力转向</stripes:option>
        </stripes:select></td>
    </tr>
    <tr>
      <td>行车制动形式:</td>
      <td colspan="5" style="text-align:center;">
      <stripes:radio value="气" checked="${actionBean.vtech.breakmode }" name="vtech.breakmode"/><label>气</label>
      <stripes:radio value="液" checked="${actionBean.vtech.breakmode }" name="vtech.breakmode"/><label>液</label>
      <stripes:radio value="气－液" checked="${actionBean.vtech.breakmode }" name="vtech.breakmode"/><label>气－液</label>
       &nbsp;
       &nbsp;
       &nbsp;
       &nbsp;
      <strong>前轮:</strong>
      <stripes:checkbox checked="${actionBean.vtech.frontbreakList }" value="气囊" name="vtech.frontbreakList"/><label>气囊</label>
      <stripes:checkbox checked="${actionBean.vtech.frontbreakList }" value="片板簧" name="vtech.frontbreakList"/><label>片板簧</label>
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      <p></p>          
      <strong>后轮:</strong>
      <stripes:checkbox checked="${actionBean.vtech.backbreakList }" value="盘式" name="vtech.backbreakList"/><label>盘式</label>
       <stripes:checkbox checked="${actionBean.vtech.backbreakList }" value="鼓式" name="vtech.backbreakList"/><label>鼓式</label>
       <stripes:checkbox checked="${actionBean.vtech.backbreakList }" value="防抱死装置" name="vtech.backbreakList"/><label>防抱死装置</label>
       <stripes:checkbox checked="${actionBean.vtech.backbreakList }" value="蹄片间隙自调" name="vtech.backbreakList"/><label>蹄片间隙自调</label>
       <stripes:checkbox checked="${actionBean.vtech.backbreakList }" value="单回路" name="vtech.backbreakList"/><label>单回路</label>
       <stripes:checkbox checked="${actionBean.vtech.backbreakList }" value="双回路" name="vtech.backbreakList"/><label>双回路</label>
        </td>
      </tr>
    <tr>
      <td>悬持形式:</td>
      <td colspan="5" style="text-align:center;">
      	前轮:
      <stripes:checkbox checked="${actionBean.vtech.hangmodefrontList }" value="独立" name="vtech.hangmodefrontList"/><label>独立</label>
      <stripes:checkbox checked="${actionBean.vtech.hangmodefrontList }" value="非独立" name="vtech.hangmodefrontList"/><label>非独立</label>
      &nbsp;
      &nbsp;
      <stripes:checkbox checked="${actionBean.vtech.hangmodefrontList }" value="气囊" name="vtech.hangmodefrontList"/><label>气囊</label>
      <stripes:checkbox checked="${actionBean.vtech.hangmodefrontList }" value="片板簧" name="vtech.hangmodefrontList"/><label>片板簧</label>
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
     	 后轮:
      <stripes:checkbox checked="${actionBean.vtech.hangmodebackList }" value="独立" name="vtech.hangmodebackList"/><label>独立</label>
      <stripes:checkbox checked="${actionBean.vtech.hangmodebackList }" value="非独立" name="vtech.hangmodebackList"/><label>非独立</label>
      &nbsp;
      &nbsp;
      <stripes:checkbox checked="${actionBean.vtech.hangmodebackList }" value="气囊" name="vtech.hangmodebackList"/><label>气囊</label>
      <stripes:checkbox checked="${actionBean.vtech.hangmodebackList }" value="片板簧" name="vtech.hangmodebackList"/><label>片板簧</label>
      </td>
      </tr>
    <tr>
      <td>其他配置:</td>
      <td colspan="5" style="text-align:center;">
      
      <stripes:checkbox checked="${actionBean.vtech.otherList }" value="底盘自动润滑" name="vtech.otherList"/><label>底盘自动润滑</label>
      <stripes:checkbox checked="${actionBean.vtech.otherList }" value="GPS" name="vtech.otherList"/><label>GPS</label>
      <stripes:checkbox checked="${actionBean.vtech.otherList }" value="行车记录仪" name="vtech.otherList"/><label>行车记录仪</label>
      <stripes:checkbox checked="${actionBean.vtech.otherList }" value="空调器" name="vtech.otherList"/><label>空调器</label>
      </td>
      </tr>
	  <tr>
	      <td colspan="6" style="color:#F00">说明:请填写或选择车辆技术各参数中有关内容，符合的请在选择项后以&quot;√&quot;表示。</td>
	  </tr>
      <tr>
      <td colspan="6">
      	<stripes:submit id="btnEdit" name="saveVehicleTechDetail" value="保存修改"/> 
      	<button id="btnClose">关闭</button>
      </td>
      </tr>
  </table>
  
</stripes:form>
</div>
</body>
</html>
