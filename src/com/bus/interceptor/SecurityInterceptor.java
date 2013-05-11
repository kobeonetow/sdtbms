package com.bus.interceptor;

import com.bus.stripes.actionbean.ErrorActionBean;
import com.bus.stripes.actionbean.LoginActionBean;
import com.bus.stripes.actionbean.MyActionBeanContext;
import com.bus.stripes.actionbean.Permission;
import com.bus.stripes.actionbean.score.ScoreitemsActionBean;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

@Intercepts(LifecycleStage.HandlerResolution)
public class SecurityInterceptor implements Interceptor{

	@Override
	public Resolution intercept(ExecutionContext ctx) throws Exception {
		Resolution resolution = ctx.proceed();
		
		if(ctx.getActionBean() instanceof LoginActionBean){
			return resolution;
		}
		if(isLoggedIn(ctx.getActionBeanContext())){
//			if(isPermitted(ctx.getActionBean(),ctx.getActionBeanContext())){
				return resolution;
//			}else{
//				return new ForwardResolution("/actionbean/Error.action").addParameter("error", "权限不足")
//						.addParameter("description", "你没有权限访问该页面，请联系管理员");
//			}
		}
		
		return new RedirectResolution("/actionbean/Login.action");
	}
	
	 /** Returns true if the user is logged in. */
    protected boolean isLoggedIn(ActionBeanContext ctx) { 
        return ((MyActionBeanContext) ctx).getUser() != null;
    }

//    /** Returns true if the user is permitted to invoke the event requested. */
//    protected boolean isPermitted(ActionBean bean, ActionBeanContext ctx) {
//    	//Public beans
//    	if(bean instanceof LoginActionBean){
//    		return true;
//    	}
//    	if(bean instanceof ErrorActionBean){
//    		return true;
//    	}
//    	//Private beans
//    	String clsname = bean.getClass().getName();
//    	int lastindex = clsname.lastIndexOf(".") +1;
//    	int endindex = clsname.indexOf("ActionBean");
//    	if(endindex ==-1) endindex = 0;
//    	clsname = clsname.substring(lastindex,endindex).toLowerCase();
//    	System.out.println(clsname);
//    	if( ((Permission)bean).allowToAccess(((MyActionBeanContext) ctx).getUser(),clsname+"_"))
//    		return true;
//    	else
//    		return false;
//    }
}
