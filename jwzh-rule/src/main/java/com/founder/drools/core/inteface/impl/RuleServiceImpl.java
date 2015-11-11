package com.founder.drools.core.inteface.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.drools.runtime.StatefulKnowledgeSession;
import org.springframework.stereotype.Service;

import com.founder.drools.core.inteface.RuleService;
import com.founder.drools.core.model.RuleBean;
import com.founder.drools.core.model.RuleConfig;
import com.founder.framework.config.SystemConfig;

/**
 * ****************************************************************************
 * @Package:      [com.founder.zdrygl.core.inteface.impl.RuleServiceImpl.java]  
 * @ClassName:    [RuleServiceImpl]   
 * @Description:  [规则服务实现]   
 * @Author:       [zhang.hai@founder.com.cn]  
 * @CreateDate:   [2015年10月16日 下午5:47:09]   
 * @UpdateUser:   [ZhangHai(如多次修改保留历史记录，增加修改记录)]   
 * @UpdateDate:   [2015年10月16日 下午5:47:09，(如多次修改保留历史记录，增加修改记录)]   
 * @UpdateRemark: [说明本次修改内容,(如多次修改保留历史记录，增加修改记录)]  
 * @Version:      [v1.0]
 */
@Service
public class RuleServiceImpl implements RuleService {		
	private Logger logger = Logger.getLogger(this.getClass());
	private String drlFilePath=null;
	
	//规则对应关系Map
	private Map<String, RuleConfig> ruleConfigMap = new HashMap<String, RuleConfig>();
	
	@Override
	public boolean executeRule(RuleBean ruleBean, Object paramObj, Map globalParamMap) {
		try{	
			String ruleFileName=ruleBean.getRuleFileName();
			logger.info("execute rule:"+ruleFileName);
			
			RuleConfig ruleConfig = (RuleConfig) ruleConfigMap.get(ruleFileName);
			if(ruleConfig == null){
				this.reLoadOne(ruleFileName);//重新加载
				ruleConfig = (RuleConfig) ruleConfigMap.get(ruleFileName);
				if(ruleConfig == null)
					throw new RuntimeException("Can not find ruleFile named \""+ruleFileName+"\"");
			}
			
			StatefulKnowledgeSession ksession = ruleConfig.getKbase().newStatefulKnowledgeSession();
			
			//循环设置全局变量
			if(globalParamMap!=null){
				Object[] keyAry = globalParamMap.keySet().toArray();
				for(int i=0;i<keyAry.length;i++){
					ksession.setGlobal((String)keyAry[i], globalParamMap.get(keyAry[i]));					
				}
				
			}
			
			//循环设置参数
			ksession.insert(ruleBean);
			if(paramObj!=null){
				if(paramObj instanceof List){		
					List list=(List)paramObj;
					for(int i=0;i<list.size();i++){
						ksession.insert(list.get(i));					
					}
				}else{
					ksession.insert(paramObj);		
				}
				
			}
	
	        //触发规则引擎
	        ksession.fireAllRules();
	        ksession.dispose();  
	        return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean reLoadOne(String ruleFileName){
		logger.info("reload rule:"+ruleFileName);
		try{
			if(drlFilePath == null)
				drlFilePath=SystemConfig.getString("DrlFilePath");
			
			RuleConfig ruleConfig = new RuleConfig(drlFilePath+"/"+ruleFileName+".drl");
			ruleConfig.getKbase();
			ruleConfigMap.put(ruleFileName, ruleConfig);	
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
}