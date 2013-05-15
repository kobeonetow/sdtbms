<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>证件管理</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/superfish.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/live_search.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/cookie.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.sortable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.tabs.js"></script>

<link href="${pageContext.request.contextPath}/css/ui/ui.base.css" rel="stylesheet" media="all" />

<link href="${pageContext.request.contextPath}/css/themes/black_rose/ui.css" rel="stylesheet" title="style"
	media="all" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script>   



<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
$(document).ready(function(){
    $(".datepickerClass").datepicker({
    	changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd' 
    });

});

</script>


<style type="text/css">
.centertext{
text-align:center;
}
</style>

</head>
<body>

	<div class="hastable">
		<div><Label>${actionBean.idcards[0].employee.fullname}</labeL></div>
		<table>
			<thead>
						<tr>
						<th></th>
						<th>Id</th>
						<th>类型</th>
						<th>号码</th>
						<th>有效期</th>
						<th>失效日期</th>
						<th>图片</th>
						<th>注释</th>
						</tr>
					</thead>
					<tbody>
					
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.idcards}" var="card" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<stripes:form beanclass="com.bus.stripes.actionbean.IDCardsActionBean">
									<div>
										<stripes:hidden name="idcard.id" value="${card.id}"/>
										<stripes:hidden name="targetId"/>
										<stripes:submit name="delete" value="删除"/>
									</div>
								</stripes:form>
							</td>
							<td>
								${card.id}
							</td>
							<td>${card.type}</td>
							<td>${card.number}</td>
							<td>${card.validfromstr}</td>
							<td>${card.expiredatestr}</td>
							<td>${card.image}</td>
							<td>${card.remark}</td>
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
					</tbody>
				</table>
	</div>
	<div>
		<stripes:form beanclass="com.bus.stripes.actionbean.IDCardsActionBean">
		<table>
			<thead style="color:red;">
						<tr>
						<th class="centertext"></th>
						<th class="centertext">类型</th>
						<th class="centertext">号码</th>
						<th class="centertext">有效期</th>
						<th class="centertext">失效日期</th>
						<th class="centertext">图片</th>
						<th class="centertext">注释</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>
								<stripes:hidden name="targetId"/>
								<stripes:submit name="create" value="增加"/>
							</td>
							<td>
								<stripes:select name="idcard.type"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.typeoptions}" label="label" value="value"/></stripes:select>
							</td>
							<td><stripes:text name="idcard.number" /></td>
							<td><stripes:text name="idcard.validfrom"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td><stripes:text name="idcard.expiredate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td></td>
							<td><stripes:text name="idcard.remark" /></td>
						</tr>
					</tbody>
				</table>
				</stripes:form>
	</div>
</body>
</html>