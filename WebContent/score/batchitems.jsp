<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/hr.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		if(0 != ${actionBean.success}){
			alert("${actionBean.msg}");
		}
		$('.excelFileSubmit').submit(function(){
			var fileName = $(this).find('input:file').val();
			var ext = fileName.split('.').pop().toLowerCase();
			if($.inArray(ext, ['xls']) == -1 && $.inArray(ext, ['xlsx']) == -1) {
		    	alert('得分文件不是合法的excel文件,请确保后缀为xls或xlsx');
		    	return false;
			}
			return true;
		});
	});
	</script>
	<style type="text/css">
		strong{
			font-weight:bold;
		}
		.uploadBlock{
			display:block;
			height:50px;
			font-size:13pt;
			padding:8px;
			border:1px 1px 1px 1px;
			border-color: black;
		}
	</style>
		<div id="sub-nav"><div class="page-title">
			<h1>批量上传</h1>
			<span><a href="#" title="Layout Options">积分系统</a> > <a href="#" title="Two column layout">批量上传</span>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				
			
				<!--  File Upload -->
				<div class="clear"></div>
				<div>
				文件上传
				<stripes:form id="file_submit_form" beanclass="com.bus.stripes.actionbean.score.ScoreFileUploadActionBean">
					
					<ss:secure roles="score_fileupload_uploaditems">
					<div class="uploadBlock">
					
					<Label>条例文件(excel):</Label><stripes:file name="itemsfile" id="file_scoreitem" />
									
											<stripes:submit name="itemsupload" value="提交"/>
					</div>
					</ss:secure>
					<br/>
									<ss:secure roles="score_fileupload_uploadscores">
					<div class="uploadBlock">
					<Label style="color:red;"><strong>普通</strong>打分文件(excel文件):</Label><stripes:file name="scorefile"  id="file_score" />
					
											<stripes:submit name="scoreupload" value="提交"/>
					</div>
									</ss:secure>
					<br/>
					<script type="text/javascript">
					$("#file_submit_form").submit(function(){
						var employee_csv =$('#file_scoreitem').val().trim();
						var employee_check = $('#file_score').val().trim();
						if(employee_csv != ""){
							var ext = employee_csv.split('.').pop().toLowerCase();
							if($.inArray(ext, ['xls']) == -1 && $.inArray(ext, ['xlsx']) == -1) {
						    	alert('条例文件不是合法的excel文件');
						    	return false;
							}
						}else if(employee_check != ""){
							var ext = employee_check.split('.').pop().toLowerCase();
							if($.inArray(ext, ['xls']) == -1 && $.inArray(ext, ['xlsx']) == -1) {
						    	alert('得分文件不是合法的excel文件,请确保后缀为xls或xlsx');
						    	return false;
							}
						}
						return true;
					});
					</script>
				</stripes:form>
				
				<ss:secure roles="score_fileupload_uploadscores">
				<div class="uploadBlock">
					<stripes:form class="excelFileSubmit" beanclass="com.bus.stripes.actionbean.score.ScoreFileUploadActionBean">
						<Label style="color:red"><strong>驾驶员事故违规</strong>打分文件(excel文件):</Label>
						<stripes:file name="scorefile" />
						<stripes:submit name="driverScoreFileUpload" value="提交"/>
						<br/>
					</stripes:form>
				</div>
				</ss:secure>
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>