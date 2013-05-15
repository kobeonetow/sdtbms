package com.bus.util;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;

import com.bus.dto.Account;
import com.bus.dto.Contract;
import com.bus.dto.Employee;
import com.bus.dto.score.Scorerecord;
import com.bus.dto.score.Scoretype;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;

public class ScoreExcelFileProcessor extends ExcelFileProcessor {

	public ScoreExcelFileProcessor(FileInputStream fis) {
		super(fis);
	}

	/**
	 * cvs file must have these four columns in order:
	 * reference, type, score, description
	 * @param scoreBean
	 * @param user
	 * @return
	 */
	public String saveItems(ScoreBean scoreBean, Account user) {
		String str = "";
		while (hasNextLine()) {

			String[] cols = strLine.split(",");
			if (cols.length < 4) {
				String num = "N.A.";
				if (cols.length > 1)
					num = cols[0];
				str += "第" + index + "行" + "录入失败,编号" + num  + "\n<br/>";
				continue;
			}
			try {
				String remark = "";
				for(int i=3;i<cols.length;i++){
					if(i == cols.length-1)
						remark += cols[i];
					else
						remark += cols[i] + ",";
				}
				Scoretype st = new Scoretype();
				st.setAccount(user);
				st.setCreatedate(Calendar.getInstance().getTime());
				st.setDescription(remark);
				st.setReference(cols[0]);
				st.setScore(Integer.parseInt(cols[2]));
				if (cols[1].equals(Scoretype.SCORE_TYPE_FIX_STR))
					st.setType(Scoretype.SCORE_TYPE_FIX);
				else if (cols[1].equals(Scoretype.SCORE_TYPE_TEMP_STR))
					st.setType(Scoretype.SCORE_TYPE_TEMP);
				else {
					str += "不知名的类型." + "第" + index + "行" + "录入失败,编号"+ cols[0]  + "\n<br/>";
					continue;
				}
				scoreBean.saveScoretype(user, st);
			} catch (Exception e) {
				str += "转换出错." + "第" + index + "行" + "录入失败,编号" + cols[0]  + "\n<br/>";
				continue;
			}

		}
		return str;
	}

	/**
	 * Return "" if no errors, else will be written inside the returned string.
	 * <br/> need 7 columns
	 * <br/>id, scoredate, reference, nameOfSender, workeridOfSender, nameOfReceiver, workeridOfReceiver
	 * @param hrBean
	 * @param scoreBean
	 * @param user
	 * @return
	 */
	public String saveScores(HRBean hrBean, ScoreBean scoreBean, Account user) {
		String str = "";
		while (hasNextLine()) {
			String[] cols = strLine.split(",");
			if (cols.length < 7) {
				String num = "N.A.";
				if (cols.length > 1)
					num = cols[0];
				str += "第" + index + "行" + "录入失败,id" + num;
				continue;
			}
			try {
				Date scoredate = HRUtil.parseDate(cols[1], "yyyy-MM-dd");
				System.out.println(cols[2]);
				Scoretype st = scoreBean.getScoreTypeByReference(cols[2]);
				if(st ==null){
					str += "" + "第" + index + "行" + "录入失败,id" + cols[0]+".编号不存在."  + "\n<br/>";
					continue;
				}
				if(!scoreBean.isScoreMemberExist(cols[4])){
					if(hrBean.isEmployeeWorkerIdExist(cols[4])){
						Employee e = hrBean.getEmployeeByWorkerId(cols[4]);
						scoreBean.createScoreMember(user,e);
					}
				}
				if(!scoreBean.isScoreMemberExist(cols[6])){
					if(hrBean.isEmployeeWorkerIdExist(cols[6])){
						Employee scorer = hrBean.getEmployeeByWorkerId(cols[6]);
						scoreBean.createScoreMember(user,scorer);
					}
				}
				Integer score=null;
				if(cols.length > 7){
					score = Integer.parseInt(cols[7]);
				}
				scoreBean.assignScoreTypeToScoreMember(user, cols[4], cols[6], st, scoredate,score);
			} catch (Exception e) {
				e.printStackTrace();
				str += "转换出错." + "第" + index + "行" + "录入失败,id" + cols[0]+"."+e.getMessage()  + "\n<br/>";
				continue;
			}

		}
		return str;
	}

}
