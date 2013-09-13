package com.bus.stripes.actionbean.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.vehicleprofile.VehicleTeam;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.stripes.actionbean.MyActionBeanContext;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding(value="/actionbean/EmployeeSelector.action")
public class EmployeeSelectorActionBean implements ActionBean{

	protected MyActionBeanContext context;
	public MyActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = (MyActionBeanContext)context; }
	
	protected HRBean hrBean;
	@SpringBean
	protected void setBean(HRBean bean){this.hrBean = bean;}
	protected HRBean getBean(){return this.hrBean;}
	
	
	private List<EmpDepartments> departments;
	private String eleIdOne;
	private String eleIdTwo;
	private String extra;
	private Boolean multi;
	
	@DefaultHandler
	public Resolution defaultAction(){
		try{
			if(eleIdOne == null)
				eleIdOne  = context.getRequest().getParameter("eleIdOne");
			if(eleIdTwo == null)
				eleIdTwo = context.getRequest().getParameter("eleIdTwo");
			if(extra == null)
				extra = context.getRequest().getParameter("extra");
			if(multi == null){
				if(context.getRequest().getParameter("multi")!= null)
					multi = true;
				else
					multi = false;
			}
			
			List<Department> depts = hrBean.getAllActiveDepartment();
			for(Department d:depts){
				if(departments == null){
					departments = new ArrayList<EmpDepartments>();
				}
				EmpDepartments temD = new EmpDepartments();
				temD.setDept(d.getName());
				temD.setDeptId(d.getId()+"");
				if(d.getName().equals("顺汽公交一分公司") || d.getName().equals("顺汽公交二分公司") ){
					List<EmpDepartments> driverteams = new ArrayList<EmpDepartments>();
					List<VehicleTeam> teams = hrBean.getExistingTeamsForDepartment(d.getId());
					for(VehicleTeam t:teams){
						EmpDepartments temD2 = new EmpDepartments();
						temD2.setDept(t.getName());
						temD2.setDeptId(t.getId()+"");
						List<Employee> temD2Employee = hrBean.getEmployeeByDepartmentIdAndTeamId(d.getId(),t.getId());
						temD2.setSize(temD2Employee.size());
						temD2.setEmps(temD2Employee);
						driverteams.add(temD2);
					}
					List<Employee> empNoTeam = hrBean.getEmployeeByDepartmentIdAndNoTeam(d.getId());
					temD.setEmps(empNoTeam);
					int total = empNoTeam.size();
					for(EmpDepartments te:driverteams){
						total += te.getEmps().size();
					}
					temD.setSize(total);
					temD.setExtras(driverteams);
				}else{
					List<Employee> employees = hrBean.getEmployeeByDepartmentId(d.getId());
					temD.setSize(employees.size());
					temD.setEmps(employees);
				}
				departments.add(temD);
			}
			return new ForwardResolution("/public/selEmp.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return new ForwardResolution("/public/selEmp.jsp");
		}
	}
	
	public String getEleIdOne() {
		return eleIdOne;
	}
	public void setEleIdOne(String eleIdOne) {
		this.eleIdOne = eleIdOne;
	}
	public String getEleIdTwo() {
		return eleIdTwo;
	}
	public void setEleIdTwo(String eleIdTwo) {
		this.eleIdTwo = eleIdTwo;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public List<EmpDepartments> getDepartments() {
		return departments;
	}
	public void setDepartments(List<EmpDepartments> departments) {
		this.departments = departments;
	}
	
	public boolean isMulti() {
		return multi;
	}
	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public class EmpDepartments {
		private String deptId;
		private String dept;
		private Integer size;
		public List<Employee> emps;
		private List<EmpDepartments> extras = null;
		
		public String getDeptId() {
			return deptId;
		}
		public void setDeptId(String deptId) {
			this.deptId = deptId;
		}
		public String getDept() {
			return dept;
		}
		public void setDept(String dept) {
			this.dept = dept;
		}
		public List<Employee> getEmps() {
			return emps;
		}
		public void setEmps(List<Employee> emps) {
			this.emps = emps;
		}
		public Integer getSize() {
			return size;
		}
		public void setSize(Integer size) {
			this.size = size;
		}
		public List<EmpDepartments> getExtras() {
			return extras;
		}
		public void setExtras(List<EmpDepartments> extras) {
			this.extras = extras;
		}
		
	}
}
