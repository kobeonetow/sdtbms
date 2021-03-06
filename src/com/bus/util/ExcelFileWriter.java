package com.bus.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bus.dto.Contract;
import com.bus.dto.Employee;
import com.bus.dto.Idmanagement;
import com.bus.dto.Promoandtransfer;
import com.bus.services.HRBean;

public class ExcelFileWriter {

	
	public ExcelFileWriter(){
	}
	
	public String writeEmployees(HRBean bean, String type){
		List<Employee> list = (List<Employee>)bean.getAllEmployee(type);
		String str = "";
//		str += "佛山市顺德区汽车运输有限公司在职员工电子档案,"+HRUtil.parseDateToString(new Date()) + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n";
		str = writeEmployeeToString(list);
		return str;
	}
	
	private List<Contract> getPassContracts(Set<Contract> contracts){
		List<Contract> c = new ArrayList<Contract>();
		for(Contract tc: contracts){
				if(tc != null && tc.getStatus().equals("E")){
					c.add(tc);
				}
		}
		return c;
	}
	
	private Contract getLastContract(Set<Contract> contracts) {
		Contract c = null;
		for(Contract tc: contracts){
			if(c == null)
				c = tc;
			else{
				if(tc.getStatus().equals("A")){
					if(tc.getEnddate() == null || c.getEnddate() == null)
						continue;
					if(tc.getEnddate().getTime() > c.getEnddate().getTime()){
						c = tc;
					}
				}
			}
		}
		return c;
	}

	private String toStr(Object obj){
		if(obj == null)
			return "";
		return obj.toString();
	}

	public String writeDrivers(HRBean bean) {
		List<Employee> list = (List<Employee>)bean.getAllDrivers();
		String str = "";
//		str += "佛山市顺德区汽车运输有限公司在职员工电子档案,"+HRUtil.parseDateToString(new Date()) + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n";
		str += "姓名"+ ",";
		str += "工号"+ ",";
		str += "部门"+ ",";
		str += "性别"+ ",";
		str += "出生年月"+ ",";
		str += "籍贯"+ ",";
		str += "入职日期"+ ",";
		str += "驾驶证号"+ ",";
		str += "准驾车型"+ ",";
		str += "初考日期"+ ",";
		str += "有效日期（驾）"+ ",";
		str += "从业资格证"+ ",";
		str += "从业资格类别"+ ",";
		str += "领证日期"+",";
		str += "有效日期（从）"+ ",";
		str += "服务资格证"+ "\n";
		
		for(Employee e:list){
			str += toStr(e.getFullname()) + ",";
			str += toStr(e.getWorkerid())+",";
			if(e.getDepartment() != null)
				str += toStr(e.getDepartment().getName()) + ",";
			else
				str += ",";
			str += toStr(e.getSex()) + ",";
			str += HRUtil.parseDateToString(e.getDob()) + ",";
			str += toStr(e.getPob()) + ",";
			str += HRUtil.parseDateToString(e.getFirstworktime()) + ",";
			
			Idmanagement driverL = getIdCard(e.getIdcards(),"驾驶证");
			if(driverL != null){
				str += toStr(driverL.getNumber()) + ",";
				str += toStr(driverL.getRemark()) + ",";
				str += HRUtil.parseDateToString(driverL.getValidfrom()) + ",";
				str += HRUtil.parseDateToString(driverL.getExpiredate()) + ",";
			}else{
				str += ",";str += ",";str += ",";str += ",";
			}
			
			Idmanagement forworkservice = getIdCard(e.getIdcards(),"从业资格证");
			if(forworkservice != null){
				str += toStr(forworkservice.getNumber()) + ",";
				str += toStr(forworkservice.getRemark()) + ",";
				if(forworkservice.getValidfrom() != null)
					str += HRUtil.parseDateToString(forworkservice.getValidfrom()) +",";
				else
					str += ",";
				str += HRUtil.parseDateToString(forworkservice.getExpiredate()) + ",";
			}else{
				str += ",";str += ",";str += ",";
			}
			
			Idmanagement serviceid = getIdCard(e.getIdcards(),"服务资格证");
			if(serviceid != null){
				str += toStr(serviceid.getNumber());
			}
			
			str +="\n";
		}
		return str;
	}

	private Idmanagement getIdCard(Set<Idmanagement> idcards, String type) {
		for(Idmanagement id:idcards){
			if(id.getType() == null)
				continue;
			else{
				if(type.equals(id.getType())){
					return id;
				}
			}
		}
		return null;
	}

	public String writeCoordination(HRBean bean, Date startdate, Date enddate,
			String coortype) {
		if(coortype.equals("resign")){
			//用离职调动的日期
			List<Promoandtransfer> coordinations = bean.getResignEmployeeByCoordinationDate(startdate,enddate);
			return writeResignationToString(coordinations);
		}else if (coortype.equals("offer")){
			List<Employee> emps = bean.getEmployeeOfferredByDate(startdate,enddate);
			return writeEmployeeToString(emps);
			//用入职日期
		}else if (coortype.equals("coor")){
			//用调动日期
			List<Promoandtransfer> transfer = bean.getCoordinationsByDate(startdate, enddate);
			return writeCoordinationToString(transfer);
		}else
			return "";
	}

	private String writeResignationToString(List<Promoandtransfer> coordinations) {
		String str = "";
		if(coordinations == null || coordinations.size() <1){
			return str + "没有找到相关数据";
		}
		str += "档案编号"+ ",";
		str += "姓名"+ ",";
		str += "工号"+ ",";
		str += "入职日期"+ ",";
		str += "身份证号码"+ ",";
		str += "民族"+ ",";
		str += "婚姻"+ ",";
		str += "出生年月"+ ",";
		str += "性别"+ ",";
		str += "年龄"+ ",";
		str += "在职工龄"+ ",";
		str += "部门"+ ",";
		str += "职位"+ ",";
		str += "所属镇街"+ ",";
		str += "籍贯"+ ",";
		str += "家庭详细地址"+ ",";
		str += "联系电话"+ ",";
		str += "户籍类型"+ ",";
		str += "学历"+ ",";
		str += "毕业院校"+ ",";
		str += "专业"+ ",";
		str += "政治面貌"+ ",";
		str += "入党时间"+ ",";
		str += "职称"+ ",";
		str += "军人"+ ",";
		str += "职级"+ ",";
		str += "合同始期"+ ",";
		str += "终结日期"+ ",";
		str += "合同次数"+ ",";
		str += "调入时间"+ ",";
		str += "变动情况" + ",";
		str += "离职时间" + ",";
		str += "离职原因" + "\n";
		
		for(Promoandtransfer coor :coordinations){
			Employee e = coor.getEmployee();
			str += toStr(e.getDocumentkey())+",";
			str += toStr(e.getFullname()) + ",";
			str += toStr(e.getWorkerid())+",";
			str += HRUtil.parseDateToString(e.getFirstworktime()) + ",";
			str += toStr(e.getIdentitycode()) + ",";
			str += toStr(e.getEthnic()) + ",";
			str += toStr(e.getMarriage()) + ",";
			str += HRUtil.parseDateToString(e.getDob()) + ",";
			str += toStr(e.getSex()) + ",";
			str += toStr(e.getAge()) + ",";
			str += toStr(e.getWorkage()) + ",";
			if(e.getDepartment() != null)
				str += toStr(e.getDepartment().getName()) + ",";
			else
				str += ",";
			if(e.getPosition() != null)
				str += toStr(e.getPosition().getName()) + ",";
			else
				str += ",";
			str += toStr(e.getPlacebelong()) + ",";
			str += toStr(e.getPob()) + ",";
			str += toStr(e.getHomeaddress()) + ",";
			str += toStr(e.getMobilenumber()) + ",";
			str += toStr(e.getDomiciletype()) + ",";
			str += toStr(e.getQualification()) + ",";
			str += toStr(e.getColleage()) + ",";
			str += toStr(e.getMajor()) + ",";
			str += toStr(e.getPoliticalstatus()) + ",";
			str += HRUtil.parseDateToString(e.getTimeofjoinrpc())+",";
			str += toStr(e.getWorkertype()) + ",";
			str += toStr(e.getArmy()) + ",";
			str += toStr(e.getJoblevel()) + ",";
			
			if(e.getContracts() != null && e.getContracts().size() > 0){
				Contract c = getLastContract(e.getContracts());
				str += HRUtil.parseDateToString(c.getStartdate())+",";
				str += HRUtil.parseDateToString(c.getEnddate())+",";
				str += e.getContracts().size()+ "次,";
			}else{
				str += ",";
				str += ",";
				str += ",";
			}
			
			
			str += HRUtil.parseDateToString(e.getTransfertime()) + ",";
//			str += toStr(e.getRemark());
			str += ",";
			str += HRUtil.parseDateToString(coor.getActivedate()) +",";
			str += toStr(coor.getRemark());
			str +="\n";
		}
		return str;
	}

	private String writeEmployeeToString(List<Employee> emps) {
		String str = "";
		if(emps == null || emps.size() < 1){
			str += "没有拿到任何数据";
			return str;
		}
		str += "档案编号"+ ",";
		str += "姓名"+ ",";
		str += "工号"+ ",";
		str += "入职日期"+ ",";
		str += "身份证号码"+ ",";
		str += "民族"+ ",";
		str += "婚姻"+ ",";
		str += "出生年月"+ ",";
		str += "性别"+ ",";
		str += "年龄"+ ",";
		str += "在职工龄"+ ",";
		str += "部门"+ ",";
		str += "职位"+ ",";
		str += "所属镇街"+ ",";
		str += "籍贯"+ ",";
		str += "家庭详细地址"+ ",";
		str += "联系电话"+ ",";
		str += "户籍类型"+ ",";
		str += "学历"+ ",";
		str += "毕业院校"+ ",";
		str += "专业"+ ",";
		str += "政治面貌"+ ",";
		str += "入党时间"+ ",";
		str += "职称"+ ",";
		str += "特殊身份"+ ",";
		str += "职级"+ ",";
		str += "合同始期"+ ",";
		str += "终结日期"+ ",";
		str += "合同次数"+ ",";
		str += "调入时间"+ ",";
		str += "变动情况" + "\n";
		
		for(Employee e:emps){
			str += toStr(e.getDocumentkey())+",";
			str += toStr(e.getFullname()) + ",";
			str += toStr(e.getWorkerid())+",";
			str += HRUtil.parseDateToString(e.getFirstworktime()) + ",";
			str += toStr(e.getIdentitycode()) + ",";
			str += toStr(e.getEthnic()) + ",";
			str += toStr(e.getMarriage()) + ",";
			str += HRUtil.parseDateToString(e.getDob()) + ",";
			str += toStr(e.getSex()) + ",";
			str += toStr(e.getAge()) + ",";
			str += toStr(e.getWorkage()) + ",";
			if(e.getDepartment() != null)
				str += toStr(e.getDepartment().getName()) + ",";
			else
				str += ",";
			if(e.getPosition() != null)
				str += toStr(e.getPosition().getName()) + ",";
			else
				str += ",";
			str += toStr(e.getPlacebelong()) + ",";
			str += toStr(e.getPob()) + ",";
			str += toStr(e.getHomeaddress()) + ",";
			str += toStr(e.getMobilenumber()) + ",";
			str += toStr(e.getDomiciletype()) + ",";
			str += toStr(e.getQualification()) + ",";
			str += toStr(e.getColleage()) + ",";
			str += toStr(e.getMajor()) + ",";
			str += toStr(e.getPoliticalstatus()) + ",";
			str += HRUtil.parseDateToString(e.getTimeofjoinrpc())+",";
			str += toStr(e.getWorkertype()) + ",";
			str += toStr(e.getArmy()) + ",";
			str += toStr(e.getJoblevel()) + ",";
			
			if(e.getContracts() != null && e.getContracts().size() > 0){
				Contract c = getLastContract(e.getContracts());
				str += HRUtil.parseDateToString(c.getStartdate())+",";
				str += HRUtil.parseDateToString(c.getEnddate())+",";
				
				String contractStr = "";
				for(Contract cp : getPassContracts(e.getContracts())){
					contractStr += "("+cp.getStartdatestr()+":"+cp.getEnddatestr()+"|"+cp.getRemark()+").";
				}
				str += contractStr+",";
			}else{
				str += ",";
				str += ",";
				str += ",";
			}
			
			
			str += HRUtil.parseDateToString(e.getTransfertime()) + ",";
//			str += toStr(e.getRemark());
			String tranfStr = "";
			if(e.getTransfers() != null){
				for(Promoandtransfer p:e.getTransfers()){
					tranfStr += "(";
					if(p.getMovedatestr() != null)
						tranfStr += p.getMovedatestr();
					
					tranfStr += ":"+p.getType()+":";
					
					if(p.getPredepartment() != null)
						tranfStr += p.getPredepartment().getName()+" ";
					if(p.getPreposition()!= null)
						tranfStr += p.getPreposition().getName();
					if(p.getCurdepartment()!= null)
						tranfStr += "--"+p.getCurdepartment().getName()+" ";
					if(p.getCurposition() != null)
						tranfStr += p.getCurposition().getName()+":";
					if(p.getRemark() != null)
						tranfStr += p.getRemark();
					tranfStr +=").";
				}
				str += tranfStr+",";
			}else
				str += ",";
			str +="\n";
		}
		return str;
	}

	private String writeCoordinationToString(
			List<Promoandtransfer> coordinations) {
		String str = "";
		if(coordinations == null || coordinations.size() < 1){
			str += "没有拿到任何数据";
			return str;
		}
		str += "姓名"+",";
		str += "工作证号"+",";
		str += "原部门"+",";
		str += "原岗位"+",";
		str += "性别"+",";
		str += "新部门"+",";
		str += "新岗位"+",";
		str += "工资停发日"+",";
		str += "新工资发放日期"+"\n";
		
		for(Promoandtransfer coor :coordinations){
			str += toStr(coor.getEmployee().getFullname()) +",";
			str += toStr(coor.getEmployee().getWorkerid()) + ",";
			str += toStr(coor.getPredepartment().getName()) + ",";
			str += toStr(coor.getPreposition().getName()) + ",";
			str += toStr(coor.getEmployee().getSex()) + ",";
			str += toStr(coor.getCurdepartment().getName())+",";
			str += toStr(coor.getCurposition().getName())+",";
			str += HRUtil.parseDateToString(coor.getMovedate())+",";
			str += HRUtil.parseDateToString(coor.getActivedate())+"\n";
		}
		return str;
	}

	public String writeSelectedEmployees(HRBean bean, String statement) {
		Map<String, Object> map = bean.getEmployeesBySelector(-1, -1, statement);
		List<Employee> employees  = (List<Employee>) map.get("list");
		if(employees == null)
			return "没有员工被选上";
		String str = writeEmployeeToString(employees);
		return str;
	}
}
