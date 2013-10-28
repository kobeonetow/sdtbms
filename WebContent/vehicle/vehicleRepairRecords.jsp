<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${actionBean.profile.vid}档案</title>
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

	$('.form1').each(function(){
		$(this).submit(function(){return false;});
	});
    
	$('#btnAdd').click(function(){
		var valid = true;
		$('#addForm input.required').each(function(){
			if($(this).val() == "")
				valid = false;
		});
		if(valid == false){
			alert("所有资料都为必填");
			return;
		}
		var action = $('#addForm').attr('action');
		var params = $('#addForm').serialize() + "&addNewRepairRecord=";
		$.ajax({
			url:action,
			type:'post',
			dateType:'text',
			data:params,
			success:function(response){
				var json = $.parseJSON(response);
				alert(json.msg);
				if(json.result == "1"){
					$('input.emptyByS').each(function(){$(this).val("");});
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
		$('#rdate').val($('.chk:checked').parent().parent().children('td').eq(1).html());
		$('#rtype').val($('.chk:checked').parent().parent().children('td').eq(2).html());
		$('#description').val($('.chk:checked').parent().parent().children('td').eq(3).html());
		$('#rcompany').val($('.chk:checked').parent().parent().children('td').eq(4).html());
		$('#registrant').val($('.chk:checked').parent().parent().children('td').eq(5).html());
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
					var params = $('#editForm').serialize() + "&editVehicleRepairRecord=";
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
		var params = $('#selectForm').serialize() + "&deleteVehicleRepairRecord=";
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
body{
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
.form1{
	border:0px none;
	color:#666;
	padding:0;
	margin:0;
	outline:0px none;
	width:auto;
	height:auto;
	vertical-align:middle;
	}
 #whole{ 
 	border:solid 1px; 
 	color:#666; 
 	padding-top:5px; 
 	height:auto; 
 	width:1145px;
 	overflow:hidden;
 	}
.bar{
	height:50px;
	border:solid 3px;
	font-size:50px;
	}
 .bar span{
 	text-align:center;
 	line-height:50px;
 	height:50px;
    display:block; 
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
	border:solid 1px;
	height:30px;
	font-size: 16px;
	line-height:30px;
	text-align:center;
	vertical-align:middle;
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
	    <td style="background-color:#FF0; color:#F00; font-weight:bolder;">
	    	
	    		<a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id }&vehicleRepairRecord=">车辆维修记录</a>
	   	 	
	   	 </td>
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
    
    <stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" class="form1" id="addForm"> 
    <table width="84%" border="1" cellpadding="0" cellspacing="0" class="kind">
  <tr>
  		<td colspan="5" style="font-size:18px; color:#999;">添加新项目</td>
  </tr>
  <tr>
    <td width="13%">维修日期</td>
    <td width="13%">维修类别</td>
    <td width="45%">小修/二级维护主要附加作业内容/大修/总成修理内容</td>
    <td width="13%">维修单位</td>
    <td width="16%">登记人签名</td>
  </tr>
  <tr>
    <td>
    	
	    	<input type="hidden" name="vRepair.vid.id" value="${actionBean.profile.id }"/>
	    	<stripes:text class="emptyByS required datepickerClass" name="vRepair.rdate" style=" width:90%"  formatPattern="yyyy-MM-dd"/>
    	
    </td>
    <td><stripes:text class="emptyByS required" name="vRepair.rtype" style=" width:90%" maxlength="8"/></td>
    <td><stripes:text class="emptyByS required" name="vRepair.description" style=" width:98%" maxlength="128"/></td>
    <td><stripes:text class="emptyByS required" name="vRepair.rcompany" style=" width:90%" maxlength="64"/></td>
    <td><stripes:text class="emptyByS required" name="vRepair.registrant" style=" width:90%" maxlength="8"/></td>
  </tr>
  	<tr>
      	<td colspan="5"><stripes:submit id="btnAdd" name="addNewRepairRecord" value="添加"/></td>
      </tr>
</table>
  
</stripes:form>


  <table width="84%" border="1" cellpadding="0" cellspacing="0" class="kind">
  <tr>
      <td colspan="6" style="font-size:25px; padding-top:20px;">
      	
        	<strong>车辆维修登记表</strong>
      	
      </td>
   </tr>
    <tr>
  	<td colspan="8" style="font-size:18px;">
  		<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
			<input type="hidden" name="targetId" value="${actionBean.profile.id }"/>
	  		查看：<stripes:text name="date1"  formatPattern="yyyy-MM-dd" class="datepickerClass" />至<stripes:text name="date2"   formatPattern="yyyy-MM-dd" class="datepickerClass" />&nbsp;&nbsp;日期的车辆维修记录
	  		&nbsp;
        	&nbsp;
        	&nbsp;
        	&nbsp;
	  		<stripes:submit name="vehicleRepairRecord" value="确定"/>
  		</stripes:form>
  	</td>
  	</tr>
  <tr>
  	<td width="4%" style="height: 50px;" ><strong>全选</strong><input type="checkbox" id="selectAll" /></td>
    <td width="12%" style="font-size:18px;" ><strong>维修日期</strong></td>
    <td width="12%" style="font-size:18px;" ><strong>维修类别</strong></td>
    <td width="44%" style="font-size:18px;" ><strong>小修/二级维护主要附加作业内容/大修/总成修理内容</strong></td>
    <td width="12%" style="font-size:18px;"><strong>维修单位</strong></td>
    <td width="15%" style="font-size:18px;"><strong>登记人签名</strong></td>
  </tr>
  <stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" class="form1" id="selectForm">
  <c:set var="color" value="0" scope="page"/>
  <c:forEach items="${actionBean.vRepairList }" var="repair" varStatus="loop">
  	<c:choose>
		<c:when test="${color%2 == 0}">
			<tr style="background-color:#FBFBFB;">
		</c:when>
		<c:otherwise>
			<tr>					
		</c:otherwise>
	</c:choose>
    <td><input type="checkbox" class="chk" name="selectList" value="${repair.id}" /></td>
    <td>${repair.rdateStr} </td>
    <td>${repair.rtype}</td>
    <td>${repair.description}</td>
    <td>${repair.rcompany}</td>
    <td>${repair.registrant}</td>
  </tr>
  <c:set var="color" value="${color + 1}" scope="page"/>
  </c:forEach>
  	<tr>
	      <td colspan="6" style="color:#F00; text-align: left !important; line-height:normal;">说明： 1、车辆维修类别栏应填小修、一级维护、二级维护、大修或总成修理.
	      2、说明：主要部件是指客车车身、货车驾驶室和货厢、发动机、离合器、变速器、传动轴、前后桥、转向器、车架等部件。</td>
  	</tr>
	<tr>
        <td colspan="6">
        	
	        	<input type="button" id="btnEdit" value="修改"/>&nbsp;&nbsp;
	        	<input type="button" id="btnDelete" value="删除"/> &nbsp;&nbsp;
	        	<input type="button" id="btnClose" value="关闭"/>
        	
        </td>
    </tr>
    </stripes:form>
</table>

</div>

<div id="editbox">
<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" id="editForm"> 
  <table>
  <tr>
  		<td colspan="5" style="font-size:18px; color:#999;">修改</td>
  </tr>
  <tr>
    <td width="13%">维修日期</td>
    <td width="13%">维修类别</td>
    <td width="45%">小修/二级维护主要附加作业内容/大修/总成修理内容</td>
    <td width="13%">维修单位</td>
    <td width="16%">登记人签名</td>
  </tr>
  <tr>
    <td>
	    	<input type="hidden" id= "id" name="vRepair.id" value=""/>
	    	<input type="text" class="required datepickerClass" id="rdate" name="vRepair.rdate" style=" width:90%"/>
    </td>
    <td><input type="text" class="required" id="rtype" name="vRepair.rtype" style=" width:90%"/></td>
    <td><input type="text" class="required" id="description" name="vRepair.description" style=" width:98%"/></td>
    <td><input type="text" class="required" id="rcompany" name="vRepair.rcompany" style=" width:90%"/></td>
    <td><input type="text" class="required" id="registrant" name="vRepair.registrant" style=" width:90%"/></td>
  </tr>
</table>
</stripes:form>
</div>
</body>
</html>
