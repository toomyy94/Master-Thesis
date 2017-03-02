package PT.PTInov.ArQoSPocketEDP.JSONConnector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.util.Log;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.TestModuleResponse;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.WebResponse;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;

@TargetApi(8)
@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
public class AskForTestModule extends JSONConnector {

	private final String tag = "AskForTestModule";

	private final static String linkGetModules = "/arqosng/restinterface/pocket/module";

	private String userName = null;
	private String password = null;
	
	public AskForTestModule(String userName, String password, String ip, String port) {
		super("http://"+ip+":"+port+linkGetModules);
		
		this.userName = userName;
		this.password = password;
	}
	
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
	public TestModuleResponse getAvailableModule() {
		
		
		Logger.v(tag, LogType.Trace, "1");
		
		StringBuilder buildRequest = new StringBuilder();

		buildRequest.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		buildRequest.append("<service user=\""+userName+"\" password=\""+password+"\" >");
		buildRequest.append("</service>");
	
		WebResponse responseServer = SendXmlPost(buildRequest.toString());
		
		Logger.v(tag, LogType.Trace, "responseServer :"+responseServer.getResponseMessage());
	
		/*
		StringBuilder fakeResponse = new StringBuilder();
	
		fakeResponse.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		fakeResponse.append("<modules>");
		fakeResponse.append("<module centralarea=\"aveiro\" technology=\"GSM\" phonenumber=\"910261606\" id=\"64803\" name=\"HSPA Mobile 1:3:MC8792V\"/>");
		fakeResponse.append("</modules>");
		
		Logger.v(tag, LogType.Trace, "xml :"+fakeResponse.toString());
		*/
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			//Document doc = builder.parse(new InputSource(new ByteArrayInputStream(fakeResponse.toString().getBytes("utf-8"))));
			Document doc = builder.parse(new InputSource(new ByteArrayInputStream(responseServer.getResponseMessage().getBytes("utf-8"))));
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			
			String centralarea = null;
			String technology = null;
			String phonenumber = null;
			String id = null;
			String name = null;
			
			Logger.v(tag, LogType.Trace, "2");
			
			
			NodeList nl = null;
			XPathExpression expr = null;
			
			expr = xpath.compile("modules/module/@centralarea");
			nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			centralarea = nl.item(0).getNodeValue();
			
			expr = xpath.compile("modules/module/@technology");
			nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			technology = nl.item(0).getNodeValue();
			
			expr = xpath.compile("modules/module/@phonenumber");
			nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			phonenumber = nl.item(0).getNodeValue();
			
			expr = xpath.compile("modules/module/@id");
			nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			id = nl.item(0).getNodeValue();
			
			expr = xpath.compile("modules/module/@name");
			nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			name = nl.item(0).getNodeValue();
			
			Logger.v(tag, LogType.Trace, "3");
			
			TestModuleResponse tmr = new TestModuleResponse(centralarea, technology, phonenumber, id, name); 
			
			Logger.v(tag, LogType.Trace, "tmr :" + tmr.toString());
			
			return tmr;
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error, "Error :"+ex.toString());
		}
		
		return null;
	}
}
