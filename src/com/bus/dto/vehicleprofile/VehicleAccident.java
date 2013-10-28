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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Account;
import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="vehicle_accident")
public class VehicleAccident  implements Serializable{

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="vid",referencedColumnName="id")
	private VehicleProfile vid;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="place")
	private String place;
	
	@Column(name="atype")
	private String atype;

	@Column(name="description")
	private String description;
	
	@Column(name="responsibility")
	private String responsibility;
	
	@Column(name="cost")
	private Double cost;
	
	
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


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getPlace() {
		return place;
	}


	public void setPlace(String where) {
		this.place = where;
	}


	public String getAtype() {
		return atype;
	}


	public void setAtype(String atype) {
		this.atype = atype;
	}


	public String getResponsibility() {
		return responsibility;
	}


	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}


	public Double getCost() {
		return cost;
	}


	public void setCost(Double cost) {
		this.cost = cost;
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
	 
	
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Transient
	public String getDateStr(){
		if (date  ==null)
			return "";
		else 
			return HRUtil.parseDateToString(date);
	}
}
