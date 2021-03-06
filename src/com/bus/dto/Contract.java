package com.bus.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.common.ContractImg;
import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="contract")
public class Contract {
	
	private Integer id;
	private Employee employee;
	private String type;
	private Date startdate;
	private Date enddate;
	private Integer probation;
	private Integer probationsalary;
	private Integer salary;
	private String remark;
	private Date activedate;
	private String status;
	private Date createdate;
	private String probationdate;
	
	private ContractImg image;
	
	public void copy(Contract c){
		type = c.getType();
		startdate = c.getStartdate();
		enddate = c.getEnddate();
		probation = c.getProbation();
		probationsalary = c.getProbationsalary();
		salary = c.getSalary();
		remark = c.getRemark();
		activedate = c.getActivedate();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="employeeid")
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Column(name="type",nullable=false,length=16)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="startdate",nullable=false)
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	
	@Column(name="enddate",nullable=false)
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
	@Column(name="probation")
	public Integer getProbation() {
		return probation;
	}
	public void setProbation(Integer probation) {
		this.probation = probation;
	}
	
	@Column(name="probationsalary")
	public Integer getProbationsalary() {
		return probationsalary;
	}
	public void setProbationsalary(Integer probationsalary) {
		this.probationsalary = probationsalary;
	}
	
	@Column(name="salary")
	public Integer getSalary() {
		return salary;
	}
	public void setSalary(Integer salary) {
		this.salary = salary;
	}
	
	@Column(name="remark",length=256)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="activedate")
	public Date getActivedate() {
		return activedate;
	}
	
	public void setActivedate(Date activedate) {
		this.activedate = activedate;
	}
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="createdate")
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Transient
	public String getProbationdate() {
		if(this.startdate == null || this.probation == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(this.startdate);
		c.add(Calendar.DAY_OF_YEAR, this.probation);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());
	}

	public void setProbationdate(String probationdate) {
		this.probationdate = probationdate;
	}
	
	@Transient
	public String getStartdatestr(){
		if(this.startdate != null)
			return HRUtil.parseDateToString(this.startdate);
		else
			return "";
	}
	@Transient
	public String getEnddatestr(){
		if(this.enddate != null)
			return HRUtil.parseDateToString(this.enddate);
		else
			return "";
	}
	@Transient
	public String getActivedatestr(){
		if(this.activedate != null)
			return HRUtil.parseDateToString(this.activedate);
		else
			return "";
	}
	@Transient
	public String getCreatedatestr(){
		if(this.createdate != null)
			return HRUtil.parseDateToString(this.createdate);
		else
			return "";
	}

	@OneToOne(fetch=FetchType.LAZY,mappedBy="contract",cascade = CascadeType.ALL)
	public ContractImg getImage() {
		return image;
	}
	public void setImage(ContractImg image) {
		this.image = image;
	}
}
