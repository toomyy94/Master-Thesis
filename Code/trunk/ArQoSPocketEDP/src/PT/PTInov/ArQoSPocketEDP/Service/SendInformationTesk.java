package PT.PTInov.ArQoSPocketEDP.Service;

import java.net.URL;

import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumTestE2EState;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import android.os.AsyncTask;

public class SendInformationTesk extends AsyncTask<URL, Integer, Long>{
	
	private final static String tag = "SendInformationTesk";
	
	private EngineServiceInterface engineService = null;
	private String comment = null;
	private String modelo_selo = null;
	private String resultDetails = null;
	
	public SendInformationTesk(EngineServiceInterface engineService, String comment, String modelo_selo, String resultDetails) {
		this.engineService = engineService;
		this.comment = comment;
		this.modelo_selo = modelo_selo;
		this.resultDetails = resultDetails;  
	}

	@Override
	protected Long doInBackground(URL... params) {
		String methodName = "doInBackground";
		
		try {
			
			Logger.v(tag, methodName, LogType.Debug, "In");
			
			engineService.saveAndSendWorkFlowResult(comment, modelo_selo, resultDetails);
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, "Error"+ex.toString());
		}
		
		return (long) 1;
	}

}
