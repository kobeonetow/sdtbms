<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/score.js"></script>
    <link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />
    
    <style type="text/css">
    	.readonly{
    		background-color:#CCCCBB;
    	}
    	.button {
    font: bold 11px Arial;
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
    <script type="text/javascript">
    	function openSelectEmpWindow(names,workerids,extras,multi){
    		var link="";
        	if(multi == true)
        		link = "${pageContext.request.contextPath}/actionbean/EmployeeSelector.action?score=yes&eleIdOne="+names+"&eleIdTwo="+workerids+"&extra="+extras+"&multi="+multi;
        	else
        		link = "${pageContext.request.contextPath}/actionbean/EmployeeSelector.action?score=yes&eleIdOne="+names+"&eleIdTwo="+workerids+"&extra="+extras;
			window.open(link,"_blank","fullscreen=no,scrollbars=1,width=400,height=600");
        }
        $(document).ready(function(){
        	$('#selectAll').click(function(){
				if(this.checked){
					$('#dataForm :checkbox').each(function(){
						this.checked = true;
					});
				}else{
					$('#dataForm :checkbox').each(function(){
						this.checked = false;
					});
				}
			});

			$('#givescores').click(function(){
				if(!isSelection())
					return;
				if(!isScorerSelected())
					return;
				var checkedBox = $('#dataForm input:checkbox:checked');

				if(checkZeroScores(checkedBox)){
					$('#setScoreDialog').dialog('open');
				}else{
					giveScores();
				}
			});
// 			$('#itemlist').change(function(){
// 				$('#filter').click();
// 			});
			function giveScores(){
				var ele = $('#givescores');
				$(ele).hide();					
				var url = $(ele).children().first().val();
				var serialize  = $('#dataForm').serialize();
// 				alert(url);
				
				$.ajax({
					url:url,
					type:"post",
					dataType:"text",
					data:serialize,
					success:function(response){
						var jobj = $.parseJSON(response);
						clearCheckBoxesAndReceivers();
						alert(jobj.msg);
						$('#givescores').show();
					}
				});
				setTimeout(function(){
					$('#givescores').show();
				},3000);
			}
			
			$("#setScoreDialog").dialog({
				autoOpen: false,
				bgiframe: true,
				resizable: true,
// 				height:100,
				width:400,
				modal: true,
				overlay: {
					backgroundColor: '#000',
					opacity: 0.5
				},
				buttons: {
					'确定':function(){
						var valOk = true;
						$(this).find(":input[type=text]").each(function(){
							var temScore = $.trim($(this).val())
							if(temScore == ""){
								valOk = false;
								alert("null");
							}else if(parseFloat(temScore) <= 0 || parseFloat(temScore) >= 1000){
								valOk = false;
								alert("值不能小于或等于0,或值过大");
							}else if($(this).next().val() == "绩效"){
								alert("绩效分");
								var s = parseFloat(temScore);
								if(s > 100.0){
									valOk = false;
									alert("分值不能大于100");
								}
							}
						});
						if(!valOk)
							return false;
						$(this).find("ul li>input").each(function(){
							var score  = $(this).val();
							var name = $(this).attr('name');
							$('#dataForm input[name="'+name+'"]').val(score);
						});
						giveScores();
						$(this).dialog('close');
					},
					'取消': function() {
						$(this).dialog('close');
					}
				}
			});
        });
        function clearCheckBoxesAndReceivers(){
			$("#dataForm input:checked").each(function(){
				$(this).click();
			});
			$('#employeenamefromid2').val("");
			$('#checkWorkerId2').val("");
		}
        function isSelection(){
			var count = $('#dataForm input:checkbox:checked').length;
			if(count <= 0){
				alert("未选上任何项目");
				return false;
			}
			return true;
		}
		function isScorerSelected(){
			var name = $('#employeenamefromid2').val();
			var wids = $('#checkWorkerId2').val();
			if(name == "" || wids == ""){
				alert("请选择受分人");
				return false;
			}
			return true;
		}
		function checkZeroScores(checked){
			var htmlstr = "";
			$(checked).each(function(){
				var types = $(this).parent().parent().children(":nth-child(3)").html();
				var words = $(this).parent().parent().children(":nth-child(4)").html().split("<input");
// 				alert($(this).parent().parent().children(":nth-child(4)").html());
// 				alert(words[0]);
				var item = $(this).parent().parent().children(":nth-child(5)").html();
// 				var scoreValue = $(this).parent().parent().children(":nth-child(4)").children().first().val();
				var scoreValue = words[0];
				var valueName = $(this).parent().parent().children(":nth-child(4)").children().first().attr('name');
				if(parseFloat(scoreValue) == 0){
					htmlstr += "<li>条例:"+item+"<br/>分值:<input type='text' name='"+valueName+"'/><input type='hidden' value='"+types+"'/></li><br/>";
				}
			});
			if(htmlstr != ""){
				$('#setScoreDialog').html("<ul>"+htmlstr+"</ul>");
				return true;
			}else{
				return false;
			}
		}
    </script>
    
		<div id="sub-nav"><div class="page-title">
			<h1>积分主页</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				<!-- 新建条例  Dialog-->
				<div id="items_top_button_bar" style="height:40px;">&nbsp;  
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				
				<ss:secure roles="score_items_create">
				<a id="btn_new_item_link" class="btn ui-state-default ui-corner-all" href="#">
						<span class="ui-icon ui-icon-person"></span>
						新建条例
				</a>
				
				<div id="btn_new_item_dialog" title="新建条例">
					<stripes:form id="form_new_item" beanclass="com.bus.stripes.actionbean.score.ScoreitemsActionBean">
						<div class='hastable'>
						<table>
								<tr>
									<td>编号:</td><td><stripes:text name="scoretype.reference" class="required"/></td>
								</tr>
								<tr>
									<td>类型:</td><td><stripes:select name="scoretype.type"  class="required"><stripes:option value="0">临时分</stripes:option><stripes:option value="1">固定分</stripes:option><stripes:option value="2">绩效分</stripes:option><stripes:option value="3">项目分</stripes:option></stripes:select></td>
								</tr>
								<tr>
									<td>注释:</td><td><stripes:textarea name="scoretype.description"  class="required"/></td>
								</tr>
								<tr>	
									<td>分值:</td><td><stripes:text name="scoretype.score"  class="required" formatType="number" formatPattern="integer"></stripes:text></td>
								</tr>
						</table>
						</div>
					</stripes:form>
				</div>
				</ss:secure>
				</div>
				<hr/>
				
				<!-- Show items and pages options -->
			<div>
				<div class="hastable">
						<div class="inner-page-title">
						条例信息
						</div>
				<stripes:form id='dataForm' beanclass="com.bus.stripes.actionbean.score.ScoreitemsActionBean">
				<table>
					<thead>
					
						<tr>
							<td colspan=9 style="text-align:left">
								<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/> <stripes:hidden name="totalcount"/>  <stripes:submit name="nextpage" value="下页"/>
								显示数量:<stripes:text name="lotsize"/>
								<stripes:submit id='filter' name="filter" value="筛选"/>
							</td>
						</tr>
						<tr>
							<td colspan=9 style="text-align:left">
								<ss:secure roles="score_items_givescore">
								<div>
									<Label class='selector'>给分人:</Label>
									<br/>
									&nbsp;&nbsp;&nbsp;&nbsp;名称:<stripes:text readonly="true" name="employee.fullname" style="background-color:#CCCCBB;"  id="employeenamefromid1"/>工号:<stripes:text name="employee.workerid" readonly="true" id="checkWorkerId1"/>
<%-- 									<a href="javascript:void;" id="checkWorkerId">(查)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?checkworkerid="/>| --%>
<%-- 									<a href="javascript:void;" id="getNameById1">获取</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?getnamebyid="/>| --%>
									<input type="hidden" value="" id="extra1"/>
<!-- 									<a href="javascript:void;" onclick="openSelectEmpWindow('employeenamefromid1','checkWorkerId1','extra1',false)">从列表选择</a> -->
								</div>
								<div>
									<Label class='selector'>受分人:</Label>
									<br/>
									&nbsp;&nbsp;&nbsp;&nbsp;名称:<stripes:text style="background-color:#CCCCBB;width:50%;" readonly="readonly" name="receivers" id="employeenamefromid2"/>
									<br/>
									&nbsp;&nbsp;&nbsp;&nbsp;工号:<stripes:text name="receiverWorkerids" id="checkWorkerId2"/><a href="javascript:void;" id="checkWorkerId">(查)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?checkworkerid="/>|
									<a href="javascript:void;" id="getNameById2">获取</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?getnamebyid="/>|
									<input type="hidden" value="" id="extra2"/><a href="javascript:void;" onclick="openSelectEmpWindow('employeenamefromid2','checkWorkerId2','extra2',true)">从列表选择</a>
								</div>
<!-- 								<div> -->
<%-- 									<Label class='selector'>自拟定分值:</Label><stripes:text name="score"/><span style="font-size:9pt;color:#CCCCCC;">(0为默认)</span> --%>
<!-- 								</div> -->
								<div>
									<Label class='selector'>日期:</Label><stripes:text name="scoredate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
								</div>
								<div>
									<a href="javascript:void;" id="givescores" class="button">
									打分
									<input type="hidden" value="${pageContext.request.contextPath}/actionbean/Scoreitems.action?givescores="/>
									</a>
								</div>
								</ss:secure>
								
							</td>
						</tr>
						<tr>
							<td colspan=9 style="text-align:left">
								<Label class='selector'>编号:</Label><stripes:text name="selector.reference"/>
								<Label class='selector'>类型:</Label><stripes:select name="selector.type"><stripes:option value="">不限</stripes:option><stripes:option value="0">临时分</stripes:option><stripes:option value="1">固定分</stripes:option><stripes:option value="2">绩效分</stripes:option><stripes:option value="3">项目分</stripes:option></stripes:select>
								<Label class='selector'>条例单:</Label><stripes:select id='itemlist' name="itemlist"><stripes:option value=""></stripes:option>不限<stripes:options-collection collection="${actionBean.sheetList}" label="name" value="id"/></stripes:select>
								<stripes:submit id='filter' name="filter" value="筛选"/>
								<br/>
								<ss:secure roles="score_items_edit">
									<stripes:submit name="deletescoretype" value="删除"/>
								</ss:secure>
							<ss:secure roles="score_sheet_add_st">
									<stripes:submit name="assignToScoreSheet" value="添加到积分表单"/>
								</ss:secure>
									
							</td>
						</tr>
						<tr>
							<th>全选<input type="checkbox" id="selectAll"/></th>
							<th>编号</th>
							<th>类型</th>
							<th>分值</th>
							<th style="width:300px !important;">条例</th>
							<th>周期</th>
							<th>奖分对象</th>
							<th>考核单位</th>
							<th style="width:300px !important;">备注</th>
						</tr>
					</thead>
					<tbody>
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.scoretypes}" var="st" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<input type="checkbox" name="selectedScoreTypes[${color}].id" value="${st.id}"/>
								<ss:secure roles="score_items_edit">
										<a target="_blank" href="${pageContext.request.contextPath}/actionbean/Scoreitems.action?editscoretype=&targetId=${st.id}">修改</a>
								</ss:secure>
							</td>
							<td>${st.reference}</td>
							<td>${st.typestr}</td>
							<td>${st.score}<input type="hidden" name="selectedScores[${color}]" value="${st.score}"/></td>
							<td>${st.description}</td>
							<td>${st.period}</td>
							<td>${st.scoreobject}</td>
							<td>${st.examine}</td>
							<td>${st.remark}</td>
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
					</tbody>
				</table>
				</stripes:form>
				</div>
			</div>
				
				
				</div>
				<div id="setScoreDialog">
					
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>