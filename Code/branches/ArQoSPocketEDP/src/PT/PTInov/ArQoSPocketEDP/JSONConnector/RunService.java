package PT.PTInov.ArQoSPocketEDP.JSONConnector;

import android.annotation.SuppressLint;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.ExecutionResults;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.RunServiceModuloInfo;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.RunServiceResponse;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.ServiceResponse;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.WebRequests;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.WebResponse;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;

@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
public class RunService extends JSONConnector {
	
	private final String tag = "RunService";
	
	private final static String linkRunService = "/arqosng/restinterface/pocket/runservice";
	private String linkGetResults = "/arqosng/restinterface/pocket/service/results/";
	private String testResultLink = "/arqosng/restinterface/akka/results";
	
	private String userName = null;
	private String password = null;
	
	private String internalCookie = null;
	
	public RunService(String userName, String password, String ip, String port) {
		
		super("http://"+ip+":"+port+linkRunService);
		
		linkGetResults = "http://"+ip+":"+port+linkGetResults;
		testResultLink = "http://"+ip+":"+port+testResultLink;
		
		this.userName = userName;
		this.password = password;
	}
	
	@SuppressLint({ "ParserError", "NewApi", "NewApi", "NewApi" })
	public RunServiceResponse runService(String module, String msisdn, String ip, String test, String probeID) {
		
		try {
		
			StringBuilder sbRequest = new StringBuilder();
		
			sbRequest.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			sbRequest.append("<service user=\""+userName+"\" password=\""+password+"\" responseurl=\""+testResultLink+"\" name=\""+test+"\">");
			
			if (test.equals(M2M_CSDService.testName)) {
				sbRequest.append("<parameter value=\""+msisdn+"\" module=\"A\" ordernumber=\"1\" internacionalcode=\"TR_20_03\"/>");
				sbRequest.append("<parameter value=\""+msisdn+"\" module=\"A\" ordernumber=\"3\" internacionalcode=\"TR_47_03\"/>");
			} else if (test.equals(M2MIPService.testName)) {
				sbRequest.append("<parameter value=\""+ip+"\" module=\"A\" ordernumber=\"2\" internacionalcode=\"TR_44_03\"/>");
				sbRequest.append("<parameter value=\""+msisdn+"\" module=\"A\" ordernumber=\"4\" internacionalcode=\"TR_47_03\"/>");
			}
			
			sbRequest.append("<module virtualid=\"A\" id=\""+probeID+"\"/>");
			sbRequest.append("</service>");
			
			Logger.v(tag, LogType.Trace, "sbRequest :" + sbRequest.toString());
		
			// Send the Post
			WebResponse responseServer = SendXmlPost(sbRequest.toString());
			
			Logger.v(tag, LogType.Trace, "responseServer :"+responseServer.toString());
			
			//save cookie
			internalCookie = responseServer.getCookie();
			
			Logger.v(tag, LogType.Trace, "2");
		
			/*
			// fake response
			StringBuilder responseServer = new StringBuilder();
			StringBuilder responseServer2 = new StringBuilder();

			responseServer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			responseServer.append("<services>");
			responseServer.append("<service id=\"577259670563000290711163434471\"/>");
			responseServer.append("</services>");
		
			responseServer2.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			responseServer2.append("<errors>");
			responseServer2.append("<error errorCode=\"002\" errorMessage=\"Modules are busy\"/>");
			responseServer2.append("</errors>");
			*/
		
		
			// falta fazer aki o parser da resposta.........................................................................................
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			//Document doc = builder.parse(responseServer.toString());
			Document doc = builder.parse(new InputSource(new ByteArrayInputStream(responseServer.getResponseMessage().getBytes("utf-8"))));
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			
			Logger.v(tag, LogType.Trace, "3");
		
			boolean stateResponseOK = false;
			String testID = null;
			String errorCode = null;
			String errorMsg = null;
			
			if (responseServer.toString().contains("<services>")) {
				stateResponseOK = true;
				
				Logger.v(tag, LogType.Trace, "4");
			
				// parse when the msg is ok
				XPathExpression expr = xpath.compile("services/service/@id");
				NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				
				for (int i = 0; i < nl.getLength(); i++) {
					testID = nl.item(i).getNodeValue();
				}
			
			} else {
			
				Logger.v(tag, LogType.Trace, "5");
				
				// parse when the msg is not ok
				XPathExpression expr1 = xpath.compile("/errors/error");
				Logger.v(tag, LogType.Trace, "5.1");
				NodeList nl1 = (NodeList) expr1.evaluate(doc, XPathConstants.NODESET);
				Logger.v(tag, LogType.Trace, "5.2");
				errorCode = nl1.item(0).getAttributes().getNamedItem("errorCode").getNodeValue();
				Logger.v(tag, LogType.Trace, "5.3 :: errorCode :"+errorCode);
				errorMsg = nl1.item(0).getAttributes().getNamedItem("errorMessage").getNodeValue();
				Logger.v(tag, LogType.Trace, "5.4 :: errorMsg :"+errorMsg);
			}
			
			Logger.v(tag, LogType.Trace, "6");
			
			RunServiceResponse rsr = new RunServiceResponse(stateResponseOK, testID, errorCode, errorMsg);
			Logger.v(tag, LogType.Trace, "rsr :\n" + rsr.toString());
			
			return rsr;
		
		} catch(Exception ex) {
			Logger.v(tag, LogType.Debug, "runService Error :" + ex.toString());
		}
		
		return null;
	}
	
	
	@SuppressLint("ParserError")
	public ServiceResponse getServiceTestResponse(String testID) {
		
		try {
			
			Logger.v(tag, LogType.Trace, "getServiceTestResponse :: In :: testID :"+testID);
			
			// fazer um get ao servidor.....
			WebResponse getResponse = WebRequests.doGET(linkGetResults+testID, internalCookie);
			
			Logger.v(tag, LogType.Trace, "getServiceTestResponse :: getResponse :"+getResponse.toString());
			
			String responseServer = getResponse.getResponseInput();
			Logger.v(tag, LogType.Trace, "getServiceTestResponse :: responseServer size :"+responseServer.length());
			Logger.v(tag, LogType.Trace, "getServiceTestResponse :: responseServer :"+responseServer);

			// fake response
			/*StringBuilder responseServer1 = new StringBuilder();
			
			responseServer1.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			responseServer1.append("<results timetoend=\"102\" executionstatus=\"0\"/>");
			*/
			
			// fake response
			/*StringBuilder responseServer = new StringBuilder();
			
			responseServer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			responseServer.append("<results timetoend=\"0\" executionstatus=\"1\" macroresultid=\"3442324\">");
			responseServer.append("<result internacionalcode=\"R_01_01\" value=\"2012-09-25 16:24:00\" name=\"Início\"/>");
			responseServer.append("<result internacionalcode=\"R_01_02\" value=\"2012-09-25 16:24:54\" name=\"Fim\"/>");
			responseServer.append("<result internacionalcode=\"R_01_06\" value=\"NOK\" name=\"Resultado\"/>");
			responseServer.append("<result internacionalcode=\"R_01_07\" value=\"cod_2009\" name=\"Causa\"/>");
			responseServer.append("<result internacionalcode=\"R_01_08\" value=\"302013894\" name=\"Destino\"/>");
			responseServer.append("<result internacionalcode=\"R_01_01\" value=\"2012-09-25 16:25:01\" name=\"Início\"/>");
			responseServer.append("<result internacionalcode=\"R_01_02\" value=\"2012-09-25 16:25:05\" name=\"Fim\"/>");
			responseServer.append("<result internacionalcode=\"R_01_06\" value=\"NOK\" name=\"Resultado\"/>");
			responseServer.append("<result internacionalcode=\"R_01_07\" value=\"cod_3004\" name=\"Causa\"/>");
			responseServer.append("</results>");
			*/
		
			// falta fazer aki o parser da resposta
			
			String timetoend = null;
			String macroresultid = null;
			String executionstatus = null;
			String servicecode = null;
			
			ArrayList<ExecutionResults> executionResults = new ArrayList<ExecutionResults>();
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new ByteArrayInputStream(responseServer.getBytes("utf-8"))));
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
		
			XPathExpression expr1 = xpath.compile("/results");
			NodeList nl1 = (NodeList) expr1.evaluate(doc, XPathConstants.NODESET);
			
			try {
				timetoend = nl1.item(0).getAttributes().getNamedItem("timetoend").getNodeValue();
			} catch(Exception ex) {
				Logger.v(tag, LogType.Debug, "parse timetoend Error :" + ex.toString());
			}
			
			try {
				macroresultid = nl1.item(0).getAttributes().getNamedItem("macroresultid").getNodeValue();
			} catch(Exception ex) {
				Logger.v(tag, LogType.Debug, "parse macroresultid Error :" + ex.toString());
			}
			
			try {
				servicecode = nl1.item(0).getAttributes().getNamedItem("servicecode").getNodeValue();
			} catch(Exception ex) {
				Logger.v(tag, LogType.Debug, "parse servicecode Error :" + ex.toString());
			}
			
			try {
				executionstatus = nl1.item(0).getAttributes().getNamedItem("executionstatus").getNodeValue();
			} catch(Exception ex) {
				Logger.v(tag, LogType.Debug, "parse executionstatus Error :" + ex.toString());
			}
			
			
			if (!executionstatus.equals("0")) {
			
				try {
					XPathExpression expr2 = xpath.compile("/results/result");
					NodeList nl2 = (NodeList) expr2.evaluate(doc, XPathConstants.NODESET);
			
					for (int i = 0; i < nl2.getLength(); i++) {
						try {
							executionResults.add(new ExecutionResults(nl2.item(i).getAttributes().getNamedItem("internacionalcode").getNodeValue(), 
									nl2.item(i).getAttributes().getNamedItem("value").getNodeValue(), 
									nl2.item(i).getAttributes().getNamedItem("taskID").getNodeValue(), nl2.item(i).getAttributes().getNamedItem("taskModule").getNodeValue(), 
									nl2.item(i).getAttributes().getNamedItem("taskOrderNumber").getNodeValue(), null, nl2.item(i).getAttributes().getNamedItem("name").getNodeValue()));
						} catch(Exception exx) {
							Logger.v(tag, LogType.Debug, "Get Result params :: Error :" + exx.toString());
						}
					}
					
					Logger.v(tag, LogType.Trace, "new array list.............................");
					for (ExecutionResults er :executionResults) {
						Logger.v(tag, LogType.Trace, er.toString());
					}
					Logger.v(tag, LogType.Trace, "end array list.............................");
					
				} catch(Exception ex) {
					Logger.v(tag, LogType.Debug, "parse executionResults Error :" + ex.toString());
				}
			}
			
			ServiceResponse sr = new ServiceResponse(timetoend, macroresultid, executionstatus, servicecode, executionResults); 
			Logger.v(tag, LogType.Trace, "sr :\n" + sr.toString());
			
			return sr;
		
		} catch(Exception ex) {
			Logger.v(tag, LogType.Debug, "getServiceTestResponse Error :" + ex.toString());
		}
		
		return null;
	}
	
}
