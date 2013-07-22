package com.bus.stripes.actionbean;

import java.util.ArrayList;
import java.util.List;

import security.action.Secure;

import com.bus.dto.Account;
import com.bus.dto.Employee;
import com.bus.dto.Idmanagement;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.util.Roles;
import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

public class IDCardsActionBean extends CustomActionBean implements Permission{
	
	private HRBean bean;
	
	private List<Idmanagement> idcards = new ArrayList<Idmanagement>();
	private List<SelectBoxOption> typeoptions;
	private Idmanagement idcard;
	private Idmanagement newidcard;
	
	private String targetId;
	
	private void loadOptionList(){
		typeoptions = SelectBoxOptions.getSelectBoxFromFixOptions(bean.getOptionListById(7));
	}
	
	@DefaultHandler
	@Secure(roles=Roles.EMPLOYEE_IDCARDS_VIEW)
	public Resolution defaultAction(){
		loadOptionList();
		setTargetId(context.getRequest().getParameter("targetId"));
		if(targetId != null)
			setIdcards(bean.getIdcardsByEmployeeId(targetId));
		return new ForwardResolution("/hr/idcards.jsp");
	}
	
	@HandlesEvent(value="create")
	@Secure(roles=Roles.EMPLOYEE_IDCARDS_ADD)
	public Resolution create(){
		if(newidcard == null)
			return defaultAction();
		Employee e = new Employee();
		e.setId(Integer.parseInt(targetId));
		newidcard.setEmployee(e);
		bean.saveIdcard(newidcard);
		newidcard = new Idmanagement();
		return defaultAction();
	}
	
	@HandlesEvent(value="delete")
	@Secure(roles=Roles.EMPLOYEE_IDCARDS_RM)
	public Resolution delete(){
		try{
			bean.deleteIdcard(idcard.getId()+"");
			return new StreamingResolution("text/utf8","修改成功");
		}catch(Exception e){
			return new StreamingResolution("text/utf8","修改失败."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="edit")
	@Secure(roles=Roles.EMPLOYEE_IDCARDS_ADD)
	public Resolution edit(){
		try{
			bean.updateIdCatd(idcard);
			return new StreamingResolution("text/utf8","修改成功");
		}catch(Exception e){
			return new StreamingResolution("text/utf8","修改失败."+e.getMessage());
		}
	}
	
	public Idmanagement getIdcard() {
		return idcard;
	}
	public void setIdcard(Idmanagement idcard) {
		this.idcard = idcard;
	}

	public List<Idmanagement> getIdcards() {
		return idcards;
	}
	public void setIdcards(List<Idmanagement> idcards) {
		this.idcards = idcards;
	}
	public List<SelectBoxOption> getTypeoptions() {
		return typeoptions;
	}
	public void setTypeoptions(List<SelectBoxOption> typeoptions) {
		this.typeoptions = typeoptions;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
	public HRBean getBean() {
		return bean;
	}
	
	@SpringBean
	public void setBean(HRBean bean) {
		this.bean = bean;
	}

	public Idmanagement getNewidcard() {
		return newidcard;
	}

	public void setNewidcard(Idmanagement newidcard) {
		this.newidcard = newidcard;
	}

}
