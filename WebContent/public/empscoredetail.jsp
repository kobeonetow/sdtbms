<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/score.js"></script> --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/scoreRank.js"></script>
<link href="${pageContext.request.contextPath}/css/ui/ui.base.css" rel="stylesheet" media="all" />   
<link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />
<link href="${pageContext.request.contextPath}/css/custom/scoreRank.css" rel="stylesheet" media="all" />
<title>员工积分详细查看</title>

</head>
<body>

<div>
<div id="empSearchBox">
		名称:<input type="text" name="name"/><br/>
		工号:<input type="text" name="workerid"/><br/>
		<button id="findEmp" class="searchBtn">查找</button>
		<div id="empResult">
			
		</div>
</div>
<div id="maincontent">
	<div id="title"><div id="imageTitle"></div><span class='nameTitle'>积分排名查询</span></div>
	<stripes:form id="formSearch" beanclass="com.bus.stripes.actionbean.score.ScoreViewPublicActionBean">
	<div id="nav">
		<div class="backToScoreSystem"><a href="${pageContext.request.contextPath}/actionbean/Scoreitems.action">点击返回系统</a></div>
		分组:
		<stripes:select name="scoreSelector.rankGroup" id="scoreMasterGroupSelect">
					<stripes:option value="0">主任级</stripes:option>
					<stripes:option value="1">管理人员</stripes:option>
<%-- 					<stripes:option value="2">维修工</stripes:option> --%>
					<stripes:option value="3">服务员</stripes:option>
					<stripes:option value="4">驾驶员</stripes:option>
					<stripes:option value="5">车队长</stripes:option>
		</stripes:select>
		<stripes:select name="scoreSelector.scoreGroup" id="scoreGroupSelect">
				<stripes:option value=""></stripes:option>
				<stripes:options-collection collection="${actionBean.scoreGroups}" value="id" label="name"/>
		</stripes:select>
		日期(从):<stripes:text name="scoreSelector.recordStartDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
				(到):<stripes:text name="scoreSelector.recordEndDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
		<stripes:submit name="ranking" class="searchBtn" value="排名搜索"/>
	</div>
	<div>
		分值类型:
		<stripes:select name="scoreSelector.scoretype">
			<stripes:option value="0">临时分</stripes:option>
			<stripes:option value="1">固定分</stripes:option>
			<stripes:option value="2">绩效分</stripes:option>
			<stripes:option value="3">总分</stripes:option>
		</stripes:select>
		顺序:
		<stripes:radio value="0" name="scoreSelector.order"/>高到底
		<stripes:radio value="1" name="scoreSelector.order"/>低到高
	</div>
	</stripes:form>
	
	<div class="data">
		<c:if test="${actionBean.member != null}">
		<div>
			<table class="nameCard">
				<tr>
					<td rowspan=6>
						<img src="${actionBean.context.hrhostidfile}profilepic/${actionBean.member.employee.image.name}" height="210px" width="155px" alt="读取人物图像失败" />
					</td>
					<td>姓名:${actionBean.member.employee.fullname}</td><td>工号:${actionBean.member.employee.workerid}</td>
				</tr>
				<tr><td>部门:${actionBean.member.employee.department.name}</td><td>职位:${actionBean.member.employee.position.name}</td></tr>
				<tr><td>工龄:${actionBean.member.employee.workage}</td><td>历史总排名:${actionBean.scoreRanking.totalHisRank }</td></tr>
				<tr><td>个人总得分:${actionBean.scoreRanking.totalScore }</td><td>年总得分:${actionBean.scoreRanking.yearScore}</td></tr>
				<tr><td>年总排名:${actionBean.scoreRanking.totalYearRank }</td><td>前${actionBean.scoreRanking.totalRankPercent }%</td></tr>
				<tr><td>年组排名:${actionBean.scoreRanking.groupYearRank }</td><td>前:${actionBean.scoreRanking.yearRankPercent }%</td></tr>
			</table>
			
			<div class="dateselection">
				<stripes:form beanclass="com.bus.stripes.actionbean.score.ScoreViewPublicActionBean">
					<input name="workerid" value="${actionBean.member.employee.workerid}" type="hidden"/>
					日期(起):<stripes:text name="recordDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>日期(到):<stripes:text name="recordEndDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
					<br/><stripes:submit name="memberDetail" value="详细记录查询" class="searchBtn"/>
				</stripes:form>
			</div>
				<div>
					<table>
						<thead>
							<tr>
								<th>ID</th>
								<th>打分日期</th>
								<th>得分日期</th>
								<th>打分人</th>
								<th>条例编号</th>
								<th>分值</th>
								<th>类型</th>
								<th>条例</th>
								<th>注释</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="color" value="0" scope="page"/>
							<c:if test="${actionBean.records == null}">
								<span style="color:white">没有找到记录</span>
							</c:if>
							<c:forEach items="${actionBean.records}" var="record" varStatus="loop">
								<c:choose>
									<c:when test="${color%2 == 0}">
										<tr>
									</c:when>
									<c:otherwise>
										<tr class="alt">
									</c:otherwise>
								</c:choose>
								<td>${record.id}</td>
								<td>${record.createdatestr}</td>
								<td>${record.scoredatestr}</td>
								<td>${record.sender.employee.fullname}</td>
								<td>${record.scoretype.reference }</td>
								<td>${record.score }</td>
								<td>${record.scoretype.typestr }</td>
								<td style="width:300px !important;">${record.scoretype.description }</td>
								<td style="width:250px !important;">${record.remark.remark }</td>
								</tr>
								<c:set var="color" value="${color + 1}" scope="page"/>
								</c:forEach>
							</tbody>
						</table>
					</div>
			</div>
			</c:if>
	</div>
</div>
</div>
</body>
</html>
		