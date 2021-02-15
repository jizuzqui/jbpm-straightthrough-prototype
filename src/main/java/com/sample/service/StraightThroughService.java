package com.sample.service;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class StraightThroughService {
	
	private KieSession ksession;
	
	public StraightThroughService() {
		 KieServices kieServices = KieServices.Factory.get();
		 KieContainer kcontainer = kieServices.getKieClasspathContainer();
		 ksession = kcontainer.getKieBase("kbase").newKieSession();
	}
	
	public Map<String, Object> startProcess(String processId, Map<String, Object> params) {
		ksession.getWorkItemManager().registerWorkItemHandler("Service", new WorkItemHandler() {
			public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
				//System.out.println("Execution workItem " + workItem);
				try {
					Thread.sleep(300);
				} catch (Throwable t) {
					// do nothing
				}
				manager.completeWorkItem(workItem.getId(), new HashMap<String, Object>());
			}
			public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
			}
		});
		WorkflowProcessInstanceImpl p = (WorkflowProcessInstanceImpl) ksession.startProcess("sample-process", params);
		return p.getVariables();
	}

}