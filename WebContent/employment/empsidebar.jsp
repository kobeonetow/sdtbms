<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<div id="sidebar">
			<div class="sidebar-content">
				<a id="close_sidebar" class="btn ui-state-default full-link ui-corner-all" href="#drill">
					<span class="ui-icon ui-icon-circle-arrow-e"></span>
					隐藏专栏
				</a>
				<a id="open_sidebar" class="btn tooltip ui-state-default full-link icon-only ui-corner-all" title="Open Sidebar" href="#"><span class="ui-icon ui-icon-circle-arrow-w"></span></a>
				<div class="hide_sidebar">
					<div class="portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
						<div class="portlet-header ui-widget-header">招聘管理专栏<span class="ui-icon ui-icon-circle-arrow-s"></span></div>
						<div class="portlet-content">
							<ul id="style-switcher" class="side-menu">
							
								<ss:secure roles="employment_home_view">
									<li>
										<a href="${pageContext.request.contextPath}/actionbean/Employment.action" title="招聘主页">招聘主页</a>
									</li>
								</ss:secure>
								
								<ss:secure roles="employment_request_view">
									<li>
										<a href="${pageContext.request.contextPath}/actionbean/EmpRequest.action" title="增补申请">增补申请</a>
									</li>
								</ss:secure>
								
								<ss:secure roles="employment_driverexam">
									<li>
										<a href="${pageContext.request.contextPath}/actionbean/EmpDriverExam.action" title="驾驶员考试">驾驶员考试</a>
									</li>
								</ss:secure>
								
								<ss:secure roles="employment_application_idcard_setting">
									<li>
										<a href="${pageContext.request.contextPath}/actionbean/AppIDCards.action" title="证件设置">证件设置</a>
									</li>
								</ss:secure>
							</ul>
						</div>
					</div>
<!-- 					<a class="fg-button btn ui-state-default full-link ui-corner-all" href="#"> -->
<!-- 						<span class="ui-icon ui-state-zoomin"></span> -->
<!-- 						Example Link -->
<!-- 					</a> -->
					
					<div class="portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
						<div class="portlet-header ui-widget-header">改变页面大小</div>
						<div class="portlet-content">
							<ul class="side-menu layout-options">
								<li>
									选择你喜欢的尺寸<br /><br />
								</li>
								<li>
									<a href="javascript:void(0);" id="" title="Switch to 100% width layout"><b>100%</b>宽度</a>
								</li>
								<li>
									<a href="javascript:void(0);" id="layout90" title="Switch to 90% width layout"><b>90%</b>宽度</a>
								</li>
								<li>
									<a href="javascript:void(0);" id="layout75" title="Switch to 75% width layout"><b>75%</b>宽度</a>
								</li>
								<li>
									<a href="javascript:void(0);" id="layout980" title="Switch to 980px layout"><b>980px</b>宽度</a>
								</li>
								<li>
									<a href="javascript:void(0);" id="layout1280" title="Switch to 1280px layout"><b>1280px</b>宽度</a>
								</li>
								<li>
									<a href="javascript:void(0);" id="layout1400" title="Switch to 1400px layout"><b>1400px</b>宽度</a>
								</li>
								<li>
									<a href="javascript:void(0);" id="layout1600" title="Switch to 1600px layout"><b>1600px</b>宽度</a>
								</li>
							</ul>
						</div>
					</div>
					
				</div>
			</div>
		</div>
			<div class="clear"></div>