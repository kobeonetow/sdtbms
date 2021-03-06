package com.bus.stripes.actionbean.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import security.action.Secure;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.Department;
import com.bus.dto.Position;
import com.bus.dto.application.ApplicationConstants;
import com.bus.dto.application.EmployeeRequest;
import com.bus.dto.application.HRApplication;
import com.bus.services.CustomActionBean;
import com.bus.services.EmpApplicationBean;
import com.bus.services.HRBean;
import com.bus.stripes.selector.EmpRequestSelector;
import com.bus.util.HRUtil;
import com.bus.util.Roles;
import com.bus.util.SelectBoxOptions;

@UrlBinding(value="/actionbean/EmpRequest.action")
public class EmpRequestActionBean extends CustomActionBean{

	private EmpApplicationBean empBean;
	public EmpApplicationBean getEmpApplicationBean(){return this.empBean;}
	@SpringBean
	public void setEmpApplicationBean(EmpApplicationBean bean){this.empBean = bean;}
	
	private HRBean hrBean;
	public HRBean getBean() {return hrBean;}
	@SpringBean
	public void setBean(HRBean bean) {this.hrBean = bean;}
	
	//Preload list
	private List<Department> departments;
	private List<Position> positions;
	
	private List<EmployeeRequest> emprequests;
	private EmployeeRequest empRequest;
	private EmpRequestSelector selector;
	private String ERR;
	
	private int pagenum;
	private int lotsize;
	private Long totalcount;
	private int pagecount;
	
	public static final String  COMMIT_NUM = "commitNum";
	public static final String APPROVE_DATE = "approveDate";
	public static final String SEND_APPROVE_DATE = "sendApproveDate";
	
	private void initData(){
		this.setDepartments(hrBean.getAllDepartment());
		this.setPositions(hrBean.getAllPosition());
		
		if(pagenum <= 0 || lotsize <= 0){
			pagenum = 1;
			lotsize = 20;
		}
		setTotalcount(0L);
		getRequestsFromSelector();
		setPagecount((int) (totalcount/lotsize + 1));
		if(pagenum > totalcount)
			pagenum = Integer.parseInt(totalcount.toString());
	}
	
	private void getRequestsFromSelector() {
		try{
			if(selector == null){
				HashMap<String,Object> map = (HashMap<String, Object>) empBean.getRequests(pagenum, lotsize);
				setEmprequests((List<EmployeeRequest>) map.get("list"));
				setTotalcount((Long) map.get("count"));
			}else if(selector != null){
				String statement = selector.getSelectorStatement();
				System.out.println(statement);
				HashMap<String,Object> map = (HashMap<String, Object>) empBean.getRequests(pagenum, lotsize,statement);
				setEmprequests((List<EmployeeRequest>) map.get("list"));
				setTotalcount((Long) map.get("count"));
			}
		}catch(Exception e){
			ERR = e.getMessage();
			setEmprequests(new ArrayList<EmployeeRequest>());
			setTotalcount(0L);
			setPagecount(0);
		}
	}
	
	public EmpRequestSelector getSelector() {
		return selector;
	}
	public void setSelector(EmpRequestSelector selector) {
		this.selector = selector;
	}
	@DefaultHandler
	@Secure(roles=Roles.EMPLOYMENT_REQUEST)
	public Resolution defaultAction(){
		initData();
		return new ForwardResolution("/employment/emprequest.jsp");
	}
	
	@HandlesEvent(value="createRequest")
	@Secure(roles=Roles.EMPLOYMENT_REQUEST_CREATE)
	public Resolution createRequestAction(){
		try{
			empBean.createEmpRequest(empRequest);
			return new StreamingResolution("text/charset=utf8","新建成功");
		}catch(Exception e){
			return new StreamingResolution("text/charset=utf8","新建失败."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="updateRequest")
	@Secure(roles=Roles.EMPLOYMENT_REQUEST_EDIT)
	public Resolution updateRequest(){
		try{
			String updateEvent = context.getRequest().getParameter("update");
			String targetId = context.getRequest().getParameter("targetId");
			String value = context.getRequest().getParameter("value");
			String dateval = context.getRequest().getParameter("dateval");
			EmployeeRequest er = empBean.getEmpRequestById(targetId);
			if(updateEvent.equals(COMMIT_NUM))
				er.setCommitNumber(Integer.parseInt(value));
			else if(updateEvent.equals(APPROVE_DATE) && dateval != null)
				er.setApproveDate(HRUtil.parseDate(dateval, "yyyy-MM-dd"));
			else if(updateEvent.equals(SEND_APPROVE_DATE) && dateval != null)
				er.setSendApproveDate(HRUtil.parseDate(dateval, "yyyy-MM-dd"));
			empBean.mergeEmpRequest(er);
			return new StreamingResolution("text/charset=utf8","修改成功");
		}catch(Exception e){
//			e.printStackTrace();
			return new StreamingResolution("text/charset=utf8","修改失败."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="approveResult")
	@Secure(roles=Roles.EMPLOYMENT_REQUEST_EDIT)
	public Resolution approveResult(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			EmployeeRequest er = empBean.getEmpRequestById(targetId);
			String approveStatus = context.getRequest().getParameter("approveStatus");
			if(approveStatus.equals("N"))
				er.setApproveResult(ApplicationConstants.APPROVE_FAIL);
			else if(approveStatus.equals("C"))
				er.setApproveResult(ApplicationConstants.APPROVE_CONSIDER);
			else if(approveStatus.equals("Y"))
				er.setApproveResult(ApplicationConstants.APPROVE_SUCCESS);
			else
				er.setApproveResult(ApplicationConstants.APPROVE_UNKNOW);
			empBean.mergeEmpRequest(er);
			return new StreamingResolution("text/charset=utf8;","修改成功");
		}catch(Exception e){
			e.printStackTrace();
			return context.errorResolutionAjax("修改出错", ""+e.getMessage());
		}
	}
	
	@HandlesEvent(value="deleteRequest")
	@Secure(roles=Roles.EMPLOYMENT_REQUEST_EDIT)
	public Resolution deleteRequest(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			empBean.removeEmployeeRequest(targetId);
			return new StreamingResolution("text/charset=utf8;","修改成功");
		}catch(Exception e){
			e.printStackTrace();
			return context.errorResolutionAjax("修改出错", ""+e.getMessage());
		}
	}
	
	@HandlesEvent(value="prevpage")
	public Resolution prevpage(){
		setPagenum(getPagenum() - 1);
		return defaultAction();
	}
	
	@HandlesEvent(value="nextpage")
	public Resolution nextpage(){
		setPagenum(getPagenum() + 1);
		return defaultAction();
	}
	
	@HandlesEvent(value="filter")
	public Resolution filter(){
		return defaultAction();
	}
	
	public List<EmployeeRequest> getEmprequests() {
		return emprequests;
	}
	public void setEmprequests(List<EmployeeRequest> emprequests) {
		this.emprequests = emprequests;
	}
	public int getPagenum() {
		return pagenum;
	}
	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}
	public int getLotsize() {
		return lotsize;
	}
	public void setLotsize(int lotsize) {
		this.lotsize = lotsize;
	}
	public Long getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}
	public EmployeeRequest getEmpRequest() {
		return empRequest;
	}
	public void setEmpRequest(EmployeeRequest empRequest) {
		this.empRequest = empRequest;
	}
	public int getPagecount() {
		return pagecount;
	}
	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}
	public String getERR() {
		return ERR;
	}
	public void setERR(String eRR) {
		ERR = eRR;
	}
	public List<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
	public List<Position> getPositions() {
		return positions;
	}
	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}
}
