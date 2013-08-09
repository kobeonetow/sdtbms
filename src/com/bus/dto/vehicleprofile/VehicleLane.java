package com.bus.dto.vehicleprofile;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name="vehiclelane")
public class VehicleLane {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="num")
	private String num;
	
	@Column(name="detail")
	private String detail;

	@OneToMany(fetch=FetchType.EAGER,mappedBy="lane")
	private Set<VehicleLaneMapper> mappers;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Set<VehicleLaneMapper> getMapper() {
		return mappers;
	}

	public void setMapper(Set<VehicleLaneMapper> mapper) {
		this.mappers = mapper;
	}
	
	@Transient
	public String getVehicleCount(){
		if(mappers == null)
			return "0";
		else{
			return mappers.size()+"";
		}
	}
}
