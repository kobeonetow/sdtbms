package com.bus.dto.vehicleprofile;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Account;
import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="vehicle_repair_record")
public class VehicleRepairRecord  implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="vid",referencedColumnName="id")
	private VehicleProfile vid;
	
	@Column(name="rdate")
	private Date rdate;
	
	@Column(name="rtype")
	private String rtype;
	
	@Column(name="description")
	private String description;
	
	@Column(name="rcompany")
	private String rcompany;
	
	@Column(name="registrant")
	private String registrant;
	
	@ManyToOne
	@JoinColumn(name="creator",referencedColumnName="id")
	private Account creator;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VehicleProfile getVid() {
		return vid;
	}

	public void setVid(VehicleProfile vid) {
		this.vid = vid;
	}
	
	public Date getRdate() {
		return rdate;
	}

	public void setRdate(Date rdate) {
		this.rdate = rdate;
	}

	public String getRtype() {
		return rtype;
	}

	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRcompany() {
		return rcompany;
	}

	public void setRcompany(String rcompany) {
		this.rcompany = rcompany;
	}

	public String getRegistrant() {
		return registrant;
	}

	public void setRegistrant(String registrant) {
		this.registrant = registrant;
	}

	public Account getCreator() {
		return creator;
	}

	public void setCreator(Account creator) {
		this.creator = creator;
	}
	
	public String getRdateStr(){
		if(rdate == null)
			return "";
		else
			return HRUtil.parseDateToString(rdate);
	}
}
