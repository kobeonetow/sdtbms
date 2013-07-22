package com.bus.dto.application;

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

import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="driving_exam")
public class DrivingExam implements Serializable{
	
	private Integer id;
	private HRApplication applicant;
	private Date examdate;
	private Integer examtimes;
	private Integer exampass;
	private Date zhuangDate;
	private Integer zhuangPass;
	private Date roadDate;
	private Integer roadPass;
	private String remark;
	private String inspector;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="applicationid")
	public HRApplication getApplicant() {
		return applicant;
	}
	public void setApplicant(HRApplication applicant) {
		this.applicant = applicant;
	}
	
	@Column(name="examtimes")
	public Integer getExamtimes() {
		return examtimes;
	}
	public void setExamtimes(Integer examtimes) {
		this.examtimes = examtimes;
	}
	
	@Column(name="exampass")
	public Integer getExampass() {
		return exampass;
	}
	public void setExampass(Integer exampass) {
		this.exampass = exampass;
	}
	
	@Column(name="zhuangdate")
	public Date getZhuangDate() {
		return zhuangDate;
	}
	public void setZhuangDate(Date zhuangDate) {
		this.zhuangDate = zhuangDate;
	}
	
	@Column(name="zhuangpass")
	public Integer getZhuangPass() {
		return zhuangPass;
	}
	public void setZhuangPass(Integer zhuangPass) {
		this.zhuangPass = zhuangPass;
	}
	
	@Column(name="roaddate")
	public Date getRoadDate() {
		return roadDate;
	}
	public void setRoadDate(Date roadDate) {
		this.roadDate = roadDate;
	}
	
	@Column(name="roadpass")
	public Integer getRoadPass() {
		return roadPass;
	}
	public void setRoadPass(Integer roadPass) {
		this.roadPass = roadPass;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="inspector")
	public String getInspector() {
		return inspector;
	}
	public void setInspector(String inspector) {
		this.inspector = inspector;
	}
	
	@Column(name="examdate")
	public Date getExamdate() {
		return examdate;
	}
	public void setExamdate(Date examdate) {
		this.examdate = examdate;
	}

	
	@Transient
	public String getExamdateStr(){
		if(examdate == null)
			return "";
		else
			return HRUtil.parseDateToString(examdate);
	}
	
	@Transient
	public String getExampassStr(){
		if(exampass == null)
			return "未完成考试";
		else if(exampass == 1)
			return "合格";
		else
			return "不合格";
	}
	
	@Transient
	public String getZhuangPassStr(){
		if(zhuangPass == null || zhuangPass == 0)
			return "未考";
		else if(zhuangPass == 1)
			return "合格";
		else
			return "不合格";
	}
	
	@Transient
	public String getRoadPassStr(){
		if(roadPass == null || roadPass == 0)
			return "未考";
		else if(roadPass == 1)
			return "合格";
		else
			return "不合格";
	}
}
