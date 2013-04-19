package com.bus.stripes.selector;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class ContractSelector implements BMSSelector{

	private String type = null;
	private int dateselector = -1; //0 nothing, 1 enddate, 2 activedateorder
	
	@Override
	public String getSelectorStatement() {
		String query = "";
		try{
		
		if(type != null){
//			System.out.println(type);
//			type =  URLDecoder.decode(type, "UTF-8");
			query += " type = '" + type +"'";
		}
		if(dateselector != 0){
			if(dateselector == 1){
				if(!query.equals(""))
					query += " AND enddate > now() ORDER BY enddate ASC";
				else
					query += " enddate > now() ORDER BY enddate ASC";
			}else if(dateselector == 2){
				query += " ORDER BY activedate DESC";
			}
		}
		return query;
		}catch(Exception e){
			e.printStackTrace();
			return query;
		}
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getDateselector() {
		return dateselector;
	}
	public void setDateselector(int dateselector) {
		this.dateselector = dateselector;
	}
}