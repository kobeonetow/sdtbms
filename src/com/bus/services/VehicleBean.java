package com.bus.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.Employee;
import com.bus.dto.vehicleprofile.VehicleCheck;
import com.bus.dto.vehicleprofile.VehicleFiles;
import com.bus.dto.vehicleprofile.VehicleLane;
import com.bus.dto.vehicleprofile.VehicleLaneMapper;
import com.bus.dto.vehicleprofile.VehicleLaneMirror;
import com.bus.dto.vehicleprofile.VehicleMiles;
import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.dto.vehicleprofile.VehicleTeam;
import com.bus.dto.vehicleprofile.VehicleTeamLeader;
import com.bus.dto.vehicleprofile.VehicleTeamMember;
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
	public Map getVehicles(int pagenum, int lotsize) throws Exception {
		Map map = new HashMap<String, Object>();
		if (pagenum == -1 || pagenum == 0) {
			List<VehicleProfile> list = em
					.createQuery(
							"SELECT q FROM VehicleProfile q ORDER BY selfid")
					.getResultList();
			Long count = (Long) em.createQuery(
					"SELECT count(q) FROM VehicleProfile q").getSingleResult();
			map.put("list", list);
			map.put("count", count);
		} else {
			List<VehicleProfile> list = em
					.createQuery(
							"SELECT q FROM VehicleProfile q ORDER BY selfid")
					.setFirstResult(pagenum * lotsize - lotsize)
					.setMaxResults(lotsize).getResultList();
			Long count = (Long) em.createQuery(
					"SELECT count(q) FROM VehicleProfile q").getSingleResult();
			map.put("list", list);
			map.put("count", count);
		}
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
	public Map getVehicles(int pagenum, int lotsize, String statement) {
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
	public void saveVehicle(VehicleProfile profile) throws Exception {
		if (profile.getId() == null) {
			profile.setStatus("A");
			em.persist(profile);
		} else {
			em.merge(profile);
		}
	}

	/**
	 * Delete vehicle profile given its ID, onlu for vehicles no referenced
	 * 
	 * @param parseInt
	 * @throws Exception
	 */
	@Transactional
	public void deleteVehicle(int id) throws Exception {
		VehicleProfile vp = em.find(VehicleProfile.class, id);
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

	/**
	 * Save mile by vehicle
	 * 
	 * @param mile
	 * @throws Exception
	 */
	@Transactional
	public void saveVehicleMile(VehicleMiles mile) throws Exception {
		if (mile.getVehicle() == null || mile.getVehicle().getId() == null)
			throw new Exception("No vid provided.");
		VehicleProfile vp = em.find(VehicleProfile.class, mile.getVehicle()
				.getId());
		mile.setVehicle(vp);
		mile.calculate();
		em.persist(mile);
	}

	/**
	 * update existing vehicle miles row given VehicleMiles Object
	 * 
	 * @param editmile
	 * @throws Exception
	 */
	@Transactional
	public void updateVehicleMiles(VehicleMiles editmile) throws Exception {
		VehicleMiles vm = em.find(VehicleMiles.class, editmile.getId());
		vm.copy(editmile);
		vm.calculate();
		em.merge(vm);
	}

	/**
	 * 
	 * @param vid
	 * @throws Exception
	 */
	@Transactional
	public void removeVehicleMiles(String vid) throws Exception {
		VehicleMiles vm = em.find(VehicleMiles.class, Integer.parseInt(vid));
		em.remove(vm);
	}

	/**
	 * save the vehicle file ,mostly image files linked to VehicleCheck object
	 * 
	 * @param vf
	 * @throws Exception
	 */
	@Transactional
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
	@Transactional
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
	public void throwVehicle(String vid, Date dateVal) throws Exception {
		VehicleProfile vp = em
				.find(VehicleProfile.class, Integer.parseInt(vid));
		if (vp.getStatus() != null && vp.getStatus().equals("E"))
			return;
		vp.setStatus("E");
		vp.setThrowdate(dateVal);
		em.merge(vp);
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
	@Transactional
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
					
					vp.setCompany(saver.getValueFromName(fullDetail, "车属单位"));
					vp.setCompanyaddr(saver.getValueFromName(fullDetail, "地址"));
					String recordid = saver.getValueFromName(fullDetail, "登记证编号");
					if(!recordid.trim().equals(""))
						vp.setRecordid(recordid);
					vp.setDatejoin(HRUtil.parseDate(
							saver.getValueFromName(fullDetail, "入户日期"),
							"yyyy/MM/dd"));
					vp.setPtaxnumber(saver.getValueFromName(fullDetail,
							"购置凭证税号"));
					vp.setSource(saver.getValueFromName(fullDetail, "车辆来源"));
					vp.setDateuse(HRUtil.parseDate(
							saver.getValueFromName(fullDetail, "运行日期"),
							"yyyy/MM/dd"));
					vp.setDatepurchase(HRUtil.parseDate(
							saver.getValueFromName(fullDetail, "购进时间"),
							"yyyy年MM月"));
					vp.setServicetype(saver
							.getValueFromName(fullDetail, "使用性质"));
					vp.setEnginenum(saver.getValueFromName(fullDetail, "发动机号"));
					String price = saver.getValueFromName(fullDetail, "车价+购置费");
					if (price != null && !price.equals("")) {
						String ps[] = price.split("\\+");
						vp.setVehicleprice(ps[0]);
						if (ps.length > 1)
							vp.setSubprice(ps[1]);
					}
					vp.setFramenum(saver.getValueFromName(fullDetail, "车架号码"));
					vp.setColor(saver.getValueFromName(fullDetail, "车身颜色"));
					vp.setDateproduction(HRUtil.parseDate(
							saver.getValueFromName(fullDetail, "车辆出厂日期"),
							"yyyy/MM/dd"));
					vp.setModel(saver.getValueFromName(fullDetail, "车辆厂牌/型号"));
					vp.setVtype(saver.getValueFromName(fullDetail, "车辆类型"));
					vp.setProductionaddr(saver.getValueFromName(fullDetail,
							"车辆制造企业名称"));
					vp.setBasednum(saver.getValueFromName(fullDetail, "底盘型号"));
					vp.setVlevel(saver.getValueFromName(fullDetail, "评定等级"));
					vp.setMadein(saver.getValueFromName(fullDetail, "车辆产地"));
					vp.setEnginemodel(saver.getValueFromName(fullDetail,
							"发动机号型号"));
					vp.setSits(saver.getValueFromName(fullDetail, "座位"));

					String tyre = saver
							.getValueFromName(fullDetail, "轮胎数/轮胎形式");
					if (tyre != null && !tyre.equals("")) {
						String tyres[] = tyre.split("/");
						if (tyres.length < 2)
							tyres = tyre.split("∕");
						vp.setTyrenum(tyres[0]);
						if (tyres.length > 1)
							vp.setTyremodel(tyres[1]);
					}else{
						vp.setTyrenum(saver.getValueFromName(fullDetail, "轮胎数"));
					}
					vp.setBodysize(saver.getValueFromName(fullDetail, "车辆外型尺寸"));
					vp.setFueltype(saver.getValueFromName(fullDetail, "燃料"));
					vp.setVpower(saver.getValueFromName(fullDetail, "排量/功率"));
					vp.setSubsides(saver.getValueFromName(fullDetail, "车厢配置"));

					String speed = saver.getValueFromName(fullDetail, "最高车速");
					vp.setVspeed(saver.removeNoneNumber(speed));
					vp.setTurntype(saver.getValueFromName(fullDetail, "转向型式"));
					vp.setTurnmethod(saver.getValueFromName(fullDetail, "转向器式"));
					vp.setMovebreak(saver
							.getValueFromName(fullDetail, "行车制动形式"));
					vp.setWheelbase(Integer.parseInt(saver
							.removeNoneNumber(saver.getValueFromName(
									fullDetail, "轴距"))));
					vp.setStopbreak(saver
							.getValueFromName(fullDetail, "驻车制动形式"));
					vp.setFrontwheel(Integer.parseInt(saver
							.removeNoneNumber(saver.getValueFromName(
									fullDetail, "前轮距"))));
					vp.setTotalweight(Integer.parseInt(saver
							.removeNoneNumber(saver.getValueFromName(
									fullDetail, "总质量"))));
					vp.setBackwheel(Integer.parseInt(saver
							.removeNoneNumber(saver.getValueFromName(
									fullDetail, "后轮距"))));
					vp.setSubweight(Integer.parseInt(saver
							.removeNoneNumber(saver.getValueFromName(
									fullDetail, "整车备质量"))));
					vp.setHangmodel(saver.getValueFromName(fullDetail, "悬挂形式"));
					vp.setDrivemode(saver.getValueFromName(fullDetail, "驱动形式"));
					vp.setAircond(saver.getValueFromName(fullDetail, "空调"));

					em.merge(vp);
					System.out.println("##############CAST :" + (++cast) + " "
							+ vp.getVid());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback(); // to terminate transactions;
		}
		return str;
	}

	/**
	 * Save vehicle repair records from file See:ExcelFileSaver.java
	 * @param excelFileSaver
	 * @return
	 */
	@Transactional
	public String saveVehicleRepaireDatesFromFile(ExcelFileSaver saver) throws Exception{
		String str = "";
		int cast = 0;
		//Get year at first line
		saver.hasNextLine();
		String yearString = saver.strLine; 
		try {
			while (saver.hasNextLine()) {
				if (saver.strLine.contains("车牌号码")) {
					List<String> profile = new ArrayList<String>();
					String vidStr = saver.strLine;
					while (saver.hasNextLine()
							&& !saver.strLine.contains("填表人")) {
						profile.add(saver.strLine);
					}
					String vid = saver.removeNoneNumber(vidStr.split(",")[1]);
					String selfId = saver.getValueFromName(vidStr, "自编号");
					VehicleProfile vp = this.getVehicleProfileLikeVid(vid,selfId);
					if(vp == null){
						str += "NH - VID:"+vid +".<br/>";
//						throw new Exception("No vehicle found for vid:"+vid);
						continue;
					}
					VehicleMiles  vm = this.getVehicleMilesByVidAndYear(vp.getId(),Integer.parseInt(yearString));
					if(vm == null){
						vm = new VehicleMiles();
						vm.setVehicle(vp);
						vm.setVyear(Integer.parseInt(yearString));
					}
					if(profile.size() < 15){
						str += "EINFO - VID:" + vid+"<br/>";
//						throw new Exception("Not enough vehicle info ("+profile.size()+") for vid:"+vid);
						continue;
					}
					for(int i=2;i<14;i++){
						String line = profile.get(i);
						String lineArr[] = line.split(",",-1);
						if(lineArr.length < 9){
							str += "ELINFO - VID:"+vid + " Line " + i + " length too short "+lineArr.length+". ["+line+"]<br/>";
//							throw new Exception("ELINFO - VID:"+vid + " Line " + i + " length too short.<br/>");
							continue;
						}
						String monthIndex = lineArr[0];
						String drivers = lineArr[1];
						String miles = lineArr[2];
						String firstMaint = lineArr[3];
						String secMaint = lineArr[4];
						String check = lineArr[5]; 
						String annul = lineArr[6];
						String midRepaire = lineArr[7];
						String bigRepaire = lineArr[8];
						Integer month = Integer.parseInt(monthIndex);
						
						
						//Set miles
						float mile = 0;
						if(miles != null && !miles.trim().equals(""))
							mile = Float.parseFloat(miles);
						switch(month){
						case 1:
							vm.setJan(mile);
							break;
						case 2:
							vm.setFeb(mile);
							break;
						case 3:
							vm.setMar(mile);
							break;
						case 4:
							vm.setApr(mile);
							break;
						case 5:
							vm.setMay(mile);
							break;
						case 6:
							vm.setJun(mile);
							break;
						case 7:
							vm.setJul(mile);
							break;
						case 8:
							vm.setAug(mile);
							break;
						case 9:
							vm.setSep(mile);
							break;
						case 10:
							vm.setOcto(mile);
							break;
						case 11:
							vm.setNov(mile);
							break;
						case 12:
							vm.setDece(mile);
							break;
						default:
							str += "EMINFO - VID:"+vid+ " month index:"+month+ " has error.<br/>";
							throw new Exception("EMINFO - VID:"+vid+ " month index:"+month+ " has error.<br/>");
						}
						
						//Check maintenance
						if(firstMaint != null && !firstMaint.equals("")){
							String dates[] = firstMaint.split(" ");
							for(String date:dates){
								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
								VehicleCheck tempVC = new VehicleCheck();
								tempVC.setVehicle(vp);
								tempVC.setCdate(tempD);
								tempVC.setCtype(VehicleCheck.ctypes[0]);
								em.persist(tempVC);
							}
						}
						
						//Check 2nd maintenance
						if(secMaint != null && !secMaint.trim().equals("")){
							String dates[] = secMaint.split(" ");
							for(String date:dates){
								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
								VehicleCheck tempVC = new VehicleCheck();
								tempVC.setVehicle(vp);
								tempVC.setCdate(tempD);
								tempVC.setCtype(VehicleCheck.ctypes[1]);
								em.persist(tempVC);
							}
						}
						
						//Check 2nd maintenance
						if(secMaint != null && !secMaint.trim().equals("")){
							String dates[] = secMaint.split(" ");
							for(String date:dates){
								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
								VehicleCheck tempVC = new VehicleCheck();
								tempVC.setVehicle(vp);
								tempVC.setCdate(tempD);
								tempVC.setCtype(VehicleCheck.ctypes[1]);
								em.persist(tempVC);
							}
						}
						
						//Check mid repair
						if(midRepaire != null && !midRepaire.trim().equals("")){
							String dates[] = midRepaire.split(" ");
							for(String date:dates){
								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
								VehicleCheck tempVC = new VehicleCheck();
								tempVC.setVehicle(vp);
								tempVC.setCdate(tempD);
								tempVC.setCtype(VehicleCheck.ctypes[3]);
								em.persist(tempVC);
							}
						}
						
						//Check big repair
						if(bigRepaire != null && !bigRepaire.trim().equals("")){
							String dates[] = bigRepaire.split(" ");
							for(String date:dates){
								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
								VehicleCheck tempVC = new VehicleCheck();
								tempVC.setVehicle(vp);
								tempVC.setCdate(tempD);
								tempVC.setCtype(VehicleCheck.ctypes[4]);
								em.persist(tempVC);
							}
						}
						
						//Check full repair
						if(check != null && !check.trim().equals("")){
							String dates[] = check.split(" ");
							for(String date:dates){
								Date tempD = HRUtil.parseDate(date, "yyy-MM-dd");
								VehicleCheck tempVC = new VehicleCheck();
								tempVC.setVehicle(vp);
								tempVC.setCdate(tempD);
								tempVC.setCtype(VehicleCheck.ctypes[5]);
								em.persist(tempVC);
							}
						}
						
						//Check annul
						if(annul != null && !annul.trim().equals("")){
							Date tempD = HRUtil.parseDate(annul, "yyy-MM-dd");
							VehicleCheck tempVC = new VehicleCheck();
							tempVC.setVehicle(vp);
							tempVC.setCdate(tempD);
							tempVC.setCtype(VehicleCheck.ctypes[6]);
							em.persist(tempVC);
						}
					}
					
					if(vm.getId() == null){
						vm.calculate();
						em.persist(vm);
					}
					else
						em.merge(vm);
					
					cast ++;
					System.out.println("##############CAST :" + (++cast) + " "
							+ vp.getVid());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback(); // to terminate transactions;
		}
		return str;
	}

	/**
	 * 
	 * @param id
	 * @param parseInt
	 * @return
	 */
	public VehicleMiles getVehicleMilesByVidAndYear(Integer id, int year) {
		try{
			VehicleMiles vm = (VehicleMiles) em.createQuery("SELECT q FROM VehicleMiles q WHERE vid=? AND vyear=?")
					.setParameter(1, id).setParameter(2,year).getSingleResult();
			return vm;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Save team name and leader
	 * @param saver
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public String saveTeamNameAndLeaderFromFile(ExcelFileSaver saver) throws Exception{
		String str = "";
		int cast = 0;
		try {
			while (saver.hasNextLine()) {
				if(saver.strLine == null || saver.strLine.equals(""))
					continue;
				String vals[] = saver.strLine.split(",");
				String teamName = vals[0];
				String teamLeader = vals[1];
				VehicleTeam team = getTeamByName(teamName);
				if(team == null){
					team = new VehicleTeam();
					team.setName(teamName);
					em.persist(team);
					em.flush();
				}
				Employee emp = null;
				try{
					emp = (Employee) em.createQuery("SELECT q FROM Employee q WHERE fullname=?")
						.setParameter(1, teamLeader).getSingleResult();
				}catch(Exception e){
					System.out.println("No such Employee name:"+teamLeader);
				}
				if(emp == null){
					str += "NH - "+teamLeader +". <br/>";
					continue;
				}
				VehicleTeamLeader leader = getLeaderByTeamOrLeader(teamName,teamLeader);
				if(leader == null){
					leader = new VehicleTeamLeader();
					leader.setLeader(emp);
					leader.setTeam(team);
					em.persist(leader);
				}
				System.out.println("###########Casted "+ (++cast));
			}
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback(); // to terminate transactions;
		}
		return str;
	}

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
	@Transactional
	public String saveNewVehicleFromFile(ExcelFileSaver saver) throws Exception{
		String str = "";
		int cast = 0;
		try {
			while (saver.hasNextLine()) {
				String vals[] = saver.strLine.split("," ,-1);
				if(vals.length < 16){
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
					continue;
				}
				vp = new VehicleProfile();
				vp.setVid(vid);
				vp.setSelfid(selfid);
				vp.setBasednum(basedModel);
				vp.setEnginemodel(engineModel);
				vp.setEnginenum(engineNum);
				vp.setFramenum(frameNum);
				vp.setTotalweight(Integer.parseInt(saver.removeNoneNumber(totalWeight)));
				vp.setColor(color);
				vp.setModel(model);
				vp.setBodysize(bodysize);
				vp.setVlevel(vlevel);
				vp.setSits(sits);
				vp.setFueltype(fuel);
				vp.setSubsides(subsides);
				vp.setDatejoin(HRUtil.parseDate(joindate, "yyyy/MM/dd"));
				vp.setDateinvalidate(HRUtil.parseDate(dateinvalide, "yyyy/MM/dd"));
				vp.setProductioncode(productionCode);
				em.persist(vp);
				
				System.out.println("###########Casted "+ (++cast));
			}
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback(); // to terminate transactions;
		}
		return str;
	}

	/**
	 * Save team and lane from file
	 * @param saver
	 * @return
	 * @throws Exception
	 */
	@Transactional
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
			em.getTransaction().rollback(); // to terminate transactions;
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
	@Transactional
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
				vp.setRecordid(recordid);
				em.merge(vp);
				System.out.println("###########Casted "+vid+" :"+ (++cast));
			}
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback(); // to terminate transactions;
		}
		return str;
	}

	/**
	 * Assign to 公交一公司
	 * @param saver
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public String assignVehiclesToCompanyOneFromFile (ExcelFileSaver saver) throws Exception{
		String str = "";
		int cast = 0;
		try {
			while (saver.hasNextLine()) {
				String vals[] = saver.strLine.split(",",-1);
				if(vals.length != 2){
					str += "ELine length not valid:["+saver.strLine+"].<br/>";
					continue;
				}
				String selfid = vals[0];
				String vid = vals[1];
				VehicleProfile vp = getVehicleProfileLikeVid(vid, selfid);
				if(vp == null){
					str += "EVP cannot find vehicle:["+saver.strLine+"].<br/>";
					continue;
				}
				vp.setSubcompany("公交一");
				em.merge(vp);
				System.out.println("###########Casted "+vid+" :"+ (++cast));
			}
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback(); // to terminate transactions;
		}
		return str;
	}

	/**
	 * Save miles for a year
	 * e.g.
	 * 2011,,
	 * 1月,,
	 * selfid,vid,miles
	 * selfid,vid,miles
	 * 2月
	 * selfid,vid,miles
	 * @param saver
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public String saveMilesFromFile(ExcelFileSaver saver) throws Exception{
		String str = "";
		int cast = 0;
		saver.hasNextLine();
		int year = Integer.parseInt(saver.strLine.split(",",-1)[0]);
		
		Integer month = null;
		try {
			while (saver.hasNextLine()) {
				if(saver.strLine.contains("月")){
					month = Integer.parseInt(saver.removeNoneNumber(saver.strLine));
					System.out.println("Set Month To:"+month);
					continue;
				}
				String vals[] = saver.strLine.split(",",-1);
				if(vals.length != 3){
					str += "E - length:["+saver.strLine+"].<br/>";
					continue;
				}
				String selfid = vals[0];
				String vid = vals[1];
				String miles = vals[2];
				
				//Get vehicle miles Obect
				VehicleProfile vp = getVehicleProfileLikeVid(vid, selfid);
				if(vp == null){
					str += "E - NVP:["+saver.strLine+"].<br/>";
					continue;
				}
				VehicleMiles vm = getVehicleMilesByVidAndYear(vp.getId(), year);
				if(vm == null){
					vm = new VehicleMiles();
					vm.setVehicle(vp);
				}
				vm.setVyear(year);
				//Set miles
				float mile = 0;
				if(miles != null && !miles.trim().equals(""))
					mile = Float.parseFloat(miles);
				switch(month){
				case 1:
					vm.setJan(mile);
					break;
				case 2:
					vm.setFeb(mile);
					break;
				case 3:
					vm.setMar(mile);
					break;
				case 4:
					vm.setApr(mile);
					break;
				case 5:
					vm.setMay(mile);
					break;
				case 6:
					vm.setJun(mile);
					break;
				case 7:
					vm.setJul(mile);
					break;
				case 8:
					vm.setAug(mile);
					break;
				case 9:
					vm.setSep(mile);
					break;
				case 10:
					vm.setOcto(mile);
					break;
				case 11:
					vm.setNov(mile);
					break;
				case 12:
					vm.setDece(mile);
					break;
				default:
					str += "EMINFO - VID:"+vid+ " month index:"+month+ " has error.<br/>";
					throw new Exception("EMINFO - VID:"+vid+ " month index:"+month+ " has error.<br/>");
				}
				
				vm.calculate();
				if(vm.getId() == null)
					em.persist(vm);
				else
					em.merge(vm);
				em.flush();
				
				System.out.println("###########Casted "+vid+" :"+ (++cast));
			}
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback(); // to terminate transactions;
		}
		return str;
	}

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
					" q.team.team=:teama  ORDER BY q.selfid").setParameter("teama", team).getResultList();
			count = (Long) em.createQuery("SELECT count(q) FROM VehicleProfile q WHERE" +
					" q.team.team=:teama").setParameter("teama", team).getSingleResult();
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

	public List<VehicleTeam> getVehicleTeams() {
		try{
			return em.createQuery("SELECT q FROM VehicleTeam q").getResultList();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return new ArrayList<VehicleTeam>();
		}
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
//		return new ArrayList<VehicleLane>(); 
		return lanes;
	}

	public List<VehicleProfile> getVehiclesNoRoute() throws Exception{
		List<VehicleProfile> members;
		members = em.createQuery("SELECT q FROM VehicleProfile q WHERE " +
				" NOT EXISTS (SELECT h FROM VehicleLaneMapper h WHERE q.id=h.vehicle.id) ORDER BY q.selfid")
				.getResultList();
		return members;
	}

}
