package com.bus.stripes.actionbean;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import security.action.Secure;

import com.bus.dto.Account;
import com.bus.dto.Department;
import com.bus.dto.Position;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.test.data.TestData;
import com.bus.util.Roles;
import com.google.gson.JsonObject;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding("/actionbean/Position.action")
public class PositionActionBean  extends CustomActionBean implements Permission{

	private Position position;
	private HRBean bean;
	
	private List<Position> positions = new ArrayList<Position>();
	
	
	@SpringBean
	protected void setBean(HRBean bean){
		this.bean = bean;
	}
	protected HRBean getBean(){
		return this.bean;
	}
	
	public Position getPosition(){
		return this.position;
	}
	public void setPosition(Position position){
		this.position = position;
	}
	
	public List<Position> getPositions() {
		return positions;
	}
	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}
	
	public void initData(){
		setPositions(bean.getAllPosition());
	}
	
	@DefaultHandler
	@Secure(roles=Roles.EMPLOYEE_POS_VIEW)
	public Resolution defaultAction(){
		initData();
//		position = TestData.getPositionData();
		return new ForwardResolution("/hr/position.jsp");
	}
	
	@HandlesEvent(value="create")
	@Secure(roles=Roles.EMPLOYEE_POS_ADD)
	public Resolution create(){
		JsonObject json = new JsonObject();
		try{
			bean.savePosition(position);
			json.addProperty("result", "1");
			json.addProperty("msg", "创建成功");
		}catch(Exception e){
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败。"+e.getMessage());
		}
		return new StreamingResolution("text;charset=utf-8", json.toString());
	}

	@HandlesEvent(value="delete")
	@Secure(roles=Roles.EMPLOYEE_POS_RM)
	public Resolution delete(){
		Position d = new Position();
		String targetId = context.getRequest().getParameter("targetId");
		d.setId(Integer.parseInt(targetId));
		if(d.getId() == null){
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "Delete Fail").addParameter("description", "This position may have already assigned to some employees or there is server error during deletion. Please contact administrator for further operation.");
		}else if(bean.deletePosition(d)){
			return defaultAction();
		}else{
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "Delete Fail").addParameter("description", "This position may have already assigned to some employees or there is server error during deletion. Please contact administrator for further operation.");
		}
	}

}
