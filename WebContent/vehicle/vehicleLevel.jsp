<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${actionBean.profile.vid }档案</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/datepickerChinese.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.dialog.js"></script>
<link href="${pageContext.request.contextPath}/css/themes/black_rose/ui.css" rel="stylesheet" title="style" media="all" />
<link href="${pageContext.request.contextPath}/css/ui/ui.base.css" rel="stylesheet" media="all" />
<link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />

<script type="text/javascript">
$(document).ready(function(){

	$(".datepickerClass").datepicker({
    	changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd' 
    });
    
    $('#btnClose').click(function(){
		window.close();
	});	

	$('.notSubmit').each(function(){
		$(this).submit(function(){return false;});
	});

	$('#btnAdd').click(function(){
		var submit = true;
		$('#addForm input.required').each(function(){
			if($(this).val() == "")
				submit = false;
		});
		if(!submit){
			alert("所有信息都为必填");
			return false;
		}
		var action = $('#addForm').attr('action');
		var params = $('#addForm').serialize() + "&addVehicleLevelCheck=";
		$.ajax({
			url:action,
			data:params,
			dataType:'text',
			type:'post',
			success:function(response){
				var json = $.parseJSON(response);
				alert(json.msg);
				if(json.result == "1"){
					$('#addForm input[type=text]').each(function(){$(this).val("");});
					location.reload();
				}
			}
		});	
	});

	$("#btnEdit").click(function(){
		var len = $('.chk:checked').length;
		if(len != 1){
			alert("请选上1个记录，一次只能同时修改1个记录");
			return;
		}
		$('#id').val($('.chk:checked').val());
		$('#date').val($('.chk:checked').parent().parent().children('td').eq(1).html());
		$('#distance').val($('.chk:checked').parent().parent().children('td').eq(2).html());
		$('#description').val($('.chk:checked').parent().parent().children('td').eq(3).html());
		$('#techlevel').val($('.chk:checked').parent().parent().children('td').eq(4).html());
		$('#carlevel').val($('.chk:checked').parent().parent().children('td').eq(5).html());
		$('#company').val($('.chk:checked').parent().parent().children('td').eq(6).html());
		$('#registrant').val($('.chk:checked').parent().parent().children('td').eq(7).html());
		$('#editbox').dialog('open');
	});
	
	$("#editbox").dialog({
		autoOpen: false,
		bgiframe: true,
		resizable: true,
		height:150,
		width:650,
		modal: true,
		overlay: {
			backgroundColor: '#000',
			opacity: 0.5
		},
		buttons: {
			'修改':function(){
					var action = $('#editForm').attr('action');
					var params = $('#editForm').serialize() + "&editVehicleLevel=";
					$.ajax({
						url:action,
						type:"post",
						dataType:'text',
						data:params,
						success:function(response){
							var json = $.parseJSON(response);
							alert(json.msg);
							if(json.result == "1"){
								$("#editbox").dialog('close');
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

	$('#btnDelete').click(function(){
		var len = $('.chk:checked').length;
		if(len < 1){
			alert("请选择要删除的记录");
			return;
		}
		var action = $('#selectForm').attr('action');
		var params = $('#selectForm').serialize() + "&deleteVehicleLevel=";
		$.ajax({
			url:action,
			type:'post',
			dateType:'text',
			data:params,
			success:function(response){
				var json = $.parseJSON(response);
				alert(json.msg);
				if(json.result == "1"){
					location.reload();
				}
			}
		});
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
#form1{
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
 	width:1145px;
 	overflow:hidden;
 	}
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
.bat{
	text-align:center;
	border:solid 1px;
	float:left;
	text-align:center;
	font-size:16px;
	}
.bat td{
	text-align:center !important;
	vertical-align:middle;
	border:solid 2px;
	height:50px;
	outline:0 none;
	font-family:inherit;
	font-weight:inherit;
	font-style:inherit;
	margin: auto;
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
	text-align:center !important;
	vertical-align:middle;
	border:solid 1px;
	height:30px;
	line-height:30px;
	font-size:16px;
	}
table>tr>td{
	text-align:center !important;
	vertical-align:middle !important;
}
</style>
</head>

<body>
<div id="whole">
	
<table border="1" cellpadding="0" cellspacing="0" class="bar" width="100%">
      <tr>
         <td>
        	<span>
    				车牌号：
	    			${actionBean.profile.vid}
	    	</span>
    	</td>
       </tr>
    </table>
    
    <table width="16%" border="1" cellpadding="0" cellspacing="0" class="bat">
	  <tr>
	    <td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleDetail=">车辆基本情况</a></td>
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
	    <td style="background-color:#FF0; color:#F00; font-weight:bolder;"><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleLevel=">车辆等级评定</a></td>
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

	<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" class="notSubmit"  id="addForm">
  <table width="84%" border="1" cellpadding="0" cellspacing="0" class="kind">
    <tr>
  <td colspan="7" style="font-size:18px;"><strong>添加新项目</strong></td>
  </tr>
  <tr>
  	<td width="12%">检测评定日期</td>
    <td width="8%">行驶里程记录</td>
    <td width="20%">其他检测/二级维护竣工质量检测</td>
    <td width="12%">车辆技术等级</td>
    <td width="28%">客车类型及等级、型号（规格）及厂名</td>
    <td width="8%">检测评定单位</td>
    <td width="10%">登记人签名</td>
  </tr>
  <tr class="char">   
    <td>
    	<input type="hidden" name="vLevel.vid.id" value="${actionBean.profile.id }"/>
    	<stripes:text name="vLevel.date"  formatPattern="yyyy-MM-dd" class="datepickerClass" style="width:80%"/>
    </td>
    <td><stripes:text name="vLevel.distance" style="width:80%"/></td>
    <td><stripes:text name="vLevel.description" style="width:95%" maxlength="128"/></td>
    <td><stripes:text name="vLevel.techlevel" style="width:90%" maxlength="8"/></td>
    <td><stripes:text name="vLevel.carlevel" style="width:95%" maxlength="8"/></td>
    <td><stripes:text name="vLevel.company" style="width:90%" maxlength="64"/></td>
    <td><stripes:text name="vLevel.registrant" style="width:80%" maxlength="8"/></td>
  </tr>
  <tr>
      	<td colspan="7" >
      		<input id="btnAdd" type="button" value="添加"/> 
      		</td>
  </tr>
</table>
</stripes:form>
	
  <table width="84%" border="1" cellpadding="0" cellspacing="0" class="kind">
      <tr>
      <td colspan="8" style="font-size:25px; padding-top:20px;">
        <strong>车辆等级评定登记表</strong>
      </td>
    </tr>
    <tr>
  <td colspan="8" style="font-size:18px;">
  	<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" >
  		<input type="hidden" name="targetId" value="${actionBean.profile.id }"/>
  		查看：<stripes:text name="date1"  formatPattern="yyyy-MM-dd" class="datepickerClass" />至<stripes:text name="date2"  formatPattern="yyyy-MM-dd" class="datepickerClass" />&nbsp;&nbsp;日期的车辆等级评定记录
        &nbsp;
        &nbsp;
        &nbsp;
        &nbsp;
  		<stripes:submit name="vehicleLevel"/>
  	</stripes:form>
  </td>
  </tr>
  
  <tr>
  	  <td width="2%" style="font-size:18px;"><strong>全选</strong><input type="checkbox" id="selectAll" /></td>
  	<td width="12%" style="font-size:18px;"><strong>检测评定日期</strong></td>
    <td width="8%" style="font-size:18px;"><strong>行驶里程记录</strong></td>
    <td width="20%" style="font-size:18px;"><strong>其他检测/二级维护竣工质量检测</strong></td>
    <td width="12%" style="font-size:18px;"><strong>车辆技术等级</strong></td>
    <td width="28%" style="font-size:18px;"><strong>客车类型及等级、型号（规格）及厂名</strong></td>
    <td width="8%" style="font-size:18px;"><strong>检测评定单位</strong></td>
    <td width="10%" style="font-size:18px;"><strong>登记人签名</strong></td>
  </tr>
  
  <stripes:form id="selectForm" beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean"  class="notSubmit">
  <c:set var="color" value="0" scope="page"/>
  <c:forEach items="${actionBean.vLevelList }" var="list" varStatus="loop">
  	<c:choose>
		<c:when test="${color%2 == 0}">
			<tr style="background-color:#FBFBFB;">
		</c:when>
		<c:otherwise>
			<tr>					
		</c:otherwise>
	</c:choose>
  	<td><input type="checkbox" class="chk" name="selectList" value="${list.id }"/></td>   
    <td>${list.dateStr }</td>
    <td>${list.distance }</td>
    <td>${list.description }</td>
    <td>${list.techlevel }</td>
    <td>${list.carlevel }</td>
    <td>${list.company }</td>
    <td>${list.registrant }</td>
   </tr>
  <c:set var="color" value="${color + 1}" scope="page"/>
  </c:forEach>
  <tr>
	<td colspan="8" style="color:#F00">说明：其他检测是指年度安全检测及各种监督检测</td>
  </tr>
  <tr>
    <td colspan="8">
    	<input id="btnEdit" type="button" value="修改"/>&nbsp;&nbsp;
    	<input id="btnDelete" type="button" value="删除"/>&nbsp;&nbsp;
    	<input id="btnClose" type="button" value="关闭"/>
    </td>
   </tr>
   </stripes:form>
</table>

</div>

<div id="editbox">
<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" id="editForm"> 
  <table>
  <tr>
  	<td width="12%">检测评定日期</td>
    <td width="8%">行驶里程记录</td>
    <td width="20%">其他检测/二级维护竣工质量检测</td>
    <td width="12%">车辆技术等级</td>
    <td width="28%">客车类型及等级、型号（规格）及厂名</td>
    <td width="8%">检测评定单位</td>
    <td width="10%">登记人签名</td>
  </tr>
  <tr class="char">   
    <td>
    	<input type="hidden" id="id" name="vLevel.id" value=""/>
    	<stripes:text name="vLevel.date"  id="date" formatPattern="yyyy-MM-dd" class="datepickerClass" style="width:80%"/>
    </td>
    <td><stripes:text name="vLevel.distance" id="distance" style="width:80%"/></td>
    <td><stripes:text name="vLevel.description" id="description" style="width:95%"/></td>
    <td><stripes:text name="vLevel.techlevel" id="techlevel" style="width:90%"/></td>
    <td><stripes:text name="vLevel.carlevel" id="carlevel" style="width:95%"/></td>
    <td><stripes:text name="vLevel.company" id="company" style="width:90%"/></td>
    <td><stripes:text name="vLevel.registrant" id="registrant" style="width:80%"/></td>
  </tr>
</table>
</stripes:form>
</div>

</body>
</html>
