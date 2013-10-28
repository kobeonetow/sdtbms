package com.bus.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.Account;
import com.bus.dto.Employee;
import com.bus.dto.logger.VehicleLog;
import com.bus.dto.vehicleprofile.VehicleBasicInfo;
import com.bus.dto.vehicleprofile.VehicleCheck;
import com.bus.dto.vehicleprofile.VehicleChange;
import com.bus.dto.vehicleprofile.VehicleAccident;
//import com.bus.dto.vehicleprofile.VehicleCheck;
import com.bus.dto.vehicleprofile.VehicleFiles;
import com.bus.dto.vehicleprofile.VehicleLane;
import com.bus.dto.vehicleprofile.VehicleLaneMapper;
import com.bus.dto.vehicleprofile.VehicleLaneMirror;
import com.bus.dto.vehicleprofile.VehicleLevel;
import com.bus.dto.vehicleprofile.VehiclePartsRepair;
import com.bus.dto.vehicleprofile.VehicleRepairRecord;
import com.bus.dto.vehicleprofile.VehicleTeamMember;
import com.bus.dto.vehicleprofile.VehicleTechnicalDetail;
import com.bus.dto.vehicleprofile.VehicleUseRecord;
//import com.bus.dto.vehicleprofile.VehicleLaneMirror;
//import com.bus.dto.vehicleprofile.VehicleMiles;
import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.dto.vehicleprofile.VehicleTeam;
import com.bus.dto.vehicleprofile.VehicleTeamLeader;
//import com.bus.dto.vehicleprofile.VehicleTeamMember;
import com.bus.util.ExcelFileSaver;
import com.bus.util.HRUtil;

public class VehicleBean extends EMBean {

	/**
	 * Get vehicles if pagenum != -1, use lotsize, else all selected
	 * 
	 * @param pagenum
	 * @param lotsize
	 * @return
	 */
	public Map getVehicleProfiles(int pagenum, int lotsize) throws Exception {
		Map map = new HashMap<String, Object>();
		if (pagenum == -1 || pagenum == 0 || lotsize <= 20) {
			pagenum = 1; lotsize =20;
		}
		List<VehicleProfile> list = em
				.createQuery(
						"SELECT q FROM VehicleProfile q WHERE q.status=? ORDER BY selfid").setParameter(1, VehicleProfile.statusA)
				.setFirstResult(pagenum * lotsize - lotsize)
				.setMaxResults(lotsize).getResultList();
		Long count = (Long) em.createQuery(
				"SELECT count(q) FROM VehicleProfile q WHERE q.status=?").setParameter(1, VehicleProfile.statusA).getSingleResult();
		map.put("list", list);
		map.put("count", count);
		return map;
	}
	

	/**
	 * Select vehicles by statement
	 * 
	 * @param pagenum
	 * @param lotsize
	 * @param statement
	 * @return
	 */
	public Map getVehicleProfilesBySelector(int pagenum, int lotsize, String statement) {
		Map map = new HashMap<String, Object>();
		List<VehicleProfile> list = em.createQuery(statement)
				.setFirstResult(pagenum * lotsize - lotsize)
				.setMaxResults(lotsize).getResultList();
		String substatement = statement.substring(statement.indexOf("FROM"),
				statement.indexOf("ORDER BY"));
		substatement = "SELECT count(q) " + substatement;
		Long count = (Long) em.createQuery(substatement).getSingleResult();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Create new vehicle
	 * 
	 * @param profile
	 * @throws Exception
	 */
	@Transactional
	public void saveVehicle(VehicleProfile profile, Account user) throws Exception {
		VehicleLog vl = new VehicleLog();
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setWho(user);
		if (profile.getId() == null) {
			profile.setStatus("A");
			profile.setCreator(user);
			em.persist(profile);
			vl.setAction(VehicleLog.CREATE);
			vl.setRemark("创建了车辆档案"+profile.getVid());
		} else {
			em.merge(profile);
			vl.setAction(VehicleLog.UPDATE);
			vl.setRemark("更新了车辆档案"+profile.getVid());
		}
		em.flush();
		vl.setRecordid(profile.getId()+"");
		em.persist(vl);
	}

	/**
	 * Delete vehicle profile given its ID, onlu for vehicles no referenced
	 * 
	 * @param parseInt
	 * @throws Exception
	 */
	@Transactional
	public void deleteVehicle(int id, Account user) throws Exception {
		VehicleProfile vp = em.find(VehicleProfile.class, id);
		VehicleLog vl = new VehicleLog();
		vl.setAction(VehicleLog.DELETE);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setWho(user);
		vl.setRecordid(vp.getId()+"");
		vl.setRemark("车牌:"+vp.getVid());
		em.persist(vl);
		em.remove(vp);
	}

	/**
	 * Get vehicle profile by id
	 * 
	 * @param parseInt
	 * @return
	 * @throws Exception
	 */
	public VehicleProfile getVehicleProfileById(int id) throws Exception {
		return em.find(VehicleProfile.class, id);
	}

//	/**
//	 * Save mile by vehicle
//	 * 
//	 * @param mile
//	 * @throws Exception
//	 */
//	@Transactional
//	public void saveVehicleMile(VehicleMiles mile) throws Exception {
//		if (mile.getVehicle() == null || mile.getVehicle().getId() == null)
//			throw new Exception("No vid provided.");
//		VehicleProfile vp = em.find(VehicleProfile.class, mile.getVehicle()
//				.getId());
//		mile.setVehicle(vp);
//		mile.calculate();
//		em.persist(mile);
//	}
//
//	/**
//	 * update existing vehicle miles row given VehicleMiles Object
//	 * 
//	 * @param editmile
//	 * @throws Exception
//	 */
//	@Transactional
//	public void updateVehicleMiles(VehicleMiles editmile) throws Exception {
//		VehicleMiles vm = em.find(VehicleMiles.class, editmile.getId());
//		vm.copy(editmile);
//		vm.calculate();
//		em.merge(vm);
//	}
//
//	/**
//	 * 
//	 * @param vid
//	 * @throws Exception
//	 */
//	@Transactional
//	public void removeVehicleMiles(String vid) throws Exception {
//		VehicleMiles vm = em.find(VehicleMiles.class, Integer.parseInt(vid));
//		em.remove(vm);
//	}

	/**
	 * save the vehicle file ,mostly image files linked to VehicleCheck object
	 * 
	 * @param vf
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public VehicleFiles saveVehicleFile(VehicleFiles vf) throws Exception {
		if (vf.getId() == null) {
			em.persist(vf);
			em.flush();
			return vf;
		} else {
			em.merge(vf);
			return vf;
		}
	}

	/**
	 * save the VehicleCheck object
	 * 
	 * @param check
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveVehicleCheck(VehicleCheck check) throws Exception {
		em.persist(check);
	}

	/**
	 * Get Vehicle Check for 一保 and 二保
	 * 
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeMaintennance(Integer id)
			throws Exception {
		try {
			return em
					.createQuery(
							"SELECT q FROM VehicleCheck q WHERE (ctype=? OR ctype=?) AND vid=? ORDER BY cdate DESC")
					.setParameter(1, "一保").setParameter(3, id).setParameter(2, "二保")
					.getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	public VehicleCheck getVehicleCheckById(int id) throws Exception {
		return em.find(VehicleCheck.class, id);
	}

	/**
	 * update vehicle check
	 * 
	 * @param vc
	 * @throws Exception
	 */
	@Transactional
	public void updateVehicleCheck(VehicleCheck vc) throws Exception {
		em.merge(vc);
	}

	/**
	 * Get types for 小修，中修，大修
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeRepaire(Integer id) throws Exception {
		try {
			return em
					.createQuery(
							"SELECT q FROM VehicleCheck q WHERE (ctype=? OR ctype=?  OR ctype=?) AND vid=? ORDER BY cdate DESC")
					.setParameter(1, "小修").setParameter(2, "中修")
					.setParameter(3, "大修").setParameter(4, id).getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Get types for 综合
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeFullCheck(Integer id) throws Exception {
		try {
			return em
					.createQuery(
							"SELECT q FROM VehicleCheck q WHERE ctype=? AND vid=? ORDER BY cdate DESC")
					.setParameter(1, "综合").setParameter(2, id).getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Get types for 附件
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeExtras(Integer id) throws Exception {
		try {
			return em
					.createQuery(
							"SELECT q FROM VehicleCheck q WHERE ctype=? AND vid=? ORDER BY cdate DESC")
					.setParameter(1, "附件").setParameter(2, id).getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Get types for 年审
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeAnnul(Integer id) throws Exception {
		try {
			return em
					.createQuery(
							"SELECT q FROM VehicleCheck q WHERE ctype=? AND vid=? ORDER BY cdate DESC")
					.setParameter(1, "年审").setParameter(2, id).getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Delete vehicle check record and its file
	 * 
	 * @param checkId
	 * @throws Exception
	 */
	@Transactional
	public void removeVehicleCheck(String checkId) throws Exception {
		VehicleCheck vc = em
				.find(VehicleCheck.class, Integer.parseInt(checkId));
		VehicleFiles image = vc.getImage();
		em.remove(vc);
		em.flush();
		File delF = new File(image.getIpath());
		if (delF.exists())
			delF.delete();
		em.remove(image);
	}

	/**
	 * Throw the vehicle，报废. change status to E FROM A and give a throw date
	 * 
	 * @param vid
	 * @param dateVal
	 * @throws Exception
	 */
	@Transactional
	public void throwVehicle(String vid, Date dateVal, Account user) throws Exception {
		VehicleProfile vp = em
				.find(VehicleProfile.class, Integer.parseInt(vid));
		if (vp.getStatus() != null && vp.getStatus().equals("E"))
			return;
		vp.setStatus("E");
		vp.setThrowDate(dateVal);
		em.merge(vp);
		VehicleLog vl = new VehicleLog();
		vl.setAction(VehicleLog.THROW);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setWho(user);
		vl.setRecordid(vp.getId()+"");
		vl.setRemark("报废车辆:"+vp.getVid()+" 日期:"+HRUtil.parseDateToString(dateVal));
		em.persist(vl);
	}

	/**
	 * Get VehicleProfile by VID , or null if not exists
	 * 
	 * @param vid
	 * @return
	 */
	public VehicleProfile getVehicleProfileByVid(String vid) {
		try {
			return (VehicleProfile) em
					.createQuery("SELECT q FROM VehicleProfile q WHERE vid=?")
					.setParameter(1, vid).getSingleResult();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 获取车牌号为vid ，自编号为selfId的车辆。
	 * 优先搜索车牌号
	 * @param vid
	 * @param selfId
	 * @return
	 */
	public VehicleProfile getVehicleProfileLikeVid(String vid, String selfId) {
		try {
			return (VehicleProfile) em
					.createQuery("SELECT q FROM VehicleProfile q WHERE vid LIKE '%"+ vid+"%'")
					.getSingleResult();
		} catch (Exception e) {
			try{
				return (VehicleProfile) em
							.createQuery("SELECT q FROM VehicleProfile q WHERE vid LIKE '%"+ HRUtil.removeNoneNumber(vid)+"%' AND selfid LIKE '%"+ HRUtil.removeNoneNumber(selfId)+"%' ")
							.getSingleResult();
			}catch(Exception e2){
				System.out.println(e2.getMessage());
				return null;
			}
		}
	}

	/**
	 * Save vehicle details from file See:ExcelFileSaver.java
	 * @param saver
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public String saveVehicleProfilesFromFile(ExcelFileSaver saver)
			throws Exception {
		String str = "";
		int cast = 0;
		try {
			while (saver.hasNextLine()) {
				if (saver.strLine.contains("车牌号")) {
					String fullDetail = saver.strLine;
					while (saver.hasNextLine()&& !saver.strLine.contains("汽车技术档案卡")) {
						fullDetail += "," + saver.strLine;
					}
					String vidStr = saver.getValueFromName(fullDetail, "车牌号");
					if(vidStr.trim().equals("")){
						str += "E-No vid:["+fullDetail+"].<br/>";
						continue;
					}
					String selfid = null;
					String vid = null;
					if(vidStr.indexOf("(") == -1){
						selfid = "";
						vid = vidStr.trim();
					}else{
						selfid = vidStr.substring(vidStr.indexOf("(")+1,vidStr.indexOf(")"));
						vid = vidStr.substring(0,vidStr.indexOf("(")).trim();
					}
					VehicleProfile vp = getVehicleProfileLikeVid(vid, selfid);
					if(vp == null){
						str += "E-No vehicleFound for :["+vidStr+"].<br/>";
						continue;
					}
					
//					vp.setCompany(saver.getValueFromName(fullDetail, "车属单位"));
//					vp.setCompanyaddr(saver.getValueFromName(fullDetail, "地址"));
//					String recordid = saver.getValueFromName(fullDetail, "登记证编号");
//					if(!recordid.trim().equals(""))
//						vp.setRecordid(recordid);
//					vp.setDatejoin(HRUtil.parseDate(
//							saver.getValueFromName(fullDetail, "入户日期"),
//							"yyyy/MM/dd"));
					VehicleTechnicalDetail vtd = getVehicleTechnicalDetailByVehicleId(vp.getId());
					VehicleBasicInfo vbi = getVehicleBasicInfoByVehicleId(vp.getId());
					if(vtd == null){
						vtd = new VehicleTechnicalDetail();
						vtd.setVid(vp);
					}
					if(vbi == null){
						vbi = new VehicleBasicInfo();
						vbi.setVid(vp);
					}
					vp.setPurchaseCode(saver.getValueFromName(fullDetail,
							"购置凭证税号"));
					vbi.setSource(saver.getValueFromName(fullDetail, "车辆来源"));
//					vp.setDateuse(HRUtil.parseDate(
//							saver.getValueFromName(fullDetail, "运行日期"),
//							"yyyy/MM/dd"));
					vp.setPurchaseDate(HRUtil.parseDate(
							saver.getValueFromName(fullDetail, "购进时间"),
							"yyyy年MM月"));
					vbi.setOperatenumber(saver
							.getValueFromName(fullDetail, "使用性质"));
					vtd.setEnginecode(saver.getValueFromName(fullDetail, "发动机号"));
					
//					String price = saver.getValueFromName(fullDetail, "车价+购置费");
//					if (price != null && !price.equals("")) {
//						String ps[] = price.split("\\+");
//						vp.setVehicleprice(ps[0]);
//						if (ps.length > 1)
//							vp.setSubprice(ps[1]);
//					}
					vtd.setVincode(saver.getValueFromName(fullDetail, "车架号码"));
					vp.setVcolor(saver.getValueFromName(fullDetail, "车身颜色"));
					vtd.setFactorydate(HRUtil.parseDate(
							saver.getValueFromName(fullDetail, "车辆出厂日期"),
							"yyyy/MM/dd"));
					vtd.setFactorycode(saver.getValueFromName(fullDetail, "车辆厂牌/型号"));
					vtd.setVtype(saver.getValueFromName(fullDetail, "车辆类型"));
//					vp.setProductionaddr(saver.getValueFromName(fullDetail,
//							"车辆制造企业名称"));
					vtd.setBasecode(saver.getValueFromName(fullDetail, "底盘型号"));
					vtd.setVlevel(saver.getValueFromName(fullDetail, "评定等级"));
					vtd.setMadein(saver.getValueFromName(fullDetail, "车辆产地"));
					vtd.setEnginemodel(saver.getValueFromName(fullDetail,"发动机号型号"));
					vtd.setSits(saver.getValueFromName(fullDetail, "座位"));

					vtd.setTyre(saver.getValueFromName(fullDetail, "轮胎数/轮胎形式"));
//					String tyre = saver
//							.getValueFromName(fullDetail, "轮胎数/轮胎形式");
//					if (tyre != null && !tyre.equals("")) {
//						String tyres[] = tyre.split("/");
//						if (tyres.length < 2)
//							tyres = tyre.split("∕");
//						vp.setTyrenum(tyres[0]);
//						if (tyres.length > 1)
//							vp.setTyremodel(tyres[1]);
//					}else{
//						vp.setTyrenum(saver.getValueFromName(fullDetail, "轮胎数"));
//					}
//					vp.setBodysize(saver.getValueFromName(fullDetail, "车辆外型尺寸"));
					vtd.setFueltype(saver.getValueFromName(fullDetail, "燃料"));
					vtd.setEnginepower(saver.getValueFromName(fullDetail, "排量/功率"));
					
//					vp.setSubsides(saver.getValueFromName(fullDetail, "车厢配置"));

//					String speed = saver.getValueFromName(fullDetail, "最高车速");
//					vp.setVspeed(saver.removeNoneNumber(speed));
					
//					vp.setTurntype(saver.getValueFromName(fullDetail, "转向型式"));
					vtd.setTurner(saver.getValueFromName(fullDetail, "转向器式"));
					
//					vp.setMovebreak(saver
//							.getValueFromName(fullDetail, "行车制动形式"));
//					vp.setWheelbase(Integer.parseInt(saver
//							.removeNoneNumber(saver.getValueFromName(
//									fullDetail, "轴距"))));
//					vp.setStopbreak(saver
//							.getValueFromName(fullDetail, "驻车制动形式"));
//					vp.setFrontwheel(Integer.parseInt(saver
//							.removeNoneNumber(saver.getValueFromName(
//									fullDetail, "前轮距"))));
//					vp.setTotalweight(Integer.parseInt(saver
//							.removeNoneNumber(saver.getValueFromName(
//									fullDetail, "总质量"))));
//					vp.setBackwheel(Integer.parseInt(saver
//							.removeNoneNumber(saver.getValueFromName(
//									fullDetail, "后轮距"))));
//					vp.setSubweight(Integer.parseInt(saver
//							.removeNoneNumber(saver.getValueFromName(
//									fullDetail, "整车备质量"))));
//					vp.setHangmodel(saver.getValueFromName(fullDetail, "悬挂形式"));
					vtd.setDrivermode(saver.getValueFromName(fullDetail, "驱动形式"));
//					vp.setAircond(saver.getValueFromName(fullDetail, "空调"));

					em.merge(vp);
					if(vtd.getId() == null){
						em.persist(vtd);
					}else{
						em.merge(vtd);
					}
					if(vbi.getId() == null){
						em.persist(vbi);
					}else{
						em.merge(vbi);
					}
					System.out.println("##############CAST :" + (++cast) + " "
							+ vp.getVid());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

//	/**
//	 * Save vehicle repair records from file See:ExcelFileSaver.java
//	 * @param excelFileSaver
//	 * @return
//	 */
//	@Transactional
//	public String saveVehicleRepaireDatesFromFile(ExcelFileSaver saver) throws Exception{
//		String str = "";
//		int cast = 0;
//		//Get year at first line
//		saver.hasNextLine();
//		String yearString = saver.strLine; 
//		try {
//			while (saver.hasNextLine()) {
//				if (saver.strLine.contains("车牌号码")) {
//					List<String> profile = new ArrayList<String>();
//					String vidStr = saver.strLine;
//					while (saver.hasNextLine()
//							&& !saver.strLine.contains("填表人")) {
//						profile.add(saver.strLine);
//					}
//					String vid = saver.removeNoneNumber(vidStr.split(",")[1]);
//					String selfId = saver.getValueFromName(vidStr, "自编号");
//					VehicleProfile vp = this.getVehicleProfileLikeVid(vid,selfId);
//					if(vp == null){
//						str += "NH - VID:"+vid +".<br/>";
////						throw new Exception("No vehicle found for vid:"+vid);
//						continue;
//					}
//					VehicleMiles  vm = this.getVehicleMilesByVidAndYear(vp.getId(),Integer.parseInt(yearString));
//					if(vm == null){
//						vm = new VehicleMiles();
//						vm.setVehicle(vp);
//						vm.setVyear(Integer.parseInt(yearString));
//					}
//					if(profile.size() < 15){
//						str += "EINFO - VID:" + vid+"<br/>";
////						throw new Exception("Not enough vehicle info ("+profile.size()+") for vid:"+vid);
//						continue;
//					}
//					for(int i=2;i<14;i++){
//						String line = profile.get(i);
//						String lineArr[] = line.split(",",-1);
//						if(lineArr.length < 9){
//							str += "ELINFO - VID:"+vid + " Line " + i + " length too short "+lineArr.length+". ["+line+"]<br/>";
////							throw new Exception("ELINFO - VID:"+vid + " Line " + i + " length too short.<br/>");
//							continue;
//						}
//						String monthIndex = lineArr[0];
//						String drivers = lineArr[1];
//						String miles = lineArr[2];
//						String firstMaint = lineArr[3];
//						String secMaint = lineArr[4];
//						String check = lineArr[5]; 
//						String annul = lineArr[6];
//						String midRepaire = lineArr[7];
//						String bigRepaire = lineArr[8];
//						Integer month = Integer.parseInt(monthIndex);
//						
//						
//						//Set miles
//						float mile = 0;
//						if(miles != null && !miles.trim().equals(""))
//							mile = Float.parseFloat(miles);
//						switch(month){
//						case 1:
//							vm.setJan(mile);
//							break;
//						case 2:
//							vm.setFeb(mile);
//							break;
//						case 3:
//							vm.setMar(mile);
//							break;
//						case 4:
//							vm.setApr(mile);
//							break;
//						case 5:
//							vm.setMay(mile);
//							break;
//						case 6:
//							vm.setJun(mile);
//							break;
//						case 7:
//							vm.setJul(mile);
//							break;
//						case 8:
//							vm.setAug(mile);
//							break;
//						case 9:
//							vm.setSep(mile);
//							break;
//						case 10:
//							vm.setOcto(mile);
//							break;
//						case 11:
//							vm.setNov(mile);
//							break;
//						case 12:
//							vm.setDece(mile);
//							break;
//						default:
//							str += "EMINFO - VID:"+vid+ " month index:"+month+ " has error.<br/>";
//							throw new Exception("EMINFO - VID:"+vid+ " month index:"+month+ " has error.<br/>");
//						}
//						
//						//Check maintenance
//						if(firstMaint != null && !firstMaint.equals("")){
//							String dates[] = firstMaint.split(" ");
//							for(String date:dates){
//								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
//								VehicleCheck tempVC = new VehicleCheck();
//								tempVC.setVehicle(vp);
//								tempVC.setCdate(tempD);
//								tempVC.setCtype(VehicleCheck.ctypes[0]);
//								em.persist(tempVC);
//							}
//						}
//						
//						//Check 2nd maintenance
//						if(secMaint != null && !secMaint.trim().equals("")){
//							String dates[] = secMaint.split(" ");
//							for(String date:dates){
//								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
//								VehicleCheck tempVC = new VehicleCheck();
//								tempVC.setVehicle(vp);
//								tempVC.setCdate(tempD);
//								tempVC.setCtype(VehicleCheck.ctypes[1]);
//								em.persist(tempVC);
//							}
//						}
//						
//						//Check 2nd maintenance
//						if(secMaint != null && !secMaint.trim().equals("")){
//							String dates[] = secMaint.split(" ");
//							for(String date:dates){
//								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
//								VehicleCheck tempVC = new VehicleCheck();
//								tempVC.setVehicle(vp);
//								tempVC.setCdate(tempD);
//								tempVC.setCtype(VehicleCheck.ctypes[1]);
//								em.persist(tempVC);
//							}
//						}
//						
//						//Check mid repair
//						if(midRepaire != null && !midRepaire.trim().equals("")){
//							String dates[] = midRepaire.split(" ");
//							for(String date:dates){
//								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
//								VehicleCheck tempVC = new VehicleCheck();
//								tempVC.setVehicle(vp);
//								tempVC.setCdate(tempD);
//								tempVC.setCtype(VehicleCheck.ctypes[3]);
//								em.persist(tempVC);
//							}
//						}
//						
//						//Check big repair
//						if(bigRepaire != null && !bigRepaire.trim().equals("")){
//							String dates[] = bigRepaire.split(" ");
//							for(String date:dates){
//								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
//								VehicleCheck tempVC = new VehicleCheck();
//								tempVC.setVehicle(vp);
//								tempVC.setCdate(tempD);
//								tempVC.setCtype(VehicleCheck.ctypes[4]);
//								em.persist(tempVC);
//							}
//						}
//						
//						//Check full repair
//						if(check != null && !check.trim().equals("")){
//							String dates[] = check.split(" ");
//							for(String date:dates){
//								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
//								VehicleCheck tempVC = new VehicleCheck();
//								tempVC.setVehicle(vp);
//								tempVC.setCdate(tempD);
//								tempVC.setCtype(VehicleCheck.ctypes[5]);
//								em.persist(tempVC);
//							}
//						}
//						
//						//Check annul
//						if(annul != null && !annul.trim().equals("")){
//							Date tempD = HRUtil.parseDate(annul, "yyy-MM-dd");
//							VehicleCheck tempVC = new VehicleCheck();
//							tempVC.setVehicle(vp);
//							tempVC.setCdate(tempD);
//							tempVC.setCtype(VehicleCheck.ctypes[6]);
//							em.persist(tempVC);
//						}
//					}
//					
//					if(vm.getId() == null){
//						vm.calculate();
//						em.persist(vm);
//					}
//					else
//						em.merge(vm);
//					
//					cast ++;
//					System.out.println("##############CAST :" + (++cast) + " "
//							+ vp.getVid());
//				}
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			em.getTransaction().rollback(); // to terminate transactions;
//		}
//		return str;
//	}
//
//	/**
//	 * 
//	 * @param id
//	 * @param parseInt
//	 * @return
//	 */
//	public VehicleMiles getVehicleMilesByVidAndYear(Integer id, int year) {
//		try{
//			VehicleMiles vm = (VehicleMiles) em.createQuery("SELECT q FROM VehicleMiles q WHERE vid=? AND vyear=?")
//					.setParameter(1, id).setParameter(2,year).getSingleResult();
//			return vm;
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			return null;
//		}
//	}
//
//	/**
//	 * Save team name and leader
//	 * @param saver
//	 * @return
//	 * @throws Exception
//	 */
//	@Transactional
//	public String saveTeamNameAndLeaderFromFile(ExcelFileSaver saver) throws Exception{
//		String str = "";
//		int cast = 0;
//		try {
//			while (saver.hasNextLine()) {
//				if(saver.strLine == null || saver.strLine.equals(""))
//					continue;
//				String vals[] = saver.strLine.split(",");
//				String teamName = vals[0];
//				String teamLeader = vals[1];
//				VehicleTeam team = getTeamByName(teamName);
//				if(team == null){
//					team = new VehicleTeam();
//					team.setName(teamName);
//					em.persist(team);
//					em.flush();
//				}
//				Employee emp = null;
//				try{
//					emp = (Employee) em.createQuery("SELECT q FROM Employee q WHERE fullname=?")
//						.setParameter(1, teamLeader).getSingleResult();
//				}catch(Exception e){
//					System.out.println("No such Employee name:"+teamLeader);
//				}
//				if(emp == null){
//					str += "NH - "+teamLeader +". <br/>";
//					continue;
//				}
//				VehicleTeamLeader leader = getLeaderByTeamOrLeader(teamName,teamLeader);
//				if(leader == null){
//					leader = new VehicleTeamLeader();
//					leader.setLeader(emp);
//					leader.setTeam(team);
//					em.persist(leader);
//				}
//				System.out.println("###########Casted "+ (++cast));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			em.getTransaction().rollback(); // to terminate transactions;
//		}
//		return str;
//	}

	/**
	 * Set either be null or both not null
	 * @param teamName
	 * @param teamLeader
	 * @return
	 */
	public VehicleTeamLeader getLeaderByTeamOrLeader(String teamName,
			String teamLeader) {
		try{
			if(teamName == null){
				return (VehicleTeamLeader) em.createQuery("SELECT q FROM VehicleTeamLeader q WHERE q.leader.fullname='"+teamLeader+"'").getSingleResult();
			}else if(teamLeader == null){
				return (VehicleTeamLeader) em.createQuery("SELECT q FROM VehicleTeamLeader q WHERE q.team.name='"+teamName+"'").getSingleResult();
			}else{
				return (VehicleTeamLeader) em.createQuery("SELECT q FROM VehicleTeamLeader q WHERE q.leader.fullname=? AND q.team.name=?")
						.setParameter(1, teamLeader).setParameter(2, teamName).getSingleResult();
			}
		}catch(Exception e){
			System.out.println("No such team leader exist yet:"+teamName+" "+teamLeader);
			return null;
		}
	}

	/**
	 * Get a team by its name
	 * @param teamName
	 * @return
	 */
	public VehicleTeam getTeamByName(String teamName) {
		try{
			return (VehicleTeam) em.createQuery("SELECT q FROM VehicleTeam q WHERE name=?").setParameter(1, teamName).getSingleResult();
		}catch(Exception e){
			System.out.println("No such team exist:"+teamName);
			return null;
		}
	}

	/**
	 * Save govn bus from file ,new vehicle
	 * @param saver
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public String saveNewVehicleFromFile(ExcelFileSaver saver) throws Exception{
		String str = "";
		int cast = 0;
		try {
			while (saver.hasNextLine()) {
				String vals[] = saver.strLine.split("," ,-1);
				if(vals.length < 15){
					str += "Length too short:["+saver.strLine+"].<br/>";
					continue;
				}
				String selfid = vals[0];
				String vid = vals[1];
				if(vid.equals("") && selfid.equals("")){
					str += "Empty String meet ["+saver.strLine+"]<br/>.";
					continue;
				}
				String basedModel = vals[2];
				String engineModel="";
				String engineNum = "";
				if(vals[3] != null && !vals[3].equals("")){
					String engine[] = vals[3].split("、");
					engineModel  = engine[0];
					if(engine.length > 1)
						engineNum = engine[1];
				}
				String frameNum = vals[4];
				String totalWeight  = vals[5];
				String color = vals[6];
				String model = vals[7];
				String bodysize = vals[8];
				String vlevel = vals[9];
				String sits = vals[10];
				String fuel = vals[11];
				String subsides = vals[12];
				String joindate = vals[13];
				String dateinvalide = vals[14];
				String productionCode = vals[15];
				
				VehicleProfile vp = getVehicleProfileLikeVid(vid, selfid);
				if(vp != null){
					System.out.println("Vehicle :" + vid + " "+ selfid+ " exists.<br/>");
//					continue;
				}else{
					vp = new VehicleProfile();
				}
				vp.setVid(vid);
				vp.setSelfid(selfid);
				vp.setVcolor(color);
				vp.setJoinDate(HRUtil.parseDate(joindate, "yyyy/MM/dd"));
				vp.setForcethrowdate(HRUtil.parseDate(dateinvalide, "yyyy/MM/dd"));
				vp.setStatus("A");
				
				VehicleTechnicalDetail vtd = null;
				if(vp.getId() != null)
					vtd = getVehicleTechnicalDetailByVehicleId(vp.getId());
				if(vtd == null)
						vtd = new VehicleTechnicalDetail();
				vtd.setBasecode(basedModel);
				vtd.setEnginemodel(engineModel);
				vtd.setEnginecode(engineNum);
				vtd.setVincode(frameNum);
				vtd.setWeight(totalWeight);
				vtd.setFactorycode(model);
				if(bodysize != null){
					String[] size = bodysize.split("\\*",-1);
					if(size.length == 3){
						vtd.setVlength(size[0]);
						vtd.setVwidth(size[1]);
						vtd.setVheight(size[2]);
					}
				}
				vtd.setVlevel(vlevel);
				vtd.setSits(sits);
				vtd.setFueltype(fuel);
				if(subsides.equals("冷气"))
					vtd.setOther("空调器");
				if(vp.getId() == null){
					em.persist(vp);
					em.flush();
				}else{
					em.merge(vp);
				}
				vtd.setVid(vp);
				if(vtd.getId() == null){
					em.persist(vtd);
				}else{
					em.merge(vtd);
				}
				
				System.out.println("###########Casted "+ (++cast));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * Save team and lane from file
	 * @param saver
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public String saveTeamAndLaneFromFile(ExcelFileSaver saver) throws Exception{
		String str = "";
		int cast = 0;
		try {
			String team[] = null;
			while (saver.hasNextLine()) {
				if(saver.strLine.contains("team")){
//					System.out.println("Team found:"+saver.strLine);
					cast = 0;
					team = saver.strLine.split("\\:",-1);
					if(team.length != 2){
						str += "Team:"+saver.strLine+" length too short.<br/>";
						throw new Exception(str);
					}
				}else{
					if(team == null){
						str += "Team:["+saver.strLine+"] No team assign.<br/>";
						continue;
					}
					String vals[] = saver.strLine.split(",",-1);
					if(vals.length < 9){
						str += "EDetail:["+saver.strLine+"] Not valid record.<br/>";
						continue;
					}
					String selfId = vals[1];
					String vid = vals[2];
//					System.out.println(vals[7] + " C:"+saver.removeNoneNumber(vals[7]));
					String laneNum = saver.removeNoneNumber(vals[7]);
					String laneDetail = vals[8];
					
					VehicleProfile vp = getVehicleProfileLikeVid(vid, selfId);
					if(vp == null){
						str += "ENH vid:["+saver.strLine+"].<br/>";
						throw new Exception("Cannot find vehicle ["+vid+"].");
					}
					
					//To assign team
					VehicleTeam vteam = getTeamByName(team[1]);
					if(vteam == null){
						str += "ENH vid:["+saver.strLine+"].<br/>";
						throw new Exception("Cannot find team ["+team[1]+"].");
					}
					VehicleTeamMember vtm = getVehicleTeamMemberByVpId(vp.getId());
					if(vtm == null){
						vtm = new VehicleTeamMember();
						vtm.setVehicle(vp);
						vtm.setTeam(vteam);
						em.persist(vtm);
					}
					
					//To map to lane
					VehicleLane vlane = null;
					if(laneNum.equals("")){
						System.out.println("Might be movable Lane number, set to 0.");
						laneNum = "0";
//						throw new Exception("stop");
					}
					vlane = getVehicleLaneByLaneNum(laneNum);
					if(vlane == null){
						vlane = new VehicleLane();
						vlane.setNum(laneNum);
						vlane.setDetail(laneDetail);
						em.persist(vlane);
						em.flush();
					}
					VehicleLaneMapper laneMapper = getVehicleLaneMapperByVpId(vp.getId());
					if(laneMapper == null){
						laneMapper = new VehicleLaneMapper();
						laneMapper.setLane(vlane);
						laneMapper.setVehicle(vp);
						em.persist(laneMapper);
						em.flush();
					}
				}
				System.out.println("###########Casted "+team[1]+" :"+ (++cast));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * Get vehicle lane mapper by profile d since oneToOne Mapping
	 * @param id
	 * @return
	 */
	public VehicleLaneMapper getVehicleLaneMapperByVpId(Integer id) {
		try{
			return (VehicleLaneMapper) em.createQuery("SELECT q FROM VehicleLaneMapper q WHERE vid=?").setParameter(1, id).getSingleResult();
		}catch(Exception e){
			System.out.println("Cannot find vehicle lane mapper with vp id:"+id);
			return null;
		}
	}

	/**
	 * 
	 * @param laneNum
	 * @param laneDetail
	 * @return
	 */
	private VehicleLane getVehicleLaneByLaneNum(String laneNum) {
		try{
			return (VehicleLane) em.createQuery("SELECT q FROM VehicleLane q WHERE num=?").setParameter(1, laneNum).getSingleResult();
		}catch(Exception e){
			System.out.println("Cannot find vehicle lane number :"+laneNum);
			return null;
		}
	}

	/**
	 * Get vehicle team member by profile id, as its one to one mapping
	 * @param id
	 * @return
	 */
	public VehicleTeamMember getVehicleTeamMemberByVpId(Integer id) {
		try{
			return (VehicleTeamMember) em.createQuery("SELECT q FROM VehicleTeamMember q WHERE vid=?").setParameter(1, id).getSingleResult();
		}catch(Exception e){
			System.out.println("Cannot find vehicle team member with vp id:"+id);
			return null;
		}
	}

	/**
	 * Save the 登记证号 to vehicles from file
	 * @param saver
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public String saveRecordIdsFromFile(ExcelFileSaver saver) throws Exception{
		String str = "";
		int cast = 0;
		try {
			while (saver.hasNextLine()) {
				String vals[] = saver.strLine.split(",",-1);
				if(vals.length != 3){
					str += "ELine length too shrot:["+saver.strLine+"].<br/>";
					continue;
				}
				String selfid = vals[0];
				String vid = vals[1];
				String recordid = vals[2];
				VehicleProfile vp = getVehicleProfileLikeVid(vid, selfid);
				if(vp == null){
					str += "EVP cannot find vehicle:["+saver.strLine+"].<br/>";
					continue;
				}
				VehicleBasicInfo vbi = getVehicleBasicInfoByVehicleId(vp.getId());
				if(vbi == null){
					vbi = new VehicleBasicInfo();
				}
				vbi.setRegisternumber(recordid);
				vbi.setVid(vp);
				if(vbi.getId() == null){
					em.persist(vbi);
				}else{
					em.merge(vbi);
				}
				System.out.println("###########Casted "+vid+" :"+ (++cast));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

//	/**
//	 * Assign to 公交一公司
//	 * @param saver
//	 * @return
//	 * @throws Exception
//	 */
//	@Transactional
//	public String assignVehiclesToCompanyOneFromFile (ExcelFileSaver saver) throws Exception{
//		String str = "";
//		int cast = 0;
//		try {
//			while (saver.hasNextLine()) {
//				String vals[] = saver.strLine.split(",",-1);
//				if(vals.length != 2){
//					str += "ELine length not valid:["+saver.strLine+"].<br/>";
//					continue;
//				}
//				String selfid = vals[0];
//				String vid = vals[1];
//				VehicleProfile vp = getVehicleProfileLikeVid(vid, selfid);
//				if(vp == null){
//					str += "EVP cannot find vehicle:["+saver.strLine+"].<br/>";
//					continue;
//				}
//				vp.setSubcompany("公交一");
//				em.merge(vp);
//				System.out.println("###########Casted "+vid+" :"+ (++cast));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			em.getTransaction().rollback(); // to terminate transactions;
//		}
//		return str;
//	}
//
//	/**
//	 * Save miles for a year
//	 * e.g.
//	 * 2011,,
//	 * 1月,,
//	 * selfid,vid,miles
//	 * selfid,vid,miles
//	 * 2月
//	 * selfid,vid,miles
//	 * @param saver
//	 * @return
//	 * @throws Exception
//	 */
//	@Transactional
//	public String saveMilesFromFile(ExcelFileSaver saver) throws Exception{
//		String str = "";
//		int cast = 0;
//		saver.hasNextLine();
//		int year = Integer.parseInt(saver.strLine.split(",",-1)[0]);
//		
//		Integer month = null;
//		try {
//			while (saver.hasNextLine()) {
//				if(saver.strLine.contains("月")){
//					month = Integer.parseInt(saver.removeNoneNumber(saver.strLine));
//					System.out.println("Set Month To:"+month);
//					continue;
//				}
//				String vals[] = saver.strLine.split(",",-1);
//				if(vals.length != 3){
//					str += "E - length:["+saver.strLine+"].<br/>";
//					continue;
//				}
//				String selfid = vals[0];
//				String vid = vals[1];
//				String miles = vals[2];
//				
//				//Get vehicle miles Obect
//				VehicleProfile vp = getVehicleProfileLikeVid(vid, selfid);
//				if(vp == null){
//					str += "E - NVP:["+saver.strLine+"].<br/>";
//					continue;
//				}
//				VehicleMiles vm = getVehicleMilesByVidAndYear(vp.getId(), year);
//				if(vm == null){
//					vm = new VehicleMiles();
//					vm.setVehicle(vp);
//				}
//				vm.setVyear(year);
//				//Set miles
//				float mile = 0;
//				if(miles != null && !miles.trim().equals(""))
//					mile = Float.parseFloat(miles);
//				switch(month){
//				case 1:
//					vm.setJan(mile);
//					break;
//				case 2:
//					vm.setFeb(mile);
//					break;
//				case 3:
//					vm.setMar(mile);
//					break;
//				case 4:
//					vm.setApr(mile);
//					break;
//				case 5:
//					vm.setMay(mile);
//					break;
//				case 6:
//					vm.setJun(mile);
//					break;
//				case 7:
//					vm.setJul(mile);
//					break;
//				case 8:
//					vm.setAug(mile);
//					break;
//				case 9:
//					vm.setSep(mile);
//					break;
//				case 10:
//					vm.setOcto(mile);
//					break;
//				case 11:
//					vm.setNov(mile);
//					break;
//				case 12:
//					vm.setDece(mile);
//					break;
//				default:
//					str += "EMINFO - VID:"+vid+ " month index:"+month+ " has error.<br/>";
//					throw new Exception("EMINFO - VID:"+vid+ " month index:"+month+ " has error.<br/>");
//				}
//				
//				vm.calculate();
//				if(vm.getId() == null)
//					em.persist(vm);
//				else
//					em.merge(vm);
//				em.flush();
//				
//				System.out.println("###########Casted "+vid+" :"+ (++cast));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			em.getTransaction().rollback(); // to terminate transactions;
//		}
//		return str;
//	}

	/**
	 * Get vehicles by team id
	 * @param teamId
	 * @return
	 * @throws Exception
	 */
	public Map getVehicleProfileByTeamId(String teamId) throws Exception{
		List<VehicleProfile> members = null;
		Long count = null;
		if(teamId == null || teamId.equals("")){
			members = em.createQuery("SELECT q FROM VehicleProfile q WHERE " +
					" NOT EXISTS (SELECT h FROM VehicleTeamMember h WHERE q.id=h.vehicle.id) ORDER BY q.selfid")
					.getResultList();
			count = (Long) em.createQuery("SELECT count(q) FROM VehicleProfile q WHERE " +
					" NOT EXISTS (SELECT h FROM VehicleTeamMember h WHERE q.id=h.vehicle.id)")
					.getSingleResult();
		}else{
			VehicleTeam team = em.find(VehicleTeam.class, Integer.parseInt(teamId));
			members = em.createQuery("SELECT q FROM VehicleProfile q WHERE" +
					" q.vehicleteam.team=:teama  ORDER BY q.selfid").setParameter("teama", team).getResultList();
			count = (Long) em.createQuery("SELECT count(q) FROM VehicleProfile q WHERE" +
					" q.vehicleteam.team=:teama").setParameter("teama", team).getSingleResult();
		}
		Map map  = new HashMap<String,Object>();
		map.put("list", members);
		map.put("count", count);
		return map;
	}

	public VehicleTeam getVehicleTeamByTeamId(String teamId) {
		try{
			return em.find(VehicleTeam.class, Integer.parseInt(teamId));
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	public List<VehicleTeam> getAllVehicleTeams() throws Exception{
		return em.createQuery("SELECT q FROM VehicleTeam q ORDER BY name").getResultList();
	}

	public Map getVehicleProfileByTeamStatement(String teamSelectorStatement) throws Exception{
		List<VehicleProfile> members = null;
		members = em.createQuery(teamSelectorStatement).getResultList();
		String substatem = teamSelectorStatement.substring(teamSelectorStatement.indexOf("FROM"), teamSelectorStatement.indexOf("ORDER BY"));
		substatem = "SELECT count(q) "+ substatem;
		Long count = (Long)em.createQuery(substatem).getSingleResult();
		Map map  = new HashMap<String,Object>();
		map.put("list", members);
		map.put("count", count);
		return map;
	}

	@Transactional
	public void saveTeam(VehicleTeam newTeam) throws Exception{
		em.persist(newTeam);
	}

	@Transactional
	public void removeTeam(String deleteId) throws Exception{
		VehicleTeam delTeam = getVehicleTeamByTeamId(deleteId);
		em.remove(delTeam);
	}

	@Transactional
	public void saveTeamLeader(VehicleTeamLeader l) throws Exception{
		em.persist(l);
	}

	@Transactional
	public void removeTeamLeader(VehicleTeam t, Employee e) throws Exception{
		VehicleTeamLeader leader = (VehicleTeamLeader) em.createQuery("SELECT q FROM VehicleTeamLeader q WHERE q.team=:teamt AND q.leader=:leadere")
				.setParameter("teamt", t).setParameter("leadere", e).getSingleResult();
		em.remove(leader);
	}

	@Transactional
	public void quitTeam(String teamId, List<String> selectedVehicles) throws Exception{
		for(String v:selectedVehicles){
			if(v == null){
				System.out.println(v);
				continue;
			}
			VehicleTeamMember member = (VehicleTeamMember) em.createQuery("SELECT q FROM VehicleTeamMember q WHERE" +
					" vid=? AND teamid=?").setParameter(1, Integer.parseInt(v)).setParameter(2, Integer.parseInt(teamId)).getSingleResult();
			em.createNativeQuery("DELETE FROM vehicleteammember WHERE vid="+Integer.parseInt(v)).executeUpdate();
		}
	}

	@Transactional
	public void JoinTeam(String teamId, List<String> selectedVehicles) throws Exception{
		for(String v:selectedVehicles){
			if(v == null){
				continue;
			}
			VehicleTeam t = getVehicleTeamByTeamId(teamId);
			VehicleProfile vp = getVehicleProfileById(Integer.parseInt(v));
			VehicleTeamMember member = getVehicleTeamMemberByVpId(vp.getId());
			if(member == null)
				member = new VehicleTeamMember();
			member.setTeam(t);
			member.setVehicle(vp);
			if(member.getId() == null)
				em.persist(member);
			else
				em.merge(member);
//			System.out.println("Done");
		}
	}

	public Map getAllVehicleLanes() throws Exception{
		List<VehicleLane> lanes = em.createQuery("SELECT q FROM VehicleLane q ORDER BY num").getResultList();
		System.out.println("get already");
		Long count = (Long) em.createQuery("SELECT count(q) FROM VehicleLane q").getSingleResult();
		Map map  = new HashMap<String,Object>();
		map.put("list", lanes);
		map.put("count", count);
		return map;
	}

	public VehicleLane getVehicleLaneById(String targetId) {
		try{
			return em.find(VehicleLane.class, Integer.parseInt(targetId));
		}catch(Exception e){
			return null;
		}
	}

	public boolean isRouteExist(VehicleLane newRoute){
		try{
			VehicleLane lane = (VehicleLane) em.createQuery("SELECT q FROM VehicleLane q WHERE num=? AND detail=?")
					.setParameter(1, newRoute.getNum().trim()).setParameter(2, newRoute.getDetail().trim()).getSingleResult();
			if(lane != null)
				return true;
			else
				return false;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}

	@Transactional
	public void saveVehicleLane(VehicleLane newRoute) throws Exception {
		em.persist(newRoute);
	}

	@Transactional
	public void removeVehicleLane(VehicleLane delRoute) throws Exception{
		VehicleLane lane = (VehicleLane) em.createQuery("SELECT q FROM VehicleLane q WHERE num=? AND detail=?")
				.setParameter(1, delRoute.getNum().trim()).setParameter(2, delRoute.getDetail().trim()).getSingleResult();
		em.remove(lane);
	}

	@Transactional
	public void joinVehiclesToRoute(VehicleLane newRoute,List<String> selectedVehicles) throws Exception{
		VehicleLane lane = (VehicleLane) em.createQuery("SELECT q FROM VehicleLane q WHERE num=? AND detail=?")
				.setParameter(1, newRoute.getNum().trim()).setParameter(2, newRoute.getDetail().trim()).getSingleResult();
		for(String v:selectedVehicles){
			VehicleProfile vp = getVehicleProfileById(Integer.parseInt(v));
			VehicleLaneMapper mapper = getVehicleLaneMapperByVpId(vp.getId());
			if(mapper == null){
				mapper = new VehicleLaneMapper();
			}
			mapper.setVehicle(vp);
			mapper.setLane(lane);
			if(mapper.getId() == null)
				em.persist(mapper);
			else
				em.merge(mapper);
		}
	}

	public List<VehicleLane> getAllVehicleLaneNames() throws Exception{
		List<VehicleLane> lanes = em.createNativeQuery("SELECT id,  num, detail FROM vehiclelane",VehicleLaneMirror.class).getResultList();
		return lanes;
	}

	public List<VehicleProfile> getVehiclesNoRoute() throws Exception{
		List<VehicleProfile> members;
		members = em.createQuery("SELECT q FROM VehicleProfile q WHERE " +
				" NOT EXISTS (SELECT h FROM VehicleLaneMapper h WHERE q.id=h.vehicle.id) ORDER BY q.selfid")
				.getResultList();
		return members;
	}



	/**
	 * 获取车辆技术档案资料
	 * @param vid
	 * @return
	 * @throws Exception
	 */
	public VehicleTechnicalDetail getVehicleTechnicalDetailByVehicleId(Integer vid) throws Exception{
		try{
			VehicleTechnicalDetail detail = (VehicleTechnicalDetail) em.createQuery("SELECT q FROM VehicleTechnicalDetail q WHERE q.vid.id=?")
					.setParameter(1, vid).getSingleResult();
			return detail;
		}catch(Exception e){
			e.printStackTrace();
			return new VehicleTechnicalDetail();
		}
	}

	/**
	 * 更新车辆技术档案
	 * @param vtech
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void saveVehicleTechDetail(VehicleTechnicalDetail vtech, Account user) throws Exception{
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		if(vtech.getId() == null){
			if(vtech.getVid().getId() == null){
				throw new Exception("该技术档案未绑定到有效的车辆档案中。");
			}
			VehicleProfile vp = this.getVehicleProfileById(vtech.getVid().getId());
			em.persist(vtech);
			vl.setAction(VehicleLog.CREATE);
			vl.setRemark("新建了技术档案For 车辆"+vp.getVid());
			em.persist(vl);
		}else{
			em.merge(vtech);
			vl.setAction(VehicleLog.UPDATE);
			vl.setRecordid(vtech.getId()+"");
			vl.setRemark("技术档案");
			em.persist(vl);
		}
	}


	/**
	 * 获取车辆档案基本信息
	 * @param vid
	 * @return
	 * @throws Exception
	 */
	public VehicleBasicInfo getVehicleBasicInfoByVehicleId(int vid) throws Exception{
		try{
			VehicleBasicInfo detail = (VehicleBasicInfo) em.createQuery("SELECT q FROM VehicleBasicInfo q WHERE q.vid.id=?")
					.setParameter(1, vid).getSingleResult();
			return detail;
		}catch(Exception e){
			return new VehicleBasicInfo();
		}
	}


	/**
	 * 更新车辆档案基础资料
	 * @param vBasic
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void saveVehicleBasicInfo(VehicleBasicInfo vBasic, Account user) throws Exception{
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		if(vBasic.getId() == null){
			if(vBasic.getVid().getId() == null){
				throw new Exception("该技术档案未绑定到有效的车辆档案中。");
			}
			VehicleProfile vp = this.getVehicleProfileById(vBasic.getVid().getId());
			vBasic.getVid().setCreator(vp.getCreator());
			vBasic.getVid().setStatus(vp.getStatus());
			em.merge(vBasic.getVid());
			em.persist(vBasic);
			vl.setAction(VehicleLog.CREATE);
			vl.setRemark("新建了档案基本资料For 车辆"+vBasic.getVid().getVid());
			em.persist(vl);
		}else{
			VehicleProfile vp = this.getVehicleProfileById(vBasic.getVid().getId());
			vBasic.getVid().setCreator(vp.getCreator());
			vBasic.getVid().setStatus(vp.getStatus());
			em.merge(vBasic.getVid());
			em.merge(vBasic);
			vl.setAction(VehicleLog.UPDATE);
			vl.setRecordid(vBasic.getId()+"");
			vl.setRemark("档案基本资料");
			em.persist(vl);
		}
	}


	/**
	 * 选择时间段内的维修记录
	 * @param vpid
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<VehicleRepairRecord> getVehicleRepairRecordsByTimeAndProfileId(int vpid, Date date1, Date date2) throws Exception{
		List<VehicleRepairRecord> list;
		if(date1 != null && date2 != null){
			list = em.createQuery("SELECT q FROM VehicleRepairRecord q WHERE q.vid.id="+vpid +" AND " 
					+"q.rdate >= '"+HRUtil.parseDateToString(date1)
					+"' AND q.rdate <= '"+HRUtil.parseDateToString(date2)+"' ORDER BY q.rdate DESC").getResultList();
		}else if(date1 != null){
			list = em.createQuery("SELECT q FROM VehicleRepairRecord q WHERE q.vid.id="+vpid +" AND "  
					+"q.rdate >= '"+HRUtil.parseDateToString(date1)
					+"' ORDER BY q.rdate DESC").getResultList();
		}else if(date2 != null){
			list = em.createQuery("SELECT q FROM VehicleRepairRecord q WHERE q.vid.id="+vpid +" AND " 
					+"q.rdate <= '"+HRUtil.parseDateToString(date2)
					+"' ORDER BY q.rdate DESC").getResultList();
		}else{
			list = em.createQuery("SELECT q FROM VehicleRepairRecord q WHERE q.vid.id="+vpid +" AND EXTRACT(year FROM q.rdate)=EXTRACT(year FROM now()) ORDER BY q.rdate DESC").getResultList();
		}
		return list;
	}


	/**
	 * 创建新的维修记录
	 * @param vRepair
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addNewRepairRecord(VehicleRepairRecord vRepair, Account user) throws Exception{
		if(vRepair.getVid() == null || vRepair.getVid().getId() == null)
			throw new Exception("车辆档案ID没有分配给维修记录档案");
		vRepair.setCreator(user);
		em.persist(vRepair);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.CREATE);
		vl.setRemark("维修记录:"+vRepair.getRtype());
		em.persist(vl);
	}
	
	/**
	 * 获取车辆主要部件更换记录详细
	 * @param vpid
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<VehiclePartsRepair> getVehiclePartsRepairByTimeAndProfileId(Integer vpid, Date date1, Date date2) throws Exception{
		List<VehiclePartsRepair> list;
		if(date1 != null && date2 != null){
			list = em.createQuery("SELECT q FROM VehiclePartsRepair q WHERE q.vid.id="+vpid +" AND " 
					+"q.changedate >= '"+HRUtil.parseDateToString(date1)
					+"' AND q.changedate <= '"+HRUtil.parseDateToString(date2)+"' ORDER BY q.changedate DESC").getResultList();
		}else if(date1 != null){
			list = em.createQuery("SELECT q FROM VehiclePartsRepair q WHERE q.vid.id="+vpid +" AND " 
					+"q.changedate >= '"+HRUtil.parseDateToString(date1)
					+"' ORDER BY q.changedate DESC").getResultList();
		}else if(date2 != null){
			list = em.createQuery("SELECT q FROM VehiclePartsRepair q WHERE q.vid.id="+vpid +" AND " 
					+"q.changedate <= '"+HRUtil.parseDateToString(date2)
					+"' ORDER BY q.changedate DESC").getResultList();
		}else{
			list = em.createQuery("SELECT q FROM VehiclePartsRepair q WHERE q.vid.id="+vpid +" AND EXTRACT(year FROM q.changedate)=EXTRACT(year FROM now()) ORDER BY q.changedate DESC").getResultList();
		}
		return list;
	}

	/**
	 * 添加车辆主要部件维修记录
	 * @param vParts
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addVehiclePartsRepair(VehiclePartsRepair vParts, Account user) throws Exception{
		if(vParts == null || vParts.getVid() == null || vParts.getVid().getId() == null)
			throw new Exception("车辆档案ID没有分配");
		vParts.setCreator(user);
		em.persist(vParts);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.CREATE);
		vl.setRemark("主要更换部件记录");
		em.persist(vl);
	}

	/**
	 * 添加车辆等级检查记录
	 * @param vLevel
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addVehicleLevelCheck(VehicleLevel vLevel, Account user) throws Exception{
		if(vLevel == null || vLevel.getVid() == null || vLevel.getVid().getId() == null)
			throw new Exception("车辆档案ID没有分配");
		vLevel.setCreator(user);
		em.persist(vLevel);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.CREATE);
		vl.setRemark("更新车辆等级检查");
		em.persist(vl);
	}


	/**
	 * 获取车辆等级评定记录
	 * @param vpid
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<VehicleLevel> getVehicleLevelCheckByTimeAndProfileId(
			int vpid, Date date1, Date date2) throws Exception{
		List<VehicleLevel> list;
		if(date1 != null && date2 != null){
			list = em.createQuery("SELECT q FROM VehicleLevel q WHERE q.vid.id="+vpid +" AND " 
					+"q.date >= '"+HRUtil.parseDateToString(date1)
					+"' AND q.date <= '"+HRUtil.parseDateToString(date2)+"' ORDER BY q.date DESC").getResultList();
		}else if(date1 != null){
			list = em.createQuery("SELECT q FROM VehicleLevel q WHERE q.vid.id="+vpid +" AND "  
					+"q.date >= '"+HRUtil.parseDateToString(date1)
					+"' ORDER BY q.date DESC").getResultList();
		}else if(date2 != null){
			list = em.createQuery("SELECT q FROM VehicleLevel q WHERE q.vid.id="+vpid +" AND " 
					+"q.date <= '"+HRUtil.parseDateToString(date2)
					+"' ORDER BY q.date DESC").getResultList();
		}else{
			list = em.createQuery("SELECT q FROM VehicleLevel q WHERE q.vid.id="+vpid +" AND EXTRACT(year FROM q.date)=EXTRACT(year FROM now()) ORDER BY q.date DESC").getResultList();
		}
		return list;
	}


	/**
	 * 获取车辆变更信息记录
	 * @param vpid
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<VehicleChange> getVehicleChangeByTimeAndProfileId(
			int vpid, Date date1, Date date2) throws Exception{
		List<VehicleChange> list;
		if(date1 != null && date2 != null){
			list = em.createQuery("SELECT q FROM VehicleChange q WHERE q.vid.id="+vpid +" AND " 
					+"q.date >= '"+HRUtil.parseDateToString(date1)
					+"' AND q.date <= '"+HRUtil.parseDateToString(date2)+"' ORDER BY q.date DESC").getResultList();
		}else if(date1 != null){
			list = em.createQuery("SELECT q FROM VehicleChange q WHERE q.vid.id="+vpid +" AND "  
					+"q.date >= '"+HRUtil.parseDateToString(date1)
					+"' ORDER BY q.date DESC").getResultList();
		}else if(date2 != null){
			list = em.createQuery("SELECT q FROM VehicleChange q WHERE q.vid.id="+vpid +" AND " 
					+"q.date <= '"+HRUtil.parseDateToString(date2)
					+"' ORDER BY q.date DESC").getResultList();
		}else{
			list = em.createQuery("SELECT q FROM VehicleChange q WHERE q.vid.id="+vpid +" AND EXTRACT(year FROM q.date)=EXTRACT(year FROM now()) ORDER BY q.date DESC").getResultList();
		}
		return list;
	}

	/**
	 * 添加车辆变更记录
	 * @param vChange
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addVehicleChange(VehicleChange vChange, Account user) throws Exception{
		if(vChange == null || vChange.getVid() == null || vChange.getVid().getId() == null)
			throw new Exception("车辆档案ID没有分配");
		vChange.setCreator(user);
		em.persist(vChange);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.CREATE);
		vl.setRemark("车辆变更记录");
		em.persist(vl);
	}


	/**
	 * 获取车辆使用记录
	 * @param vpid
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<VehicleUseRecord> getVehicleUseRecordByTimeAndProfileId(
			int vpid, Date date1, Date date2) throws Exception{
		List<VehicleUseRecord> list;
		if(date1 != null && date2 != null){
			list = em.createQuery("SELECT q FROM VehicleUseRecord q WHERE q.vid.id="+vpid +" AND " 
					+"q.date >= '"+HRUtil.parseDateToString(date1)
					+"' AND q.date <= '"+HRUtil.parseDateToString(date2)+"' ORDER BY q.date DESC").getResultList();
		}else if(date1 != null){
			list = em.createQuery("SELECT q FROM VehicleUseRecord q WHERE q.vid.id="+vpid +" AND "  
					+"q.date >= '"+HRUtil.parseDateToString(date1)
					+"' ORDER BY q.date DESC").getResultList();
		}else if(date2 != null){
			list = em.createQuery("SELECT q FROM VehicleUseRecord q WHERE q.vid.id="+vpid +" AND " 
					+"q.date <= '"+HRUtil.parseDateToString(date2)
					+"' ORDER BY q.date DESC").getResultList();
		}else{
			list = em.createQuery("SELECT q FROM VehicleUseRecord q WHERE q.vid.id="+vpid +" AND EXTRACT(year FROM q.date)=EXTRACT(year FROM now()) ORDER BY q.date DESC").getResultList();
		}
		return list;
	}


	/**
	 * 添加车辆使用记录
	 * @param vUseRecord
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addVehicleUseRecord(VehicleUseRecord vUseRecord, Account user) throws Exception{
		if(vUseRecord == null || vUseRecord.getVid() == null || vUseRecord.getVid().getId() == null)
			throw new Exception("车辆档案ID没有分配");
		vUseRecord.setCreator(user);
		em.persist(vUseRecord);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.CREATE);
		vl.setRemark("车辆使用记录");
		em.persist(vl);
	}


	/**
	 * 获取车辆事故记录
	 * @param vpid
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<VehicleAccident> getVehicleAccidentByTimeAndProfileId(
			int vpid, Date date1, Date date2) throws Exception{
		List<VehicleAccident> list;
		if(date1 != null && date2 != null){
			list = em.createQuery("SELECT q FROM VehicleAccident q WHERE q.vid.id="+vpid +" AND " 
					+"q.date >= '"+HRUtil.parseDateToString(date1)
					+"' AND q.date <= '"+HRUtil.parseDateToString(date2)+"' ORDER BY q.date DESC").getResultList();
		}else if(date1 != null){
			list = em.createQuery("SELECT q FROM VehicleAccident q WHERE q.vid.id="+vpid +" AND "  
					+"q.date >= '"+HRUtil.parseDateToString(date1)
					+"' ORDER BY q.date DESC").getResultList();
		}else if(date2 != null){
			list = em.createQuery("SELECT q FROM VehicleAccident q WHERE q.vid.id="+vpid +" AND " 
					+"q.date <= '"+HRUtil.parseDateToString(date2)
					+"' ORDER BY q.date DESC").getResultList();
		}else{
			list = em.createQuery("SELECT q FROM VehicleAccident q WHERE q.vid.id="+vpid +" AND EXTRACT(year FROM q.date)=EXTRACT(year FROM now()) ORDER BY q.date DESC").getResultList();
		}
		return list;
	}


	/**
	 * 添加车辆事故记录
	 * @param vAccident
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addVehicleAccident(VehicleAccident vAccident, Account user) throws Exception{
		if(vAccident == null || vAccident.getVid() == null || vAccident.getVid().getId() == null)
			throw new Exception("车辆档案ID没有分配");
		vAccident.setCreator(user);
		em.persist(vAccident);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.CREATE);
		vl.setRemark("车辆事故记录");
		em.persist(vl);
	}

	/**
	 * 修改车辆维修记录
	 * @param vRepair
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void editVehicleRepairRecord(VehicleRepairRecord vRepair,
			Account user) throws Exception{
		if(vRepair == null || vRepair.getId() == null)
			throw new Exception("");
		VehicleRepairRecord vrr = em.find(VehicleRepairRecord.class, vRepair.getId());
		vrr.setDescription(vRepair.getDescription());
		vrr.setRcompany(vRepair.getRcompany());
		vrr.setRdate(vRepair.getRdate());
		vrr.setRegistrant(vRepair.getRegistrant());
		vrr.setRtype(vRepair.getRtype());
		em.merge(vrr);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.UPDATE);
		vl.setRecordid(vrr.getId()+"");
		vl.setRemark("车辆维修记录");
		em.persist(vl);
	}

	/**
	 * 删除维修记录
	 * @param s
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeVehicleRepairRecord(String id, Account user) throws Exception{
		VehicleRepairRecord vrr = em.find(VehicleRepairRecord.class, Integer.parseInt(id));
		em.remove(vrr);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.DELETE);
		vl.setRemark("车辆维修记录");
		em.persist(vl);
	}

	/**
	 * 修改车辆主要部件维修记录
	 * @param vParts
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void editVehiclePartsRepair(VehiclePartsRepair vParts, Account user) throws Exception{
		if(vParts == null || vParts.getId() == null)
			throw new Exception("");
		VehiclePartsRepair vrr = em.find(VehiclePartsRepair.class, vParts.getId());
		vrr.setDescription(vParts.getDescription());
		vrr.setCompany(vParts.getCompany());
		vrr.setChangedate(vParts.getChangedate());
		vrr.setRegistrant(vParts.getRegistrant());
		em.merge(vrr);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.UPDATE);
		vl.setRecordid(vrr.getId()+"");
		vl.setRemark("车辆主要部件维修记录");
		em.persist(vl);
	}

	/**
	 * 删除车辆主要部件维修记录
	 * @param id
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeVehiclePartsRepair(String id, Account user) throws Exception{
		VehiclePartsRepair vrr = em.find(VehiclePartsRepair.class, Integer.parseInt(id));
		em.remove(vrr);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.DELETE);
		vl.setRemark("车辆主要部件维修记录");
		em.persist(vl);
	}

	/**
	 * 修改车辆等级评测
	 * @param vLevel
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void editVehicleLevel(VehicleLevel vLevel, Account user) throws Exception{
		if(vLevel == null || vLevel.getId() == null)
			throw new Exception("");
		VehicleLevel vrr = em.find(VehicleLevel.class, vLevel.getId());
		vrr.setDescription(vLevel.getDescription());
		vrr.setCompany(vLevel.getCompany());
		vrr.setDate(vLevel.getDate());
		vrr.setRegistrant(vLevel.getRegistrant());
		vrr.setCarlevel(vLevel.getCarlevel());
		vrr.setTechlevel(vLevel.getTechlevel());
		vrr.setDistance(vLevel.getDistance());
		em.merge(vrr);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.UPDATE);
		vl.setRecordid(vrr.getId()+"");
		vl.setRemark("车辆主要部件维修记录");
		em.persist(vl);
	}

	/**
	 * 删除车辆等级评测
	 * @param id
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeVehicleLevel(String id, Account user) throws Exception{
		VehicleLevel vlevel = em.find(VehicleLevel.class, Integer.parseInt(id));
		em.remove(vlevel);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.DELETE);
		vl.setRemark("车辆等级评测");
		em.persist(vl);
	}

	/**
	 * 修改车辆变更记录
	 * @param vChange
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void editVehicleChange(VehicleChange vChange, Account user) throws Exception{
		if(vChange == null || vChange.getId() == null)
			throw new Exception("");
		VehicleChange vrr = em.find(VehicleChange.class, vChange.getId());
		vrr.setDescription(vChange.getDescription());
		vrr.setReason(vChange.getReason());
		vrr.setDate(vChange.getDate());
		vrr.setRegistrant(vChange.getRegistrant());
		em.merge(vrr);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.UPDATE);
		vl.setRecordid(vrr.getId()+"");
		vl.setRemark("车辆变更记录");
		em.persist(vl);
	}

	/**
	 * 删除车辆变更记录
	 * @param id
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeVehicleChange(String id, Account user) throws Exception {
		VehicleChange vchange = em.find(VehicleChange.class, Integer.parseInt(id));
		em.remove(vchange);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.DELETE);
		vl.setRemark("车辆变更记录");
		em.persist(vl);
	}

	/**
	 * 删除车辆使用记录
	 * @param id
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeVehicleUseRecord(String id, Account user) throws Exception{
		VehicleUseRecord v = em.find(VehicleUseRecord.class, Integer.parseInt(id));
		em.remove(v);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.DELETE);
		vl.setRemark("车辆使用记录");
		em.persist(vl);
	}

	/**
	 * 修改车辆使用记录
	 * @param vUseRecord
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void editVehicleUseRecord(VehicleUseRecord vUseRecord, Account user) throws Exception{
		if(vUseRecord == null || vUseRecord.getId() == null)
			throw new Exception("");
		VehicleUseRecord vrr = em.find(VehicleUseRecord.class, vUseRecord.getId());
		vrr.setCompany(vUseRecord.getCompany());
		vrr.setExceed(vUseRecord.getExceed());
		vrr.setFuel(vUseRecord.getFuel());
		vrr.setIntervalmileage(vUseRecord.getIntervalmileage());
		vrr.setMileage(vUseRecord.getMileage());
		vrr.setRemine(vUseRecord.getRemine());
		vrr.setSetprice(vUseRecord.getSetprice());
		vrr.setDate(vUseRecord.getDate());
		vrr.setRegistrant(vUseRecord.getRegistrant());
		em.merge(vrr);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.UPDATE);
		vl.setRecordid(vrr.getId()+"");
		vl.setRemark("车辆使用记录");
		em.persist(vl);
	}

	/**
	 * 删除车辆事故记录
	 * @param id
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeVehicleAccident(String id, Account user) throws Exception{
		VehicleAccident v = em.find(VehicleAccident.class, Integer.parseInt(id));
		em.remove(v);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.DELETE);
		vl.setRemark("车辆事故记录");
		em.persist(vl);
	}

	/**
	 * 修改车辆事故记录
	 * @param vAccident
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void editVehicleAccident(VehicleAccident vAccident, Account user) throws Exception{
		if(vAccident == null || vAccident.getId() == null)
			throw new Exception("");
		VehicleAccident vrr = em.find(VehicleAccident.class, vAccident.getId());
		vrr.setAtype(vAccident.getAtype());
		vrr.setCost(vAccident.getCost());
		vrr.setDescription(vAccident.getDescription());
		vrr.setPlace(vAccident.getPlace());
		vrr.setResponsibility(vAccident.getResponsibility());
		vrr.setDate(vAccident.getDate());
		vrr.setRegistrant(vAccident.getRegistrant());
		em.merge(vrr);
		VehicleLog vl = new VehicleLog();
		vl.setWho(user);
		vl.setCreatetime(Calendar.getInstance().getTime());
		vl.setAction(VehicleLog.UPDATE);
		vl.setRecordid(vrr.getId()+"");
		vl.setRemark("车辆事故记录");
		em.persist(vl);
	}
}
