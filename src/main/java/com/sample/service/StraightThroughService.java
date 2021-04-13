package com.sample.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.internal.runtime.conf.DeploymentDescriptor;
import org.kie.internal.runtime.conf.NamedObjectModel;
import org.kie.internal.runtime.manager.deploy.DeploymentDescriptorIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;


@Service
public class StraightThroughService {

	@Autowired
	private Environment env;
	
	private KieSession ksession;
	private static String KIE_DEPLOYMENT_DESCRIPTOR_PATH = "META-INF/kie-deployment-descriptor.xml";
	private static String GROUP_ID = "PROCESS_GROUPID";
	private static String ARTIFACT_ID = "PROCESS_ARTIFACTID";
	private static String VERSION = "PROCESS_VERSION";
	
	/*
	 * public StraightThroughService() { KieServices kieServices =
	 * KieServices.Factory.get(); KieContainer kcontainer =
	 * kieServices.getKieClasspathContainer(); ksession =
	 * kcontainer.getKieBase("kbase").newKieSession(); }
	 */
	
	public StraightThroughService() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		KieServices kieServices = KieServices.Factory.get();
	
		System.out.println("******"+System.getenv(GROUP_ID) + " " + System.getenv(ARTIFACT_ID) + " " + System.getenv(VERSION)+"******");
		
		//cogemos el proceso
		ReleaseId releaseId = kieServices.newReleaseId(System.getenv(GROUP_ID),System.getenv(ARTIFACT_ID), System.getenv(VERSION));
		
		//recuperamos el kie-deployment-descriptor del proceso
		InputStream deploymentDescriptor = this.getClass().getClassLoader()
				.getResourceAsStream(KIE_DEPLOYMENT_DESCRIPTOR_PATH);

		DeploymentDescriptor descriptor = null;

		try (ByteArrayInputStream input = new ByteArrayInputStream(ByteStreams.toByteArray(deploymentDescriptor))) {
			descriptor = DeploymentDescriptorIO.fromXml(input);
		} catch (IOException e) {
			System.out.println("Error while reading stream of kie-deployment-descriptor.xml");
		}
		
		KieContainer kcontainer = kieServices.newKieContainer(releaseId);
		
		System.out.println("**************"+kcontainer.getKieBaseNames());
		
		ksession = kcontainer.getKieBase().newKieSession();

		List<NamedObjectModel> ltWorkItemHandlers = descriptor.getWorkItemHandlers();

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		
		
		for (NamedObjectModel namedObjectModel : ltWorkItemHandlers) {
			System.out.println(namedObjectModel.getName());
			System.out.println(namedObjectModel.getIdentifier());
			
			//registramos el WIH
			ksession.getWorkItemManager().registerWorkItemHandler(
					namedObjectModel.getName(), 
					buildWorkitemHandlerInstance(namedObjectModel.getIdentifier(),classloader));
		}
	}

	public Map<String, Object> startProcess(String processId, Map<String, Object> params) throws ClassNotFoundException, IOException {
		
		
		System.out.println("************ Tamaño de la entrada: "+params.size());
		
		Map<String, Object> paramsAux = transformBody(params);
		
		
		//////Metemos el body a fuego
		/*params = new HashMap<String, Object>();
		es.bbva.dhbemrcu.protech_process.DatosProceso processData = new DatosProceso();
		processData.setPath("asdfasfasdfasdfasfasdfasfasfasf");
		params.put("processData", processData);*/
		///////
		
		
		
		
		WorkflowProcessInstanceImpl p = (WorkflowProcessInstanceImpl) ksession.startProcess(processId, paramsAux);
		System.out.println(p.getId());
		
		return p.getVariables();
		
	}

	/*private void sendGET(String url) throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection(Proxy.NO_PROXY);

		con.setRequestMethod("GET");
		// con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept", "application/json");
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println(
					"GET request not worked ResponseCode:" + responseCode + "message: " + con.getErrorStream());
		}

	}*/
	
	@SuppressWarnings("unchecked")
	private <T extends WorkItemHandler> T buildWorkitemHandlerInstance (String workitemhandlerIdentifier, ClassLoader classloader) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException,
				IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		String workitemhandlerClassName = workitemhandlerIdentifier.replaceAll("new ", "");
		
		workitemhandlerClassName = workitemhandlerClassName.substring(0,workitemhandlerClassName.indexOf("("));
		
		T wihObject = null;
		
		Class<T> wihClass = (Class<T>)Class.forName(workitemhandlerClassName);
		
		if(workitemhandlerIdentifier.contains("()")) {
			wihObject = wihClass.getDeclaredConstructor().newInstance();
		}
		else {//En el caso de necesitar pasar parametros al wih, de dónde saco el classloader? ¿¿¿kcontainer.getClassLoader();???
			// Sólo para constructores con parámetros (x ej. los que requieran de classloader o ksession).
			
			if(workitemhandlerIdentifier.contains("ksession") && workitemhandlerIdentifier.contains("classloader")) {//tiene los dos parametros de entrada
				Constructor<T> constructor = wihClass.getConstructor(KieSession.class,ClassLoader.class);
				wihObject = constructor.newInstance(ksession,classloader);
			}else if(workitemhandlerIdentifier.contains("ksession")) {//tiene solo el ksession de entrada
				Constructor<T> constructor = wihClass.getConstructor(KieSession.class);
				wihObject = constructor.newInstance(ksession);
			}else if(workitemhandlerIdentifier.contains("classLoader")) {//tiene solo el classloader de entrada
				Constructor<T> constructor = wihClass.getConstructor(ClassLoader.class);
				wihObject = constructor.newInstance(classloader);
			}
			
			
		}
				
		return wihObject;
	}
	
	
	private Object getClassFromString(Class<?> clazz, String contentType, String content) throws IOException {
    	//logger.debug("transformResult({},{},{})_init",clazz,contentType,content);
        
        if (contentType.toLowerCase().contains("application/json")) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(content, clazz);
        }
        else {    	
            //logger.warn("Unable to find transformer for content type '{}' to handle for content '{}'", contentType, content);
        	
        	System.out.println("Unable to find transformer for content type '{}' to handle for content '{}'\", contentType, content");
        }
        // returning RAW string
        //logger.debug("transformResult()_end");
        return content;
        
    }
	
	private Map<String, Object> transformBody(Map<String, Object> params) throws ClassNotFoundException, IOException{
		
		Map<String, Object> paramsAux = new HashMap<String, Object>();
		
		LinkedHashMap<String, Object> submap;
		String keyClass = null;
		String jsonString = null;
		ObjectMapper objectMapper;
		
		for (Map.Entry entry : params.entrySet()) {//recorremos los de primera linea del body
		    System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
		    
		    if(entry.getValue().getClass().equals(LinkedHashMap.class)) {//si es de tipo linkedHashMap quiere decir que es un obejto por lo que tenemos que parsearlo
		    	
		    	submap = (LinkedHashMap<String, Object>) entry.getValue(); //sacamos el objeto que dice el tipo
		    	
		    	for (String key : submap.keySet()) {
		    		keyClass = key;
		    	}
		    	
		    	//Class<?> clazz = Class<T>.forName(keyClass);
		    	Class<?> clazz = Class.forName(keyClass, true, Thread.currentThread().getContextClassLoader());
		    	
		    	objectMapper = new ObjectMapper();
		    	String json = objectMapper.writeValueAsString(submap.get(keyClass));
		    	
		    	
		    	
		    	Object resultObject = getClassFromString(clazz, "application/json", json);
		    	
		    	paramsAux.put((String)entry.getKey(), resultObject);
		    	
		    }else {//si no es de tipo HashMap lo metemos directamente
		    	paramsAux.put((String)entry.getKey(), entry.getValue());
		    }
		}
		
		return paramsAux;
		
		
	}

}