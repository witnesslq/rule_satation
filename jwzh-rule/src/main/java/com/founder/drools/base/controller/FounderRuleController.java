package com.founder.drools.base.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.founder.drools.base.service.DroolsGroupService;
import com.founder.drools.base.service.DroolsMethodService;
import com.founder.drools.base.service.DroolsRuleService;
import com.founder.drools.base.service.DroolsServiceService;
import com.founder.drools.base.service.DroolsUrlService;
import com.founder.drools.core.inteface.RuleService;
import com.founder.drools.core.request.RuleBean;
import com.founder.framework.base.controller.BaseController;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * ****************************************************************************
 * @Package:      [com.founder.drools.base.controller.FounderRuleController.java]  
 * @ClassName:    [FounderRuleController]   
 * @Description:  [规则平台 控制器]   
 * @Author:       [zhang.hai@founder.com.cn]  
 * @CreateDate:   [2015年12月1日 下午5:46:29]   
 * @UpdateUser:   [ZhangHai(如多次修改保留历史记录，增加修改记录)]   
 * @UpdateDate:   [2015年12月1日 下午5:46:29，(如多次修改保留历史记录，增加修改记录)]   
 * @UpdateRemark: [说明本次修改内容,(如多次修改保留历史记录，增加修改记录)]  
 * @Version:      [v1.0]
 */
@Controller
@RequestMapping("founderRule")
public class FounderRuleController extends BaseController {					
	
	@Autowired
	private RuleService ruleService;
	
	@Autowired
	private DroolsUrlService droolsUrlService;
	
	@Autowired
	private DroolsServiceService droolsServiceService;
	
	@Autowired
	private DroolsMethodService droolsMethodService;
	
	@Resource(name="droolsGroupService")
	private DroolsGroupService droolsGroupService;
	
	@Autowired
	private DroolsRuleService droolsRuleService;
	
	/**
	 * 
	 * @Title: executeRule
	 * @Description: TODO(提供给应用服务器的规则调用接口)
	 * @param @param ruleBeanXmlStr XML格式的ruleBean对象字符串
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throw
	 */
	@RequestMapping(value = "/executeRule", method = {RequestMethod.POST})
	public @ResponseBody String executeRule(String ruleBeanXmlStr){
		XStream xStream = new XStream(new DomDriver());
		RuleBean ruleBean = (RuleBean) xStream.fromXML(ruleBeanXmlStr);
        
        //执行规则
        try{        	
        	ruleService.executeRule(ruleBean);
        }catch(Exception e){
        	e.printStackTrace();
        	ruleBean.setResponse(e.toString());        	
        }
		
		return xStream.toXML(ruleBean);
	}	
	
	/**
	 * 
	 * @Title: index
	 * @Description: TODO(规则平台自身主页面)
	 * @param @return    设定文件
	 * @return ModelAndView    返回类型
	 * @throw
	 */
	@RequestMapping(value = "/index", method = {RequestMethod.GET})
	public ModelAndView index(){
		ModelAndView mv = new ModelAndView("main/main");		
		return mv;
	}	
	
	/**
	 * 
	 * @Title: getUrlNum
	 * @Description: TODO(获取地址数)
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throw
	 */
	@RequestMapping(value = "/getUrlNum", method = {RequestMethod.POST})
	public @ResponseBody int getUrlNum(){
		return droolsUrlService.countUrlNum();
	}
	
	/**
	 * 
	 * @Title: getServiceNum
	 * @Description: TODO(获取服务数)
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throw
	 */
	@RequestMapping(value = "/getServiceNum", method = {RequestMethod.POST})
	public @ResponseBody int getServiceNum(){
		return droolsServiceService.countServiceNum();
	}
	
	/**
	 * 
	 * @Title: getMethodNum
	 * @Description: TODO(获取方法数)
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throw
	 */
	@RequestMapping(value = "/getMethodNum", method = {RequestMethod.POST})
	public @ResponseBody int getMethodNum(){
		return droolsMethodService.countMethodNum();
	}
	
	/**
	 * 
	 * @Title: getGroupNum
	 * @Description: TODO(获取分组数)
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throw
	 */
	@RequestMapping(value = "/getGroupNum", method = {RequestMethod.POST})
	public @ResponseBody int getGroupNum(){
		return droolsGroupService.countGroupNum();
	}
	
	/**
	 * 
	 * @Title: getRuleNum
	 * @Description: TODO(获取已有规则数)
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throw
	 */
	@RequestMapping(value = "/getRuleNum", method = {RequestMethod.POST})
	public @ResponseBody int getRuleNum(){
		return droolsRuleService.countRuleNum();
	}
		
}
