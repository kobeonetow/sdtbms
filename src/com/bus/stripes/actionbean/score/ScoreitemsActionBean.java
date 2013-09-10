package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import security.action.Secure;

import com.bus.dto.Account;
import com.bus.dto.Accountgroup;
import com.bus.dto.Action;
import com.bus.dto.Actiongroup;
import com.bus.dto.Employee;
import com.bus.dto.score.Scoresheets;
import com.bus.dto.score.Scoretype;
import com.bus.services.AccountBean;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.stripes.actionbean.MyActionBeanContext;
import com.bus.stripes.actionbean.Permission;
import com.bus.stripes.selector.ScoreitemSelector;
import com.bus.util.Roles;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding(value = "/actionbean/Scoreitems.action")
public class ScoreitemsActionBean extends CustomActionBean {

	private HRBean hrBean;

	public HRBean getHrBean() {
		return hrBean;
	}

	@SpringBean
	public void setHrBean(HRBean bean) {
		this.hrBean = bean;
	}

	private ScoreBean scoreBean;

	public ScoreBean getScoreBean() {
		return this.scoreBean;
	}

	@SpringBean
	public void setScoreBean(ScoreBean bean) {
		this.scoreBean = bean;
	}

	private Scoretype scoretype;
	private List<Scoretype> scoretypes;
	private List<Scoretype> selectedScoreTypes;
	private List<Scoresheets> sheetList;
	private String itemlist;
	private ScoreitemSelector selector;
	private Employee employee;
	private Employee scorer;
	private Float score = 0F;
	private Date scoredate;

	private int pagenum;
	private int lotsize;
	private Long totalcount;
	private Long recordsTotal;

	private void initData() {
		if (pagenum <= 0 || lotsize <= 0) {
			pagenum = 1;
			lotsize = 20;
		}
		try {
			sheetList = scoreBean.getAllScoreSheets();
		} catch (Exception e) {
			sheetList = new ArrayList<Scoresheets>();
		}
		getFromSelector();
		if (pagenum > totalcount)
			pagenum = Integer.parseInt(totalcount.toString());
	}

	private void getFromSelector() {
		try {
			if (selector == null && itemlist == null) {
				Map map = scoreBean.getAllScoreTypes(pagenum, lotsize);
				scoretypes = (List<Scoretype>) map.get("list");
				setRecordsTotal((Long) map.get("count"));
			} else {
				if (itemlist != null) {
					scoretypes = scoreBean.getScoretypesFromSheet(itemlist);
					setRecordsTotal(new Long(scoretypes.size()));
				} else if (selector != null) {
					String statement = selector.getSelectorStatement();
					Map map = scoreBean.getScoretypeByStatement(pagenum,
							lotsize, statement);
					scoretypes = (List<Scoretype>) map.get("list");
					setRecordsTotal((Long) map.get("count"));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			scoretypes = new ArrayList<Scoretype>();
			setRecordsTotal(0L);
		}
		setTotalcount(getRecordsTotal() / lotsize + 1);
	}

	@DefaultHandler
	@Secure(roles = Roles.SCORE_ITEMS_VIEW)
	public Resolution defaultAction() {
		initData();
		if (employee == null || employee.getFullname() == null
				|| employee.getWorkerid() == null) {
			Account a = context.getUser();
			if (a.getEmployee() != null && !a.getEmployee().equals("")) {
				employee = hrBean.getEmployeeByWorkerId(a.getEmployee());
			}
		}
		return new ForwardResolution("/score/items.jsp").addParameter(
				"pagenum", pagenum);
	}

	@HandlesEvent(value = "createscoretype")
	@Secure(roles = Roles.SCORE_ITEMS_CREATE)
	public Resolution createscoretype() {
		if (scoretype == null) {
			return new StreamingResolution("text/html;charset=utf-8;", "");
		}

		try {
			scoreBean.saveScoretype(context.getUser(), scoretype);
			return new StreamingResolution("text/html;charset=utf-8;", "创建成功");
		} catch (Exception e) {
			return new StreamingResolution("text/html;charset=utf-8;", "创建失败");
		}

	}

	@HandlesEvent(value = "deletescoretype")
	@Secure(roles = Roles.SCORE_ITEMS_EDIT)
	public Resolution deletescoretype() {
		try {
			if (selectedScoreTypes == null)
				return defaultAction();
			for (Scoretype st : selectedScoreTypes) {
				if (st != null && st.getId() != null)
					scoreBean.removeScoreType(context.getUser(), st);
			}
			return defaultAction();
		} catch (Exception e) {
			return context.errorResolution(
					"删除错误",
					"可能要删除的条例已经列入条例表单中，请先从该条例单中删除。" + "或者该条例已经赋值过给员工，无法删除."
							+ e.getMessage());
		}
	}

	@HandlesEvent(value = "editscoretype")
	@Secure(roles = Roles.SCORE_ITEMS_EDIT)
	public Resolution editscoretype() {
		try {
			if (scoretype == null) {
				String targetId = context.getRequest().getParameter("targetId");
				scoretype = scoreBean.getScoreTypeById(targetId);
				return new ForwardResolution("/score/editscoretype.jsp");
			} else {
				scoreBean.updateScoreType(context.getUser(), scoretype);
				return new StreamingResolution("text/html;charset=utf-8;",
						"修改成功");
			}
		} catch (Exception e) {
			return context.errorResolution("修改错误", "请确保输入的内容正确，如果还有问题联系管理员");
		}
	}

	@HandlesEvent(value = "givescores")
	@Secure(roles = Roles.SCORE_GIVE_SCORE)
	public Resolution givescores() {
		if (employee == null || selectedScoreTypes == null) {
			// System.out.println("these is an null here");
			return defaultAction();
		}
		try {
			String receiverWorkerids = context.getRequest().getParameter(
					"receiverWorkerids");
			String[] receivers = receiverWorkerids.split(",", -1);
			if (receivers.length > 1)
				score = 0F;

			for (String workerid : receivers) {
				scorer = hrBean.getEmployeeByWorkerId(workerid);
				if (!scoreBean.isScoreMemberExist(employee.getWorkerid())) {
					if (hrBean.isWorkerExist(employee))
						scoreBean
								.createScoreMember(context.getUser(), employee);
				}
				if (!scoreBean.isScoreMemberExist(workerid)) {
					if (hrBean.isWorkerExist(scorer))
						scoreBean.createScoreMember(context.getUser(), scorer);
				}
				if (scoredate == null) {
					scoredate = Calendar.getInstance().getTime();
				}
				for (Scoretype st : selectedScoreTypes) {
					if (st != null && st.getId() != null) {
							scoreBean.assignScoreTypeToScoreMember(
									context.getUser(), employee.getWorkerid(),
									scorer.getWorkerid(), st, scoredate, score);
					}
				}
				scorer = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return context.errorResolution("打分错误", "请确认输入员工信息正确再试一次，或联系管理员");
		}
		return defaultAction();
	}

	@HandlesEvent(value = "assignToScoreSheet")
	@Secure(roles = Roles.SCORE_SHEET_ADD_ST)
	public Resolution assignToScoreSheet() {
		if (selectedScoreTypes == null || itemlist == null) {
			return defaultAction();
		}
		try {
			for (Scoretype st : selectedScoreTypes) {
				if (st != null && st.getId() != null) {
					if (!scoreBean.isScoretypeExistForSheet(st.getId(),
							Integer.parseInt(itemlist))) {
						scoreBean.assignScoreTypeToSheet(context.getUser(),
								st.getId(), Integer.parseInt(itemlist));
					}
				}
			}
		} catch (Exception e) {
			return context.errorResolution("添加出错", "请确认选择了正确的积分表单和条例，或联系管理员."
					+ e.getMessage());
		}
		return defaultAction();
	}

	@HandlesEvent(value = "prevpage")
	public Resolution prevpage() {
		pagenum--;
		return defaultAction();
	}

	@HandlesEvent(value = "nextpage")
	public Resolution nextpage() {
		pagenum++;
		return defaultAction();
	}

	@HandlesEvent(value = "filter")
	public Resolution filter() {
		return defaultAction();
	}

	public Scoretype getScoretype() {
		return scoretype;
	}

	public void setScoretype(Scoretype scoretype) {
		this.scoretype = scoretype;
	}

	public List<Scoretype> getScoretypes() {
		return scoretypes;
	}

	public void setScoretypes(List<Scoretype> scoretypes) {
		this.scoretypes = scoretypes;
	}

	public List<Scoretype> getSelectedScoreTypes() {
		return selectedScoreTypes;
	}

	public void setSelectedScoreTypes(List<Scoretype> selectedScoreTypes) {
		this.selectedScoreTypes = selectedScoreTypes;
	}

	public String getItemlist() {
		return itemlist;
	}

	public void setItemlist(String itemlist) {
		this.itemlist = itemlist;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public int getLotsize() {
		return lotsize;
	}

	public void setLotsize(int lotsize) {
		this.lotsize = lotsize;
	}

	public Long getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}

	public ScoreitemSelector getSelector() {
		return selector;
	}

	public void setSelector(ScoreitemSelector selector) {
		this.selector = selector;
	}

	public Long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Employee getScorer() {
		return scorer;
	}

	public void setScorer(Employee scorer) {
		this.scorer = scorer;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public List<Scoresheets> getSheetList() {
		return sheetList;
	}

	public void setSheetList(List<Scoresheets> sheetList) {
		this.sheetList = sheetList;
	}

	public Date getScoredate() {
		return scoredate;
	}

	public void setScoredate(Date scoredate) {
		this.scoredate = scoredate;
	}
}
