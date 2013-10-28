package com.bus.util;

public interface Roles {
	
	public static final String ADMINISTRATOR = "administrator_system";

	//For employee system
	public static final String EMPLOYEE_SYSTEM = "employee_system";
	public static final String EMPLOYEE_VIEW = "employee_view";
	public static final String EMPLOYEE_EDIT = "employee_edit";
	public static final String EMPLOYEE_RM = "employee_rm";
	public static final String EMPLOYEE_VIEW_DETAIL= "employee_view_detail";
	public static final String EMPLOYEE_DATA_MIGRATE= "employee_data_migrate";
	public static final String EMPLOYEE_DATAFILE_UPLOAD= "employee_datafile_upload";
	public static final String EMPLOYEE_IDCHECK_FILE_UPLOAD= "employee_idcheck_file_upload";
	public static final String EMPLOYEE_COOR_FILE_UPLOAD = "employee_coordination_file_upload";
	public static final String EMPLOYEE_RESIGN = "employee_resign";
	public static final String EMPLOYEE_ADD_CONTRACT = "employee_add_contract";
	public static final String EMPLOYEE_VIEW_CONTRACT = "employee_view_contract";
	public static final String EMPLOYEE_EDIT_CONTRACT = "employee_edit_contract";
	public static final String EMPLOYEE_RM_CONTRACT = "employee_rm_contract";
	public static final String EMPLOYEE_DEPT_VIEW = "employee_dept_view";
	public static final String EMPLOYEE_DEPT_ADD = "employee_dept_add";
	public static final String EMPLOYEE_DEPT_RM = "employee_dept_rm";
	public static final String EMPLOYEE_DATA_DOWNLOAD = "employee_data_download";
	public static final String EMPLOYEE_DRIVER_DATA_DOWNLOAD = "employee_driver_data_download";
	public static final String EMPLOYEE_COOR_DATA_DOWNLOAD = "employee_coor_data_download";
	public static final String EMPLOYEE_COOR_VIEW = "employee_coor_view";
	public static final String EMPLOYEE_COOR_ADD = "employee_coor_add";
	public static final String EMPLOYEE_COOR_EDIT = "employee_coor_edit";
	public static final String EMPLOYEE_PROPERTY_LIST_VIEW = "employee_property_list_view";
	public static final String EMPLOYEE_PROPERTY_LIST_ADD = "employee_property_list_add";
	public static final String EMPLOYEE_PROPERTY_LIST_RM = "employee_property_list_rm";
	public static final String EMPLOYEE_IDCARDS_VIEW = "employee_idcards_view";
	public static final String EMPLOYEE_IDCARDS_ADD = "employee_idcards_add";
	public static final String EMPLOYEE_IDCARDS_RM = "employee_idcards_rm";
	public static final String EMPLOYEE_IDCARDS_FILE_UPLOAD = "employee_idcards_file_upload";
	public static final String EMPLOYEE_POS_VIEW = "employee_pos_view";
	public static final String EMPLOYEE_POS_ADD = "employee_pos_add";
	public static final String EMPLOYEE_POS_RM = "employee_pos_rm";
	public static final String EMPLOYEE_CONTRACT_FILE_UPLOAD = "employee_contract_file_upload";
	public static final String EMPLOYEE_PROFILEPIC_UPLOAD = "employee_profilepic_upload";
	
	
	//For Score System
	public static final String SCORE_SYSTEM = "score_system";
	public static final String SCORE_HOME_VIEW = "score_home_view";
	public static final String SCORE_DETAIL_VIEW = "score_detail_view";
	public static final String SCORE_DETAIL_REMOVE_RECORD = "score_detail_remove_record";
	public static final String SCORE_ITEMS_FILE_UPLOAD = "score_fileupload_uploaditems";
	public static final String SCORE_SCORES_FILE_UPLOAD = "score_fileupload_uploadscores";
	public static final String SCORE_ITEMS_VIEW = "score_items_view";
	public static final String SCORE_ITEMS_CREATE = "score_items_create";
	public static final String SCORE_ITEMS_EDIT = "score_items_edit";
	public static final String SCORE_GIVE_SCORE= "score_items_givescore";
	public static final String SCORE_SHEET_VIEW = "score_sheet_view";
	public static final String SCORE_SHEET_ADD = "score_sheet_create";
	public static final String SCORE_SHEET_RM = "score_sheet_remove";
	public static final String Score_SHEET_RM_ST = "score_sheet_rm_st";
	public static final String SCORE_SHEET_ADD_ST = "score_sheet_add_st";
	public static final String SCORE_RANK_GROUP_VIEW = "score_rank_group_view";
	public static final String SCORE_RANK_GROUP_EDIT = "score_rank_group_edit";
	public static final String SCORE_VOUCHER_VIEW = "score_voucher_view";
	public static final String SCORE_VOUCHER_MANAGEMENT = "score_voucher_management";
	public static final String SCORE_APPROVE_ITEMS = "score_approve_items";
	public static final String SCORE_APPROVE_SUBMIT_ITEMS = "score_approve_submit_items";
	
	public static final String SCOREAPPROVER_SYSTEM = "score_scoreapprover";
	
	
	//For HR System
	public static final String ACCOUNT_SYSTEM = "account_system";
	public static final String ACCOUNT_ACCOUNT_CREATE = "account_createaccount";
	public static final String ACCOUNT_GROUP_CREATE = "account_creategroup";
	public static final String ACCOUNT_ASSIGN_ACC_TO_GP= "account_assigngroup";
	public static final String ACCOUNT_REMOVE_ACC = "account_removeaccount";
	public static final String ACCOUNT_RESIGN_ACC = "account_resignaccount";
	public static final String ACCOUNT_REMOVE_GP = "account_removegroup";
	public static final String ACCOUNT_VIEW_ACC_GPS = "account_viewaccountgroups";
	public static final String ACCOUNT_VIEW_GP_ACTIONS = "account_viewgroupactions";
	public static final String ACCOUNT_REMOVE_GP_FROM_USER = "account_removegroupfromuser";
	public static final String ACCOUNT_ASSIGN_ACTION_TO_GP = "account_assignactiontogroup";
	
	//For Employment System
	public static final String EMPLOYMENT_SYSTEM = "employement_system";
	public static final String EMPLOYMENT_REQUEST = "employment_request_view";
	public static final String EMPLOYMENT_REQUEST_CREATE = "employment_request_create";
	public static final String EMPLOYMENT_REQUEST_EDIT = "employment_request_edit";
	public static final String EMPLOYMENT_HOME_VIEW = "employment_home_view";
	public static final String EMPLOYMENT_APPLICATION_CREATE = "employment_application_create";
	public static final String EMPLOYMENT_APPLICATION_EDIT = "employment_application_edit";
	public static final String EMPLOYMENT_DRIVER_EXAM = "employment_driverexam";
	public static final String EMPLOYMENT_DRIVER_EXAM_EDIT = "employment_driverexam_edit";
	public static final String EMPLOYMENT_APPLY_EXAM = "employment_apply_exam";
	public static final String EMPLOYMENT_IDCARD = "employment_application_idcard_setting";
	
	
	//For Vehicle System
	public static final String VEHICLE_SYSTEM = "vehicle_system";
	public static final String VEHICLE_PROFILE_VIEW = "vehicle_system";
	public static final String VEHICLE_PROFILE_EDIT = "vehicle_profile_edit";
	public static final String VEHICLE_MILE_VIEW = "vehicle_mile_view";
	public static final String VEHICLE_MILE_EDIT = "vehicle_mile_edit";
	public static final String VEHICLE_FILE_CHECK = "vehicle_file_check";
	public static final String VEHICLE_FILE_MAINTENANCE = "vehicle_file_maintenance";
	public static final String VEHICLE_FILE_REPAIR = "vehicle_file_repair";
	public static final String VEHICLE_FILE_FULLCHECK = "vehicle_file_fullcheck"; 
	public static final String VEHICLE_FILE_ANNUL = "vehicle_file_annul";
	public static final String VEHICLE_FILE_EXTRAS = "vehicle_file_extras";
	public static final String VEHICLE_TEAM_VIEW = "vehicle_team_view";
	public static final String VEHICLE_TEAM_EDIT = "vehicle_team_edit";
	public static final String VEHICLE_TEAM_EDIT_TEAM = "vehicle_team_edit_team";
	public static final String VEHICLE_ROUTE_VIEW = "vehicle_route_view";
	public static final String VEHICLE_ROUTE_EDIT = "vehicle_route_edit";

	

	

	
}
