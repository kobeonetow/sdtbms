sdtbms
==========
版本sdbms_20130711
修复了以下 bug:
初次获得证件日期，有效日期，修改。 
合同结束日期提供输入框选择排序起始日期。
不应该看到检查ID这个文件上传的权限  。
花名册有噶嘛，花名册"退伍军人"个栏有4个是"干部",用特殊身份表示。
从业资格证导出新增了领证日期

===========

版本sdbms_20130713
修复了以下 bug:
-7. 导出的花名册没有合同信息和调动信息 -- 20130713 
-17. 证件资料可以修改 --- 2013-07-13
-18. 离职，调动导出，复职后没有了 --- 移除了employee.status 的条件筛选

============

版本sdbms_20130722
修复了以下 bug:
20. 自动转换调动部门和职位！先不考虑时间问题。删除的时候 如果调动记录的新职位跟现在的新职位相同，则还原到调动前职位，否知不对现职位作更改。
21. 所属镇街 用下来菜单，不用输入框,可以再属性 页面添加选项
22. 新建调动的时候，输入工号可自动获取 原部门职位信息


============

版本sdbms_20130724
***新增了密码修改
***新增了招聘系统，权限，表 等
***新增了权限组，新增了人事部用户
***增加了证件上传功能
***24. 将新建档案里面的“联系电话”改成“住宅电话”
============

