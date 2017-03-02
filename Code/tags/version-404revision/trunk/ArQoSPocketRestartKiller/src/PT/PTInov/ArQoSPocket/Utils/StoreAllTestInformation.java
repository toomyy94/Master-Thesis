package PT.PTInov.ArQoSPocket.Utils;

import java.io.Serializable;
import java.util.Date;

import PT.PTInov.ArQoSPocket.Enums.ResponseEnum;
import PT.PTInov.ArQoSPocket.Service.FTPResponse;
import PT.PTInov.ArQoSPocket.Service.HttpServiceResponse;
import PT.PTInov.ArQoSPocket.Service.PingResponse;

public class StoreAllTestInformation implements Serializable {

	private Date registryDate = null;
	
	// save network information
	private StoreInformation networkInformation = null;
	
	// save ping information
	private PingResponse pingInformation = null;
	
	// save http information
	private HttpServiceResponse httpDownloadInformation = null;
	
	// save http information
	private HttpServiceResponse httpUploadInformation = null;
	
	// save http information
	private HttpServiceResponse httpMAXDownloadInformation = null;
		
	// save http information
	private HttpServiceResponse httpMAXUploadInformation = null;
	
	// save ftp information
	private FTPResponse ftpUploadResponse = null;
	
	// save ftp information
	private FTPResponse ftpDownloadResponse = null;
	
	private boolean sendedState = false;
	
	public StoreAllTestInformation(StoreInformation networkInformation, PingResponse pingInformation, HttpServiceResponse httpDownloadInformation, HttpServiceResponse httpUploadInformation, FTPResponse ftpUploadResponse, FTPResponse ftpDownloadResponse, HttpServiceResponse httpMAXDownloadInformation, HttpServiceResponse httpMAXUploadInformation) {
		this.networkInformation = networkInformation;
		this.pingInformation = pingInformation;
		this.httpDownloadInformation = httpDownloadInformation;
		this.httpUploadInformation = httpUploadInformation;
		this.ftpUploadResponse = ftpUploadResponse;
		this.ftpDownloadResponse = ftpDownloadResponse;
		this.httpMAXDownloadInformation = httpMAXDownloadInformation;
		this.httpMAXUploadInformation = httpMAXUploadInformation;
		
		registryDate = new Date();
		sendedState = false;
	}
	
	/*
	public void setNetworkInformationOK() {
		networkInformationOK = true;
	}
	
	public void setPingResponseOK() {
		pingInformationOK = true;
	}
	
	public void setHttpInformationOK() {
		httpInformationOK = true;
	}*/
	
	public boolean getNetworkInformationState() {
		
		try {
			return networkInformation.getSuccess();
		} catch(Exception ex) {}
		
		return false;
	}
	
	public boolean getPingResponseState() {
		
		try {
			return (pingInformation.getResponseEnum() == ResponseEnum.OK);
		} catch(Exception ex) {}
	
		return false;
	}
	
	public boolean gethttpInformationState() {
		
		try {
			return ((httpDownloadInformation.getExecState() == ResponseEnum.OK) && (httpUploadInformation.getExecState() == ResponseEnum.OK)
					&& (httpMAXDownloadInformation.getExecState() == ResponseEnum.OK) && (httpMAXUploadInformation.getExecState() == ResponseEnum.OK));
		} catch(Exception ex) {}
		
		return false;
	}
	
	public boolean getFTPInformationState() {
		
		try {
			return ((ftpUploadResponse.getExecState() == ResponseEnum.OK) && (ftpDownloadResponse.getExecState() == ResponseEnum.OK));
		} catch(Exception ex) {}
		
		return false;
	}
	
	public boolean allTestState() {
		
		try {
			return getNetworkInformationState() && getPingResponseState()  && gethttpInformationState() && getFTPInformationState();
		} catch(Exception ex) {}
		
		return false;
	}
	
	public StoreInformation getStoreInformation() {
		return networkInformation;
	}
	
	public PingResponse getPingResponse() {
		return pingInformation;
	}
	
	public HttpServiceResponse getHttpDownloadServiceResponse() {
		return httpDownloadInformation;
	}
	
	public HttpServiceResponse getHttpUploadServiceResponse() {
		return httpMAXUploadInformation;
	}
	
	public HttpServiceResponse getHttpMaxDownloadServiceResponse() {
		return httpMAXDownloadInformation;
	}
	
	public HttpServiceResponse getHttpMaxUploadServiceResponse() {
		return httpUploadInformation;
	}
	
	public FTPResponse getFTPDownloadResponse() {
		return ftpDownloadResponse;
	}
	
	public FTPResponse getFTPUploadResponse() {
		return ftpUploadResponse;
	}
	
	public void setResultAsSent() {
		sendedState = true;
	}
	
	public boolean getSentResultState() {
		return sendedState;
	}
	
	public Date getRegistryDate() {
		return registryDate;
	}
}
