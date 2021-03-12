package com.sample.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
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
	
		// This method "executeWorkItem" registers a task in the process that takes 300ms to finish;
		public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
				//System.out.println("Execution workItem " + workItem);
				try {

					String GET_URL = "http://10.90.0.59:8080/risks/v1/arrears";
					sendGET(GET_URL);
					//Thread.sleep(500);

				} catch (Throwable t) {
					// do nothing
				}
				manager.completeWorkItem(workItem.getId(), new HashMap<String, Object>());
			}
			public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
			}
		});
		
		WorkflowProcessInstanceImpl p = (WorkflowProcessInstanceImpl) ksession.startProcess(processId, params);
		return p.getVariables();
	}

	private void sendGET(String url) throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection(Proxy.NO_PROXY);

		con.setRequestMethod("GET");
		//con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept", "application/json");
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("GET request not worked ResponseCode:"+responseCode + "message: "+con.getErrorStream());
		}

	}

}