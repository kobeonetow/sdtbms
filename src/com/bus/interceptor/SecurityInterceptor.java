package com.bus.interceptor;

import com.bus.stripes.actionbean.ErrorActionBean;
import com.bus.stripes.actionbean.LoginActionBean;
import com.bus.stripes.actionbean.MyActionBeanContext;
import com.bus.stripes.actionbean.Permission;
import com.bus.stripes.actionbean.common.EmployeeSelectorActionBean;
import com.bus.stripes.actionbean.score.ScoreViewPublicActionBean;
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
		
		//Public Beans
		if(ctx.getActionBean() instanceof LoginActionBean){
			return resolution;
		}else if(ctx.getActionBean() instanceof ScoreViewPublicActionBean){
			return resolution;
		}else if(ctx.getActionBean() instanceof ErrorActionBean){
			return resolution;
		}else if(ctx.getActionBean() instanceof EmployeeSelectorActionBean){
			return resolution;
		}
		
		if(isLoggedIn(ctx.getActionBeanContext())){
			return resolution;
		}
//		System.out.println(((MyActionBeanContext)ctx.getActionBeanContext()).getFullURL() );
		return new RedirectResolution("/actionbean/Login.action").addParameter("url"
				,((MyActionBeanContext)ctx.getActionBeanContext()).getFullURL() );
	}
	
	 /** Returns true if the user is logged in. */
    protected boolean isLoggedIn(ActionBeanContext ctx) { 
        return ((MyActionBeanContext) ctx).getUser() != null;
    }
}
